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
import frostygames0.elementalamulets.util.AmuletUtil;
import frostygames0.elementalamulets.world.structures.ModStructures;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.village.PointOfInterestType;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.template.ProcessorLists;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;
import net.minecraftforge.common.BasicTrade;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = ElementalAmulets.MOD_ID)
public class ModVillagers {
    public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(ForgeRegistries.PROFESSIONS, ElementalAmulets.MOD_ID);
    public static final DeferredRegister<PointOfInterestType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, ElementalAmulets.MOD_ID);

    public static final RegistryObject<PointOfInterestType> JEWELLER_POI = POI_TYPES.register("jeweller",
            () -> new PointOfInterestType("jeweller", PointOfInterestType.getBlockStates(ModBlocks.ELEMENTAL_COMBINATOR.get()), 1, 1));
    public static final RegistryObject<VillagerProfession> JEWELLER = PROFESSIONS.register("jeweller",
            () -> new VillagerProfession("jeweller", JEWELLER_POI.get(), ImmutableSet.of(ModItems.ELEMENTAL_ORE.get(), ModItems.FIRE_ELEMENT.get(), ModItems.EARTH_ELEMENT.get(), ModItems.WATER_ELEMENT.get(), ModItems.AIR_ELEMENT.get()), ImmutableSet.of(), SoundEvents.ENCHANTMENT_TABLE_USE));

    public static void register(IEventBus bus) {
        POI_TYPES.register(bus);
        PROFESSIONS.register(bus);
    }

    @SubscribeEvent
    public static void registerTrades(final VillagerTradesEvent event) {
        if (event.getType() == JEWELLER.get()) {
            Int2ObjectMap<List<VillagerTrades.ITrade>> trades = event.getTrades();

            trades.get(1).add(new BasicTrade(new ItemStack(ModItems.ELEMENTAL_SHARDS.get(), 5), new ItemStack(Items.EMERALD), 10, 1, 1f));

            List<VillagerTrades.ITrade> trades2 = trades.get(2);
            trades2.add(new BasicTrade(2, new ItemStack(ModItems.ELEMENTAL_GUIDE.get()), 1, 2, 1.3F));
            trades2.add(new BasicTrade(5, new ItemStack(ModBlocks.ELEMENTAL_COMBINATOR.get().asItem()), 2, 5, 1.5f));

            List<VillagerTrades.ITrade> trades3 = trades.get(3);
            trades3.add(new BasicTrade(3, new ItemStack(ModItems.FIRE_ELEMENT.get()), 10, 1, 1.2f));
            trades3.add(new BasicTrade(3, new ItemStack(ModItems.WATER_ELEMENT.get()), 10, 1, 1.2f));
            trades3.add(new BasicTrade(3, new ItemStack(ModItems.AIR_ELEMENT.get()), 10, 1, 1.2f));
            trades3.add(new BasicTrade(3, new ItemStack(ModItems.EARTH_ELEMENT.get()), 10, 1, 1.2f));

            trades.get(4).add(new BasicTrade(new ItemStack(Items.EMERALD, 10), new ItemStack(ModItems.AETHER_ELEMENT.get(), 2), new ItemStack(ModItems.ALL_SEEING_LENS.get()), 4, 10, 1.3F));

            List<VillagerTrades.ITrade> trades5 = trades.get(5);
            trades5.add(new BasicTrade(new ItemStack(Items.EMERALD, 25), new ItemStack(ModItems.AETHER_ELEMENT.get()), new ItemStack(ModItems.AMULET_BELT.get()), 1, 30, 2.5f));
            trades5.add(new CultTempleTrade(30, 1, 10));
        }
    }

    @SubscribeEvent
    public static void registerWandererTrades(final WandererTradesEvent event) {
        event.getRareTrades().add(new EmeraldsForRandomAmulet(45, 25, 1.5f));
    }

    public static class CultTempleTrade implements VillagerTrades.ITrade {
        private final int emerald;
        private final int maxUses;
        private final int villagerXp;

        public CultTempleTrade(int emerald, int maxUses, int villagerXp) {
            this.emerald = emerald;
            this.maxUses = maxUses;
            this.villagerXp = villagerXp;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity pTrader, Random pRand) {
            if (!(pTrader.level instanceof ServerWorld)) {
                return null;
            } else {
                ServerWorld serverworld = (ServerWorld) pTrader.level;
                BlockPos blockpos = serverworld.findNearestMapFeature(ModStructures.CULT_TEMPLE.get(), pTrader.blockPosition(), 2000, true);
                if (blockpos != null) {
                    ItemStack itemstack = FilledMapItem.create(serverworld, blockpos.getX(), blockpos.getZ(), (byte) 3, true, true);
                    FilledMapItem.renderBiomePreviewMap(serverworld, itemstack);
                    MapData.addTargetDecoration(itemstack, blockpos, "+", MapDecoration.Type.RED_X);
                    itemstack.setHoverName(new TranslationTextComponent("filled_map.elementalamulets.cult_temple"));
                    return new MerchantOffer(new ItemStack(Items.EMERALD, this.emerald), new ItemStack(Items.COMPASS), itemstack, this.maxUses, this.villagerXp, 0.2F);
                } else {
                    return null;
                }
            }
        }
    }

    public static class EmeraldsForRandomAmulet implements VillagerTrades.ITrade {
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
            List<AmuletItem> amulets = AmuletItem.getAmulets();
            ItemStack stack = AmuletUtil.setStackTier(amulets.get(pRand.nextInt(amulets.size())), 3);
            return new MerchantOffer(new ItemStack(Items.EMERALD, this.emerald), stack, 1, villagerXp, priceMult);
        }
    }


    public static class Structures {
        public static void addHouses(final FMLServerAboutToStartEvent event) {
            if (ModConfig.CachedValues.GENERATE_JEWELLER_HOUSE) {
                MutableRegistry<JigsawPattern> registry = event.getServer().registryAccess().registryOrThrow(Registry.TEMPLATE_POOL_REGISTRY);
                String[] allowedBiomes = {"plains", "taiga"};
                for (String biome : allowedBiomes) { // This is because it should be all village biomes but for now there is only plains
                    addHouse(registry, new ResourceLocation("village/" + biome + "/houses"),
                            ElementalAmulets.MOD_ID + ":villages/jeweller_house_" + biome, 12);
                }
                ElementalAmulets.LOGGER.debug("Jeweller's house was successfully added to: " + Arrays.toString(allowedBiomes) + " villages");
            } else ElementalAmulets.LOGGER.debug("Jeweller's house generation was skipped (Config Preference)");
        }

        private static void addHouse(MutableRegistry<JigsawPattern> registry, ResourceLocation poolId, String houseToAdd, int weight) {
            JigsawPattern pool = registry.get(poolId);
            if (pool == null) {
                ElementalAmulets.LOGGER.warn("Jigsaw pool " + poolId + " is not found, " + houseToAdd + " cannot be generated. Skipping...");
                return;
            }

            JigsawPiece piece = JigsawPiece.single(houseToAdd, ProcessorLists.MOSSIFY_10_PERCENT).apply(JigsawPattern.PlacementBehaviour.RIGID);

            for (int i = 0; i < weight; i++) {
                pool.templates.add(piece);
            }

            List<Pair<JigsawPiece, Integer>> listOfPieceEntries = new ArrayList<>(pool.rawTemplates);
            listOfPieceEntries.add(new Pair<>(piece, weight));
            pool.rawTemplates = listOfPieceEntries;
        }
    }

}
