package dev.metallurgists.metallurgica.content.entity.moltenShrapnel;

import com.drmangotea.tfmg.TFMG;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class MoltenShrapnelRenderer extends EntityRenderer<MoltenShrapnelEntity> {
    private static final ResourceLocation TEXTURE_LOCATION = TFMG.asResource("textures/entity/blue_spark.png");
    private static final RenderType RENDER_TYPE;

    public MoltenShrapnelRenderer(EntityRendererProvider.Context p_173962_) {
        super(p_173962_);
    }

    protected int getBlockLightLevel(MoltenShrapnelEntity p_114087_, BlockPos p_114088_) {
        return 15;
    }

    public void render(MoltenShrapnelEntity p_114080_, float p_114081_, float p_114082_, PoseStack p_114083_, MultiBufferSource p_114084_, int p_114085_) {
        p_114083_.pushPose();
        p_114083_.scale(0.5F, 0.5F, 0.5F);
        p_114083_.mulPose(this.entityRenderDispatcher.cameraOrientation());
        p_114083_.mulPose(Axis.YP.rotationDegrees(180.0F));
        PoseStack.Pose posestack$pose = p_114083_.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();
        VertexConsumer vertexconsumer = p_114084_.getBuffer(RENDER_TYPE);
        vertex(vertexconsumer, matrix4f, matrix3f, p_114085_, 0.0F, 0, 0, 1);
        vertex(vertexconsumer, matrix4f, matrix3f, p_114085_, 1.0F, 0, 1, 1);
        vertex(vertexconsumer, matrix4f, matrix3f, p_114085_, 1.0F, 1, 1, 0);
        vertex(vertexconsumer, matrix4f, matrix3f, p_114085_, 0.0F, 1, 0, 0);
        p_114083_.popPose();
        super.render(p_114080_, p_114081_, p_114082_, p_114083_, p_114084_, p_114085_);
    }

    private static void vertex(VertexConsumer p_114090_, Matrix4f p_114091_, Matrix3f p_114092_, int p_114093_, float p_114094_, int p_114095_, int p_114096_, int p_114097_) {
        p_114090_.vertex(p_114091_, p_114094_ - 0.5F, (float)p_114095_ - 0.25F, 0.0F).color(255, 255, 255, 255).uv((float)p_114096_, (float)p_114097_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_114093_).normal(p_114092_, 0.0F, 1.0F, 0.0F).endVertex();
    }

    public ResourceLocation getTextureLocation(MoltenShrapnelEntity p_114078_) {
        return TEXTURE_LOCATION;
    }

    static {
        RENDER_TYPE = RenderType.entityCutoutNoCull(TEXTURE_LOCATION);
    }
}
