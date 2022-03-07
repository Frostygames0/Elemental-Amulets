/*
 *  Copyright (c) 2021-2022
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

package frostygames0.elementalamulets.items;

import frostygames0.elementalamulets.advancements.triggers.ModCriteriaTriggers;
import frostygames0.elementalamulets.init.ModStats;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;


import javax.annotation.Nullable;
import java.util.List;

import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

public class ElementalGuideItem extends Item {
    public static final ResourceLocation BOOK_ID = modPrefix("elemental_guide");

    public ElementalGuideItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(new TranslatableComponent("item.elementalamulets.guide_book.subtitle").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        if (playerIn instanceof ServerPlayer) {
            if (ModList.get().isLoaded("patchouli")) {
                //PatchouliAPI.get().openBookGUI((ServerPlayer) playerIn, BOOK_ID);
                ModCriteriaTriggers.SUCCESS_USE.trigger((ServerPlayer) playerIn, playerIn.getItemInHand(handIn));
                playerIn.awardStat(ModStats.GUIDE_OPENED);
                return InteractionResultHolder.success(playerIn.getItemInHand(handIn));
            } else {
                playerIn.displayClientMessage(new TranslatableComponent("patchouli.elementalamulets.not_present").withStyle(ChatFormatting.RED), true);
                return InteractionResultHolder.fail(playerIn.getItemInHand(handIn));
            }
        }
        return InteractionResultHolder.consume(playerIn.getItemInHand(handIn));
    }
}
