package frostygames0.elementalamulets.command.debug;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ElementStorageCommand {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("storage")
                .then(Commands.literal("block"))
                .then(Commands.literal("entity"))
                .then(Commands.literal("item"));
    }
}
