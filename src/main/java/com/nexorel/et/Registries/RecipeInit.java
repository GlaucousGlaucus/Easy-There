package com.nexorel.et.Registries;

import com.nexorel.et.content.blocks.GemRefinery.GemRefiningRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.nexorel.et.Reference.MOD_ID;

public class RecipeInit {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MOD_ID);

    public static void init() {
        RECIPE_SERIALIZER.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    // Recipe Serializers
    public static final RegistryObject<RecipeSerializer<?>> GEM_REFINING = RECIPE_SERIALIZER.register("gem_refining", GemRefiningRecipe.Serializer::new);


    // Recipe Types
    public static final RecipeType<GemRefiningRecipe> GEM_REFINING_TYPE = RecipeType.register("et:gem_refining");
}
