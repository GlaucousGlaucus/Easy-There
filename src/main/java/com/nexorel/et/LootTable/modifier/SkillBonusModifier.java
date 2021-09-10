package com.nexorel.et.LootTable.modifier;

import com.google.gson.JsonObject;
import com.nexorel.et.capabilities.skills.FarmingSkill.FarmingSkill;
import com.nexorel.et.capabilities.skills.FarmingSkill.FarmingSkillCapability;
import com.nexorel.et.capabilities.skills.MiningSkill.MiningSkill;
import com.nexorel.et.capabilities.skills.MiningSkill.MiningSkillCapability;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

import static com.nexorel.et.Reference.MOD_ID;

public class SkillBonusModifier extends LootModifier {

    public static final Tag.Named<Block> CROPS = BlockTags.createOptional(new ResourceLocation(MOD_ID, "farming_crops"));
    public static final Tag.Named<Block> XP_LOGS = BlockTags.createOptional(new ResourceLocation(MOD_ID, "xp_logs"), Set.of(
            () -> Blocks.ACACIA_LOG,
            () -> Blocks.BIRCH_LOG,
            () -> Blocks.DARK_OAK_LOG,
            () -> Blocks.JUNGLE_LOG,
            () -> Blocks.OAK_LOG,
            () -> Blocks.SPRUCE_LOG
    ));

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
            MiningSkill miningSkill = player.getCapability(MiningSkillCapability.MINING_CAP).orElse(null);
            FarmingSkill farmingSkill = player.getCapability(FarmingSkillCapability.FARMING_CAP).orElse(null);
            if (target_block.getTags().contains(new ResourceLocation("forge", "ores"))) {
                generatedLoot.forEach(itemStack -> {
                    int double_drops = context.getRandom().nextFloat() < miningSkill.getLevel() * 0.05F ? itemStack.getCount() * 2 : itemStack.getCount();
                    itemStack.setCount(double_drops);
                });
            } else if (target_block.getTags().contains(CROPS.getName())) {
                if (target_block instanceof CropBlock cropBlock) {
                    if (cropBlock.isMaxAge(targetState)) {
                        generatedLoot.forEach(itemStack -> {
                            int bonus = (int) (itemStack.getCount() * Math.ceil((double) farmingSkill.getLevel() / 2));
                            int drops = context.getRandom().nextFloat() < farmingSkill.getLevel() * 0.05F ? bonus : itemStack.getCount();
                            itemStack.setCount(drops);
                        });
                    }
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
