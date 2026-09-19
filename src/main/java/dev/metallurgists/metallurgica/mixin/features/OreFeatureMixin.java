package dev.metallurgists.metallurgica.mixin.features;

import com.mojang.serialization.Codec;
import dev.metallurgists.metallurgica.registry.MetallurgicaTags;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

@Mixin(OreFeature.class)
public abstract class OreFeatureMixin extends Feature<OreConfiguration> {
    public OreFeatureMixin(Codec<OreConfiguration> codec) {
        super(codec);
    }

    @Inject(method = "place", at = @At("HEAD"), cancellable = true)
    private void metallurgica$placeCancel(FeaturePlaceContext<OreConfiguration> context, CallbackInfoReturnable<Boolean> cir) {
        OreConfiguration config = context.config();

        for (OreConfiguration.TargetBlockState target : config.targetStates) {
            if (metallurgica$isOre(target.state)) {
                cir.setReturnValue(false);
                return;
            }
        }
    }

    @Inject(method = "doPlace", at = @At("HEAD"), cancellable = true)
    private void metallurgica$doPlaceCancel(WorldGenLevel level, RandomSource random, OreConfiguration config, double minX, double maxX, double minZ, double maxZ, double minY, double maxY, int x, int y, int z, int width, int height, CallbackInfoReturnable<Boolean> cir) {
        for (OreConfiguration.TargetBlockState target : config.targetStates) {
            if (metallurgica$isOre(target.state)) {
                cir.setReturnValue(false);
                return;
            }
        }
    }

    @Inject(method = "canPlaceOre", at = @At("HEAD"), cancellable = true)
    private static void metallurgica$noYouCanNotPlaceOre(BlockState state, Function<BlockPos, BlockState> adjacentStateAccessor, RandomSource random, OreConfiguration config, OreConfiguration.TargetBlockState targetState, BlockPos.MutableBlockPos mutablePos, CallbackInfoReturnable<Boolean> cir) {
        if (metallurgica$isOre(state)) {
            cir.setReturnValue(false);
        }
    }

    @Unique
    private static boolean metallurgica$isOre(BlockState state) {
        return state.is(MetallurgicaTags.Blocks.C_ORES.tag);
    }
}
