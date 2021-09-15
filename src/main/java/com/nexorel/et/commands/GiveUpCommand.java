package com.nexorel.et.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.player.Player;

public class GiveUpCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> skill_set_cmd =
                Commands.literal("giveup")
                        .requires((commandSource -> commandSource.hasPermission(0)))
                        .executes(GiveUpCommand::giveUp);
        dispatcher.register(skill_set_cmd);
    }

    static int giveUp(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        if (commandContext.getSource().getEntity() instanceof Player player) player.kill();
        return 1;
    }

}
