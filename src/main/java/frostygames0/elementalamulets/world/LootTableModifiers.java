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

package frostygames0.elementalamulets.world;

import com.google.gson.JsonObject;
import frostygames0.elementalamulets.config.ModConfig;
import frostygames0.elementalamulets.init.ModItems;
import frostygames0.elementalamulets.items.amulets.AmuletItem;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.event.RegistryEvent;


import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

/**
 * @author Frostygames0
 * @date 21.09.2021 15:47
 */
public class LootTableModifiers {

    public static void registerLootModifierSerializer(RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        event.getRegistry().register(new TreasureLoot.Serializer().setRegistryName(modPrefix("vanilla_dungeons_loot")));
    }

    public static class TreasureLoot extends LootModifier {
        private static final Random RANDOM = new Random();

        private final float desertChance;
        private final float buriedChance;
        private final float shipwreckChance;
        private final float netherChance;

        public TreasureLoot(ILootCondition[] conditionsIn, float desertChance, float buriedChance, float shipwreckChance, float netherChance) {
            super(conditionsIn);
            this.desertChance = desertChance;
            this.buriedChance = buriedChance;
            this.shipwreckChance = shipwreckChance;
            this.netherChance = netherChance;
        }

        @Nonnull
        @Override
        protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
            List<AmuletItem> AMULETS = ModItems.getAmulets();
            if (ModConfig.CachedValues.MODIFY_VANILLA_LOOT) {
                if (LootTables.DESERT_PYRAMID.equals(context.getQueriedLootTableId()) && RANDOM.nextDouble() <= desertChance) {
                    generatedLoot.add(AmuletItem.getStackWithTier(new ItemStack(AMULETS.get(RANDOM.nextInt(AMULETS.size()))), 1));
                } else if (LootTables.BURIED_TREASURE.equals(context.getQueriedLootTableId()) && RANDOM.nextDouble() <= buriedChance) {
                    generatedLoot.add(new ItemStack(ModItems.AETHER_ELEMENT.get(), 2));
                } else if (LootTables.SHIPWRECK_TREASURE.equals(context.getQueriedLootTableId()) && RANDOM.nextDouble() <= shipwreckChance) {
                    generatedLoot.add(new ItemStack(ModItems.WATER_ELEMENT.get(), 5));
                } else if (LootTables.NETHER_BRIDGE.equals(context.getQueriedLootTableId()) && RANDOM.nextDouble() <= netherChance) {
                    generatedLoot.add(new ItemStack(ModItems.FIRE_ELEMENT.get(), 4));
                }
            }
            return generatedLoot;
        }

        public static class Serializer extends GlobalLootModifierSerializer<TreasureLoot> {

            @Override
            public TreasureLoot read(ResourceLocation location, JsonObject object, ILootCondition[] ailootcondition) {
                float desert = JSONUtils.getAsFloat(object, "desert_pyramid", 0.1f);
                float treasure = JSONUtils.getAsFloat(object, "buried_treasure", 0.3f);
                float shipwreck = JSONUtils.getAsFloat(object, "shipwreck_treasure", 0.3f);
                float nether = JSONUtils.getAsFloat(object, "nether_bridge", 0.2f);
                return new TreasureLoot(ailootcondition, desert, treasure, shipwreck, nether);
            }

            @Override
            public JsonObject write(TreasureLoot instance) {
                JsonObject json = makeConditions(instance.conditions);
                json.addProperty("desert_pyramid", instance.desertChance);
                json.addProperty("buried_treasure", instance.buriedChance);
                json.addProperty("shipwreck_treasure", instance.shipwreckChance);
                json.addProperty("nether_bridge", instance.netherChance);
                return json;
            }
        }
    }
}
