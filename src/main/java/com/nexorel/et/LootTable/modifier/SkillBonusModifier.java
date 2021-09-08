package com.nexorel.et.LootTable.modifier;

import com.google.gson.JsonObject;
import com.nexorel.et.capabilities.MiningSkill.MiningSkill;
import com.nexorel.et.capabilities.MiningSkill.MiningSkillCapability;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;

public class SkillBonusModifier extends LootModifier {

//    private static final Tag.Named<Block> DOUBLEABLE_BLOCKS = BlockTags.createOptional(new ResourceLocation(MOD_ID, "dble_blocks"), Set.of(
//            ()-> Blocks.DIAMOND_ORE,
//            ()-> Blocks.COAL_ORE,
//            ()-> Blocks.COPPER_ORE,
//            ()-> Blocks.LAPIS_ORE,
//            ()-> Blocks.REDSTONE_ORE,
//            ()-> Blocks.GOLD_ORE,
//            ()-> Blocks.IRON_ORE,
//            ()-> Blocks.EMERALD_ORE,
//            ()-> Blocks.DEEPSLATE_DIAMOND_ORE,
//            ()-> Blocks.DEEPSLATE_COAL_ORE,
//            ()-> Blocks.DEEPSLATE_COPPER_ORE,
//            ()-> Blocks.DEEPSLATE_LAPIS_ORE,
//            ()-> Blocks.DEEPSLATE_REDSTONE_ORE,
//            ()-> Blocks.DEEPSLATE_GOLD_ORE,
//            ()-> Blocks.DEEPSLATE_IRON_ORE,
//            ()-> Blocks.DEEPSLATE_EMERALD_ORE,
//            ()-> Blocks.NETHER_QUARTZ_ORE,
//            ()-> Blocks.NETHER_GOLD_ORE,
//            ()-> Blocks.ANCIENT_DEBRIS
//    ));

    /**
     * Constructs a LootModifier.
     *
     * @param conditionsIn the ILootConditions that need to be matched before the loot is modified.
     */
    protected SkillBonusModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        Entity e = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        Player player = e instanceof Player ? (Player) e : null;
        BlockState targetState = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        if (player != null && targetState != null) {
            Block target_block = targetState.getBlock();
//            EasyThere.LOGGER.info(target_block.getTags().contains(new ResourceLocation("forge", "ores")));
            MiningSkill skill = player.getCapability(MiningSkillCapability.MINING_CAP).orElse(null);
            if (target_block.getTags().contains(new ResourceLocation("forge", "ores"))) {
                generatedLoot.forEach(itemStack -> {
                    int j = context.getRandom().nextFloat() < skill.getLevel() * 0.05F ? itemStack.getCount() * 2 : itemStack.getCount();
                    itemStack.setCount(10);
                });
            }
        }
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<SkillBonusModifier> {
        @Override
        public SkillBonusModifier read(ResourceLocation name, JsonObject json, LootItemCondition[] conditionsIn) {
            return new SkillBonusModifier(conditionsIn);
        }

        @Override
        public JsonObject write(SkillBonusModifier instance) {
            return makeConditions(instance.conditions);
        }
    }
}
