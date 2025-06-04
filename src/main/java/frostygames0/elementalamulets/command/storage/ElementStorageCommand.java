package frostygames0.elementalamulets.command.storage;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import frostygames0.elementalamulets.command.CommandHelper;
import frostygames0.elementalamulets.element.Element;
import frostygames0.elementalamulets.element.ElementalHelper;
import frostygames0.elementalamulets.element.storage.IElementStorage;
import frostygames0.elementalamulets.element.storage.OperationMode;
import frostygames0.elementalamulets.initialization.ModElements;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Function;

public class ElementStorageCommand {
    private static final List<IElementStorageProvider> PROVIDERS = ImmutableList.of(
            BlockElementStorageAccessor.PROVIDER
    );

    private static final List<IAction> ACTIONS =
            ImmutableList.of(
                    new ElementOperationAction.Add(),
                    new ElementOperationAction.Take(),
                    new SetStorageAction(),
                    new GetStorageAction()
            );

    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        LiteralArgumentBuilder<CommandSourceStack> builder = Commands.literal("storage").requires(source -> source.hasPermission(Commands.LEVEL_GAMEMASTERS));

        for (var provider : PROVIDERS) {
            for (var action : ACTIONS) {
                builder.then(provider.wrap(Commands.literal(action.getName()), wrapped -> action.buildCommand(wrapped, provider)));
            }
        }

        return builder;
    }

    public interface IElementStorageProvider {
        IElementStorageAccessor access(CommandContext<CommandSourceStack> context) throws CommandSyntaxException;

        ArgumentBuilder<CommandSourceStack, ?> wrap(
                ArgumentBuilder<CommandSourceStack, ?> builder,
                Function<ArgumentBuilder<CommandSourceStack, ?>, ArgumentBuilder<CommandSourceStack, ?>> action
        );
    }

    public interface IAction {
        String getName();

        ArgumentBuilder<CommandSourceStack, ?> buildCommand(ArgumentBuilder<CommandSourceStack, ?> builder, IElementStorageProvider provider);
    }

    public static abstract class SimpleExecuteAction implements IAction {
        @Override
        public ArgumentBuilder<CommandSourceStack, ?> buildCommand(ArgumentBuilder<CommandSourceStack, ?> builder, IElementStorageProvider provider) {
            return builder.executes(ctx -> execute(ctx, provider.access(ctx)));
        }

        public abstract int execute(CommandContext<CommandSourceStack> ctx, IElementStorageAccessor accessor) throws CommandSyntaxException;
    }

    public abstract static class ElementOperationAction implements IAction {
        @Override
        public ArgumentBuilder<CommandSourceStack, ?> buildCommand(ArgumentBuilder<CommandSourceStack, ?> builder, IElementStorageProvider provider) {
            return builder.then(
                    Commands.argument("element", ResourceKeyArgument.key(ModElements.ELEMENTS))
                            .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                    .executes(ctx ->
                                            execute(ctx.getSource(),
                                                    CommandHelper.getElement(ctx, "element"),
                                                    IntegerArgumentType.getInteger(ctx, "amount"),
                                                    provider.access(ctx)
                                            )
                                    )
                            ));
        }

        private int execute(CommandSourceStack source, Holder<Element> element, int amount, IElementStorageAccessor storageAccessor) throws CommandSyntaxException {
            var storage = storageAccessor.getStorage();

            if (!canPerform(storage, element)) {
                source.sendFailure(storageAccessor.getFailureMessage(getFailureSubMessage(element, amount)));
                return 0;
            }

            var resulted = performOperation(storage, element, amount);
            if (resulted == 0) {
                source.sendFailure(storageAccessor.getFailureMessage(getFailureSubMessage(element, amount)));
                return 0;
            }

            source.sendSuccess(() -> storageAccessor.getSuccessMessage(getSuccessSubMessage(element, resulted)), true);
            return resulted;
        }

        protected abstract int performOperation(IElementStorage storage, Holder<Element> element, int amount);

        protected abstract boolean canPerform(IElementStorage storage, Holder<Element> element);

        protected abstract Component getSuccessSubMessage(Holder<Element> element, int amount);

        protected abstract Component getFailureSubMessage(Holder<Element> element, int amount);

        public static class Add extends ElementOperationAction {
            @Override
            public String getName() {
                return "add";
            }

            @Override
            protected int performOperation(IElementStorage storage, Holder<Element> element, int amount) {
                return storage.addElement(element, amount, OperationMode.PERFORM);
            }

            @Override
            protected boolean canPerform(IElementStorage storage, Holder<Element> element) {
                return storage.canAddElement(element);
            }

            @Override
            protected Component getSuccessSubMessage(Holder<Element> element, int amount) {
                return Component.translatable("command.elementalamulets.storage.add.success", ElementalHelper.createFancyElementComponent(element), amount);
            }

            @Override
            protected Component getFailureSubMessage(Holder<Element> element, int amount) {
                return Component.translatable("command.elementalamulets.storage.add.fail", ElementalHelper.createFancyElementComponent(element));
            }
        }

        public static class Take extends ElementOperationAction {
            @Override
            public String getName() {
                return "take";
            }

            @Override
            protected int performOperation(IElementStorage storage, Holder<Element> element, int amount) {
                return storage.takeElement(element, amount, OperationMode.PERFORM);
            }

            @Override
            protected boolean canPerform(IElementStorage storage, Holder<Element> element) {
                return storage.canTakeElement(element);
            }

            @Override
            protected Component getSuccessSubMessage(Holder<Element> element, int amount) {
                return Component.translatable("command.elementalamulets.storage.take.success", ElementalHelper.createFancyElementComponent(element), amount);
            }

            @Override
            protected Component getFailureSubMessage(Holder<Element> element, int amount) {
                return Component.translatable("command.elementalamulets.storage.take.fail", ElementalHelper.createFancyElementComponent(element));
            }
        }
    }

    public static class SetStorageAction implements IAction {
        @Override
        public String getName() {
            return "set";
        }

        @Override
        public ArgumentBuilder<CommandSourceStack, ?> buildCommand(ArgumentBuilder<CommandSourceStack, ?> builder, IElementStorageProvider provider) {
            return builder.executes(ctx -> {
                ctx.getSource().sendFailure(Component.literal("Not Implemented!"));
                return 0;
            });
        }
    }

    public static class GetStorageAction implements IAction {
        private static final List<SimpleExecuteAction> ACTIONS =
                ImmutableList.of(
                        new AllStoredElementTypes(),
                        new MaxDistinctElementsStored(),
                        new MaxCapacity(),
                        new TotalAmount(),
                        new Element()
                );

        @Override
        public String getName() {
            return "get";
        }

        @Override
        public ArgumentBuilder<CommandSourceStack, ?> buildCommand(ArgumentBuilder<CommandSourceStack, ?> builder, IElementStorageProvider provider) {
            builder.executes(ctx -> execute(ctx, provider.access(ctx)));
            for (var action : ACTIONS) {
                builder.then(action.buildCommand(Commands.literal(action.getName()), provider));
            }

            return builder;
        }

        private static int execute(CommandContext<CommandSourceStack> ctx, IElementStorageAccessor storageAccessor) throws CommandSyntaxException {
            var result = 0;
            for (var action : ACTIONS) {
                result += action.execute(ctx, storageAccessor);
            }

            return result;
        }

        public static class AllStoredElementTypes extends SimpleExecuteAction {
            @Override
            public String getName() {
                return "all_stored_element_types";
            }

            @Override
            public int execute(CommandContext<CommandSourceStack> ctx, IElementStorageAccessor accessor) throws CommandSyntaxException {
                var storage = accessor.getStorage();
                var allStoredElementTypes = storage.getAllStoredElementTypes();

                var source = ctx.getSource();
                source.sendSuccess(() -> Component.translatable("command.elementalamulets.storage.get.all_stored_element_types", allStoredElementTypes.size()), true);
                for (var element : allStoredElementTypes) {
                    source.sendSuccess(() -> Component.literal(" - ").withStyle(ChatFormatting.GRAY).append(ElementalHelper.createFancyElementComponentForCommand(element)), true);
                }

                return allStoredElementTypes.size();
            }
        }

        public static class MaxDistinctElementsStored extends SimpleExecuteAction {
            @Override
            public String getName() {
                return "max_distinct_elements_stored";
            }

            @Override
            public int execute(CommandContext<CommandSourceStack> ctx, IElementStorageAccessor accessor) throws CommandSyntaxException {
                var storage = accessor.getStorage();
                ctx.getSource().sendSuccess(() -> Component.translatable("command.elementalamulets.storage.get.max_distinct_elements_stored", String.valueOf(storage.getDistinctElementsAmount())), true);
                return storage.getDistinctElementsAmount();
            }
        }

        public static class MaxCapacity extends SimpleExecuteAction {
            @Override
            public String getName() {
                return "max_capacity";
            }

            @Override
            public int execute(CommandContext<CommandSourceStack> ctx, IElementStorageAccessor accessor) throws CommandSyntaxException {
                var storage = accessor.getStorage();
                ctx.getSource().sendSuccess(() -> Component.translatable("command.elementalamulets.storage.get.max_capacity", String.valueOf(storage.getMaxCapacity())), true);
                return storage.getDistinctElementsAmount();
            }
        }

        public static class TotalAmount extends SimpleExecuteAction {
            @Override
            public String getName() {
                return "total_amount";
            }

            @Override
            public int execute(CommandContext<CommandSourceStack> ctx, IElementStorageAccessor accessor) throws CommandSyntaxException {
                var storage = accessor.getStorage();
                ctx.getSource().sendSuccess(() -> Component.translatable("command.elementalamulets.storage.get.total_amount", storage.getTotalAmount()), true);
                return storage.getTotalAmount();
            }
        }

        public static class Element extends SimpleExecuteAction {
            @Override
            public String getName() {
                return "element";
            }

            @Override
            public ArgumentBuilder<CommandSourceStack, ?> buildCommand(ArgumentBuilder<CommandSourceStack, ?> builder, IElementStorageProvider provider) {
                return super.buildCommand(builder, provider)
                        .then(Commands.argument("element", ResourceKeyArgument.key(ModElements.ELEMENTS)).
                                executes(ctx -> execute(ctx, provider.access(ctx))));
            }

            @Override
            public int execute(CommandContext<CommandSourceStack> ctx, IElementStorageAccessor accessor) throws CommandSyntaxException {
                var source = ctx.getSource();
                var element = CommandHelper.getElement(ctx, "element");
                var storage = accessor.getStorage();

                var fancyElement = ElementalHelper.createFancyElementComponent(element);
                if (!storage.containsElement(element)) {
                    source.sendFailure(accessor.getNeutralMessage(Component.translatable("command.elementalamulets.storage.get.element.none", fancyElement)));
                    return 0;
                }

                source.sendSuccess(() -> accessor.getNeutralMessage(Component.translatable("command.elementalamulets.storage.get.element", fancyElement, storage.getElementAmount(element))), true);
                return Command.SINGLE_SUCCESS;
            }
        }
    }
}
