package com.nexorel.et.Registries;

import com.nexorel.et.content.Entity.boss.aura.AuraEntity;
import com.nexorel.et.content.Entity.damage_ind.DamageIndicatorEntity;
import com.nexorel.et.content.Entity.projectile.aura_blast.AuraBlast;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.nexorel.et.Reference.MOD_ID;

public class EntityInit {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, MOD_ID);

    public static void initialization() {
        ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<EntityType<AuraEntity>> AURA = ENTITIES.register("aura",
            () -> EntityType.Builder.of(AuraEntity::new, EntityClassification.MISC)
                    .fireImmune()
                    .sized(2F, 2.5F)
                    .clientTrackingRange(10)
                    .build("necron"));


    // Projectile

    public static final RegistryObject<EntityType<AuraBlast>> AURA_BLAST = ENTITIES.register("aura_blast",
            () -> EntityType.Builder.<AuraBlast>of(AuraBlast::new, EntityClassification.MISC)
                    .fireImmune()
                    .sized(0.9F, 3.5F)
                    .clientTrackingRange(10)
                    .build("aura_blast"));

    // DMGIND

    public static final RegistryObject<EntityType<DamageIndicatorEntity>> DMG_IND = ENTITIES.register("damage_indicator",
            () -> EntityType.Builder.<DamageIndicatorEntity>of(DamageIndicatorEntity::new, EntityClassification.MISC)
                    .fireImmune()
                    .sized(0.5F, 0.5F)
                    .build("damage_indicator"));

}
