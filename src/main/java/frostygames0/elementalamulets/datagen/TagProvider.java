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
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;


import javax.annotation.Nullable;

public class TagProvider extends ItemTagsProvider {
    public TagProvider(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, new BlockTagsProvider(dataGenerator, ElementalAmulets.MOD_ID, existingFileHelper), ElementalAmulets.MOD_ID, existingFileHelper);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void registerTags() {
        //Amulets
        AmuletItem[] amulets = ModItems.getAmulets().toArray(new AmuletItem[0]);
        this.getOrCreateBuilder(ModTags.NECKLACES).add(amulets);

        // Curios tag
        this.getOrCreateBuilder(ItemTags.makeWrapperTag("curios:necklace")).addTag(ModTags.NECKLACES);

        // Elements
        this.getOrCreateBuilder(ModTags.ELEMENTS).add(ModItems.AIR_ELEMENT.get(), ModItems.EARTH_ELEMENT.get(), ModItems.FIRE_ELEMENT.get(), ModItems.WATER_ELEMENT.get(), ModItems.AETHER_ELEMENT.get(), ModItems.ELEMENTAL_SHARDS.get());
        // Fire
        this.getOrCreateBuilder(ModTags.FIRE_ELEMENT_CONVERTIBLE).add(Items.BLAZE_POWDER, Items.NETHERITE_SCRAP, Items.LAVA_BUCKET, Items.FIRE_CHARGE);
        // Water
        this.getOrCreateBuilder(ModTags.WATER_ELEMENT_CONVERTIBLE).add(Items.WATER_BUCKET, Items.WET_SPONGE,
                Items.PRISMARINE, Items.PRISMARINE_BRICK_SLAB, Items.PRISMARINE_BRICK_STAIRS,
                Items.PRISMARINE_BRICKS, Items.PRISMARINE_SLAB, Items.PRISMARINE_STAIRS,
                Items.PRISMARINE_CRYSTALS, Items.PRISMARINE_SHARD, Items.PRISMARINE_WALL,
                Items.DARK_PRISMARINE, Items.DARK_PRISMARINE_SLAB, Items.DARK_PRISMARINE_STAIRS );
        // Air
        this.getOrCreateBuilder(ModTags.AIR_ELEMENT_CONVERTIBLE).add(Items.FEATHER).addTags(ItemTags.SAND, ItemTags.WOOL);
        // Earth
        this.getOrCreateBuilder(ModTags.EARTH_ELEMENT_CONVERTIBLE).add(Items.DIRT, Items.COARSE_DIRT, Items.GRASS_BLOCK).addTags(ItemTags.FLOWERS, ItemTags.LEAVES, ItemTags.LOGS).addTags(Tags.Items.SEEDS, Tags.Items.CROPS);
    }
}
