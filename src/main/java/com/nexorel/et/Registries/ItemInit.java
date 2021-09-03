package com.nexorel.et.Registries;

import com.nexorel.et.content.items.ModSpawnEggItem;
import com.nexorel.et.content.items.Talismans.VenomProtectionTalismanItem;
import com.nexorel.et.content.items.Weapons.AuraWand;
import com.nexorel.et.content.items.talisBag.TalismanBagItem;
import net.minecraft.ChatFormatting;
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
import java.util.List;

import static com.nexorel.et.EasyThere.EASY_THERE;
import static com.nexorel.et.Reference.MOD_ID;

public class ItemInit {

    private static Item.Properties properties = new Item.Properties().tab(EASY_THERE);

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

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
    public static final RegistryObject<Item> Agriyelite = ITEMS.register("agriyelite", () -> new Item(properties));
    public static final RegistryObject<Item> Aquomite = ITEMS.register("aquomite", () -> new Item(properties));
    public static final RegistryObject<Item> Gold_Almao = ITEMS.register("gold_almao", () -> new Item(properties));
    public static final RegistryObject<Item> Orange_Red_Temarelite = ITEMS.register("orange_red_temarelite", () -> new Item(properties));
    public static final RegistryObject<Item> Cryophanite = ITEMS.register("cryophanite", () -> new Item(properties));
    public static final RegistryObject<Item> Peach_Ekanesia = ITEMS.register("peach_ekanesia", () -> new Item(properties));
    public static final RegistryObject<Item> Crimson_Pectene = ITEMS.register("crimson_pectene", () -> new Item(properties));
    public static final RegistryObject<Item> Blue_Violet_Aegidonyx = ITEMS.register("blue_violet_aegidonyx", () -> new Item(properties));
    public static final RegistryObject<Item> Electric_Blue_Cypberite = ITEMS.register("electric_blue_cypberite", () -> new Item(properties));
    public static final RegistryObject<Item> Twinkling_Breadite = ITEMS.register("twinkling_breadite", () -> new Item(properties));
    public static final RegistryObject<Item> Salmon_Linadingerite = ITEMS.register("salmon_linadingerite", () -> new Item(properties) {
        @Override
        public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
            super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
            p_41423_.add(new TextComponent(ChatFormatting.DARK_PURPLE + "The Mod author was also not able to find out why he added this.. "));
        }
    });
    public static final RegistryObject<Item> Blue_Rapmony = ITEMS.register("blue_rapmony", () -> new Item(properties));
    public static final RegistryObject<Item> Violet_Tunoradoite = ITEMS.register("violet_tunoradoite", () -> new Item(properties));
    public static final RegistryObject<Item> Magenta_Rose_Lollnic = ITEMS.register("magenta_rose_lollnic", () -> new Item(properties));
    public static final RegistryObject<Item> Jade_Petaogopite = ITEMS.register("jade_petaogopite", () -> new Item(properties));

}
