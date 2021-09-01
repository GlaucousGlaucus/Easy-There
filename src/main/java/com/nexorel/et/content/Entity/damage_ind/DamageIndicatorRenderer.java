package com.nexorel.et.content.Entity.damage_ind;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.text.DecimalFormat;

@OnlyIn(Dist.CLIENT)
public class DamageIndicatorRenderer extends EntityRenderer<DamageIndicatorEntity> {

    private static final DecimalFormat df = new DecimalFormat("###,###,###,###.##");

    public DamageIndicatorRenderer(EntityRendererManager manager) {
        super(manager);
    }

    @Override
    public ResourceLocation getTextureLocation(DamageIndicatorEntity p_110775_1_) {
        return null;
    }

    @Override
    public void render(DamageIndicatorEntity entity, float entityYaw, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight) {
        FontRenderer fontRenderer = this.entityRenderDispatcher.getFont();
        matrixStack.pushPose();
        double d = Math.sqrt(this.entityRenderDispatcher.distanceToSqr(entity.getX(), entity.getY(), entity.getZ()));
        d = MathHelper.clamp(d, 0, 2.5);
        float scale = (float) (0.006F * d);
        matrixStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        matrixStack.scale(-scale, -scale, scale);
        float damage_number = entity.getDamage();
        StringBuilder text = getDamageText(damage_number, entity.wasCrit(), entity.getTargetAlive());
        float f1 = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
        int j = (int) (f1 * 255.0F) << 25;
        matrixStack.translate((-fontRenderer.width(text.toString()) / 2f) + 0.5f, 0, 0);
        fontRenderer.drawInBatch(text.toString(), 0, 0, 0xffffffff, false, matrixStack.last().pose(), buffer, false, j, packedLight);
        matrixStack.popPose();
    }

    private StringBuilder getDamageText(float damage_number, boolean wasCrit, boolean isAlive) {
        StringBuilder text = new StringBuilder();
        TextFormatting dmg_type_txt = wasCrit ? TextFormatting.DARK_RED : TextFormatting.RED;
        TextFormatting dmg_type_dmg = wasCrit ? TextFormatting.GOLD : TextFormatting.YELLOW;
        String sword_or_skull = isAlive ? "\u2694" : "\u2620";
        text.append(dmg_type_txt);
        text.append(sword_or_skull).append(" ");
        text.append(TextFormatting.RESET);
        text.append(dmg_type_dmg);
        text.append(df.format(damage_number));
        text.append(dmg_type_txt);
        text.append(" ").append(sword_or_skull);
        return text;
    }
}
