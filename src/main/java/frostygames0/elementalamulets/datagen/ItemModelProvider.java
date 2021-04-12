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
        singleTexture(ModItems.FIRE_AMULET.get().getRegistryName().getPath(), new ResourceLocation("item/generated"), "layer0", new ResourceLocation(ElementalAmulets.MOD_ID, "item/fire_amulet"));
        singleTexture(ModItems.JUMP_AMULET.get().getRegistryName().getPath(), new ResourceLocation("item/generated"), "layer0", new ResourceLocation(ElementalAmulets.MOD_ID, "item/jump_amulet"));
        singleTexture(ModItems.SPEED_AMULET.get().getRegistryName().getPath(), new ResourceLocation("item/generated"), "layer0", new ResourceLocation(ElementalAmulets.MOD_ID, "item/water_amulet"));
        singleTexture(ModItems.INVISIBILITY_AMULET.get().getRegistryName().getPath(), new ResourceLocation("item/generated"), "layer0", new ResourceLocation(ElementalAmulets.MOD_ID, "item/invisibility_amulet"));
        withExistingParent(ModBlocks.ELEMENTAL_CRAFTER.get().getRegistryName().getPath(), new ResourceLocation(ElementalAmulets.MOD_ID, "block/elemental_crafter"));
    }
}
