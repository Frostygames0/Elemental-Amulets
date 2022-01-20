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

package frostygames0.elementalamulets.init;

import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.config.ModConfig;
import frostygames0.elementalamulets.items.amulets.AmuletItem;
import frostygames0.elementalamulets.world.structures.ModStructures;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = ElementalAmulets.MOD_ID)
public class ModVillagers {
    public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(ForgeRegistries.PROFESSIONS, ElementalAmulets.MOD_ID);
    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, ElementalAmulets.MOD_ID);

    public static final RegistryObject<PoiType> JEWELLER_POI = POI_TYPES.register("jeweller",
            () -> new PoiType("jeweller", PoiType.getBlockStates(ModBlocks.ELEMENTAL_COMBINATOR.get()), 1, 1));
    public static final RegistryObject<VillagerProfession> JEWELLER = PROFESSIONS.register("jeweller",
            () -> new VillagerProfession("jeweller", JEWELLER_POI.get(), ImmutableSet.of(ModItems.ELEMENTAL_ORE.get(), ModItems.FIRE_ELEMENT.get(), ModItems.EARTH_ELEMENT.get(), ModItems.WATER_ELEMENT.get(), ModItems.AIR_ELEMENT.get()), ImmutableSet.of(), SoundEvents.ENCHANTMENT_TABLE_USE));

    public static void register(IEventBus bus) {
        POI_TYPES.register(bus);
        PROFESSIONS.register(bus);
    }

    @SubscribeEvent
    public static void registerTrades(final VillagerTradesEvent event) {
        if (event.getType() == JEWELLER.get()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            trades.get(1).add(new BasicItemListing(new ItemStack(ModItems.ELEMENTAL_SHARDS.get(), 5), new ItemStack(Items.EMERALD), 10, 1, 1f));

            List<VillagerTrades.ItemListing> trades2 = trades.get(2);
            trades2.add(new BasicItemListing(2, new ItemStack(ModItems.ELEMENTAL_GUIDE.get()), 1, 2, 1.3F));
            trades2.add(new BasicItemListing(5, new ItemStack(ModBlocks.ELEMENTAL_COMBINATOR.get().asItem()), 2, 5, 1.5f));

            List<VillagerTrades.ItemListing> trades3 = trades.get(3);
            trades3.add(new BasicItemListing(3, new ItemStack(ModItems.FIRE_ELEMENT.get()), 10, 1, 1.2f));
            trades3.add(new BasicItemListing(3, new ItemStack(ModItems.WATER_ELEMENT.get()), 10, 1, 1.2f));
            trades3.add(new BasicItemListing(3, new ItemStack(ModItems.AIR_ELEMENT.get()), 10, 1, 1.2f));
            trades3.add(new BasicItemListing(3, new ItemStack(ModItems.EARTH_ELEMENT.get()), 10, 1, 1.2f));

            trades.get(4).add(new BasicItemListing(new ItemStack(Items.EMERALD, 10), new ItemStack(ModItems.AETHER_ELEMENT.get(), 2), new ItemStack(ModItems.ALL_SEEING_LENS.get()), 4, 10, 1.3F));

            List<VillagerTrades.ItemListing> trades5 = trades.get(5);
            trades5.add(new BasicItemListing(new ItemStack(Items.EMERALD, 25), new ItemStack(ModItems.AETHER_ELEMENT.get()), new ItemStack(ModItems.AMULET_BELT.get()), 1, 30, 2.5f));
            trades5.add(new CultTempleListing(30, 1, 10));
        }
    }

    @SubscribeEvent
    public static void registerWandererTrades(final WandererTradesEvent event) {
        event.getRareTrades().add(new EmeraldsForRandomAmulet(45, 25, 1.5f));
    }

    public static class CultTempleListing implements VillagerTrades.ItemListing {
        private final int emerald;
        private final int maxUses;
        private final int villagerXp;

        public CultTempleListing(int emerald, int maxUses, int villagerXp) {
            this.emerald = emerald;
            this.maxUses = maxUses;
            this.villagerXp = villagerXp;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity pTrader, Random pRand) {
            if (!(pTrader.level instanceof ServerLevel serverworld)) {
                return null;
            } else {
                BlockPos blockpos = serverworld.findNearestMapFeature(ModStructures.CULT_TEMPLE.get(), pTrader.blockPosition(), 2000, true);
                if (blockpos != null) {
                    ItemStack itemstack = MapItem.create(serverworld, blockpos.getX(), blockpos.getZ(), (byte) 3, true, true);
                    MapItem.renderBiomePreviewMap(serverworld, itemstack);
                    MapItemSavedData.addTargetDecoration(itemstack, blockpos, "+", MapDecoration.Type.RED_X);
                    itemstack.setHoverName(new TranslatableComponent("filled_map.elementalamulets.cult_temple"));
                    return new MerchantOffer(new ItemStack(Items.EMERALD, this.emerald), new ItemStack(Items.COMPASS), itemstack, this.maxUses, this.villagerXp, 0.2F);
                } else {
                    return null;
                }
            }
        }
    }

    public static class EmeraldsForRandomAmulet implements VillagerTrades.ItemListing {
        private final int emerald;
        private final int villagerXp;
        private final float priceMult;

        public EmeraldsForRandomAmulet(int emerald, int villagerXp, float priceMult) {
            this.emerald = emerald;
            this.villagerXp = villagerXp;
            this.priceMult = priceMult;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity pTrader, Random pRand) {
            List<AmuletItem> amulets = ModItems.AMULETS.get();
            ItemStack stack = new ItemStack(amulets.get(pRand.nextInt(amulets.size())));
            return new MerchantOffer(new ItemStack(Items.EMERALD, this.emerald), AmuletItem.getStackWithTier(stack, 3), 1, villagerXp, priceMult);
        }
    }


    public static class Structures {
        public static void addHouses(final ServerAboutToStartEvent event) {
            if (ModConfig.CachedValues.GENERATE_JEWELLER_HOUSE) {
                Registry<StructureTemplatePool> registry = event.getServer().registryAccess().registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
                String[] allowedBiomes = {"plains", "taiga"};
                for (String biome : allowedBiomes) { // This is because it should be all village biomes but for now there is only plains
                    addHouse(registry, new ResourceLocation("village/" + biome + "/houses"),
                            ElementalAmulets.MOD_ID + ":villages/jeweller_house_" + biome, 12);
                }
                ElementalAmulets.LOGGER.debug("Jeweller's house was successfully added to: " + Arrays.toString(allowedBiomes) + " villages");
            } else ElementalAmulets.LOGGER.debug("Jeweller's house generation was skipped (Config Preference)");
        }

        private static void addHouse(Registry<StructureTemplatePool> registry, ResourceLocation poolId, String houseToAdd, int weight) {
            StructureTemplatePool pool = registry.get(poolId);
            if (pool == null) {
                ElementalAmulets.LOGGER.warn("Jigsaw pool " + poolId + " is not found, " + houseToAdd + " cannot be generated. Skipping...");
                return;
            }

            StructurePoolElement piece = StructurePoolElement.single(houseToAdd, ProcessorLists.MOSSIFY_10_PERCENT).apply(StructureTemplatePool.Projection.RIGID);

            for (int i = 0; i < weight; i++) {
                pool.templates.add(piece);
            }

            List<Pair<StructurePoolElement, Integer>> listOfPieceEntries = new ArrayList<>(pool.rawTemplates);
            listOfPieceEntries.add(new Pair<>(piece, weight));
            pool.rawTemplates = listOfPieceEntries;
        }
    }

}
