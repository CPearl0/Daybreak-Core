package io.github.cpearl0.daybreakcore.mixin;

import io.github.cpearl0.daybreakcore.data.DaybreakSavedData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {
    @Shadow
    public abstract void setDayTime(long pTime);
    @Shadow
    public MinecraftServer server;
    @Shadow
    public ServerLevelData serverLevelData;

    @Inject(method = "tickTime", at = @At(value = "HEAD"), cancellable = true)
    public void tickTime(CallbackInfo ci) {
        var self = (ServerLevel) (Object) this;
        if (self.dimension() != Level.OVERWORLD)
            return;

        var data = self.getDataStorage().computeIfAbsent(new SavedData.Factory<>(DaybreakSavedData::new, DaybreakSavedData::load), "time_state");
        if (data.timeState == DaybreakSavedData.NORMAL)
            return;

        long delta = 0;
        switch (data.timeState) {
            case DaybreakSavedData.NIGHT_NEGATIVE -> delta = -1;
            case DaybreakSavedData.NIGHT_POSITIVE -> delta = 1;
        }

        long i = self.getLevelData().getGameTime() + 1;
        serverLevelData.setGameTime(i);
        serverLevelData.getScheduledEvents().tick(server, i);

        var time = self.getDayTime() + delta;
        if (time > 22200) {
            time = 22200;
            data.timeState = DaybreakSavedData.NIGHT_NEGATIVE;
            data.setDirty();
            self.getDataStorage().set("time_state", data);
        } else if (time < 13800) {
            time = 13800;
            data.timeState = DaybreakSavedData.NIGHT_POSITIVE;
            data.setDirty();
            self.getDataStorage().set("time_state", data);
        }
        setDayTime(time);
        ci.cancel();
    }

    @Inject(method = "updateSleepingPlayerList", at = @At(value = "HEAD"), cancellable = true)
    public void updateSleepingPlayerList(CallbackInfo ci) {
        var self = (ServerLevel) (Object) this;
        if (self.dimension() != Level.OVERWORLD)
            return;

        var data = self.getDataStorage().computeIfAbsent(new SavedData.Factory<>(DaybreakSavedData::new, DaybreakSavedData::load), "time_state");
        if (data.timeState == DaybreakSavedData.NORMAL)
            return;

        // Prevent players from skipping a night
        ci.cancel();
    }
}
