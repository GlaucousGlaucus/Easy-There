package com.nexorel.et.content.Entity.boss.aura;// Made with Blockbench 3.9.2
// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
// Paste this class into your mod and generate all required imports


import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.BlazeModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.nexorel.et.Reference.MOD_ID;

/**
 * AuraModel - Nexorel
 * Created using Tabula 8.0.0
 */
@OnlyIn(Dist.CLIENT)
public class AuraEntityModel extends EntityModel<AuraEntity> {
    private final ModelPart root;
    public ModelPart Head;
    public ModelPart A;
    public ModelPart B;
    public ModelPart C;
    public ModelPart D;
    private int tick = 20;

    public static ModelLayerLocation CUBE_LAYER = new ModelLayerLocation(new ResourceLocation(MOD_ID, "aura"), "aura");

    public AuraEntityModel(ModelPart body) {
        this.root = body;
        this.Head = this.root.getChild("head");
        this.A = this.root.getChild("a");
        this.B = this.root.getChild("b");
        this.C = this.root.getChild("c");
        this.D = this.root.getChild("d");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("a", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.95F, -17.85F, -2.05F, 3.9F, 35.7F, 4.1F),
                PartPose.offset(20.0F, 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(0, 40)
                        .addBox(-6.0F, -6.0F, -6.0F, 12.0F, 12.0F, 12.0F),
                PartPose.offset(0, -8.1F, 0));

        partdefinition.addOrReplaceChild("c", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.95F, -17.85F, -2.05F, 3.9F, 35.7F, 4.1F),
                PartPose.offset(0.0F, 0.0F, -24.0F));

        partdefinition.addOrReplaceChild("d", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.95F, -17.85F, -2.05F, 3.9F, 35.7F, 4.1F),
                PartPose.offset(0.0F, 0.0F, 20.0F));

        partdefinition.addOrReplaceChild("b", CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-1.95F, -17.85F, -2.05F, 3.9F, 35.7F, 4.1F),
                PartPose.offset(-23.0F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        ImmutableList.of(this.B, this.A, this.Head, this.D, this.C).forEach((modelRenderer) ->
                modelRenderer.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
    }

    /**
     * Copied Animation from BlazeModel
     *
     * @see BlazeModel
     */
    @Override
    public void setupAnim(AuraEntity auraEntity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (auraEntity.isAggressive()) {
            if (tick > 0) {
                tick--;
                this.Head.y -= 1F;
            }
        } else {
            if (tick < 20) {
                this.Head.y += 1F;
                tick++;
            }
        }

        float thingies_rot_angle = 0.05F;
        float r = 2.0F;
        float aNumber = ageInTicks * 3.1415927F * -0.035F;
        float increment = 1.65F;
        float uhhAnotherNumber = 0.25F;
        int l = 0;

        this.A.yRot += thingies_rot_angle;
        this.A.x = r * Mth.sin(aNumber) * 9.0F;
        this.A.z = r * Mth.cos(aNumber) * 9.0F;
        this.A.y = -0.5F + Mth.cos(((float) (0) + (ageInTicks * 2)) * uhhAnotherNumber);

        aNumber += increment;
        l++;

        this.B.yRot += thingies_rot_angle;
        this.B.x = r * Mth.sin(aNumber) * 9.0F;
        this.B.z = r * Mth.cos(aNumber) * 9.0F;
        this.B.y = -0.2F + Mth.cos(((float) (l * 2) + (ageInTicks * 2)) * uhhAnotherNumber);

        aNumber += increment;
        l++;

        this.C.yRot += thingies_rot_angle;
        this.C.x = r * Mth.sin(aNumber) * 9.0F;
        this.C.z = r * Mth.cos(aNumber) * 9.0F;
        this.C.y = -0.2F + Mth.cos(((float) (l * 2) + (ageInTicks * 2)) * uhhAnotherNumber);

        aNumber += increment;
        l++;

        this.D.yRot += thingies_rot_angle;
        this.D.x = r * Mth.sin(aNumber) * 9.0F;
        this.D.z = r * Mth.cos(aNumber) * 9.0F;
        this.D.y = -0.2F + Mth.cos(((float) (l * 2) + (ageInTicks * 2)) * uhhAnotherNumber);
        this.Head.xRot = headPitch * ((float) Math.PI / 180);
        this.Head.yRot = netHeadYaw * ((float) Math.PI / 180);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelPart modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }
}