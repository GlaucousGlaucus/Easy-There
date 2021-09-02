package com.nexorel.et.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.nexorel.et.EasyThere;
import com.nexorel.et.capabilities.CombatSkill;
import com.nexorel.et.capabilities.CombatSkillCapability;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
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
                                .then(Commands.literal("crit_chance")
                                        .then(Commands.argument("amount", IntegerArgumentType.integer(0, 100))
                                                .executes(SkillSetCommand::setCritChance)))
                        );
        dispatcher.register(skill_set_cmd);
    }

    static int setCritChance(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        Entity entity = commandContext.getSource().getEntity();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            CombatSkill combatSkill = player.getCapability(CombatSkillCapability.COMBAT_CAP).orElse(null);
            int cc = IntegerArgumentType.getInteger(commandContext, "amount");
            EasyThere.LOGGER.info("start" + cc);
            combatSkill.setCrit_chance(Mth.clamp(cc, 0, 100));
            combatSkill.shareData((ServerPlayer) player);
            EasyThere.LOGGER.info(combatSkill.getCrit_chance());
            Component component = new TextComponent("Crit Chance Set to " + ChatFormatting.AQUA + combatSkill.getCrit_chance());
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
            int init_level = combatSkill.getLevel();
            int target_level = IntegerArgumentType.getInteger(commandContext, "level");
            if (target_level == 0) {
                combatSkill.setXp(0);
            } else {
                double target_xp = ((CombatSkill.calculateFullTargetXp(target_level - 1) + 1));
                combatSkill.setXp(target_xp);
                /*EasyThere.LOGGER.info("XP _ CRR" + CombatSkill.getXPProgress(combatSkill));
                EasyThere.LOGGER.info("TL " + (CombatSkill.calculateFullTargetXp(2)));
                EasyThere.LOGGER.info("NL " + (CombatSkill.calculateFullTargetXp(target_level + 1)));
                EasyThere.LOGGER.info("TARGET " + (target_xp));
                EasyThere.LOGGER.info("% " + ((target_xp - CombatSkill.calculateFullTargetXp(target_level - 1)) / (CombatSkill.calculateFullTargetXp(target_level + 1)) * 100));*/
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
