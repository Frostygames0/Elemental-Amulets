package frostygames0.elementalamulets.command;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import frostygames0.elementalamulets.element.Element;
import frostygames0.elementalamulets.initialization.ModElements;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.server.command.CommandUtils;

public class CommandHelper {
    public static final DynamicCommandExceptionType REGISTRY_NOT_FOUND_ERROR = new DynamicCommandExceptionType(object -> Component.translatable("command.elementalamulets.no_registry_error", object));
    public static final DynamicCommandExceptionType ELEMENT_NOT_FOUND_ERROR = new DynamicCommandExceptionType(object -> Component.translatable("command.elementalamulets.no_element_error", object));

    public static Holder<Element> getElement(CommandContext<CommandSourceStack> context, String argument) throws CommandSyntaxException {
        var elementsRegistry = getRegistry(context, ModElements.ELEMENTS);
        var elementKey = CommandUtils.getResourceKey(context, argument, ModElements.ELEMENTS).orElseThrow(() -> ELEMENT_NOT_FOUND_ERROR.create(argument));

        return elementsRegistry.get(elementKey).orElseThrow(() -> ELEMENT_NOT_FOUND_ERROR.create(elementKey.location().toString()));
    }

    public static <T> Registry<T> getRegistry(CommandContext<CommandSourceStack> context, ResourceKey<Registry<T>> key) throws CommandSyntaxException {
        var registries = context.getSource().registryAccess();
        return registries.lookup(key).orElseThrow(() -> REGISTRY_NOT_FOUND_ERROR.create(ModElements.ELEMENTS.location().toString()));
    }

}
