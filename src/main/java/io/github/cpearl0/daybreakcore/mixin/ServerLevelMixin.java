package io.github.cpearl0.daybreakcore.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin {
    @Shadow
    public abstract void setDayTime(long pTime);

    @Inject(method = "tickTime", at = @At(value = "RETURN"))
    public void setTime(CallbackInfo ci) {
        var self = (ServerLevel) (Object) this;
        if (self.dimension().equals(Level.OVERWORLD))
            ;
    }
}
