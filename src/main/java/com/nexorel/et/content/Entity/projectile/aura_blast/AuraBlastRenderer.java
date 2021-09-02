package com.nexorel.et.content.Entity.projectile.aura_blast;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * COPIED FROM DragonFireballRenderer
 *
 * @see net.minecraft.client.renderer.entity.DragonFireballRenderer
 */

@OnlyIn(Dist.CLIENT)
public class AuraBlastRenderer extends EntityRenderer<AuraBlast> {

    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("textures/entity/enderdragon/dragon_fireball.png");
    private static final RenderType RENDER_TYPE;

    public AuraBlastRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected int getBlockLightLevel(AuraBlast p_225624_1_, BlockPos p_225624_2_) {
        return 15;
    }

    @Override
    public void render(AuraBlast auraBlast, float p_225623_2_, float p_225623_3_, PoseStack p_225623_4_, MultiBufferSource p_225623_5_, int p_225623_6_) {
        p_225623_4_.pushPose();
        p_225623_4_.scale(2.0F, 2.0F, 2.0F);
        p_225623_4_.mulPose(this.entityRenderDispatcher.cameraOrientation());
        p_225623_4_.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        PoseStack.Pose lvt_7_1_ = p_225623_4_.last();
        Matrix4f lvt_8_1_ = lvt_7_1_.pose();
        Matrix3f lvt_9_1_ = lvt_7_1_.normal();
        VertexConsumer lvt_10_1_ = p_225623_5_.getBuffer(RENDER_TYPE);
        vertex(lvt_10_1_, lvt_8_1_, lvt_9_1_, p_225623_6_, 0.0F, 0, 0, 1);
        vertex(lvt_10_1_, lvt_8_1_, lvt_9_1_, p_225623_6_, 1.0F, 0, 1, 1);
        vertex(lvt_10_1_, lvt_8_1_, lvt_9_1_, p_225623_6_, 1.0F, 1, 1, 0);
        vertex(lvt_10_1_, lvt_8_1_, lvt_9_1_, p_225623_6_, 0.0F, 1, 0, 0);
        p_225623_4_.popPose();
        super.render(auraBlast, p_225623_2_, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_);
    }

    @Override
    public ResourceLocation getTextureLocation(AuraBlast p_110775_1_) {
        return TEXTURE_LOCATION;
    }

    private static void vertex(VertexConsumer p_229045_0_, Matrix4f p_229045_1_, Matrix3f p_229045_2_, int p_229045_3_, float p_229045_4_, int p_229045_5_, int p_229045_6_, int p_229045_7_) {
        p_229045_0_.vertex(p_229045_1_, p_229045_4_ - 0.5F, (float) p_229045_5_ - 0.25F, 0.0F).color(255, 255, 255, 255).uv((float) p_229045_6_, (float) p_229045_7_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_229045_3_).normal(p_229045_2_, 0.0F, 1.0F, 0.0F).endVertex();
    }

    static {
        RENDER_TYPE = RenderType.entityCutoutNoCull(TEXTURE_LOCATION);
    }
}
