package frostygames0.elementalamulets.command.storage;

import com.google.common.collect.ImmutableList;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Pair;
import frostygames0.elementalamulets.command.CommandHelper;
import frostygames0.elementalamulets.command.ElementalCompositionArgument;
import frostygames0.elementalamulets.element.ElementType;
import frostygames0.elementalamulets.element.ElementalComposition;
import frostygames0.elementalamulets.element.ElementalHelper;
import frostygames0.elementalamulets.element.storage.IElementStorage;
import frostygames0.elementalamulets.element.storage.IElementStorageModifiable;
import frostygames0.elementalamulets.initialization.ModElements;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Function;

public class ElementStorageCommands {
    private static final List<IElementStorageProvider> PROVIDERS = ImmutableList.of(
            BlockElementStorageAccessor.PROVIDER
    );

    private static final List<IAction> ACTIONS =
            ImmutableList.of(
                    new ElementOperationAction.AddElement(),
                    //new ElementOperationAction.Take(),
                    new SetStorageAction(),
                    new GetStorageAction()
            );

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandBuildContext context) {
        var builder = Commands.literal("storage").requires(source -> source.hasPermission(Commands.LEVEL_GAMEMASTERS));

        for (var provider : PROVIDERS) {
            for (var action : ACTIONS) {
                builder.then(provider.wrap(Commands.literal(action.getName()), wrapped -> action.buildCommand(wrapped, provider, context)));
            }
        }

        return builder;
    }

    public interface IArgumentProvider<T> {
        T access(CommandContext<CommandSourceStack> context) throws CommandSyntaxException;

        ArgumentBuilder<CommandSourceStack, ?> wrap(
                ArgumentBuilder<CommandSourceStack, ?> builder,
                Function<ArgumentBuilder<CommandSourceStack, ?>, ArgumentBuilder<CommandSourceStack, ?>> action
        );
    }

    public interface IElementStorageProvider extends IArgumentProvider<IElementStorageAccessor> {
        IElementStorageAccessor access(CommandContext<CommandSourceStack> context) throws CommandSyntaxException;
    }

    public interface IAction {
        String getName();

        ArgumentBuilder<CommandSourceStack, ?> buildCommand(ArgumentBuilder<CommandSourceStack, ?> builder, IElementStorageProvider provider, CommandBuildContext context);
    }

    public static abstract class SimpleExecuteAction implements IAction {
        @Override
        public ArgumentBuilder<CommandSourceStack, ?> buildCommand(ArgumentBuilder<CommandSourceStack, ?> builder, IElementStorageProvider provider, CommandBuildContext context) {
            return builder.executes(ctx -> execute(ctx, provider.access(ctx)));
        }

        public abstract int execute(CommandContext<CommandSourceStack> ctx, IElementStorageAccessor accessor) throws CommandSyntaxException;
    }

    public abstract static class ElementOperationAction<O> implements IAction {

        @Override
        public ArgumentBuilder<CommandSourceStack, ?> buildCommand(ArgumentBuilder<CommandSourceStack, ?> builder, IElementStorageProvider storageProvider, CommandBuildContext context) {
            return builder.then(getProvider().wrap(builder, wrapped ->
                    wrapped.executes(ctx -> execute(ctx.getSource(), getProvider().access(ctx), storageProvider.access(ctx)))));
//                    Commands.argument("element", ResourceKeyArgument.key(ModElements.ELEMENTS))
//                            .then(Commands.argument("amount", IntegerArgumentType.integer(1))
//                                    .executes(ctx ->
//                                            execute(ctx.getSource(),
//                                                    CommandHelper.getElement(ctx, "element"),
//                                                    IntegerArgumentType.getInteger(ctx, "amount"),
//                                                    provider.access(ctx)
//                                            )
//                                    )
//                            ))
//                        .then(Commands.argument("composition", ElementalCompositionArgument.composition(context)).executes(ctx ->
//                            execute(ctx.getSource(), ElementalCompositionArgument.getComposition(ctx, "composition"), provider.access(ctx))));
        }

        private int execute(CommandSourceStack source, O operand, IElementStorageAccessor storageAccessor) throws CommandSyntaxException {
            var storage = storageAccessor.getStorage();

            if (!canPerform(storage, operand)) {
                source.sendFailure(storageAccessor.getFailureMessage(getFailureSubMessage(operand)));
                return 0;
            }

            var resulted = performOperation(storage, operand);
            if (resulted == 0) {
                source.sendFailure(storageAccessor.getFailureMessage(getFailureSubMessage(operand)));
                return 0;
            }

            source.sendSuccess(() -> storageAccessor.getSuccessMessage(getSuccessSubMessage(operand)), true);
            return resulted;
        }

        protected abstract IArgumentProvider<O> getProvider();

        protected abstract int performOperation(IElementStorage storage, O operand);

        protected abstract boolean canPerform(IElementStorage storage, O operand);

        protected abstract Component getSuccessSubMessage(O operand);

        protected abstract Component getFailureSubMessage(O operand);

        public static class AddElement extends ElementOperationAction<Pair<Holder<ElementType>, Integer>> {
            private static final IArgumentProvider<Pair<Holder<ElementType>, Integer>> PROVIDER = new IArgumentProvider<Pair<Holder<ElementType>, Integer>>() {
                @Override
                public Pair<Holder<ElementType>, Integer> access(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
                    return Pair.of(CommandHelper.getElement(context, "element"), IntegerArgumentType.getInteger(context, "amount"));
                }

                @Override
                public ArgumentBuilder<CommandSourceStack, ?> wrap(ArgumentBuilder<CommandSourceStack, ?> builder, Function<ArgumentBuilder<CommandSourceStack, ?>, ArgumentBuilder<CommandSourceStack, ?>> action) {
                    return builder.then(Commands.argument("element", ResourceKeyArgument.key(ModElements.ELEMENTS))
                            .then(Commands.argument("amount", IntegerArgumentType.integer(1))));
                }
            };

            @Override
            protected IArgumentProvider<Pair<Holder<ElementType>, Integer>> getProvider() {
                return PROVIDER;
            }

            @Override
            public String getName() {
                return "add";
            }

            @Override
            protected int performOperation(IElementStorage storage, Pair<Holder<ElementType>, Integer> operand) {
                return storage.addElement(operand.getFirst(), operand.getSecond(), IElementStorage.Operation.PERFORM);
            }

            @Override
            protected boolean canPerform(IElementStorage storage, Pair<Holder<ElementType>, Integer> operand) {
                return storage.canAddElement(operand.getFirst());
            }

            @Override
            protected Component getSuccessSubMessage(Pair<Holder<ElementType>, Integer> operand) {
                return Component.translatable("command.elementalamulets.storage.add.success", ElementalHelper.createFancyElementComponentForCommand(operand.getFirst()), operand.getSecond());
            }

            @Override
            protected Component getFailureSubMessage(Pair<Holder<ElementType>, Integer> operand) {
                return Component.translatable("command.elementalamulets.storage.add.fail", ElementalHelper.createFancyElementComponentForCommand(operand.getFirst()));
            }
        }

        public static class Take extends ElementOperationAction {
            @Override
            public String getName() {
                return "take";
            }
//
//            @Override
//            protected int performOperation(IElementStorage storage, Holder<Element> element, int amount) {
//                return storage.takeElement(element, amount, IElementStorage.Operation.PERFORM);
//            }
//
//            @Override
//            protected ElementalComposition performOperation(IElementStorage storage, ElementalComposition composition) {
//                return null;
//            }
//
//            @Override
//            protected boolean canPerform(IElementStorage storage, Holder<Element> element) {
//                return storage.canTakeElement(element);
//            }
//
//            @Override
//            protected boolean canPerform(IElementStorage storage, ElementalComposition composition) {
//                return false;
//            }
//
//            @Override
//            protected Component getSuccessSubMessage(Holder<Element> element, int amount) {
//                return Component.translatable("command.elementalamulets.storage.take.success", ElementalHelper.createFancyElementComponentForCommand(element), amount);
//            }
//
//            @Override
//            protected Component getSuccessSubMessage(ElementalComposition elementalComposition) {
//                return null;
//            }
//
//            @Override
//            protected Component getFailureSubMessage(Holder<Element> element, int amount) {
//                return Component.translatable("command.elementalamulets.storage.take.fail", ElementalHelper.createFancyElementComponentForCommand(element));
//            }
//
//            @Override
//            protected Component getFailureSubMessage(ElementalComposition elementalComposition) {
//                return null;
//            }

            @Override
            protected IArgumentProvider getProvider() {
                return null;
            }

            @Override
            protected int performOperation(IElementStorage storage, Object operand) {
                return 0;
            }

            @Override
            protected boolean canPerform(IElementStorage storage, Object operand) {
                return false;
            }

            @Override
            protected Component getSuccessSubMessage(Object operand) {
                return null;
            }

            @Override
            protected Component getFailureSubMessage(Object operand) {
                return null;
            }
        }
    }

    public static class SetStorageAction implements IAction {
        @Override
        public String getName() {
            return "set";
        }

        @Override
        public ArgumentBuilder<CommandSourceStack, ?> buildCommand(ArgumentBuilder<CommandSourceStack, ?> builder, IElementStorageProvider provider, CommandBuildContext context) {
            return builder.then(Commands.argument("contents", ElementalCompositionArgument.composition(context))
                    .executes(ctx -> execute(ctx.getSource(), ElementalCompositionArgument.getComposition(ctx, "contents"), provider.access(ctx))));
        }

        private static int execute(CommandSourceStack source, ElementalComposition composition, IElementStorageAccessor storageAccessor) throws CommandSyntaxException {
            var storage = storageAccessor.getStorage();

            if (!(storage instanceof IElementStorageModifiable storageModifiable)) {
                source.sendFailure(storageAccessor.getFailureMessage(Component.translatable("command.elementalamulets.storage.set.not_modifiable")));
                return 0;
            }

            storageModifiable.setStored(composition);
            source.sendSuccess(() -> storageAccessor.getSuccessMessage(Component.translatable("command.elementalamulets.storage.set.success")), true);
            return Command.SINGLE_SUCCESS;
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
        public ArgumentBuilder<CommandSourceStack, ?> buildCommand(ArgumentBuilder<CommandSourceStack, ?> builder, IElementStorageProvider provider, CommandBuildContext context) {
            for (var action : ACTIONS) {
                builder.then(action.buildCommand(Commands.literal(action.getName()), provider, context));
            }

            return builder;
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
                source.sendSuccess(() -> accessor.getNeutralMessage(Component.translatable("command.elementalamulets.storage.get.all_stored_element_types", allStoredElementTypes.size())), true);
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
                ctx.getSource().sendSuccess(() -> accessor.getNeutralMessage(Component.translatable("command.elementalamulets.storage.get.max_distinct_elements_stored", String.valueOf(storage.getMaxDistinctElementsStored() == Integer.MAX_VALUE ? "∞" : storage.getMaxDistinctElementsStored()))), true);
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
            public ArgumentBuilder<CommandSourceStack, ?> buildCommand(ArgumentBuilder<CommandSourceStack, ?> builder, IElementStorageProvider provider, CommandBuildContext context) {
                return super.buildCommand(builder, provider, context)
                        .then(Commands.argument("element", ResourceKeyArgument.key(ModElements.ELEMENTS)).
                                executes(ctx -> execute(ctx, provider.access(ctx))));
            }

            @Override
            public int execute(CommandContext<CommandSourceStack> ctx, IElementStorageAccessor accessor) throws CommandSyntaxException {
                var source = ctx.getSource();
                var element = CommandHelper.getElement(ctx, "element");
                var storage = accessor.getStorage();

                var fancyElement = ElementalHelper.createFancyElementComponentForCommand(element);
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
