package com.nexorel.et.Registries;

import com.nexorel.et.LootTable.modifier.SkillBonusModifier;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.nexorel.et.Reference.MOD_ID;

public class LootInit {

//    public static final LootItemConditionType SKILL_LEVEL_BONUS = new LootItemConditionType(MiningLevelCondition.SERIALIZER);

    public static final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, MOD_ID);

    public static final RegistryObject<GlobalLootModifierSerializer<?>> SKILL_LEVEL_BONUS_A = LOOT_MODIFIERS.register("skill_bonus_drops", SkillBonusModifier.Serializer::new);

    private LootInit() {
    }

    public static void init() {
        LOOT_MODIFIERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
