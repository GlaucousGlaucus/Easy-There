package com.nexorel.et.world.structure.auras_dungeon;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.nexorel.et.EasyThere;
import com.nexorel.et.Reference;
import com.nexorel.et.util.SomeFunctions;
import com.nexorel.et.world.ETJigsaw;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class AuraDungeonFeature extends StructureFeature<NoneFeatureConfiguration> {

    public AuraDungeonFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
        return AuraDungeonFeature.FeatureStart::new;
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.UNDERGROUND_STRUCTURES;
    }

    private static final List<MobSpawnSettings.SpawnerData> STRUCTURE_MONSTERS = ImmutableList.of(
            new MobSpawnSettings.SpawnerData(EntityType.ILLUSIONER, 1, 1, 1),
            new MobSpawnSettings.SpawnerData(EntityType.EVOKER, 1, 1, 1)
    );

    @Override
    public List<MobSpawnSettings.SpawnerData> getDefaultSpawnList() {
        return STRUCTURE_MONSTERS;
    }

    @Override
    @ParametersAreNonnullByDefault
    protected boolean isFeatureChunk(ChunkGenerator generator, BiomeSource biomeSource, long seed, WorldgenRandom random, ChunkPos pos, Biome biome, ChunkPos chunkPos, NoneFeatureConfiguration featureConfiguration, LevelHeightAccessor accessor) {
        double chance = SomeFunctions.generateRandomDoubleBetweenRange(0, 10, random);
        return chance < 0.25;
    }

    public static class FeatureStart extends StructureStart<NoneFeatureConfiguration> {

        public FeatureStart(StructureFeature<NoneFeatureConfiguration> feature, ChunkPos chunkPos, int reference, long seed) {
            super(feature, chunkPos, reference, seed);
        }

        @Override
        @ParametersAreNonnullByDefault
        public void generatePieces(RegistryAccess access, ChunkGenerator generator, StructureManager manager, ChunkPos chunkPos, Biome biome, NoneFeatureConfiguration configuration, LevelHeightAccessor heightAccessor) {
            int x = chunkPos.x * 16;
            int z = chunkPos.z * 16;

            String file = "aura_dungeon/entrance";
            BlockPos center = new BlockPos(x, 20, z);
            ETJigsaw.addPieces(
                    access,
                    new JigsawConfiguration(() -> access.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
                            .get(new ResourceLocation(Reference.MODID, file)),
                            30),
                    PoolElementStructurePiece::new,
                    generator,
                    manager,
                    center,
                    this,
                    this.random,
                    false, // Keep false | Special boundry for villages
                    false, // Place at height map, places structure at top of land
                    heightAccessor);

            Vec3i structureCenter = this.pieces.get(0).getBoundingBox().getCenter();
            int xOffset = center.getX() - structureCenter.getX();
            int zOffset = center.getZ() - structureCenter.getZ();
            for (StructurePiece structurePiece : this.pieces) {
                structurePiece.move(xOffset, 0, zOffset);
            }
            this.createBoundingBox();
            EasyThere.LOGGER.debug("Dungeon at " +
                    this.pieces.get(0).getBoundingBox().getCenter()
            );
        }
    }
}
