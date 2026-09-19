package dev.metallurgists.metallurgica.mixin.features;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.GeodeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GeodeFeature.class)
public abstract class GeodeFeatureMixin extends Feature<GeodeConfiguration>  {
    public GeodeFeatureMixin(Codec<GeodeConfiguration> codec) {
        super(codec);
    }

    @Inject(method = "place", at = @At("HEAD"), cancellable = true)
    private void metallurgica$noMoreGeodes(FeaturePlaceContext<GeodeConfiguration> p_159836_, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }
}
