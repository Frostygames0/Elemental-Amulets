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

package frostygames0.elementalamulets.datagen.tags;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.init.ModBlocks;
import frostygames0.elementalamulets.init.ModItems;
import frostygames0.elementalamulets.init.ModTags;
import frostygames0.elementalamulets.items.amulets.AmuletItem;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;


import javax.annotation.Nullable;

public class ItemTagsProvider extends net.minecraft.data.tags.ItemTagsProvider {
    public ItemTagsProvider(DataGenerator dataGenerator, net.minecraft.data.tags.BlockTagsProvider blockProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockProvider, ElementalAmulets.MOD_ID, existingFileHelper);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void addTags() {
        //Amulets
        AmuletItem[] amulets = AmuletItem.getAmulets().toArray(new AmuletItem[0]);
        this.tag(ModTags.Items.NECKLACES).add(amulets);
        this.tag(ModTags.Items.SHARD_BLOCKS).add(ModBlocks.ELEMENTAL_SHARDS_BLOCK.get().asItem(),
                ModBlocks.FIRE_SHARDS_BLOCK.get().asItem(),
                ModBlocks.WATER_SHARDS_BLOCK.get().asItem(),
                ModBlocks.EARTH_SHARDS_BLOCK.get().asItem(),
                ModBlocks.AIR_SHARDS_BLOCK.get().asItem());

        // Curios tag
        this.tag(ItemTags.create(new ResourceLocation("curios:necklace"))).addTag(ModTags.Items.NECKLACES);
        this.tag(ItemTags.create(new ResourceLocation("curios:belt"))).add(ModItems.AMULET_BELT.get());

        // Elements
        this.tag(ModTags.Items.ELEMENTS).add(ModItems.AIR_ELEMENT.get(), ModItems.EARTH_ELEMENT.get(), ModItems.FIRE_ELEMENT.get(), ModItems.WATER_ELEMENT.get(), ModItems.ELEMENTAL_SHARDS.get());
        // Fire
        this.tag(ModTags.Items.FIRE_ELEMENT_CONVERTIBLE).add(Items.BLAZE_POWDER, Items.NETHERITE_SCRAP, Items.LAVA_BUCKET, Items.FIRE_CHARGE, Items.MAGMA_BLOCK);
        // Water
        this.tag(ModTags.Items.WATER_ELEMENT_CONVERTIBLE).add(Items.WATER_BUCKET, Items.WET_SPONGE, Items.PRISMARINE_CRYSTALS, Items.PRISMARINE_SHARD).addTag(ItemTags.FISHES);
        // Air
        this.tag(ModTags.Items.AIR_ELEMENT_CONVERTIBLE).add(Items.FEATHER).addTags(ItemTags.SAND, ItemTags.WOOL);
        // Earth
        this.tag(ModTags.Items.EARTH_ELEMENT_CONVERTIBLE).add(Items.DIRT, Items.COARSE_DIRT, Items.GRASS_BLOCK).addTags(ItemTags.FLOWERS, ItemTags.LEAVES, ItemTags.LOGS).addTags(Tags.Items.SEEDS, Tags.Items.CROPS);
    }
}
