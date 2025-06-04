package frostygames0.elementalamulets.initialization;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.command.ElementsCommand;
import frostygames0.elementalamulets.command.storage.ElementStorageCommand;
import net.minecraft.commands.Commands;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public final class ModCommands {
    private ModCommands() {
    }

    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher()
                .register(
                        Commands.literal(ElementalAmulets.MOD_ID)
                                .then(ElementsCommand.register())
                                .then(ElementStorageCommand.register())
                );
    }
}
