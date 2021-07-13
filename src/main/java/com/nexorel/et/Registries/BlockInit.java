package com.nexorel.et.Registries;

import com.nexorel.et.content.blocks.GemRefinery.GemRefineryBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.nexorel.et.Reference.MOD_ID;

public class BlockInit {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);

    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    // Blocks

    public static final RegistryObject<Block> DELERITE_CRYSTAL_BLOCK = BLOCKS.register("delerite_crystal_block", () -> new Block(
            Block.Properties.of(Material.GLASS)
                    .sound(SoundType.GLASS)
    ));

    public static final RegistryObject<Block> GEM_REFINERY = BLOCKS.register("gem_refinery", GemRefineryBlock::new);

}
