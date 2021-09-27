package frostygames0.elementalamulets.core.init;

import net.minecraft.stats.Stats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;


import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

/**
 * @author Frostygames0
 * @date 26.09.2021 17:22
 */
public class ModStats {
    public static final ResourceLocation AMULET_WORN_STAT = modPrefix("amulets_worn");
    public static final ResourceLocation TIMES_COMBINED = modPrefix("times_combined");
    public static final ResourceLocation GUIDE_OPENED = modPrefix("guide_opened");

    public static void registerStats() {
        registerStat(AMULET_WORN_STAT);
        registerStat(TIMES_COMBINED);
        registerStat(GUIDE_OPENED);
    }

    private static void registerStat(ResourceLocation name) {
        Registry.register(Registry.CUSTOM_STAT, name.getPath(), name);
        Stats.CUSTOM.get(name);
    }
}
