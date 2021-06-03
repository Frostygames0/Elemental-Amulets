package frostygames0.elementalamulets.items.triggers;

import com.google.gson.JsonObject;
import frostygames0.elementalamulets.ElementalAmulets;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.ResourceLocation;

/**
 * @author Frostygames0
 * @date 02.06.2021 10:01
 */
public class ItemSuccessUseTrigger extends AbstractCriterionTrigger<ItemSuccessUseTrigger.Instance> {
    public static final ResourceLocation ID = new ResourceLocation(ElementalAmulets.MOD_ID, "success_use_item");

    @Override
    protected Instance deserializeTrigger(JsonObject json, EntityPredicate.AndPredicate entityPredicate, ConditionArrayParser conditionsParser) {
        return new ItemSuccessUseTrigger.Instance(entityPredicate, ItemPredicate.deserialize(json.get("item")));
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public void trigger(ServerPlayerEntity player, ItemStack stack) {
        triggerListeners(player, instance -> instance.test(stack));
    }

    public static class Instance extends CriterionInstance {
        private final ItemPredicate item;

        public Instance(EntityPredicate.AndPredicate playerCondition, ItemPredicate item) {
            super(ID, playerCondition);
            this.item = item;
        }

        boolean test(ItemStack stack) {
            return this.item.test(stack);
        }

        public ItemPredicate getItem() {
            return this.item;
        }
    }
}
