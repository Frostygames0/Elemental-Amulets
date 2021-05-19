package frostygames0.elementalamulets.datagen;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.core.init.ModItems;
import frostygames0.elementalamulets.core.util.ModTags;
import frostygames0.elementalamulets.items.amulets.AmuletItem;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;

public class TagProvider extends ItemTagsProvider {
    public TagProvider(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, new BlockTagsProvider(dataGenerator, ElementalAmulets.MOD_ID, existingFileHelper), ElementalAmulets.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerTags() {
        Item[] amulets = ModItems.ITEMS.getEntries().stream().map(RegistryObject::get).filter(item -> item instanceof AmuletItem).toArray(Item[]::new);
        getOrCreateBuilder(ModTags.NECKLACE).add(amulets);
        getOrCreateBuilder(ModTags.ELEMENTS).add(ModItems.AIR_ELEMENT.get(), ModItems.EARTH_ELEMENT.get(), ModItems.FIRE_ELEMENT.get(), ModItems.WATER_ELEMENT.get());
    }
}
