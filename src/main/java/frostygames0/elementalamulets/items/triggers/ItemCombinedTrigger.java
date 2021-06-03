package frostygames0.elementalamulets.items.triggers;

import com.google.gson.JsonObject;
import frostygames0.elementalamulets.ElementalAmulets;
import net.minecraft.advancements.criterion.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;

/**
 * @author Frostygames0
 * @date 02.06.2021 10:19
 */
public class ItemCombinedTrigger extends AbstractCriterionTrigger<ItemCombinedTrigger.Instance> {
    public static final ResourceLocation ID = new ResourceLocation(ElementalAmulets.MOD_ID, "item_elemental_combined");
    @Override
    protected Instance deserializeTrigger(JsonObject json, EntityPredicate.AndPredicate entityPredicate, ConditionArrayParser conditionsParser) {
        return new ItemCombinedTrigger.Instance(entityPredicate, ItemPredicate.deserialize(json.get("item")), LocationPredicate.deserialize(json.get("location")));
    }

    public void trigger(ServerPlayerEntity player, ItemStack stack, ServerWorld world, double x, double y, double z) {
        triggerListeners(player, instance -> instance.test(stack, world, x, y, z));
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
            return item.test(stack) && location.test(world, x, y, z);
        }

        public ItemPredicate getItem() {
            return this.item;
        }

        public LocationPredicate getLocation() {
            return this.location;
        }
    }
}
