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

package frostygames0.elementalamulets.advancements.triggers;

import com.google.gson.JsonObject;
import net.minecraft.advancements.criterion.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;


import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

/**
 * Triggered when combination happens.
 *
 * @author Frostygames0
 * @date 02.06.2021 10:19
 */
public class ItemCombinedTrigger extends AbstractCriterionTrigger<ItemCombinedTrigger.Instance> {
    public static final ResourceLocation ID = modPrefix("item_elemental_combined");

    @Override
    protected Instance createInstance(JsonObject json, EntityPredicate.AndPredicate entityPredicate, ConditionArrayParser conditionsParser) {
        return new ItemCombinedTrigger.Instance(entityPredicate, ItemPredicate.fromJson(json.get("item")), LocationPredicate.fromJson(json.get("location")));
    }

    public void trigger(ServerPlayerEntity player, ItemStack stack, ServerWorld world, double x, double y, double z) {
        trigger(player, instance -> instance.test(stack, world, x, y, z));
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public static class Instance extends CriterionInstance {
        private final ItemPredicate item;
        private final LocationPredicate location;

        public Instance(EntityPredicate.AndPredicate playerCondition, ItemPredicate item, LocationPredicate location) {
            super(ID, playerCondition);
            this.item = item;
            this.location = location;
        }

        boolean test(ItemStack stack, ServerWorld world, double x, double y, double z) {
            return item.matches(stack) && location.matches(world, x, y, z);
        }
    }
}
