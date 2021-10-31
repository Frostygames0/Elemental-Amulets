/*
 *     Copyright (c) 2021
 *
 *     This file is part of Elemental Amulets, a Minecraft Mod.
 *
 *     Elemental Amulets is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Elemental Amulets is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Elemental Amulets.  If not, see <https://www.gnu.org/licenses/>.
 */

package frostygames0.elementalamulets.items;

import frostygames0.elementalamulets.advancements.triggers.ModCriteriaTriggers;
import frostygames0.elementalamulets.client.patchouli.PatchouliUtils;
import frostygames0.elementalamulets.init.ModStats;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import vazkii.patchouli.api.PatchouliAPI;


import javax.annotation.Nullable;
import java.util.List;

import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

public class GuideBookItem extends Item {
    public static final ResourceLocation BOOK_ID = modPrefix("guide_book");

    public GuideBookItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if(PatchouliUtils.isSafeToUse()) tooltip.add(((IFormattableTextComponent)PatchouliAPI.get().getSubtitle(BOOK_ID)).withStyle(TextFormatting.GOLD));
        tooltip.add(new TranslationTextComponent("item.elementalamulets.guide_book.subtitle").withStyle(TextFormatting.GRAY));
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if(playerIn instanceof ServerPlayerEntity) {
            if (PatchouliUtils.isSafeToUse()) {
                PatchouliAPI.get().openBookGUI((ServerPlayerEntity) playerIn, BOOK_ID);
                ModCriteriaTriggers.SUCCESS_USE.trigger((ServerPlayerEntity) playerIn, playerIn.getItemInHand(handIn));
                playerIn.awardStat(ModStats.GUIDE_OPENED);
                return ActionResult.success(playerIn.getItemInHand(handIn));
            } else {
                playerIn.displayClientMessage(new TranslationTextComponent("patchouli.elementalamulets.not_present").withStyle(TextFormatting.RED), true);
                return ActionResult.fail(playerIn.getItemInHand(handIn));
            }
        }
        return ActionResult.consume(playerIn.getItemInHand(handIn));
    }
}
