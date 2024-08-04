package io.github.cpearl0.daybreakcore.mixin;

import io.github.cpearl0.daybreakcore.data.DaybreakSavedData;
import io.github.cpearl0.daybreakcore.network.TimeStateMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Shadow
    private PlayerList playerList;

    @Inject(method = "synchronizeTime", at = @At(value = "TAIL"))
    private void synchronizeTime(ServerLevel pLevel, CallbackInfo ci) {
        if (pLevel.dimension() != Level.OVERWORLD)
            return;

        var data = pLevel.getDataStorage().computeIfAbsent(new SavedData.Factory<>(DaybreakSavedData::new, DaybreakSavedData::load), "time_state");
        for (ServerPlayer serverplayer : playerList.getPlayers()) {
            if (serverplayer.level().dimension() == Level.OVERWORLD) {
                serverplayer.connection.send(new TimeStateMessage(data.timeState));
            }
        }
    }
}
