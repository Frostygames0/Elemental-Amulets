/*
 *    This file is part of Elemental Amulets.
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

package frostygames0.elementalamulets.core.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

/**
 * @author Frostygames0
 * @date 25.09.2021 15:04
 */
public class AmuletHelper {

    public static void damage(ItemStack stack, LivingEntity entity, String id, int index) {
        stack.hurtAndBreak(2, entity, ent -> CuriosApi.getCuriosHelper().onBrokenCurio(id, index, ent));
    }
}
