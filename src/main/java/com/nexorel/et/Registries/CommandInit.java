package com.nexorel.et.Registries;

import com.mojang.brigadier.CommandDispatcher;
import com.nexorel.et.commands.GiveUpCommand;
import com.nexorel.et.commands.SkillSetCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CommandInit {
    @SubscribeEvent
    public static void onRegisterCommandEvent(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> commandDispatcher = event.getDispatcher();
        SkillSetCommand.register(commandDispatcher);
        GiveUpCommand.register(commandDispatcher);
    }
}
