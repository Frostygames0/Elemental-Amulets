package frostygames0.elementalamulets.command;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import frostygames0.elementalamulets.element.ElementalComposition;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.ParserUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;

import java.util.Collection;
import java.util.List;

public class ElementalCompositionArgument implements ArgumentType<ElementalComposition> {
    public static final Collection<String> EXAMPLES = List.of("{\"elementalamulets:fire\": 3, \"elementalamulets:aether\": 1}", "[{\"element\": \"elementalamulets:earth\", \"amount\":34}]");

    public static final DynamicCommandExceptionType ERROR_INVALID_JSON = new DynamicCommandExceptionType(
            p_304083_ -> Component.translatableEscape("argument.elementalamulets.elemental_composition.invalid", p_304083_)
    );

    private final HolderLookup.Provider registries;

    private ElementalCompositionArgument(HolderLookup.Provider registries) {
        this.registries = registries;
    }

    public static ElementalComposition getComposition(CommandContext<CommandSourceStack> context, String name) {
        return context.getArgument(name, ElementalComposition.class);
    }

    public static ElementalCompositionArgument composition(CommandBuildContext context) {
        return new ElementalCompositionArgument(context);
    }

    @Override
    public ElementalComposition parse(StringReader reader) throws CommandSyntaxException {
        try {
            return ParserUtils.parseJson(registries, reader, ElementalComposition.JSON_CODEC);
        } catch (Exception exception) {
            String s = exception.getCause() != null ? exception.getCause().getMessage() : exception.getMessage();
            throw ERROR_INVALID_JSON.createWithContext(reader, s);
        }
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
