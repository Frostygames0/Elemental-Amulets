/*
 *  Copyright (c) 2021
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

package frostygames0.elementalamulets.util;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;


import java.util.UUID;

/**
 * @author Frostygames0
 * @date 21.10.2021 16:14
 */
public final class AttributeUtil {
    public static void applyModifier(AttributeInstance attribute, AttributeModifier modifier) {
        if (!attribute.hasModifier(modifier)) {
            attribute.addTransientModifier(modifier);
        }
    }

    public static void removeModifier(AttributeInstance attribute, AttributeModifier modifier) {
        if (attribute.hasModifier(modifier)) {
            attribute.removeModifier(modifier);
        }
    }

    public static void removeModifierByUUID(AttributeInstance attribute, UUID modifier) {
        if (attribute.getModifier(modifier) != null) {
            attribute.removeModifier(modifier);
        }
    }
}
