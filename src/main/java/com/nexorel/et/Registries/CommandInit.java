package com.nexorel.et.Registries;

import com.mojang.brigadier.CommandDispatcher;
import com.nexorel.et.commands.SkillSetCommand;
import net.minecraft.command.CommandSource;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommandInit {
    @SubscribeEvent
    public static void onRegisterCommandEvent(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSource> commandDispatcher = event.getDispatcher();
        SkillSetCommand.register(commandDispatcher);
    }
}
