package frostygames0.elementalamulets.core.util;

import frostygames0.elementalamulets.ElementalAmulets;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.TextFormatting;

public class ElementalColors {
    public static final Rarity FIRE = Rarity.create(ElementalAmulets.MOD_ID+":fire_rarity", TextFormatting.RED);
    public static final Rarity SPEED = Rarity.create(ElementalAmulets.MOD_ID+":speed_rarity", TextFormatting.BLUE);
    public static final Rarity JUMP = Rarity.create(ElementalAmulets.MOD_ID+":jump_rarity", TextFormatting.GREEN);
    public static final Rarity INVISIBILITY = Rarity.create(ElementalAmulets.MOD_ID+":invisibility_rarity", TextFormatting.WHITE);
}
