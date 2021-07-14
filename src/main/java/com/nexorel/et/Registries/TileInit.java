package com.nexorel.et.Registries;

import com.nexorel.et.content.blocks.AuraInfestedBlock.AuraInfestedTile;
import com.nexorel.et.content.blocks.GemRefinery.GemRefineryTile;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.nexorel.et.Reference.MOD_ID;

public class TileInit {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MOD_ID);

    public static void init() {
        TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    // Tile Entites

    public static final RegistryObject<TileEntityType<GemRefineryTile>> GEM_REFINERY_TILE = TILE_ENTITIES.register("gem_refinery", () -> TileEntityType.Builder.of(GemRefineryTile::new, BlockInit.GEM_REFINERY.get()).build(null));
    public static final RegistryObject<TileEntityType<AuraInfestedTile>> AURA_INFESTED_TILE = TILE_ENTITIES.register("aura_infested_block", () -> TileEntityType.Builder.of(AuraInfestedTile::new, BlockInit.AURA_INFESTED_BLOCK.get()).build(null));

}
