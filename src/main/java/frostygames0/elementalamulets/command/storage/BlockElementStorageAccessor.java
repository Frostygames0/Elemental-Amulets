package frostygames0.elementalamulets.command.storage;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import frostygames0.elementalamulets.element.ElementalHelper;
import frostygames0.elementalamulets.element.storage.IElementStorage;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import java.util.function.Function;

public class BlockElementStorageAccessor implements IElementStorageAccessor {
    private static final DynamicCommandExceptionType ERROR_NO_CAPABILITY = new DynamicCommandExceptionType(object -> Component.translatable("command.elementalamulets.no_block_storage", object));

    public static final ElementStorageCommand.IElementStorageProvider PROVIDER = new ElementStorageCommand.IElementStorageProvider() {
        @Override
        public IElementStorageAccessor access(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
            var blockPos = BlockPosArgument.getLoadedBlockPos(context, "pos");
            var level = context.getSource().getLevel();
            return new BlockElementStorageAccessor(level, blockPos);
        }

        @Override
        public ArgumentBuilder<CommandSourceStack, ?> wrap(ArgumentBuilder<CommandSourceStack, ?> builder, Function<ArgumentBuilder<CommandSourceStack, ?>, ArgumentBuilder<CommandSourceStack, ?>> action) {
            return builder.then(
                    Commands.literal("block")
                            .then(
                                    action.apply(
                                            Commands.argument("pos", BlockPosArgument.blockPos())
                                    )
                            )
            );
        }
    };

    private final Level level;
    private final BlockPos blockPos;

    public BlockElementStorageAccessor(Level level, BlockPos blockPos) {
        this.level = level;
        this.blockPos = blockPos;
    }

    @Override
    public IElementStorage getStorage() throws CommandSyntaxException {
        var optionalStorage = ElementalHelper.getElementStorage(level, blockPos, null);
        if (optionalStorage.isEmpty()) {
            throw ERROR_NO_CAPABILITY.create(blockPos.toShortString());
        }

        return optionalStorage.get();
    }

    @Override
    public Component getSuccessMessage(Component actionSubMessage) {
        return Component.translatable("command.elementalamulets.storage.block_success", actionSubMessage, blockPos.toShortString());
    }

    @Override
    public Component getFailureMessage(Component actionSubMessage) {
        return Component.translatable("command.elementalamulets.storage.block_fail", actionSubMessage, blockPos.toShortString());
    }

    @Override
    public Component getNeutralMessage(Component actionSubMessage) {
        return Component.translatable("command.elementalamulets.storage.block_query", blockPos.toShortString(), actionSubMessage);
    }
}
