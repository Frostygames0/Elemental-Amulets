/*
 *  Copyright (c) 2021
 *
 *     This file is part of Elemental Amulets, a Minecraft Mod.
 *
 *     Elemental Amulets is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Elemental Amulets is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with Elemental Amulets.  If not, see <https://www.gnu.org/licenses/>.
 */

package frostygames0.elementalamulets.init;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.items.amulets.AmuletItem;
import frostygames0.elementalamulets.util.AmuletUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
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
                LiteralArgumentBuilder.<CommandSourceStack>literal(ElementalAmulets.MOD_ID).
                        executes(src -> {
                            src.getSource().sendSuccess(new TextComponent("Elemental Amulets").withStyle(ChatFormatting.GOLD), false);
                            return 0;
                        }).then(GiveAmuletCommand.register())
        );
    }

    private static class GiveAmuletCommand {
        private static final DynamicCommandExceptionType ISNT_AMULET = new DynamicCommandExceptionType(item -> new TranslatableComponent("commands.elementalamulets.give.failure", ((ItemStack) item).getDisplayName()));

        private static ArgumentBuilder<CommandSourceStack, ?> register() {
            return Commands.literal("give")
                    .requires(s -> s.hasPermission(2))
                    .then(Commands.argument("player", EntityArgument.players())
                            .then(Commands.argument("amulet", ItemArgument.item()).suggests((ctx, builder) -> SharedSuggestionProvider.suggest(ModItems.getAmulets().stream().map(item -> item.getRegistryName().toString()), builder)).executes(ctx -> execute(ctx.getSource(), EntityArgument.getPlayers(ctx, "player"), ItemArgument.getItem(ctx, "amulet"), 1))
                                    .then(Commands.argument("tier", IntegerArgumentType.integer(1, AmuletItem.MAX_TIER))
                                            .executes(ctx -> execute(ctx.getSource(), EntityArgument.getPlayers(ctx, "player"), ItemArgument.getItem(ctx, "amulet"), IntegerArgumentType.getInteger(ctx, "tier"))))));
        }

        private static int execute(CommandSourceStack source, Collection<ServerPlayer> players, ItemInput item, int tier) throws CommandSyntaxException {
            ItemStack stack = AmuletUtil.setStackTier(item.createItemStack(1, false), tier);
            for (ServerPlayer player : players) {
                if (item.getItem() instanceof AmuletItem) {
                    ItemHandlerHelper.giveItemToPlayer(player, stack);
                } else {
                    throw ISNT_AMULET.create(stack);
                }

            }
            if (players.size() == 1) {
                source.sendSuccess(new TranslatableComponent("commands.elementalamulets.give.success", tier, stack.getDisplayName(), players.iterator().next().getDisplayName()), true);
            } else {
                source.sendSuccess(new TranslatableComponent("commands.elementalamulets.give.success", tier, stack.getDisplayName(), players.size()), true);
            }

            return players.size();
        }
    }
}
