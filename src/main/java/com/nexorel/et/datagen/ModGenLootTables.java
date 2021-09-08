package com.nexorel.et.datagen;

import com.nexorel.et.Registries.BlockInit;
import com.nexorel.et.Registries.ItemInit;
import net.minecraft.data.DataGenerator;

public class ModGenLootTables extends BaseLootTableProvider {

    public ModGenLootTables(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected void addTables() {
        lootTables.put(BlockInit.AGRIYELITE_BLOCK.get(), createGemOresTable("agriyelite_block", BlockInit.AGRIYELITE_BLOCK.get(), ItemInit.AGRIYELITE_ITEM.get()));
        lootTables.put(BlockInit.AQUOMITE_BLOCK.get(), createGemOresTable("aquomite_block", BlockInit.AQUOMITE_BLOCK.get(), ItemInit.AQUOMITE_ITEM.get()));
        lootTables.put(BlockInit.GOLD_ALMAO_BLOCK.get(), createGemOresTable("gold_almao_block", BlockInit.GOLD_ALMAO_BLOCK.get(), ItemInit.GOLD_ALMAO_ITEM.get()));
        lootTables.put(BlockInit.ORANGE_RED_TEMARELITE_BLOCK.get(), createGemOresTable("orange_red_temarelite_block", BlockInit.ORANGE_RED_TEMARELITE_BLOCK.get(), ItemInit.ORANGE_RED_TEMARELITE_ITEM.get()));
        lootTables.put(BlockInit.CRYOPHANITE_BLOCK.get(), createGemOresTable("cryophanite_block", BlockInit.CRYOPHANITE_BLOCK.get(), ItemInit.CRYOPHANITE_ITEM.get()));
        lootTables.put(BlockInit.PEACH_EKANESIA_BLOCK.get(), createGemOresTable("peach_ekanesia_block", BlockInit.PEACH_EKANESIA_BLOCK.get(), ItemInit.PEACH_EKANESIA_ITEM.get()));
        lootTables.put(BlockInit.CRIMSON_PECTENE_BLOCK.get(), createGemOresTable("crimson_pectene_block", BlockInit.CRIMSON_PECTENE_BLOCK.get(), ItemInit.CRIMSON_PECTENE_ITEM.get()));
        lootTables.put(BlockInit.BLUE_VIOLET_AEGIDONYX_BLOCK.get(), createGemOresTable("blue_violet_aegidonyx_block", BlockInit.BLUE_VIOLET_AEGIDONYX_BLOCK.get(), ItemInit.BLUE_VIOLET_AEGIDONYX_ITEM.get()));
        lootTables.put(BlockInit.ELECTRIC_BLUE_CYPBERITE_BLOCK.get(), createGemOresTable("electric_blue_cypberite_block", BlockInit.ELECTRIC_BLUE_CYPBERITE_BLOCK.get(), ItemInit.ELECTRIC_BLUE_CYPBERITE_ITEM.get()));
        lootTables.put(BlockInit.TWINKLING_BREADITE_BLOCK.get(), createGemOresTable("twinkling_breadite_block", BlockInit.TWINKLING_BREADITE_BLOCK.get(), ItemInit.TWINKLING_BREADITE_ITEM.get()));
        lootTables.put(BlockInit.SALMON_LINADINGERITE_BLOCK.get(), createGemOresTable("salmon_linadingerite_block", BlockInit.SALMON_LINADINGERITE_BLOCK.get(), ItemInit.SALMON_LINADINGERITE_ITEM.get()));
        lootTables.put(BlockInit.BLUE_RAPMONY_BLOCK.get(), createGemOresTable("blue_rapmony_block", BlockInit.BLUE_RAPMONY_BLOCK.get(), ItemInit.BLUE_RAPMONY_ITEM.get()));
        lootTables.put(BlockInit.VIOLET_TUNORADOITE_BLOCK.get(), createGemOresTable("violet_tunoradoite_block", BlockInit.VIOLET_TUNORADOITE_BLOCK.get(), ItemInit.VIOLET_TUNORADOITE_ITEM.get()));
        lootTables.put(BlockInit.MAGENTA_ROSE_LOLLNIC_BLOCK.get(), createGemOresTable("magenta_rose_lollnic_block", BlockInit.MAGENTA_ROSE_LOLLNIC_BLOCK.get(), ItemInit.MAGENTA_ROSE_LOLLNIC_ITEM.get()));
        lootTables.put(BlockInit.JADE_PETAOGOPITE_BLOCK.get(), createGemOresTable("jade_petaogopite_block", BlockInit.JADE_PETAOGOPITE_BLOCK.get(), ItemInit.JADE_PETAOGOPITE_ITEM.get()));
    }
}
