package dev.metallurgists.metallurgica.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.WorldGenLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldGenRegion.class)
public abstract class WorldGenRegionMixin implements WorldGenLevel {
    @Inject(method = "ensureCanWrite",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/Util;logAndPauseIfInIde(Ljava/lang/String;)V",
                    ordinal = 0),
            cancellable = true)
    private void metallurgica$onEnsureCanWrite(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
