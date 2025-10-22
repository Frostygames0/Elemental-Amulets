package frostygames0.elementalamulets.util;

import net.minecraft.util.Mth;

public class MathUtils {
    public static int calculateClamp(int current, int maxCapacity, int value) {
        if (maxCapacity <= 0) {
            return 0;
        }

        float percentage = (float) current / maxCapacity;
        return Mth.clamp((int) Math.floor(percentage * value), 0, value);
    }
}
