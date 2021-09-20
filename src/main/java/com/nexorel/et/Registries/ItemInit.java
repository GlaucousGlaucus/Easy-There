package com.nexorel.et.Registries;

import com.nexorel.et.content.items.ModSpawnEggItem;
import com.nexorel.et.content.items.Talismans.VenomProtectionTalismanItem;
import com.nexorel.et.content.items.Weapons.AuraWand;
import com.nexorel.et.content.items.talisBag.TalismanBagItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

import static com.nexorel.et.EasyThere.EASY_THERE;
import static com.nexorel.et.Reference.MODID;

@SuppressWarnings("unused")
public class ItemInit {

    private static final Item.Properties properties = new Item.Properties().tab(EASY_THERE);

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static void init() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    // Items

    public static final RegistryObject<Item> DELERITE_CRYSTAL = ITEMS.register("delerite_crystal", () -> new Item(properties));
    public static final RegistryObject<Item> AURA_WAND = ITEMS.register("aura_wand", AuraWand::new);
    public static final RegistryObject<Item> TALIS_BAG_ITEM = ITEMS.register("talisman_bag", TalismanBagItem::new);

    // BlockItems

    public static final RegistryObject<Item> DELERITE_CRYSTAL_BLOCK_ITEM = ITEMS.register("delerite_crystal_block", () -> new BlockItem(BlockInit.DELERITE_CRYSTAL_BLOCK.get(), properties));
    public static final RegistryObject<Item> GEM_REFINERY = ITEMS.register("gem_refinery", () -> new BlockItem(BlockInit.GEM_REFINERY.get(), properties));
//    public static final RegistryObject<Item> AURA_INFESTED_BLOCK = ITEMS.register("aura_infested_block", () -> new BlockItem(BlockInit.AURA_INFESTED_BLOCK.get(), properties));

    // Spawn Eggs

    public static final RegistryObject<Item> AURA_SPAWN_EGG = ITEMS.register("aura_spawn_egg", ModSpawnEggItem::new);

    // Talismans
//    public static final RegistryObject<Item> TEST_TALISMAN = ITEMS.register("test_talisman", TalismanItem::new);
    public static final RegistryObject<Item> VENOM_TALISMAN = ITEMS.register("venom_talisman", VenomProtectionTalismanItem::new);

    // Gems
    public static final RegistryObject<Item> AGRIYELITE_ITEM = ITEMS.register("agriyelite", () -> new Item(properties));
    public static final RegistryObject<Item> AQUOMITE_ITEM = ITEMS.register("aquomite", () -> new Item(properties));
    public static final RegistryObject<Item> GOLD_ALMAO_ITEM = ITEMS.register("gold_almao", () -> new Item(properties));
    public static final RegistryObject<Item> ORANGE_RED_TEMARELITE_ITEM = ITEMS.register("orange_red_temarelite", () -> new Item(properties));
    public static final RegistryObject<Item> CRYOPHANITE_ITEM = ITEMS.register("cryophanite", () -> new Item(properties));
    public static final RegistryObject<Item> PEACH_EKANESIA_ITEM = ITEMS.register("peach_ekanesia", () -> new Item(properties));
    public static final RegistryObject<Item> CRIMSON_PECTENE_ITEM = ITEMS.register("crimson_pectene", () -> new Item(properties));
    public static final RegistryObject<Item> BLUE_VIOLET_AEGIDONYX_ITEM = ITEMS.register("blue_violet_aegidonyx", () -> new Item(properties));
    public static final RegistryObject<Item> ELECTRIC_BLUE_CYPBERITE_ITEM = ITEMS.register("electric_blue_cypberite", () -> new Item(properties));
    public static final RegistryObject<Item> TWINKLING_BREADITE_ITEM = ITEMS.register("twinkling_breadite", () -> new Item(properties));
    public static final RegistryObject<Item> SALMON_LINADINGERITE_ITEM = ITEMS.register("salmon_linadingerite", () -> new Item(properties) {
        @Override
        @ParametersAreNonnullByDefault
        public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
            super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
            if (Screen.hasShiftDown()) {
                p_41423_.add(new TextComponent(ChatFormatting.DARK_PURPLE + "The Mod author was also not able to find out why he added this.. "));
            }
        }
    });
    public static final RegistryObject<Item> BLUE_RAPMONY_ITEM = ITEMS.register("blue_rapmony", () -> new Item(properties));
    public static final RegistryObject<Item> VIOLET_TUNORADOITE_ITEM = ITEMS.register("violet_tunoradoite", () -> new Item(properties));
    public static final RegistryObject<Item> MAGENTA_ROSE_LOLLNIC_ITEM = ITEMS.register("magenta_rose_lollnic", () -> new Item(properties));
    public static final RegistryObject<Item> JADE_PETAOGOPITE_ITEM = ITEMS.register("jade_petaogopite", () -> new Item(properties));

    // Gem Block
    public static final RegistryObject<Item> AGRIYELITE_BLOCKITEM = ITEMS.register("agriyelite_block", () -> new BlockItem(BlockInit.AGRIYELITE_BLOCK.get(), properties));
    public static final RegistryObject<Item> AQUOMITE_BLOCKITEM = ITEMS.register("aquomite_block", () -> new BlockItem(BlockInit.AQUOMITE_BLOCK.get(), properties));
    public static final RegistryObject<Item> GOLD_ALMAO_BLOCKITEM = ITEMS.register("gold_almao_block", () -> new BlockItem(BlockInit.GOLD_ALMAO_BLOCK.get(), properties));
    public static final RegistryObject<Item> ORANGE_RED_TEMARELITE_BLOCKITEM = ITEMS.register("orange_red_temarelite_block", () -> new BlockItem(BlockInit.ORANGE_RED_TEMARELITE_BLOCK.get(), properties));
    public static final RegistryObject<Item> CRYOPHANITE_BLOCKITEM = ITEMS.register("cryophanite_block", () -> new BlockItem(BlockInit.CRYOPHANITE_BLOCK.get(), properties));
    public static final RegistryObject<Item> PEACH_EKANESIA_BLOCKITEM = ITEMS.register("peach_ekanesia_block", () -> new BlockItem(BlockInit.PEACH_EKANESIA_BLOCK.get(), properties));
    public static final RegistryObject<Item> CRIMSON_PECTENE_BLOCKITEM = ITEMS.register("crimson_pectene_block", () -> new BlockItem(BlockInit.CRIMSON_PECTENE_BLOCK.get(), properties));
    public static final RegistryObject<Item> BLUE_VIOLET_AEGIDONYX_BLOCKITEM = ITEMS.register("blue_violet_aegidonyx_block", () -> new BlockItem(BlockInit.BLUE_VIOLET_AEGIDONYX_BLOCK.get(), properties));
    public static final RegistryObject<Item> ELECTRIC_BLUE_CYPBERITE_BLOCKITEM = ITEMS.register("electric_blue_cypberite_block", () -> new BlockItem(BlockInit.ELECTRIC_BLUE_CYPBERITE_BLOCK.get(), properties));
    public static final RegistryObject<Item> TWINKLING_BREADITE_BLOCKITEM = ITEMS.register("twinkling_breadite_block", () -> new BlockItem(BlockInit.TWINKLING_BREADITE_BLOCK.get(), properties));
    public static final RegistryObject<Item> SALMON_LINADINGERITE_BLOCKITEM = ITEMS.register("salmon_linadingerite_block", () -> new BlockItem(BlockInit.SALMON_LINADINGERITE_BLOCK.get(), properties) {
        @Override
        @ParametersAreNonnullByDefault
        public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
            super.appendHoverText(stack, level, components, flag);
            if (Screen.hasShiftDown()) {
                components.add(new TextComponent(ChatFormatting.DARK_PURPLE + "The Mod author was also not able to find out why he added this.. "));
            }
        }
    });
    public static final RegistryObject<Item> BLUE_RAPMONY_BLOCKITEM = ITEMS.register("blue_rapmony_block", () -> new BlockItem(BlockInit.BLUE_RAPMONY_BLOCK.get(), properties));
    public static final RegistryObject<Item> VIOLET_TUNORADOITE_BLOCKITEM = ITEMS.register("violet_tunoradoite_block", () -> new BlockItem(BlockInit.VIOLET_TUNORADOITE_BLOCK.get(), properties));
    public static final RegistryObject<Item> MAGENTA_ROSE_LOLLNIC_BLOCKITEM = ITEMS.register("magenta_rose_lollnic_block", () -> new BlockItem(BlockInit.MAGENTA_ROSE_LOLLNIC_BLOCK.get(), properties));
    public static final RegistryObject<Item> JADE_PETAOGOPITE_BLOCKITEM = ITEMS.register("jade_petaogopite_block", () -> new BlockItem(BlockInit.JADE_PETAOGOPITE_BLOCK.get(), properties));

    // Dungeon
    public static final RegistryObject<Item> DUNGEON_CHISELED_POLISHED_BLACKSTONE_ITEM = ITEMS.register("dungeon_chiseled_polished_blackstone", () -> new BlockItem(BlockInit.DUNGEON_CHISELED_POLISHED_BLACKSTONE.get(), properties));
    public static final RegistryObject<Item> DUNGEON_CRACKED_POLISHED_BLACKSTONE_BRICKS_ITEM = ITEMS.register("dungeon_cracked_polished_blackstone_bricks", () -> new BlockItem(BlockInit.DUNGEON_CRACKED_POLISHED_BLACKSTONE_BRICKS.get(), properties));
    public static final RegistryObject<Item> DUNGEON_GILDED_BLACKSTONE_ITEM = ITEMS.register("dungeon_gilded_blackstone", () -> new BlockItem(BlockInit.DUNGEON_GILDED_BLACKSTONE.get(), properties));
    public static final RegistryObject<Item> DUNGEON_POLISHED_BLACKSTONE_ITEM = ITEMS.register("dungeon_polished_blackstone", () -> new BlockItem(BlockInit.DUNGEON_POLISHED_BLACKSTONE.get(), properties));
    public static final RegistryObject<Item> DUNGEON_POLISHED_BLACKSTONE_BRICKS_ITEM = ITEMS.register("dungeon_polished_blackstone_bricks", () -> new BlockItem(BlockInit.DUNGEON_POLISHED_BLACKSTONE_BRICKS.get(), properties));
    public static final RegistryObject<Item> DUNGEON_STONE_ITEM = ITEMS.register("dungeon_stone", () -> new BlockItem(BlockInit.DUNGEON_STONE.get(), properties));

    public static final RegistryObject<Item> DUNGEON_PITFALL_TRAP_ITEM = ITEMS.register("dungeon_pitfall_trap", () -> new BlockItem(BlockInit.DUNGEON_PIT_TRAP.get(), properties));
    public static final RegistryObject<Item> INSTA_KILLER_ITEM = ITEMS.register("insta_killer", () -> new BlockItem(BlockInit.INSTA_KILLER.get(), properties));
    public static final RegistryObject<Item> IPP_ITEM = ITEMS.register("ipp", () -> new BlockItem(BlockInit.IPP.get(), properties));
    public static final RegistryObject<Item> ARROW_TRAP_ITEM = ITEMS.register("arrow_trap", () -> new BlockItem(BlockInit.ARROW_TRAP.get(), properties));
    public static final RegistryObject<Item> SPIKE_TRAP_ITEM = ITEMS.register("spike_trap", () -> new BlockItem(BlockInit.SPIKE_TRAP.get(), properties));
    public static final RegistryObject<Item> QUESTION_BLOCK_ITEM = ITEMS.register("question_block", () -> new BlockItem(BlockInit.QUESTION_BLOCK.get(), properties));

}
