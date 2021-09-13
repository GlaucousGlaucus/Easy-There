package com.nexorel.et.Registries;

import com.nexorel.et.content.blocks.GemRefinery.GemRefineryTile;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.nexorel.et.Reference.MOD_ID;

public class BlockEntityInit {

    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MOD_ID);

    public static void init() {
        TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    // Tile Entites

    public static final RegistryObject<BlockEntityType<GemRefineryTile>> GEM_REFINERY_TILE = TILE_ENTITIES.register("gem_refinery", () -> BlockEntityType.Builder.of(GemRefineryTile::new, BlockInit.GEM_REFINERY.get()).build(null));
//    public static final RegistryObject<BlockEntityType<PitTrapTile>> PIT_TRAP_TILE = TILE_ENTITIES.register("dungeon_pitfall_trap", () -> BlockEntityType.Builder.of(PitTrapTile::new, BlockInit.DUNGEON_PIT_TRAP.get()).build(null));
//    public static final RegistryObject<TileEntityType<AuraInfestedTile>> AURA_INFESTED_TILE = TILE_ENTITIES.register("aura_infested_block", () -> TileEntityType.Builder.of(AuraInfestedTile::new, BlockInit.AURA_INFESTED_BLOCK.get()).build(null));

}
