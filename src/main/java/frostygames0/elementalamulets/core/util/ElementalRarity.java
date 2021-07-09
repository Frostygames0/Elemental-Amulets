package frostygames0.elementalamulets.core.util;

import net.minecraft.item.Rarity;
import net.minecraft.util.text.TextFormatting;

import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

public class ElementalRarity {
    // Main elements
    public static final Rarity FIRE = Rarity.create(modPrefix("fire_rarity").toString(), TextFormatting.RED);
    public static final Rarity WATER = Rarity.create(modPrefix("water_rarity").toString(), TextFormatting.BLUE);
    public static final Rarity EARTH = Rarity.create(modPrefix("earth_rarity").toString(), TextFormatting.DARK_GREEN);
    public static final Rarity AIR = Rarity.create(modPrefix("air_rarity").toString(), TextFormatting.WHITE);

    // Sub-elements
    public static final Rarity SPEED = Rarity.create(modPrefix("speed_rarity").toString(), TextFormatting.AQUA);
    public static final Rarity JUMP = Rarity.create(modPrefix("jump_rarity").toString(), TextFormatting.GREEN);
}
