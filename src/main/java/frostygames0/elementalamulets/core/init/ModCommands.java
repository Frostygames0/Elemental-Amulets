package frostygames0.elementalamulets.core.init;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.items.amulets.AmuletItem;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.ItemArgument;
import net.minecraft.command.arguments.ItemInput;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.Collection;

/**
 * @author Frostygames0
 * @date 14.06.2021 11:15
 */
public class ModCommands {

    public static void registerCommandsEvent(final RegisterCommandsEvent event) {
        event.getDispatcher().register(
                LiteralArgumentBuilder.<CommandSource>literal(ElementalAmulets.MOD_ID)
                        .then(GiveAmuletCommand.register())
        );
    }

    private static class GiveAmuletCommand {
        private static final DynamicCommandExceptionType ISNT_AMULET = new DynamicCommandExceptionType(item -> new TranslationTextComponent("commands.elementalamulets.give.failure", ((ItemStack)item).getTextComponent()));
        static ArgumentBuilder<CommandSource, ?> register() {
            return Commands.literal("give")
                    .requires(s -> s.hasPermissionLevel(2))
                    .then(Commands.argument("player", EntityArgument.players())
                    .then(Commands.argument("amulet", ItemArgument.item()).suggests((ctx, builder) -> ISuggestionProvider.suggest(ModItems.getAmulets().stream().map(item -> item.getRegistryName().toString()), builder))
                    .then(Commands.argument("tier", IntegerArgumentType.integer(1, AmuletItem.MAX_TIER))
                    .executes(ctx -> execute(ctx.getSource(), EntityArgument.getPlayers(ctx, "player"), ItemArgument.getItem(ctx, "amulet"), IntegerArgumentType.getInteger(ctx, "tier"))))));
        }

        private static int execute(CommandSource source, Collection<ServerPlayerEntity> players, ItemInput item, int tier) throws CommandSyntaxException {
            for(ServerPlayerEntity player : players) {
                ItemStack stack = AmuletItem.getStackWithTier(item.createStack(1, false), tier);
                if(item.getItem() instanceof AmuletItem) {
                    ItemHandlerHelper.giveItemToPlayer(player, stack);
                } else {
                    throw ISNT_AMULET.create(stack);
                }

            }
            if (players.size() == 1) {
                source.sendFeedback(new TranslationTextComponent("commands.elementalamulets.give.success", tier, item.createStack(1, false).getTextComponent(), players.iterator().next().getDisplayName()), true);
            } else {
                source.sendFeedback(new TranslationTextComponent("commands.elementalamulets.give.success", tier, item.createStack(1, false).getTextComponent(), players.size()), true);
            }

            return players.size();
        }
    }
}
