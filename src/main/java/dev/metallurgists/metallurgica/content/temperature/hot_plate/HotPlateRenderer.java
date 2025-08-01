package dev.metallurgists.metallurgica.content.temperature.hot_plate;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.metallurgists.metallurgica.content.temperature.hot_plate.heating_coil.HeatingCoilType;
import dev.metallurgists.metallurgica.content.temperature.hot_plate.heating_coil.behaviour.HeatingCoilBehaviour;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class HotPlateRenderer extends SmartBlockEntityRenderer<HotPlateBlockEntity> {
    public HotPlateRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(HotPlateBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
        if (be.getLevel() == null)
            return;

        if (!be.isController()) return;

        BlockState blockState = be.getBlockState();
        HotPlateBlockEntity controllerBE = be.getControllerBE();

        HeatingCoilBehaviour coilBehaviour = controllerBE.getControllerBE().getBehaviour(HeatingCoilBehaviour.TYPE);

        if (coilBehaviour.getCoilType() == null) return;

        HeatingCoilType heatingCoil = coilBehaviour.getCoilType();

        if (heatingCoil == null) return;

        if (anyNull(heatingCoil.smallCoil(), heatingCoil.mediumCoil(), heatingCoil.largeCoil())) return;

        PartialModel borderModel = controllerBE.getWidth() == 3 ? heatingCoil.largeCoil().getSecond() : controllerBE.getWidth() == 2 ? heatingCoil.mediumCoil().getSecond() : heatingCoil.smallCoil().getSecond();
        PartialModel coilModel = controllerBE.getWidth() == 3 ? heatingCoil.largeCoil().getFirst() : controllerBE.getWidth() == 2 ? heatingCoil.mediumCoil().getFirst() : heatingCoil.smallCoil().getFirst();

        float posX = controllerBE.getWidth() == 2 ? (float)(be.getController().getX() - be.getBlockPos().getX()) + 0.5F : 0.0F;
        float posZ = controllerBE.getWidth() == 2 ? (float)(be.getController().getZ() - be.getBlockPos().getZ()) + 0.5F : 0.0F;

        if (controllerBE.getWidth() == 3) {
            posX = (float)(be.getController().getX() - be.getBlockPos().getX()) + 1.0F;
            posZ = (float)(be.getController().getZ() - be.getBlockPos().getZ()) + 1.0F;
        }

        CachedBuffers.partial(borderModel, blockState)
                .light(LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos()))
                .translate(posX, 0, posZ).renderInto(ms, bufferSource.getBuffer(RenderType.cutoutMipped()));
        CachedBuffers.partial(coilModel, blockState)
                .light(LevelRenderer.getLightColor(be.getLevel(), be.getBlockPos()))
                .translate(posX, 0, posZ).renderInto(ms, bufferSource.getBuffer(RenderType.cutoutMipped()));
    }

    private boolean anyNull(Object... objects) {
        for (Object object : objects) {
            if (object == null) {
                return true;
            }
        }
        return false;
    }
}
