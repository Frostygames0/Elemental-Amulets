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

package frostygames0.elementalamulets.items.amulets;

import frostygames0.elementalamulets.config.ModConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import top.theillusivec4.curios.api.SlotContext;


import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

// TODO: Finish it
public class WaterAmulet extends AmuletItem {
    public static UUID MODIFIER_UUID = UUID.fromString("88f5e6ea-c12f-4cfd-b3f4-40d9ca8cdfad");
    public WaterAmulet(Properties properties) {
        super(properties, true);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if(Screen.hasShiftDown()) tooltip.add(new TranslationTextComponent("item.elementalamulets.wip_50").withStyle(TextFormatting.YELLOW));
    }

    @Override
    public void curioTick(String identifier, int index, LivingEntity livingEntity, ItemStack stack) {
        if(!livingEntity.level.isClientSide()) {
            ModifiableAttributeInstance att = livingEntity.getAttribute(ForgeMod.SWIM_SPEED.get());
            AttributeModifier attMod = new AttributeModifier(MODIFIER_UUID, modPrefix("speed").toString(),
                    this.getSwimSpeed(stack), AttributeModifier.Operation.MULTIPLY_TOTAL);
            if(livingEntity.isUnderWater()) {
                if(livingEntity.isSwimming()) {
                    if (!att.hasModifier(attMod)) {
                        att.addTransientModifier(attMod);
                    }
                } else {
                    if (att.hasModifier(attMod)) {
                        att.removeModifier(attMod);
                    }
                }
                if(this.getTier(stack) > 2) livingEntity.setAirSupply(300);
                // if(livingEntity.tickCount % 20 == 0) AmuletHelper.damage(stack, livingEntity, identifier, index);
            }
        }
    }

    @Override
    public void onUnequip(SlotContext slotContext, ItemStack newStack, ItemStack stack) {
        ModifiableAttributeInstance att = slotContext.getWearer().getAttribute(Attributes.MOVEMENT_SPEED);
        if (stack.getItem() != newStack.getItem()) {
            if (att.getModifier(MODIFIER_UUID) != null) {
                att.removeModifier(MODIFIER_UUID);
            }
        }
    }

    public float getSwimSpeed(ItemStack stack) {
        return (float) ModConfig.cached.WATER_AMULET_SPEED_BOOST*this.getTier(stack);
    }
}
