package dev.metallurgists.metallurgica.mixin;

import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NoiseGeneratorSettings.class)
public abstract class NoiseGeneratorSettingsMixin {
    @Inject(method = "oreVeinsEnabled", at = @At("RETURN"), cancellable = true)
    private void metallurgica$killLargeVeins(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
