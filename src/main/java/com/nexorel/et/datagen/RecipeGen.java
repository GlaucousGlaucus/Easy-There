package com.nexorel.et.datagen;

import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class RecipeGen extends RecipeProvider {

    public RecipeGen(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(Items.NETHERITE_AXE)
                .pattern("aaa")
                .pattern("aba")
                .pattern("aaa")
                .define('a', Tags.Items.COBBLESTONE)
                .define('b', Tags.Items.DUSTS_REDSTONE)
                .group("et")
                .unlockedBy("cobblestone", InventoryChangeTrigger.TriggerInstance.hasItems(Blocks.COBBLESTONE))
                .save(consumer);
    }
}
