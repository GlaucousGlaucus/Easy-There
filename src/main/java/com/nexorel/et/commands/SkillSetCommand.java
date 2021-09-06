package com.nexorel.et.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.nexorel.et.EasyThere;
import com.nexorel.et.capabilities.CombatSkill;
import com.nexorel.et.capabilities.CombatSkillCapability;
import com.nexorel.et.capabilities.MiningSkill;
import com.nexorel.et.capabilities.MiningSkillCapability;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

/**
 * skill_set <PlayerName> <Skill_Type> <Level>
 */

public class SkillSetCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> skill_set_cmd =
                Commands.literal("setskill")
                        .requires((commandSource -> commandSource.hasPermission(2)))
                        .then(Commands.argument("target", EntityArgument.players())
                                .then(Commands.literal("combat")
                                        .then(Commands.argument("level", IntegerArgumentType.integer(0, 20))
                                                .executes(SkillSetCommand::setCombatSkillLvl)))
                                .then(Commands.literal("mining")
                                        .then(Commands.argument("level", IntegerArgumentType.integer(0, 20))
                                                .executes(SkillSetCommand::setMiningSkillLvl)))
                        );
        dispatcher.register(skill_set_cmd);
    }

    static int setMiningSkillLvl(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        Entity entity = commandContext.getSource().getEntity();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            MiningSkill miningSkill = player.getCapability(MiningSkillCapability.MINING_CAP).orElse(null);
            int target_level = IntegerArgumentType.getInteger(commandContext, "level");
            if (target_level == 0) {
                miningSkill.setXp(0);
            } else {
                double target_xp = ((MiningSkill.calculateFullTargetXp(target_level - 1) + 1));
                miningSkill.setXp(target_xp);
            }
            miningSkill.shareData((ServerPlayer) player);
            EasyThere.LOGGER.info(miningSkill.getLevel());
            Component component = new TextComponent("Skill Level Set to " + ChatFormatting.AQUA + miningSkill.getLevel() + ChatFormatting.WHITE + " For: " + ChatFormatting.AQUA + "Mining Skill");
            TranslatableComponent text =
                    new TranslatableComponent("chat.type.announcement",
                            commandContext.getSource().getDisplayName(), component);
            commandContext.getSource().getServer().getPlayerList().broadcastMessage(text, ChatType.CHAT, entity.getUUID());
        }
        return 1;
    }

    static int setCombatSkillLvl(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        Entity entity = commandContext.getSource().getEntity();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            CombatSkill combatSkill = player.getCapability(CombatSkillCapability.COMBAT_CAP).orElse(null);
            int target_level = IntegerArgumentType.getInteger(commandContext, "level");
            if (target_level == 0) {
                combatSkill.setXp(0);
            } else {
                double target_xp = ((CombatSkill.calculateFullTargetXp(target_level - 1) + 1));
                combatSkill.setXp(target_xp);
            }
            combatSkill.shareData((ServerPlayer) player);
            EasyThere.LOGGER.info(combatSkill.getLevel());
            Component component = new TextComponent("Skill Level Set to " + ChatFormatting.AQUA + combatSkill.getLevel() + ChatFormatting.WHITE + " For: " + ChatFormatting.AQUA + "Combat Skill");
            TranslatableComponent text =
                    new TranslatableComponent("chat.type.announcement",
                            commandContext.getSource().getDisplayName(), component);
            commandContext.getSource().getServer().getPlayerList().broadcastMessage(text, ChatType.CHAT, entity.getUUID());
        }
        return 1;
    }
}
