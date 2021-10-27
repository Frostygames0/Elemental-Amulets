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

package frostygames0.elementalamulets.util;

import net.minecraft.item.ItemStack;


import java.util.UUID;

public final class NBTUtil {

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

    public static UUID getUUID(ItemStack stack, String tagName) {
        return stack.getOrCreateTag().hasUUID(tagName) ? stack.getOrCreateTag().getUUID(tagName) : new UUID(0L, 0L);
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

    public static void putUUID(ItemStack stack, String name, UUID value) {
        stack.getOrCreateTag().putUUID(name, value);
    }
}
