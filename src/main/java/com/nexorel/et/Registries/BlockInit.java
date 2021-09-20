package com.nexorel.et.Registries;

import com.nexorel.et.content.blocks.GemOreBlock;
import com.nexorel.et.content.blocks.GemRefinery.GemRefineryBlock;
import com.nexorel.et.content.blocks.dungeon.DungeonBlock;
import com.nexorel.et.content.blocks.dungeon.puzzles.quiz.QuestionBlock;
import com.nexorel.et.content.blocks.dungeon.traps.ArrowTrap.ArrowTrap;
import com.nexorel.et.content.blocks.dungeon.traps.InstaKiller;
import com.nexorel.et.content.blocks.dungeon.traps.InvisPressurePlate;
import com.nexorel.et.content.blocks.dungeon.traps.PitTrap;
import com.nexorel.et.content.blocks.dungeon.traps.SpikeTrap.SpikeTrap;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.nexorel.et.Reference.MODID;

public class BlockInit {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    // Blocks

    public static final RegistryObject<Block> DELERITE_CRYSTAL_BLOCK = BLOCKS.register("delerite_crystal_block", () -> new Block(
            Block.Properties.of(Material.GLASS)
                    .sound(SoundType.GLASS)
    ));

    public static final RegistryObject<Block> GEM_REFINERY = BLOCKS.register("gem_refinery", GemRefineryBlock::new);
//    public static final RegistryObject<Block> AURA_INFESTED_BLOCK = BLOCKS.register("aura_infested_block", AuraInfestedBlock::new);

    // Gems
    public static final RegistryObject<Block> AGRIYELITE_BLOCK = BLOCKS.register("agriyelite_block", () -> new GemOreBlock(20));
    public static final RegistryObject<Block> AQUOMITE_BLOCK = BLOCKS.register("aquomite_block", () -> new GemOreBlock(25));
    public static final RegistryObject<Block> GOLD_ALMAO_BLOCK = BLOCKS.register("gold_almao_block", () -> new GemOreBlock(23));
    public static final RegistryObject<Block> ORANGE_RED_TEMARELITE_BLOCK = BLOCKS.register("orange_red_temarelite_block", () -> new GemOreBlock(43));
    public static final RegistryObject<Block> CRYOPHANITE_BLOCK = BLOCKS.register("cryophanite_block", () -> new GemOreBlock(25));
    public static final RegistryObject<Block> PEACH_EKANESIA_BLOCK = BLOCKS.register("peach_ekanesia_block", () -> new GemOreBlock(27));
    public static final RegistryObject<Block> CRIMSON_PECTENE_BLOCK = BLOCKS.register("crimson_pectene_block", () -> new GemOreBlock(32));
    public static final RegistryObject<Block> BLUE_VIOLET_AEGIDONYX_BLOCK = BLOCKS.register("blue_violet_aegidonyx_block", () -> new GemOreBlock(23));
    public static final RegistryObject<Block> ELECTRIC_BLUE_CYPBERITE_BLOCK = BLOCKS.register("electric_blue_cypberite_block", () -> new GemOreBlock(43));
    public static final RegistryObject<Block> TWINKLING_BREADITE_BLOCK = BLOCKS.register("twinkling_breadite_block", () -> new GemOreBlock(34));
    public static final RegistryObject<Block> SALMON_LINADINGERITE_BLOCK = BLOCKS.register("salmon_linadingerite_block", () -> new GemOreBlock(54));
    public static final RegistryObject<Block> BLUE_RAPMONY_BLOCK = BLOCKS.register("blue_rapmony_block", () -> new GemOreBlock(54));
    public static final RegistryObject<Block> VIOLET_TUNORADOITE_BLOCK = BLOCKS.register("violet_tunoradoite_block", () -> new GemOreBlock(56));
    public static final RegistryObject<Block> MAGENTA_ROSE_LOLLNIC_BLOCK = BLOCKS.register("magenta_rose_lollnic_block", () -> new GemOreBlock(76));
    public static final RegistryObject<Block> JADE_PETAOGOPITE_BLOCK = BLOCKS.register("jade_petaogopite_block", () -> new GemOreBlock(23));

    // Dungeon Blocks
    public static final RegistryObject<Block> DUNGEON_CHISELED_POLISHED_BLACKSTONE = BLOCKS.register("dungeon_chiseled_polished_blackstone", DungeonBlock::new);
    public static final RegistryObject<Block> DUNGEON_CRACKED_POLISHED_BLACKSTONE_BRICKS = BLOCKS.register("dungeon_cracked_polished_blackstone_bricks", DungeonBlock::new);
    public static final RegistryObject<Block> DUNGEON_GILDED_BLACKSTONE = BLOCKS.register("dungeon_gilded_blackstone", DungeonBlock::new);
    public static final RegistryObject<Block> DUNGEON_POLISHED_BLACKSTONE = BLOCKS.register("dungeon_polished_blackstone", DungeonBlock::new);
    public static final RegistryObject<Block> DUNGEON_POLISHED_BLACKSTONE_BRICKS = BLOCKS.register("dungeon_polished_blackstone_bricks", DungeonBlock::new);
    public static final RegistryObject<Block> DUNGEON_STONE = BLOCKS.register("dungeon_stone", DungeonBlock::new);

    public static final RegistryObject<Block> DUNGEON_PIT_TRAP = BLOCKS.register("dungeon_pitfall_trap", PitTrap::new);
    public static final RegistryObject<Block> INSTA_KILLER = BLOCKS.register("insta_killer", InstaKiller::new);
    public static final RegistryObject<Block> IPP = BLOCKS.register("ipp", InvisPressurePlate::new);
    public static final RegistryObject<Block> ARROW_TRAP = BLOCKS.register("arrow_trap", ArrowTrap::new);
    public static final RegistryObject<Block> SPIKE_TRAP = BLOCKS.register("spike_trap", SpikeTrap::new);
    public static final RegistryObject<Block> QUESTION_BLOCK = BLOCKS.register("question_block", QuestionBlock::new);

}
