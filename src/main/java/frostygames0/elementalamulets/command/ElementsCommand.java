package frostygames0.elementalamulets.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import frostygames0.elementalamulets.element.Element;
import frostygames0.elementalamulets.initialization.ModElements;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.*;
import net.neoforged.neoforge.server.command.CommandUtils;

import java.util.stream.Collectors;

public class ElementsCommand {
    private static final SimpleCommandExceptionType REGISTRY_NOT_FOUND_ERROR = new SimpleCommandExceptionType(CommandUtils.makeTranslatableWithFallback("command.elementalamulets.no_registry_error"));
    private static final SimpleCommandExceptionType ELEMENT_NOT_FOUND_ERROR = new SimpleCommandExceptionType(CommandUtils.makeTranslatableWithFallback("command.elementalamulets.no_element_error"));

    private static final Component FOUND_NO_ELEMENTS = CommandUtils.makeTranslatableWithFallback("command.elementalamulets.found_no_elements");

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("elements")
                .then(createDumpCommand())
                .then(createInfoCommand());
    }

    private static ArgumentBuilder<CommandSourceStack, ?> createDumpCommand() {
        return Commands.literal("dump")
                .requires(sc -> sc.hasPermission(Commands.LEVEL_ALL))
                .executes(ElementsCommand::executeDumpCommand);
    }

    private static ArgumentBuilder<CommandSourceStack, ?> createInfoCommand() {
        return Commands.literal("get")
                .requires(sc -> sc.hasPermission(Commands.LEVEL_ALL))
                .then(Commands.argument("element", ResourceKeyArgument.key(ModElements.ELEMENTS))
                        .executes(ElementsCommand::executeGetCommand));
    }

    private static int executeGetCommand(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        var source = ctx.getSource();
        var resourceKey = CommandUtils.getResourceKey(ctx, "element", ModElements.ELEMENTS)
                .orElseThrow(ELEMENT_NOT_FOUND_ERROR::create);

        var registry = getRegistryOrThrow(source);

        var element = registry.get(resourceKey)
                .orElseThrow(ELEMENT_NOT_FOUND_ERROR::create);

        source.sendSuccess(() -> constructDetailedElementComponent(element), false);
        return Command.SINGLE_SUCCESS;
    }

    private static Registry<Element> getRegistryOrThrow(CommandSourceStack commandSourceStack) throws CommandSyntaxException {
        return commandSourceStack.registryAccess().lookup(ModElements.ELEMENTS)
                .orElseThrow(REGISTRY_NOT_FOUND_ERROR::create);
    }

    private static int executeDumpCommand(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        var source = ctx.getSource();

        var registry = getRegistryOrThrow(source);

        var elements = registry.listElements().collect(Collectors.toSet());
        if (elements.isEmpty()) {
            source.sendFailure(FOUND_NO_ELEMENTS);
            return 0;
        }

        source.sendSuccess(() ->
                CommandUtils.makeTranslatableWithFallback("command.elementalamulets.found_n_elements",
                        Component.literal(String.valueOf(elements.size())).withStyle(ChatFormatting.GOLD)), false);

        for (var element : elements) {
            var elementShortDescription = constructElementComponent(element)
                    .withStyle(style ->
                            style.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("/elementalamulets elements get %s", element.getKey().location())))
                    );

            source.sendSuccess(() -> Component.literal(" - ").append(elementShortDescription).withStyle(ChatFormatting.GRAY), false);
        }

        return elements.size();
    }

    private static MutableComponent constructElementComponent(Holder<Element> holderElement) {
        return Component.empty().append(holderElement.value().colorizeNameMutable()).withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("/elementalamulets elements get %s", holderElement.getKey().location()))))
                .append(" ")
                .append(ComponentUtils.wrapInSquareBrackets(Component.literal(holderElement.getKey().location().toString()))
                        .withStyle(ChatFormatting.GRAY));
    }

    private static Component constructDetailedElementComponent(Holder<Element> elementHolder) {
        var value = elementHolder.value();

        var detailed = Component.empty()
                .append(constructElementComponent(elementHolder))
                .append("\n")
                .append(CommandUtils.makeTranslatableWithFallback("command.elementalamulets.get_element.description",
                                value.description()
                                        .map(Component::copy)
                                        .orElse(Component.literal("NONE")))
                        .withStyle(ChatFormatting.GOLD))
                .append("\n")
                .append(CommandUtils.makeTranslatableWithFallback("command.elementalamulets.get_element.is_primordial",
                        value.isPrimordial() ?
                                CommonComponents.GUI_YES
                                : CommonComponents.GUI_NO
                ).withStyle(ChatFormatting.GOLD));

        if (!value.composition().isEmpty()) {
            detailed.append("\n")
                    .append(CommandUtils.makeTranslatableWithFallback("command.elementalamulets.get_element.composition")
                            .withStyle(ChatFormatting.GOLD))
                    .append("\n");

            for (var elementInside : elementHolder.value().composition()) {
                var elementShortDesc = constructElementComponent(elementInside);
                detailed.append(
                                Component.literal(" - ")
                                        .append(elementShortDesc)
                                        .withStyle(ChatFormatting.GRAY))
                        .append("\n");
            }
        }

        return detailed;
    }
}
