package frostygames0.elementalamulets.core.util;

import frostygames0.elementalamulets.ElementalAmulets;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.TextFormatting;

public class ElementalColors {
    // Main elements
    public static final Rarity FIRE = Rarity.create(ElementalAmulets.MOD_ID+":fire_rarity", TextFormatting.RED);
    public static final Rarity WATER = Rarity.create(ElementalAmulets.MOD_ID+":water_rarity", TextFormatting.AQUA);
    public static final Rarity EARTH = Rarity.create(ElementalAmulets.MOD_ID+":earth_rarity", TextFormatting.DARK_GREEN);
    public static final Rarity AIR = Rarity.create(ElementalAmulets.MOD_ID+":air_rarity", TextFormatting.WHITE);

    // Sub-elements
    public static final Rarity SPEED = Rarity.create(ElementalAmulets.MOD_ID+":speed_rarity", TextFormatting.BLUE);
    public static final Rarity JUMP = Rarity.create(ElementalAmulets.MOD_ID+":jump_rarity", TextFormatting.GREEN);
}
