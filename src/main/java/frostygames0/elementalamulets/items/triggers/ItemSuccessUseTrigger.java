package frostygames0.elementalamulets.items.triggers;

import com.google.gson.JsonObject;
import net.minecraft.advancements.criterion.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;


import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

/**
 * Works like ConsumeItemTrigger but triggers ONLY when action was successful
 * E.G: When amulet is worn or guide is opened
 * @author Frostygames0
 * @date 02.06.2021 10:01
 */
public class ItemSuccessUseTrigger extends AbstractCriterionTrigger<ItemSuccessUseTrigger.Instance> {
    public static final ResourceLocation ID = modPrefix("success_use_item");

    @Override
    protected Instance deserializeTrigger(JsonObject json, EntityPredicate.AndPredicate entityPredicate, ConditionArrayParser conditionsParser) {
        return new ItemSuccessUseTrigger.Instance(entityPredicate, ItemPredicate.deserialize(json.get("item")), LocationPredicate.deserialize(json.get("location")));
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public void trigger(ServerPlayerEntity player, ItemStack stack, ServerWorld world, double x, double y, double z) {
        triggerListeners(player, instance -> instance.test(stack, world, x, y, z));
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
            return this.item.test(stack) && this.location.test(world, x, y, z);
        }

        public ItemPredicate getItem() {
            return this.item;
        }

        public LocationPredicate getLocation() {
            return location;
        }
    }
}
