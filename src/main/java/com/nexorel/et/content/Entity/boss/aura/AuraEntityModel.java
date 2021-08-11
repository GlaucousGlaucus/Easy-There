package com.nexorel.et.content.Entity.boss.aura;// Made with Blockbench 3.9.2
// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
// Paste this class into your mod and generate all required imports


import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * AuraModel - Nexorel
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class AuraEntityModel extends EntityModel<AuraEntity> {
    public ModelRenderer Head;
    public ModelRenderer A;
    public ModelRenderer B;
    public ModelRenderer C;
    public ModelRenderer D;

    public AuraEntityModel() {
        this.texWidth = 64;
        this.texHeight = 64;
        this.A = new ModelRenderer(this, 0, 0);
        this.A.setPos(20.0F, 0.0F, 0.0F);
        this.A.addBox(-1.95F, -17.85F, -2.05F, 3.9F, 35.7F, 4.1F, 0.0F, 0.0F, 0.0F);
        this.Head = new ModelRenderer(this, 0, 40);
        this.Head.setPos(0.0F, -8.1F, 0.0F);
        this.Head.addBox(-6.0F, -6.0F, -6.0F, 12.0F, 12.0F, 12.0F, 0.0F, 0.0F, -0.3F);
        this.C = new ModelRenderer(this, 0, 0);
        this.C.setPos(0.0F, 0.0F, -24.0F);
        this.C.addBox(-1.95F, -17.85F, -2.05F, 3.9F, 35.7F, 4.1F, 0.0F, 0.0F, 0.0F);
        this.D = new ModelRenderer(this, 0, 0);
        this.D.setPos(0.0F, 0.0F, 20.0F);
        this.D.addBox(-1.95F, -17.85F, -2.05F, 3.9F, 35.7F, 4.1F, 0.0F, 0.0F, 0.0F);
        this.B = new ModelRenderer(this, 0, 0);
        this.B.setPos(-23.0F, 0.0F, 0.0F);
        this.B.addBox(-1.95F, -17.85F, -2.05F, 3.9F, 35.7F, 4.1F, 0.0F, 0.0F, 0.0F);
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        ImmutableList.of(this.B, this.A, this.Head, this.D, this.C).forEach((modelRenderer) -> {
            modelRenderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        });
    }

    @Override
    public void setupAnim(AuraEntity auraEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        /** Copied Animation from BlazeEntity
         * @see net.minecraft.client.renderer.entity.model.BlazeModel
         * */
        float thingies_rot_angle = 0.05F;
        float r = 2.0F;
        float age = ageInTicks * 3.1415927F * -0.035F;
        float increment = 1.65F;
        this.A.yRot += thingies_rot_angle;
        this.A.x = r * MathHelper.sin(age) * 9.0F;
        this.A.z = r * MathHelper.cos(age) * 9.0F;
        this.A.y = -2.0F + MathHelper.cos(((float) (2) + ageInTicks) * 0.25F);

        age += increment;

        this.B.yRot += thingies_rot_angle;
        this.B.x = r * MathHelper.sin(age) * 9.0F;
        this.B.z = r * MathHelper.cos(age) * 9.0F;
        this.B.y = -2.0F + MathHelper.cos(((float) (4) + ageInTicks) * 0.25F);

        age += increment;

        this.C.yRot += thingies_rot_angle;
        this.C.x = r * MathHelper.sin(age) * 9.0F;
        this.C.z = r * MathHelper.cos(age) * 9.0F;
        this.C.y = -2.0F + MathHelper.cos(((float) (6) + ageInTicks) * 0.25F);

        age += increment;

        this.D.yRot += thingies_rot_angle;
        this.D.x = r * MathHelper.sin(age) * 9.0F;
        this.D.z = r * MathHelper.cos(age) * 9.0F;
        this.D.y = -2.0F + MathHelper.cos(((float) (8) + ageInTicks) * 0.25F);

        this.Head.xRot = headPitch * ((float) Math.PI / 180);
        this.Head.yRot = netHeadYaw * ((float) Math.PI / 180);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}