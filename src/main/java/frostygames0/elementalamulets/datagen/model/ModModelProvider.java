package frostygames0.elementalamulets.datagen.model;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.block.SimpleStorageBlock;
import frostygames0.elementalamulets.client.color.item.ElementalCompositionTintSource;
import frostygames0.elementalamulets.initialization.ModBlocks;
import frostygames0.elementalamulets.initialization.ModItems;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.*;
import net.minecraft.client.data.models.model.*;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ModModelProvider extends ModelProvider {
    public ModModelProvider(PackOutput output) {
        super(output, ElementalAmulets.MOD_ID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        registerBlockModels(blockModels);
        registerItemModels(itemModels);
    }

    private static void registerBlockModels(BlockModelGenerators blockModels) {
        generateElementalExtractor(blockModels, ModBlocks.PRIMITIVE_ELEMENTAL_EXTRACTOR.get());
        blockModels.createTrivialCube(ModBlocks.ELEMENTUM_CRYSTAL_ORE.get());
        blockModels.createTrivialCube(ModBlocks.ELEMENTUM_CRYSTAL_DEEPSLATE_ORE.get());
        generatePipe(blockModels, ModBlocks.ELEMENTAL_PIPE.get());
        generateFullBlockPipe(blockModels, ModBlocks.PRESSURIZER_PIPE.get());
        generateSimpleStorage(blockModels, ModBlocks.SIMPLE_STORAGE.get());
    }

    private static void registerItemModels(ItemModelGenerators itemModels) {
        generateTintedFlatItem(itemModels, ModItems.ELEMENTUM_SHARD, new ElementalCompositionTintSource());
        generateFlatItem(itemModels, ModItems.RING_OF_ELEMENTAL_SENSE.get());
    }

    private static void generateFlatItem(ItemModelGenerators itemModels, Item item) {
        itemModels.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
    }

    private static void generateTintedFlatItem(ItemModelGenerators itemModelGenerators, Supplier<? extends Item> item, ItemTintSource... sources) {
        var model = itemModelGenerators.createFlatItemModel(item.get(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.itemModelOutput.accept(item.get(), ItemModelUtils.tintedModel(model, sources));
    }

    private static void generateSimpleStorage(BlockModelGenerators blockModelGenerators, Block block) {
        blockModelGenerators.blockStateOutput.accept(MultiVariantGenerator.multiVariant(block)
                .with(PropertyDispatch.property(SimpleStorageBlock.FILL).generate(fillValue ->
                        Variant.variant().with(VariantProperties.MODEL, blockModelGenerators.createSuffixedVariant(block, fillValue == 0 ? "" : "_side" + fillValue, ModelTemplates.CUBE_COLUMN,
                                suffixedTexture -> TextureMapping.column(TextureMapping.getBlockTexture(block, "_side" + fillValue), TextureMapping.getBlockTexture(block)))))));
    }

    private static void generateFullBlockPipe(BlockModelGenerators blockModels, Block block) {
        var modelTemplate = ModelTemplates.CUBE_BOTTOM_TOP;

        var texture = TextureMapping.getBlockTexture(block, "_top");
        var sideTexture = TextureMapping.getBlockTexture(block, "_side");

        TextureMapping texturemapping = new TextureMapping()
                .put(TextureSlot.TOP, texture)
                .put(TextureSlot.BOTTOM, texture)
                .put(TextureSlot.SIDE, sideTexture)
                .put(TextureSlot.PARTICLE, sideTexture);

        var enabledModel = modelTemplate.create(block, texturemapping, blockModels.modelOutput);

        var disabledSideTexture = TextureMapping.getBlockTexture(block, "_side_off");
        var disabledModel = modelTemplate.createWithSuffix(block, "_off", texturemapping.copyAndUpdate(TextureSlot.SIDE, disabledSideTexture), blockModels.modelOutput);

        blockModels.blockStateOutput.accept(
                MultiVariantGenerator
                        .multiVariant(block)
                        .with(BlockModelGenerators.createBooleanModelDispatch(BlockStateProperties.ENABLED, enabledModel, disabledModel))
                        .with(createPipeFacingDispatch())
        );
    }

    private static PropertyDispatch createPipeFacingDispatch() {
        return PropertyDispatch.property(BlockStateProperties.FACING)
                .select(Direction.UP, Variant.variant())
                .select(Direction.DOWN, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R180))
                .select(Direction.NORTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90))
                .select(Direction.SOUTH, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R180))
                .select(Direction.WEST, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R270))
                .select(Direction.EAST, Variant.variant().with(VariantProperties.X_ROT, VariantProperties.Rotation.R90).with(VariantProperties.Y_ROT, VariantProperties.Rotation.R90));
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

    private static void generatePipe(BlockModelGenerators blockModels, Block block) {
        var connectionsModel = createPipeConnectionsModel(blockModels, block);
        var centerModel = createPipeCenterModel(blockModels, block);

        blockModels.blockStateOutput.accept(
                MultiPartGenerator.multiPart(block)
                        .with(Variant.variant().with(VariantProperties.MODEL, connectionsModel))
                        .with(Variant.variant().with(VariantProperties.MODEL, centerModel)));

        blockModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(createPipeItemModel(blockModels.modelOutput, block)));
    }

    private static ResourceLocation createPipeConnectionsModel(BlockModelGenerators blockModels, Block block) {
        return Templates.createPipeConnections(block).create(block, new TextureMapping(), blockModels.modelOutput);
    }

    private static ResourceLocation createPipeCenterModel(BlockModelGenerators blockModels, Block block) {
        return Templates.PIPE_CENTER.createWithSuffix(block, "_center", new TextureMapping().put(TextureSlot.TEXTURE, ModelLocationUtils.getModelLocation(block)), blockModels.modelOutput);
    }

    private static ResourceLocation createPipeItemModel(BiConsumer<ResourceLocation, ModelInstance> modelOutput, Block block) {
        return Templates.PIPE_ITEM.create(block.asItem(), new TextureMapping().put(TextureSlot.TEXTURE, ModelLocationUtils.getModelLocation(block)), modelOutput);
    }

    private static class Templates {
        private static final ModelTemplate PIPE_RIM = createSimplePipePart("rim");
        private static final ModelTemplate PIPE_CONNECTOR = createSimplePipePart("connector");
        private static final ModelTemplate PIPE_CONNECTOR_SHORT = createSimplePipePart("connector_short");
        private static final ModelTemplate PIPE_CENTER = createSimplePipePart("center");

        private static final ModelTemplate PIPE_ITEM = createSimplePipeItem();

        private static ModelTemplate createPipeConnections(Block block) {
            var textures = new TextureMapping()
                    .put(TextureSlot.TEXTURE, ModelLocationUtils.getModelLocation(block));

            return ExtendedModelTemplateBuilder.builder()
                    .customLoader(PipeConnectionsModelBuilder::new, loader -> loader
                            .withRimModel(Templates.PIPE_RIM, textures)
                            .withConnectorModel(Templates.PIPE_CONNECTOR, textures)
                            .withShortConnectorModel(Templates.PIPE_CONNECTOR_SHORT, textures)
                    ).suffix("_connections").build();
        }

        private static ModelTemplate createSimplePipePart(String name) {
            return new ModelTemplate(Optional.of(ElementalAmulets.id(name).withPrefix("block/pipe/connections_templates/")), Optional.empty(), TextureSlot.TEXTURE, TextureSlot.PARTICLE);
        }

        private static ModelTemplate createSimplePipeItem() {
            return new ModelTemplate(Optional.of(ElementalAmulets.id("pipe_template").withPrefix("item/")), Optional.empty(), TextureSlot.TEXTURE, TextureSlot.PARTICLE);
        }
    }
}
