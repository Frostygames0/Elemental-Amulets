package frostygames0.elementalamulets.datagen;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.core.init.ModBlocks;
import frostygames0.elementalamulets.core.init.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ItemModelProvider extends net.minecraftforge.client.model.generators.ItemModelProvider {
    public ItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, ElementalAmulets.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        singleTexture(ModItems.GUIDE_BOOK.get().getRegistryName().getPath(), new ResourceLocation("item/generated"), "layer0", new ResourceLocation(ElementalAmulets.MOD_ID, "item/guide_book"));
    }
}
