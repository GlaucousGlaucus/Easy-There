package com.nexorel.et.Registries;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.nexorel.et.Reference.MODID;

public class FeaturesInit {

    public static final DeferredRegister<Feature<?>> FEATURE = DeferredRegister.create(ForgeRegistries.FEATURES, MODID);

    public static void init() {
        FEATURE.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

}
