package com.nexorel.et.Registries;

import com.nexorel.et.content.blocks.GemRefinery.GemRefineryTile;
import com.nexorel.et.content.blocks.dungeon.puzzles.quiz.QuestionBE;
import com.nexorel.et.content.blocks.dungeon.traps.ArrowTrap.ArrowTrapBE;
import com.nexorel.et.content.blocks.dungeon.traps.SpikeTrap.SpikeTrapBE;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.nexorel.et.Reference.MODID;

public class BlockEntityInit {

    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MODID);

    public static void init() {
        TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    // Tile Entites

    public static final RegistryObject<BlockEntityType<GemRefineryTile>> GEM_REFINERY_TILE = TILE_ENTITIES.register("gem_refinery", () -> BlockEntityType.Builder.of(GemRefineryTile::new, BlockInit.GEM_REFINERY.get()).build(null));
    public static final RegistryObject<BlockEntityType<ArrowTrapBE>> ARROW_TRAP_TILE = TILE_ENTITIES.register("arrow_trap", () -> BlockEntityType.Builder.of(ArrowTrapBE::new, BlockInit.ARROW_TRAP.get()).build(null));
    public static final RegistryObject<BlockEntityType<SpikeTrapBE>> SPIKE_TRAP_TILE = TILE_ENTITIES.register("spike_trap", () -> BlockEntityType.Builder.of(SpikeTrapBE::new, BlockInit.SPIKE_TRAP.get()).build(null));
    public static final RegistryObject<BlockEntityType<QuestionBE>> QUESTION_TILE = TILE_ENTITIES.register("question_block", () -> BlockEntityType.Builder.of(QuestionBE::new, BlockInit.QUESTION_BLOCK.get()).build(null));
//    public static final RegistryObject<TileEntityType<AuraInfestedTile>> AURA_INFESTED_TILE = TILE_ENTITIES.register("aura_infested_block", () -> TileEntityType.Builder.of(AuraInfestedTile::new, BlockInit.AURA_INFESTED_BLOCK.get()).build(null));

}
