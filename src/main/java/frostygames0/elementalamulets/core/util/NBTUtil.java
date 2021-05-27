package frostygames0.elementalamulets.core.util;

import net.minecraft.item.ItemStack;

public class NBTUtil {
    private NBTUtil() {}

    public static boolean isSafeToGet(ItemStack stack, String tagName) {
        return !stack.isEmpty() && stack.getOrCreateTag().contains(tagName);
    }

    public static int getInteger(ItemStack stack, String tagName) {
        return isSafeToGet(stack, tagName) ? stack.getOrCreateTag().getInt(tagName) : 0;
    }

    public static float getFloat(ItemStack stack, String tagName) {
        return isSafeToGet(stack, tagName) ? stack.getOrCreateTag().getFloat(tagName) : 0;
    }

    public static double getDouble(ItemStack stack, String tagName) {
        return isSafeToGet(stack, tagName) ? stack.getOrCreateTag().getDouble(tagName) : 0;
    }

    public static boolean getBoolean(ItemStack stack, String tagName) {
        return isSafeToGet(stack, tagName) && stack.getOrCreateTag().getBoolean(tagName);
    }

    public static String getString(ItemStack stack, String tagName) {
        return isSafeToGet(stack, tagName) ? stack.getOrCreateTag().getString(tagName) : "";
    }

    public static void putInteger(ItemStack stack, String tagName, int value) {
        stack.getOrCreateTag().putInt(tagName, value);
    }

    public static void putDouble(ItemStack stack, String tagName, double value) {
        stack.getOrCreateTag().putDouble(tagName, value);
    }

    public static void putFloat(ItemStack stack, String tagName, float value) {
        stack.getOrCreateTag().putFloat(tagName, value);
    }

    public static void putBoolean(ItemStack stack, String tagName, boolean value) {
        stack.getOrCreateTag().putBoolean(tagName, value);
    }

    public static void putString(ItemStack stack, String tagName, String value) {
        stack.getOrCreateTag().putString(tagName, value);
    }
}
