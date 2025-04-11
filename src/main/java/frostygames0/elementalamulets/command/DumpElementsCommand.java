package frostygames0.elementalamulets.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import frostygames0.elementalamulets.element.Element;
import frostygames0.elementalamulets.registration.Elements;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.server.command.CommandUtils;

import java.util.stream.Collectors;

public class DumpElementsCommand {
    private static final SimpleCommandExceptionType REGISTRY_NOT_FOUND_ERROR = new SimpleCommandExceptionType(CommandUtils.makeTranslatableWithFallback("command.elementalamulets.no_registry_error"));

    private static final Component FOUND_NO_ELEMENTS = CommandUtils.makeTranslatableWithFallback("command.elementalamulets.found_no_elements");

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("dump_elements")
                .requires(sc -> sc.hasPermission(Commands.LEVEL_ALL))
                .executes(ctx ->
                        execute(ctx.getSource()));
    }

    private static int execute(CommandSourceStack commandSourceStack) throws CommandSyntaxException {
        var optionalElementRegistry = commandSourceStack.registryAccess().lookup(Elements.ELEMENTS_REGISTRY_KEY);

        if (optionalElementRegistry.isEmpty()) {
            throw REGISTRY_NOT_FOUND_ERROR.create();
        }

        var elementRegistry = optionalElementRegistry.get();

        var elements = elementRegistry.listElements().collect(Collectors.toSet());

        if (elements.isEmpty()) {
            commandSourceStack.sendFailure(FOUND_NO_ELEMENTS);
            return 0;
        }

        commandSourceStack.sendSuccess(() ->
                CommandUtils.makeTranslatableWithFallback("command.elementalamulets.found_n_elements",
                        Component.literal(String.valueOf(elements.size())).withStyle(ChatFormatting.GOLD)), false);

        for (var element : elements) {
            commandSourceStack.sendSuccess(() -> constructElementComponent(element), false);
        }

        return elements.size();
    }

    private static Component constructElementComponent(Holder<Element> holderElement) {
        return Component.literal(" - ").append(holderElement.value().colorizeNameMutable())
                .append(
                        Component.empty()
                                .append(" [")
                                .append(holderElement.getKey().location().toString())
                                .append("]").withStyle(ChatFormatting.GRAY)
                );
    }
}
