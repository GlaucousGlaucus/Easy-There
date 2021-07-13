package com.nexorel.et.Registries;

import com.nexorel.et.content.items.ModSpawnEggItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

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

    // BlockItems

    public static final RegistryObject<Item> DELERITE_CRYSTAL_BLOCK_ITEM = ITEMS.register("delerite_crystal_block", () -> new BlockItem(BlockInit.DELERITE_CRYSTAL_BLOCK.get(), properties));
    public static final RegistryObject<Item> GEM_REFINERY = ITEMS.register("gem_refinery", () -> new BlockItem(BlockInit.GEM_REFINERY.get(), properties));

    // Spawn Eggs

    public static final RegistryObject<Item> AURA_SPAWN_EGG = ITEMS.register("aura_spawn_egg", ModSpawnEggItem::new);

}
