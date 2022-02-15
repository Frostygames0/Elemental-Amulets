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

package frostygames0.elementalamulets.world;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import frostygames0.elementalamulets.config.ModConfig;
import frostygames0.elementalamulets.items.amulets.AmuletItem;
import frostygames0.elementalamulets.util.AmuletUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.NotNull;


import javax.annotation.Nonnull;
import java.util.List;

import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

/**
 * @author Frostygames0
 * @date 21.09.2021 15:47
 */
public class LootTableModifiers {

    public static void registerLootModifierSerializer(RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        IForgeRegistry<GlobalLootModifierSerializer<?>> registry = event.getRegistry();
        registry.register(new TreasureLoot.Serializer().setRegistryName(modPrefix("treasure_loot")));
        registry.register(new RandomAmuletLoot.Serializer().setRegistryName(modPrefix("random_amulet_loot")));
    }

    public static class TreasureLoot extends LootModifier {
        private static final Gson LOOT_GSON = Deserializers.createFunctionSerializer().create();

        private final LootPoolEntryContainer item;

        public TreasureLoot(LootItemCondition[] conditionsIn, LootPoolEntryContainer item) {
            super(conditionsIn);
            this.item = item;
        }

        @Nonnull
        @Override
        protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
            if(ModConfig.CachedValues.MODIFY_VANILLA_LOOT) item.expand(context, generator -> generator.createItemStack(generatedLoot::add, context));
            return generatedLoot;
        }

        public static class Serializer extends GlobalLootModifierSerializer<TreasureLoot> {

            @Override
            public TreasureLoot read(ResourceLocation location, JsonObject object, LootItemCondition[] aiLootCondition) {
                LootPoolEntryContainer item = LOOT_GSON.fromJson(GsonHelper.getAsJsonObject(object, "item_entry"), LootPoolEntryContainer.class);
                return new TreasureLoot(aiLootCondition, item);
            }

            @Override
            public JsonObject write(TreasureLoot instance) {
                JsonObject json = makeConditions(instance.conditions);
                json.add("item_entry", LOOT_GSON.toJsonTree(instance.item));
                return json;
            }
        }
    }

    public static class RandomAmuletLoot extends LootModifier {

        public RandomAmuletLoot(LootItemCondition[] conditionsIn) {
            super(conditionsIn);
        }

        @NotNull
        @Override
        protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
            List<AmuletItem> amulets = AmuletItem.getAmulets().stream().filter(AmuletItem::canBeGenerated).toList();
            if(ModConfig.CachedValues.MODIFY_VANILLA_LOOT) generatedLoot.add(AmuletUtil.setStackTier(amulets.get(context.getRandom().nextInt(amulets.size())), 1));
            return generatedLoot;
        }

        public static class Serializer extends GlobalLootModifierSerializer<RandomAmuletLoot> {

            @Override
            public RandomAmuletLoot read(ResourceLocation location, JsonObject object, LootItemCondition[] ailootcondition) {
                return new RandomAmuletLoot(ailootcondition);
            }

            @Override
            public JsonObject write(RandomAmuletLoot instance) {
                return makeConditions(instance.conditions);
            }
        }
    }
}
