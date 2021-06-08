package frostygames0.elementalamulets.datagen;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.core.init.ModItems;
import frostygames0.elementalamulets.core.init.ModTags;
import frostygames0.elementalamulets.items.amulets.AmuletItem;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.data.ExistingFileHelper;


import javax.annotation.Nullable;

public class TagProvider extends ItemTagsProvider {
    public TagProvider(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, new BlockTagsProvider(dataGenerator, ElementalAmulets.MOD_ID, existingFileHelper), ElementalAmulets.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        //Amulets
        AmuletItem[] amulets = ModItems.getAmulets().toArray(new AmuletItem[0]);
        getOrCreateBuilder(ModTags.NECKLACE).add(amulets);
        // Elements
        getOrCreateBuilder(ModTags.ELEMENTS).add(ModItems.AIR_ELEMENT.get(), ModItems.EARTH_ELEMENT.get(), ModItems.FIRE_ELEMENT.get(), ModItems.WATER_ELEMENT.get(), ModItems.ELEMENTAL_SHARDS.get());
        // Fire
        getOrCreateBuilder(ModTags.FIRE_ELEMENT_CONVERTIBLE).add(Items.BLAZE_POWDER, Items.BLAZE_ROD, Items.NETHERITE_INGOT,
                Items.NETHERITE_BLOCK, Items.NETHERITE_SCRAP, Items.ANCIENT_DEBRIS, Items.LAVA_BUCKET, Items.FIRE_CHARGE);
        // Water
        getOrCreateBuilder(ModTags.WATER_ELEMENT_CONVERTIBLE).add(Items.WATER_BUCKET, Items.WET_SPONGE, Items.HEART_OF_THE_SEA,
                Items.PRISMARINE, Items.PRISMARINE_BRICK_SLAB, Items.PRISMARINE_BRICK_STAIRS,
                Items.PRISMARINE_BRICKS, Items.PRISMARINE_SLAB, Items.PRISMARINE_STAIRS,
                Items.PRISMARINE_CRYSTALS, Items.PRISMARINE_SHARD, Items.PRISMARINE_WALL,
                Items.DARK_PRISMARINE, Items.DARK_PRISMARINE_SLAB, Items.DARK_PRISMARINE_STAIRS ).addTag(ItemTags.FISHES);
        // Air
        getOrCreateBuilder(ModTags.AIR_ELEMENT_CONVERTIBLE).add(Items.FEATHER, Items.FIREWORK_ROCKET);
        // Earth
        getOrCreateBuilder(ModTags.EARTH_ELEMENT_CONVERTIBLE).add(Items.DIRT, Items.COARSE_DIRT, Items.GRASS_BLOCK).addTag(ItemTags.FLOWERS)
                .addTag(ItemTags.LEAVES).addTag(ItemTags.LOGS).addTag(ItemTags.PLANKS);
    }
}
