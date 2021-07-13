package com.nexorel.et.content.blocks.GemRefinery;

import com.google.gson.JsonObject;
import com.nexorel.et.Registries.RecipeInit;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class GemRefiningRecipe implements IRecipe<IInventory> {

    protected final Ingredient ingredient;
    protected final Ingredient ingredient1;
    protected final ItemStack result;
    private final IRecipeType<?> type;
    private final IRecipeSerializer<?> serializer;
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
    public boolean matches(IInventory inv, World world) {
        return this.ingredient.test(inv.getItem(0)) && this.ingredient1.test(inv.getItem(1));
    }

    @Override
    public ItemStack assemble(IInventory inventory) {
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
    public IRecipeSerializer<?> getSerializer() {
        return RecipeInit.GEM_REFINING.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return RecipeInit.GEM_REFINING_TYPE;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<GemRefiningRecipe> {

        @Override
        public GemRefiningRecipe fromJson(ResourceLocation RecipeID, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(json.get("a"));
            Ingredient ingredient_1 = Ingredient.fromJson(json.get("b"));
            ResourceLocation itemID = new ResourceLocation(JSONUtils.getAsString(json, "result"));
            int count= JSONUtils.getAsInt(json, "count", 1);

            ItemStack result = new ItemStack(ForgeRegistries.ITEMS.getValue(itemID), count);

            return new GemRefiningRecipe(RecipeID, ingredient, ingredient_1, result);
        }

        @Nullable
        @Override
        public GemRefiningRecipe fromNetwork(ResourceLocation RecipeID, PacketBuffer buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            Ingredient ingredient1 = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            return new GemRefiningRecipe(RecipeID, ingredient, ingredient1, result);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, GemRefiningRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            recipe.ingredient1.toNetwork(buffer);
            buffer.writeItem(recipe.result);
        }
    }
}
