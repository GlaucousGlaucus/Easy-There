/*
package com.nexorel.et.world.structure.auras_dungeon;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

import java.util.Random;

public class AuraDungeonPieces {

    public static class AuraDungeonPiece extends TemplateStructurePiece {
        public AuraDungeonPiece(StructurePieceType type, int p_163661_, StructureManager p_163662_, ResourceLocation p_163663_, String p_163664_, StructurePlaceSettings p_163665_, BlockPos p_163666_) {
            super(type, p_163661_, p_163662_, p_163663_, p_163664_, p_163665_, p_163666_);
        }

        private static StructurePlaceSettings makeSettings(boolean p_162430_, Rotation p_162431_) {
            BlockIgnoreProcessor blockignoreprocessor = p_162430_ ? BlockIgnoreProcessor.STRUCTURE_BLOCK : BlockIgnoreProcessor.STRUCTURE_AND_AIR;
            return (new StructurePlaceSettings()).setIgnoreEntities(true).addProcessor(blockignoreprocessor).setRotation(p_162431_);
        }

        @Override
        protected ResourceLocation makeTemplateLocation() {
            return makeResourceLocation(this.templateName);
        }

        private static ResourceLocation makeResourceLocation(String name) {
            return new ResourceLocation("aura_dungeon/" + name);
        }

        @Override
        protected void addAdditionalSaveData(ServerLevel level, CompoundTag tag) {
            super.addAdditionalSaveData(level, tag);
            tag.putString("Rot", this.placeSettings.getRotation().name());
        }

        @Override
        protected void handleDataMarker(String s, BlockPos blockPos, ServerLevelAccessor levelAccessor, Random random, BoundingBox bb) {

        }
    }
}
*/
