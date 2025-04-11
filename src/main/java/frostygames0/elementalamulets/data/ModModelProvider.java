package frostygames0.elementalamulets.data;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.client.item.ElementalCompositionTintSource;
import frostygames0.elementalamulets.registration.ModBlocks;
import frostygames0.elementalamulets.registration.ModItems;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.Variant;
import net.minecraft.client.data.models.blockstates.VariantProperties;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, ElementalAmulets.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        generateTintedFlatItem(itemModels, ModItems.ELEMENT_SHARD, new ElementalCompositionTintSource());
        generateElementalExtractor(blockModels, ModBlocks.PRIMITIVE_ELEMENTAL_EXTRACTOR.get());

        itemModels.generateFlatItem(ModItems.RING_OF_ELEMENTAL_SENSE.get(), ModelTemplates.FLAT_ITEM);
    }

    private static void generateTintedFlatItem(ItemModelGenerators itemModelGenerators, Supplier<Item> item, ItemTintSource... sources) {
        var model = itemModelGenerators.createFlatItemModel(item.get(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.itemModelOutput.accept(item.get(), ItemModelUtils.tintedModel(model, sources));
    }

    private static void generateElementalExtractor(BlockModelGenerators blockModels, Block block) {
        var mapping = new TextureMapping()
                .put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block))
                .put(TextureSlot.NORTH, TextureMapping.getBlockTexture(block, "_front"))
                .put(TextureSlot.EAST, TextureMapping.getBlockTexture(block, "_side"))
                .put(TextureSlot.WEST, TextureMapping.getBlockTexture(block, "_side"))
                .put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(block, "_back"))
                .put(TextureSlot.UP, TextureMapping.getBlockTexture(block))
                .put(TextureSlot.DOWN, TextureMapping.getBlockTexture(block, "_bottom"));

        var model = ModelTemplates.CUBE.create(block, mapping, blockModels.modelOutput);
        var multiVariant = MultiVariantGenerator.multiVariant(block,
                        Variant.variant().with(VariantProperties.MODEL, model))
                .with(BlockModelGenerators.createHorizontalFacingDispatch());

        blockModels.blockStateOutput.accept(multiVariant);
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return Stream.empty();
    }
}
