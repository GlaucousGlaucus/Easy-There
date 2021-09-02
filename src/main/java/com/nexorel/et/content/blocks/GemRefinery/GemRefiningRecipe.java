package com.nexorel.et.content.blocks.GemRefinery;

import com.google.gson.JsonObject;
import com.nexorel.et.Registries.RecipeInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class GemRefiningRecipe implements Recipe<Container> {

    protected final Ingredient ingredient;
    protected final Ingredient ingredient1;
    protected final ItemStack result;
    private final RecipeType<?> type;
    private final RecipeSerializer<?> serializer;
    protected final ResourceLocation id;


    public GemRefiningRecipe(ResourceLocation recipeid, Ingredient ingredient, Ingredient ingredient1, ItemStack result) {
        this.ingredient = ingredient;
        this.ingredient1 = ingredient1;
        this.result = result;
        this.type = RecipeInit.GEM_REFINING_TYPE;
        this.serializer = RecipeInit.GEM_REFINING.get();
        this.id = recipeid;
    }

    @Override
    public boolean matches(Container inv, Level world) {
        return this.ingredient.test(inv.getItem(0)) && this.ingredient1.test(inv.getItem(1));
    }

    @Override
    public ItemStack assemble(Container inventory) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int p_194133_1_, int p_194133_2_) {
        return true;
    }

    public boolean isAdditionIngredient(ItemStack p_241456_1_) {
        return this.ingredient1.test(p_241456_1_);
    }

    @Override
    public ItemStack getResultItem() {
        return this.result;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeInit.GEM_REFINING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeInit.GEM_REFINING_TYPE;
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<GemRefiningRecipe> {

        @Override
        public GemRefiningRecipe fromJson(ResourceLocation RecipeID, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(json.get("a"));
            Ingredient ingredient_1 = Ingredient.fromJson(json.get("b"));
            ResourceLocation itemID = new ResourceLocation(GsonHelper.getAsString(json, "result"));
            int count = GsonHelper.getAsInt(json, "count", 1);

            ItemStack result = new ItemStack(ForgeRegistries.ITEMS.getValue(itemID), count);

            return new GemRefiningRecipe(RecipeID, ingredient, ingredient_1, result);
        }

        @Nullable
        @Override
        public GemRefiningRecipe fromNetwork(ResourceLocation RecipeID, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            Ingredient ingredient1 = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            return new GemRefiningRecipe(RecipeID, ingredient, ingredient1, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, GemRefiningRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            recipe.ingredient1.toNetwork(buffer);
            buffer.writeItem(recipe.result);
        }
    }
}
