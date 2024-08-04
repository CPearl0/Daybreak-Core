package io.github.cpearl0.daybreakcore.mixin;

import io.github.cpearl0.daybreakcore.data.DaybreakSavedData;
import io.github.cpearl0.daybreakcore.network.ClientPayloadHandler;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {
    @Shadow
    public abstract void setDayTime(long pTime);

    @Inject(method = "tickTime", at = @At(value = "HEAD"), cancellable = true)
    public void tickTime(CallbackInfo ci) {
        var self = (ClientLevel) (Object) this;
        if (!self.dimension().equals(Level.OVERWORLD))
            return;

        var timeState = ClientPayloadHandler.timeState;
        if (timeState == DaybreakSavedData.NORMAL)
            return;

        long delta = 0;
        switch (timeState) {
            case DaybreakSavedData.NIGHT_NEGATIVE -> delta = -1;
            case DaybreakSavedData.NIGHT_POSITIVE -> delta = 1;
        }

        self.setGameTime(self.getLevelData().getGameTime() + 1);

        var time = self.getLevelData().getDayTime() + delta;
        if (time > 22200) {
            time = 22200;
        } else if (time < 13800) {
            time = 13800;
        }
        setDayTime(time);
        ci.cancel();
    }
}
