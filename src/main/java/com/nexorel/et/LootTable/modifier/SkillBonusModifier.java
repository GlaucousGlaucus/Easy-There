package com.nexorel.et.LootTable.modifier;

import com.google.gson.JsonObject;
import com.nexorel.et.capabilities.skills.Stats.Stats;
import com.nexorel.et.capabilities.skills.Stats.StatsCapability;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;

public class SkillBonusModifier extends LootModifier {

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
            Stats stats = player.getCapability(StatsCapability.STATS_CAP).orElse(null);
            double fortune = stats.getFortune();
            if (target_block.getTags().contains(new ResourceLocation("forge", "ores"))) {
                for (ItemStack itemStack : generatedLoot) {
                    int double_drops = context.getRandom().nextFloat() < (fortune / 100) ? itemStack.getCount() * 2 : itemStack.getCount();
                    int triple_drops = context.getRandom().nextFloat() + 1 < (fortune / 100) ? itemStack.getCount() * 3 : double_drops;
                    itemStack.setCount(triple_drops);
                }
            } else if (target_block.getTags().contains(BlockTags.CROPS.getName()) && (target_block instanceof CropBlock cropBlock && cropBlock.isMaxAge(targetState))) {
                for (ItemStack itemStack : generatedLoot) {
                    int double_drops = context.getRandom().nextFloat() < (fortune / 100) ? itemStack.getCount() * 2 : itemStack.getCount();
                    int triple_drops = context.getRandom().nextFloat() + 1 < (fortune / 100) ? itemStack.getCount() * 3 : double_drops;
                    itemStack.setCount(triple_drops);
                }
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
