package frostygames0.elementalamulets.registration;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.command.DumpElementsCommand;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public class ModCommandHandler {
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher()
                .register(
                        LiteralArgumentBuilder.<CommandSourceStack>literal(ElementalAmulets.MOD_ID)
                                .then(DumpElementsCommand.register())
                );
    }
}
