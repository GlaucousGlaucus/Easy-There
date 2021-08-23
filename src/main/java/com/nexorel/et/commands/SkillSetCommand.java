package com.nexorel.et.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.nexorel.et.EasyThere;
import com.nexorel.et.capabilities.CombatSkill;
import com.nexorel.et.capabilities.CombatSkillCapability;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.*;


/**
 * skill_set <PlayerName> <Skill_Type> <Level>
 */

public class SkillSetCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        LiteralArgumentBuilder<CommandSource> skill_set_cmd =
                Commands.literal("set_skill")
                        .requires((commandSource -> commandSource.hasPermission(2)))
                        .then(Commands.argument("target", EntityArgument.players())
                                .then(Commands.literal("combat")
                                        .then(Commands.argument("level", IntegerArgumentType.integer(0, 20))
                                                .executes(SkillSetCommand::setCombatSkillLvl)))
                        );
        dispatcher.register(skill_set_cmd);
    }

    static int setCombatSkillLvl(CommandContext<CommandSource> commandContext) throws CommandSyntaxException {
        Entity entity = commandContext.getSource().getEntity();
        if (entity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) entity;
            CombatSkill combatSkill = player.getCapability(CombatSkillCapability.COMBAT_CAP).orElse(null);
            if (combatSkill == null) {
                EasyThere.LOGGER.error("COMBAT SKILL IS NULL");
                return 1;
            } else {
                int init_level = combatSkill.getLevel();
                int target_level = IntegerArgumentType.getInteger(commandContext, "level");
                if (target_level == 0) {
                    combatSkill.setXp(0);
                } else if (target_level < init_level) {
                    double target_xp = CombatSkill.calculateFullTargetXp(target_level);
                    combatSkill.setXp(target_xp);
                } else if (target_level > init_level && target_level <= 20) {
                    double target_xp = CombatSkill.calculateFullTargetXp(target_level);
                    combatSkill.setXp(target_xp);
                }
                EasyThere.LOGGER.info(combatSkill.getLevel());
            }
            ITextComponent component = new StringTextComponent("Skill Level Set to " + TextFormatting.AQUA + combatSkill.getLevel() + TextFormatting.WHITE + " For: " + TextFormatting.AQUA + "Combat Skill");
            TranslationTextComponent text =
                    new TranslationTextComponent("chat.type.announcement",
                            commandContext.getSource().getDisplayName(), component);
            commandContext.getSource().getServer().getPlayerList().broadcastMessage(text, ChatType.CHAT, entity.getUUID());
        }
        return 1;
    }
}
