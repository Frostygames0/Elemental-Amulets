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
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.ItemArgument;
import net.minecraft.command.arguments.ItemInput;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
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
                LiteralArgumentBuilder.<CommandSource>literal(ElementalAmulets.MOD_ID).
                        executes(src -> {
                            src.getSource().sendSuccess(new StringTextComponent("Elemental Amulets").withStyle(TextFormatting.GOLD), false);
                            return 0;
                        }).then(GiveAmuletCommand.register())
        );
    }

    private static class GiveAmuletCommand {
        private static final DynamicCommandExceptionType ISNT_AMULET = new DynamicCommandExceptionType(item -> new TranslationTextComponent("commands.elementalamulets.give.failure", ((ItemStack) item).getDisplayName()));

        private static ArgumentBuilder<CommandSource, ?> register() {
            return Commands.literal("give")
                    .requires(s -> s.hasPermission(2))
                    .then(Commands.argument("player", EntityArgument.players())
                            .then(Commands.argument("amulet", ItemArgument.item()).suggests((ctx, builder) -> ISuggestionProvider.suggest(AmuletItem.getAmulets().stream().map(item -> item.getRegistryName().toString()), builder)).executes(ctx -> execute(ctx.getSource(), EntityArgument.getPlayers(ctx, "player"), ItemArgument.getItem(ctx, "amulet"), 1))
                                    .then(Commands.argument("tier", IntegerArgumentType.integer(1, AmuletItem.MAX_TIER))
                                            .executes(ctx -> execute(ctx.getSource(), EntityArgument.getPlayers(ctx, "player"), ItemArgument.getItem(ctx, "amulet"), IntegerArgumentType.getInteger(ctx, "tier"))))));
        }

        private static int execute(CommandSource source, Collection<ServerPlayerEntity> players, ItemInput item, int tier) throws CommandSyntaxException {
            ItemStack stack = AmuletUtil.setStackTier(item.createItemStack(1, false), tier);
            for (ServerPlayerEntity player : players) {
                if (item.getItem() instanceof AmuletItem) {
                    ItemHandlerHelper.giveItemToPlayer(player, stack);
                } else {
                    throw ISNT_AMULET.create(stack);
                }

            }
            if (players.size() == 1) {
                source.sendSuccess(new TranslationTextComponent("commands.elementalamulets.give.success", tier, stack.getDisplayName(), players.iterator().next().getDisplayName()), true);
            } else {
                source.sendSuccess(new TranslationTextComponent("commands.elementalamulets.give.success", tier, stack.getDisplayName(), players.size()), true);
            }

            return players.size();
        }
    }
}
