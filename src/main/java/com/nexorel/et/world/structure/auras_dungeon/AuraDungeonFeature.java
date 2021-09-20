package com.nexorel.et.world.structure.auras_dungeon;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.nexorel.et.EasyThere;
import com.nexorel.et.Reference;
import com.nexorel.et.util.SomeFunctions;
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
import net.minecraft.world.level.levelgen.feature.structures.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

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
            new MobSpawnSettings.SpawnerData(EntityType.ILLUSIONER, 100, 4, 9),
            new MobSpawnSettings.SpawnerData(EntityType.VINDICATOR, 100, 4, 9)
    );

    @Override
    public List<MobSpawnSettings.SpawnerData> getDefaultSpawnList() {
        return STRUCTURE_MONSTERS;
    }

    /**
     * Used to check for specific conditions for spawnning of structure
     */


    @Override
    protected boolean isFeatureChunk(ChunkGenerator generator, BiomeSource biomeSource, long seed, WorldgenRandom random, ChunkPos pos, Biome biome, ChunkPos chunkPos, NoneFeatureConfiguration featureConfiguration, LevelHeightAccessor accessor) {
        int chunk_x = pos.x;
        int chunk_z = pos.z;
        BlockPos centerOfChunk = new BlockPos(chunk_x * 16, 0, chunk_z * 16);

        return super.isFeatureChunk(generator, biomeSource, seed, random, pos, biome, chunkPos, featureConfiguration, accessor);
    }

    public static class FeatureStart extends StructureStart<NoneFeatureConfiguration> {

        public FeatureStart(StructureFeature<NoneFeatureConfiguration> feature, ChunkPos chunkPos, int reference, long seed) {
            super(feature, chunkPos, reference, seed);
        }

        @Override
        public void generatePieces(RegistryAccess access, ChunkGenerator generator, StructureManager manager, ChunkPos chunkPos, Biome biome, NoneFeatureConfiguration configuration, LevelHeightAccessor heightAccessor) {
            int x = chunkPos.x * 16;
            int z = chunkPos.z * 16;

            int max = 45;
            int min = 20;
            int randomNum = SomeFunctions.generateRandomIntBetweenRange(min, max, random);
            String file = "aura_dungeon/start_pool";
            BlockPos center = new BlockPos(x, randomNum, z);
            JigsawPlacement.addPieces(
                    access,
                    new JigsawConfiguration(() -> access.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY)
                            .get(new ResourceLocation(Reference.MODID, file)),
                            15),
                    PoolElementStructurePiece::new,
                    generator,
                    manager,
                    center,
                    this,
                    this.random,
                    false,
                    false, heightAccessor);

            // Boss and Puzzles
            String file2 = "aura_dungeon/boss_pool";

            Vec3i structureCenter = this.pieces.get(0).getBoundingBox().getCenter();
            int xOffset = center.getX() - structureCenter.getX();
            int zOffset = center.getZ() - structureCenter.getZ();
            for (StructurePiece structurePiece : this.pieces) {
                structurePiece.move(xOffset, 0, zOffset);
            }
            this.createBoundingBox();

            EasyThere.LOGGER.info("Dungeon at " +
                    this.pieces.get(0).getBoundingBox().getCenter()
            );
        }
    }
}
