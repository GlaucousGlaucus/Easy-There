package com.nexorel.et.world;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.nexorel.et.EasyThere;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.*;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.mutable.MutableObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static com.nexorel.et.Reference.MODID;

/**
 * Credit : Yungs better strongholds (Piece count and priority check)
 * <a href="https://github.com/TelepathicGrunt/YUNGs-Better-Strongholds/blob/fabric-1.17/src/main/java/com/yungnickyoung/minecraft/betterstrongholds/world/jigsaw/JigsawManager.java">YUNGs better strongholds</a>
 * <p>
 * Other Code Copied from JigsawPlacement
 *
 * @see JigsawPlacement
 **/
public class ETJigsaw {
    static final Logger LOGGER = LogManager.getLogger();

    public static void addPieces(RegistryAccess p_161613_, JigsawConfiguration config, JigsawPlacement.PieceFactory p_161615_, ChunkGenerator p_161616_, StructureManager p_161617_, BlockPos p_161618_, StructurePieceAccessor accessor, Random p_161620_, boolean p_161621_, boolean p_161622_, LevelHeightAccessor p_161623_) {
        StructureFeature.bootstrap();
        List<PoolElementStructurePiece> list = Lists.newArrayList();
        Registry<StructureTemplatePool> registry = p_161613_.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
        Rotation rotation = Rotation.getRandom(p_161620_);
        StructureTemplatePool structuretemplatepool = config.startPool().get();
        StructurePoolElement structurepoolelement = structuretemplatepool.getRandomTemplate(p_161620_);
        if (structurepoolelement != EmptyPoolElement.INSTANCE) {
            PoolElementStructurePiece poolelementstructurepiece = p_161615_.create(p_161617_, structurepoolelement, p_161618_, structurepoolelement.getGroundLevelDelta(), rotation, structurepoolelement.getBoundingBox(p_161617_, p_161618_, rotation));
            BoundingBox boundingbox = poolelementstructurepiece.getBoundingBox();
            int i = (boundingbox.maxX() + boundingbox.minX()) / 2;
            int j = (boundingbox.maxZ() + boundingbox.minZ()) / 2;
            int k;
            if (p_161622_) {
                k = p_161618_.getY() + p_161616_.getFirstFreeHeight(i, j, Heightmap.Types.WORLD_SURFACE_WG, p_161623_);
            } else {
                k = p_161618_.getY();
            }

            int l = boundingbox.minY() + poolelementstructurepiece.getGroundLevelDelta();
            poolelementstructurepiece.move(0, k - l, 0);
            list.add(poolelementstructurepiece);
            if (config.maxDepth() > 0) {
                AABB aabb = new AABB(i - 80, k - 80, j - 80, i + 80 + 1, k + 80 + 1, j + 80 + 1);
                ETJigsaw.Placer jigsawplacement$placer = new ETJigsaw.Placer(registry, config.maxDepth(), p_161615_, p_161616_, p_161617_, list, p_161620_);
                jigsawplacement$placer.placing.addLast(new ETJigsaw.PieceState(poolelementstructurepiece, new MutableObject<>(Shapes.join(Shapes.create(aabb), Shapes.create(AABB.of(boundingbox)), BooleanOp.ONLY_FIRST)), k + 80, 0));

                while (!jigsawplacement$placer.placing.isEmpty()) {
                    ETJigsaw.PieceState jigsawplacement$piecestate = jigsawplacement$placer.placing.removeFirst();
                    jigsawplacement$placer.tryPlacingChildren(jigsawplacement$piecestate.piece, jigsawplacement$piecestate.free, jigsawplacement$piecestate.boundsTop, jigsawplacement$piecestate.depth, p_161621_, p_161623_);
                }
                /*for (PoolElementStructurePiece piece : list) {
                    EasyThere.LOGGER.info(piece.getElement().toString());
                    if (piece.getElement().toString().equals("Single[Left[et:rooms/dungeon_puzzle_room_a]]")) {
//                    if (piece.getElement().toString().equals("Single[Left[et:rooms/dungeon_corridor_size_small]]")) {
                        EasyThere.LOGGER.info(true);
                        list.remove(piece);
                        placed.add(piece);
                    }
                }*/
//                list.addAll(placed);
                Set<PoolElementStructurePiece> placed = new LinkedHashSet<>(list);
                list.clear();
                list.addAll(placed);
                list.forEach(accessor::addPiece);
            }
        }
    }

    public static void addPieces(RegistryAccess p_161625_, PoolElementStructurePiece p_161626_, int p_161627_, JigsawPlacement.PieceFactory p_161628_, ChunkGenerator p_161629_, StructureManager p_161630_, List<? super PoolElementStructurePiece> p_161631_, Random p_161632_, LevelHeightAccessor p_161633_) {
        Registry<StructureTemplatePool> registry = p_161625_.registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
        ETJigsaw.Placer jigsawplacement$placer = new ETJigsaw.Placer(registry, p_161627_, p_161628_, p_161629_, p_161630_, p_161631_, p_161632_);
        jigsawplacement$placer.placing.addLast(new ETJigsaw.PieceState(p_161626_, new MutableObject<>(Shapes.INFINITY), 0, 0));

        while (!jigsawplacement$placer.placing.isEmpty()) {
            ETJigsaw.PieceState jigsawplacement$piecestate = jigsawplacement$placer.placing.removeFirst();
            jigsawplacement$placer.tryPlacingChildren(jigsawplacement$piecestate.piece, jigsawplacement$piecestate.free, jigsawplacement$piecestate.boundsTop, jigsawplacement$piecestate.depth, false, p_161633_);
        }

    }

    public interface PieceFactory {
        PoolElementStructurePiece create(StructureManager p_68965_, StructurePoolElement p_68966_, BlockPos p_68967_, int p_68968_, Rotation p_68969_, BoundingBox p_68970_);
    }

    static final class PieceState {
        final PoolElementStructurePiece piece;
        final MutableObject<VoxelShape> free;
        final int boundsTop;
        final int depth;

        PieceState(PoolElementStructurePiece p_68976_, MutableObject<VoxelShape> p_68977_, int p_68978_, int p_68979_) {
            this.piece = p_68976_;
            this.free = p_68977_;
            this.boundsTop = p_68978_;
            this.depth = p_68979_;
        }
    }

    static final class Placer {
        private final Registry<StructureTemplatePool> pools;
        private final int maxDepth;
        private final JigsawPlacement.PieceFactory factory;
        private final ChunkGenerator chunkGenerator;
        private final StructureManager structureManager;
        private final List<? super PoolElementStructurePiece> pieces;
        private final Random random;
        final Deque<ETJigsaw.PieceState> placing = Queues.newArrayDeque();
        private final Map<ResourceLocation, Integer> pieceCounts;

        Placer(Registry<StructureTemplatePool> pools, int maxDepth, JigsawPlacement.PieceFactory factory, ChunkGenerator generator, StructureManager manager, List<? super PoolElementStructurePiece> pieces, Random random) {
            this.pools = pools;
            this.maxDepth = maxDepth;
            this.factory = factory;
            this.chunkGenerator = generator;
            this.structureManager = manager;
            this.pieces = pieces;
            this.random = random;
            this.pieceCounts = new HashMap<>();
            this.pieceCounts.put(new ResourceLocation(MODID, "rooms/dungeon_puzzle_room_a"), 1);
        }

        void tryPlacingChildren(PoolElementStructurePiece piece, MutableObject<VoxelShape> p_161638_, int bounds, int depth, boolean bool, LevelHeightAccessor accessor) {
            StructurePoolElement structurepoolelement = piece.getElement();
            BlockPos blockpos = piece.getPosition();
            Rotation rotation = piece.getRotation();
            StructureTemplatePool.Projection structuretemplatepool$projection = structurepoolelement.getProjection();
            boolean projectionIsRigid = structuretemplatepool$projection == StructureTemplatePool.Projection.RIGID;
            MutableObject<VoxelShape> mutableobject = new MutableObject<>();
            BoundingBox boundingbox = piece.getBoundingBox();
            int bbMinY = boundingbox.minY();

            label139:
            for (StructureTemplate.StructureBlockInfo structure_block_info : structurepoolelement.getShuffledJigsawBlocks(this.structureManager, blockpos, rotation, this.random)) {
                Direction direction = JigsawBlock.getFrontFacing(structure_block_info.state);
                BlockPos blockpos1 = structure_block_info.pos;
                BlockPos blockpos2 = blockpos1.relative(direction);
                int j = blockpos1.getY() - bbMinY;
                int k = -1;
                ResourceLocation pool = new ResourceLocation(structure_block_info.nbt.getString("pool"));
                Optional<StructureTemplatePool> poolsOptional = this.pools.getOptional(pool);
                // Checks if the pool is not empty
                if (poolsOptional.isPresent() && (poolsOptional.get().size() != 0 || Objects.equals(pool, Pools.EMPTY.location()))) {
                    ResourceLocation fallback = poolsOptional.get().getFallback();
                    Optional<StructureTemplatePool> fallbackOptional = this.pools.getOptional(fallback);
                    // Checks if fallback is not empty
                    if (fallbackOptional.isPresent() && (fallbackOptional.get().size() != 0 || Objects.equals(fallback, Pools.EMPTY.location()))) {
                        // Checks if the block is in the bounding box of the structure ?
                        boolean flag1 = boundingbox.isInside(blockpos2);
                        MutableObject<VoxelShape> mutableobject1;
                        int l;
                        if (flag1) {
                            mutableobject1 = mutableobject;
                            l = bbMinY;
                            if (mutableobject.getValue() == null) {
                                mutableobject.setValue(Shapes.create(AABB.of(boundingbox)));
                            }
                        } else {
                            mutableobject1 = p_161638_;
                            l = bounds;
                        }

                        // () -> Single[Left[et:rooms/dungeon_puzzle_room_a]]

                        List<StructurePoolElement> list = Lists.newArrayList();
                        if (depth != this.maxDepth) {
                            List<StructurePoolElement> test = poolsOptional.get().getShuffledTemplates(this.random);
                            List<StructurePoolElement> nl = new ArrayList<>(test);
                            for (StructurePoolElement element : test) {
                                String loc = element.toString().replace("Single[Left[", "").replace("]]", "");
                                loc = loc.strip();
                                ResourceLocation location = new ResourceLocation(loc);
                                if (location.equals(new ResourceLocation(MODID, "rooms/dungeon_puzzle_room_a"))) {
                                    if (depth >= 1) {
                                        if (this.pieceCounts.get(new ResourceLocation(MODID, "rooms/dungeon_puzzle_room_a")) > 0) {
                                            nl.add(element);
                                        }
                                        continue;
                                    } else {
                                        continue;
                                    }
                                }
                                nl.add(element);
                            }
                            list.addAll(nl);
                        }
                        list.addAll(fallbackOptional.get().getShuffledTemplates(this.random));

                        for (StructurePoolElement structurepoolelement1 : list) {
                            if (structurepoolelement1 == EmptyPoolElement.INSTANCE) {
                                break;
                            }
                            String loc = structurepoolelement1.toString().replace("Single[Left[", "").replace("]]", "");
                            loc = loc.strip();
                            ResourceLocation location = new ResourceLocation(loc);
                            if (this.pieceCounts.containsKey(location)) {
                                if (this.pieceCounts.get(location) <= 0) {
                                    continue;
                                }
                            }

                            for (Rotation rotation1 : Rotation.getShuffled(this.random)) {
                                List<StructureTemplate.StructureBlockInfo> list1 = structurepoolelement1.getShuffledJigsawBlocks(this.structureManager, BlockPos.ZERO, rotation1, this.random);
                                BoundingBox boundingbox1 = structurepoolelement1.getBoundingBox(this.structureManager, BlockPos.ZERO, rotation1);
                                int i1;
                                if (bool && boundingbox1.getYSpan() <= 16) {
                                    i1 = list1.stream().mapToInt((p_69032_) -> {
                                        if (!boundingbox1.isInside(p_69032_.pos.relative(JigsawBlock.getFrontFacing(p_69032_.state)))) {
                                            return 0;
                                        } else {
                                            ResourceLocation pool1 = new ResourceLocation(p_69032_.nbt.getString("pool"));
                                            Optional<StructureTemplatePool> poolsOptional1 = this.pools.getOptional(pool1);
                                            Optional<StructureTemplatePool> optional3 = poolsOptional1.flatMap((p_161646_) -> this.pools.getOptional(p_161646_.getFallback()));
                                            int k3 = poolsOptional1.map((p_161644_) -> p_161644_.getMaxSize(this.structureManager)).orElse(0);
                                            int l3 = optional3.map((p_161635_) -> p_161635_.getMaxSize(this.structureManager)).orElse(0);
                                            return Math.max(k3, l3);
                                        }
                                    }).max().orElse(0);
                                } else {
                                    i1 = 0;
                                }

                                for (StructureTemplate.StructureBlockInfo structuretemplate$structureblockinfo1 : list1) {
                                    if (JigsawBlock.canAttach(structure_block_info, structuretemplate$structureblockinfo1)) {
                                        BlockPos blockpos3 = structuretemplate$structureblockinfo1.pos;
                                        BlockPos blockpos4 = blockpos2.subtract(blockpos3);
                                        BoundingBox boundingbox2 = structurepoolelement1.getBoundingBox(this.structureManager, blockpos4, rotation1);
                                        int j1 = boundingbox2.minY();
                                        StructureTemplatePool.Projection structuretemplatepool$projection1 = structurepoolelement1.getProjection();
                                        boolean flag2 = structuretemplatepool$projection1 == StructureTemplatePool.Projection.RIGID;
                                        int k1 = blockpos3.getY();
                                        int l1 = j - k1 + JigsawBlock.getFrontFacing(structure_block_info.state).getStepY();
                                        int i2;
                                        if (projectionIsRigid && flag2) {
                                            i2 = bbMinY + l1;
                                        } else {
                                            if (k == -1) {
                                                k = this.chunkGenerator.getFirstFreeHeight(blockpos1.getX(), blockpos1.getZ(), Heightmap.Types.WORLD_SURFACE_WG, accessor);
                                            }

                                            i2 = k - k1;
                                        }

                                        int j2 = i2 - j1;
                                        BoundingBox boundingbox3 = boundingbox2.moved(0, j2, 0);
                                        BlockPos blockpos5 = blockpos4.offset(0, j2, 0);
                                        if (i1 > 0) {
                                            int k2 = Math.max(i1 + 1, boundingbox3.maxY() - boundingbox3.minY());
                                            boundingbox3.encapsulate(new BlockPos(boundingbox3.minX(), boundingbox3.minY() + k2, boundingbox3.minZ()));
                                        }

                                        if (!Shapes.joinIsNotEmpty(mutableobject1.getValue(), Shapes.create(AABB.of(boundingbox3).deflate(0.25D)), BooleanOp.ONLY_SECOND)) {
                                            mutableobject1.setValue(Shapes.joinUnoptimized(mutableobject1.getValue(), Shapes.create(AABB.of(boundingbox3)), BooleanOp.ONLY_FIRST));
                                            int j3 = piece.getGroundLevelDelta();
                                            int l2;
                                            if (flag2) {
                                                l2 = j3 - l1;
                                            } else {
                                                l2 = structurepoolelement1.getGroundLevelDelta();
                                            }

                                            PoolElementStructurePiece poolelementstructurepiece = this.factory.create(this.structureManager, structurepoolelement1, blockpos5, l2, rotation1, boundingbox3);
                                            int i3;
                                            if (projectionIsRigid) {
                                                i3 = bbMinY + j;
                                            } else if (flag2) {
                                                i3 = i2 + k1;
                                            } else {
                                                if (k == -1) {
                                                    k = this.chunkGenerator.getFirstFreeHeight(blockpos1.getX(), blockpos1.getZ(), Heightmap.Types.WORLD_SURFACE_WG, accessor);
                                                }

                                                i3 = k + l1 / 2;
                                            }

                                            piece.addJunction(new JigsawJunction(blockpos2.getX(), i3 - j + j3, blockpos2.getZ(), l1, structuretemplatepool$projection1));
                                            poolelementstructurepiece.addJunction(new JigsawJunction(blockpos1.getX(), i3 - k1 + l2, blockpos1.getZ(), -l1, structuretemplatepool$projection));
                                            this.pieces.add(poolelementstructurepiece);
                                            if (this.pieceCounts.containsKey(location)) {
                                                this.pieceCounts.put(location, this.pieceCounts.get(location) - 1);
                                            }
                                            if (depth + 1 <= this.maxDepth) {
                                                this.placing.addLast(new ETJigsaw.PieceState(poolelementstructurepiece, mutableobject1, l, depth + 1));
                                            }
                                            continue label139;
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        EasyThere.LOGGER.warn("Empty or non-existent fallback pool: {}", fallback);
                    }
                } else {
                    EasyThere.LOGGER.warn("Empty or non-existent pool: {}", pool);
                }
            }
        }

        private List<StructurePoolElement> processList(List<StructurePoolElement> elements) {
            List<StructurePoolElement> newList = new ArrayList<>(elements);
            for (StructurePoolElement element : elements) {
                if (element.toString().equals("Single[Left[et:rooms/dungeon_puzzle_room_a]]")) {
                    EasyThere.LOGGER.info("True");
                    newList.remove(element);
                }
            }
            return newList;
        }
    }
}
