package frostygames0.elementalamulets.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import frostygames0.elementalamulets.element.Element;
import frostygames0.elementalamulets.element.ElementalHelper;
import frostygames0.elementalamulets.initialization.ModElements;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.server.command.CommandUtils;

import java.util.stream.Collectors;

public class ElementsCommand {
    private static final Component FOUND_NO_ELEMENTS = CommandUtils.makeTranslatableWithFallback("command.elementalamulets.found_no_elements");

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return Commands.literal("elements")
                .then(createDumpCommand())
                .then(createInfoCommand());
    }

    private static ArgumentBuilder<CommandSourceStack, ?> createDumpCommand() {
        return Commands.literal("list")
                .requires(sc -> sc.hasPermission(Commands.LEVEL_ALL))
                .executes(ctx -> listAllElements(ctx.getSource(), CommandHelper.getRegistry(ctx, ModElements.ELEMENTS)));
    }

    private static ArgumentBuilder<CommandSourceStack, ?> createInfoCommand() {
        return Commands.literal("get")
                .requires(sc -> sc.hasPermission(Commands.LEVEL_ALL))
                .then(Commands.argument("element", ResourceKeyArgument.key(ModElements.ELEMENTS))
                        .executes(ctx -> getElementInfo(ctx.getSource(), CommandHelper.getElement(ctx, "element"))));
    }

    private static int getElementInfo(CommandSourceStack source, Holder<Element> element) {
        var value = element.value();

        source.sendSuccess(() -> Component.literal(String.format("[%s]", element.getRegisteredName())).withStyle(ChatFormatting.GOLD), false);
        source.sendSuccess(() -> Component.literal(" ").append(Component.translatable("command.elementalamulets.get_element.name", value.colorizeNameMutable())), false);
        source.sendSuccess(() -> Component.literal(" ").append(Component.translatable("command.elementalamulets.get_element.description", value.description().orElse(CommonComponents.GUI_NO))), false);
        source.sendSuccess(() -> Component.literal(" ").append(Component.translatable("command.elementalamulets.get_element.is_primordial", value.isPrimordial() ? CommonComponents.GUI_YES : CommonComponents.GUI_NO)), false);

        var composition = value.composition();
        if (!composition.isEmpty()) {
            source.sendSuccess(() -> Component.literal(" ").append(Component.translatable("command.elementalamulets.get_element.composition")), false);
            printSuccessPrettyElementsToSource(source, value.composition());
        }

        return Command.SINGLE_SUCCESS;
    }

    private static int listAllElements(CommandSourceStack source, Registry<Element> registry) {
        var elements = registry.listElements().collect(Collectors.toSet());
        if (elements.isEmpty()) {
            source.sendFailure(FOUND_NO_ELEMENTS);
            return 0;
        }

        source.sendSuccess(() ->
                CommandUtils.makeTranslatableWithFallback("command.elementalamulets.found_n_elements",
                        Component.literal(String.valueOf(elements.size())).withStyle(ChatFormatting.GOLD)), false);

        printSuccessPrettyElementsToSource(source, elements);

        return elements.size();
    }

    private static <T extends Holder<Element>> void printSuccessPrettyElementsToSource(CommandSourceStack source, Iterable<T> elements) {
        for (var element : elements) {
            source.sendSuccess(() -> Component.literal(" - ").append(ElementalHelper.createFancyElementComponentForCommand(element)).withStyle(ChatFormatting.GRAY), false);
        }
    }
}
