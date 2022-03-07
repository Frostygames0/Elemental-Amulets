/*
 *  Copyright (c) 2021-2022
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

package frostygames0.elementalamulets.advancements.triggers;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;


import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

/**
 * Works like ConsumeItemTrigger but triggers ONLY when action was successful
 * E.G: When amulet is worn or guide is opened
 *
 * @author Frostygames0
 * @date 02.06.2021 10:01
 */
public class ItemSuccessUseTrigger extends SimpleCriterionTrigger<ItemSuccessUseTrigger.Instance> {
    public static final ResourceLocation ID = modPrefix("success_use_item");

    @Override
    protected Instance createInstance(JsonObject json, EntityPredicate.Composite entityPredicate, DeserializationContext conditionsParser) {
        return new ItemSuccessUseTrigger.Instance(entityPredicate, ItemPredicate.fromJson(json.get("item")));
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public void trigger(ServerPlayer player, ItemStack stack) {
        trigger(player, instance -> instance.test(stack));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        private final ItemPredicate item;

        public Instance(EntityPredicate.Composite playerCondition, ItemPredicate item) {
            super(ID, playerCondition);
            this.item = item;
        }

        boolean test(ItemStack stack) {
            return this.item.matches(stack);
        }

        public ItemPredicate getItem() {
            return this.item;
        }
    }
}
