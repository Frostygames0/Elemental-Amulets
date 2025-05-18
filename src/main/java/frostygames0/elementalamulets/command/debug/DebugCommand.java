package frostygames0.elementalamulets.command.debug;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class DebugCommand {
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("debug")
                .requires(source -> source.hasPermission(Commands.LEVEL_GAMEMASTERS));
    }
}
