package frostygames0.elementalamulets.initialization.command;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.command.ElementsCommand;
import frostygames0.elementalamulets.command.storage.ElementStorageCommands;
import net.minecraft.commands.Commands;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public final class ModCommands {
    private ModCommands() {
    }

    public static void onRegisterCommands(RegisterCommandsEvent event) {
        var context = event.getBuildContext();
        var dispatcher = event.getDispatcher();

        dispatcher.register(
                Commands.literal(ElementalAmulets.MOD_ID)
                        .then(ElementsCommand.register())
                        .then(ElementStorageCommands.register(context))
        );
    }
}
