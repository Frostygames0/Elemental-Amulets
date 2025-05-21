package frostygames0.elementalamulets.data.model;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.client.item.ElementalCompositionTintSource;
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
        blockModels.createTrivialCube(ModBlocks.SIMPLE_STORAGE.get());
        blockModels.createTrivialCube(ModBlocks.SIMPLE_GENERATOR.get());
        blockModels.createTrivialCube(ModBlocks.TEST_BLOCK.get());
        generateFullBlockPipe(blockModels, ModBlocks.PRESSURIZER_PIPE.get());
    }

    private static void registerItemModels(ItemModelGenerators itemModels) {
        generateTintedFlatItem(itemModels, ModItems.ELEMENTUM_SHARD, new ElementalCompositionTintSource());
        generateFlatItem(itemModels, ModItems.RING_OF_ELEMENTAL_SENSE.get());
        generatePipeItem(itemModels, ModBlocks.ELEMENTAL_PIPE.get());
    }

    private static void generateFlatItem(ItemModelGenerators itemModels, Item item) {
        itemModels.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
    }

    private static void generateTintedFlatItem(ItemModelGenerators itemModelGenerators, Supplier<? extends Item> item, ItemTintSource... sources) {
        var model = itemModelGenerators.createFlatItemModel(item.get(), ModelTemplates.FLAT_ITEM);
        itemModelGenerators.itemModelOutput.accept(item.get(), ItemModelUtils.tintedModel(model, sources));
    }

    private static void generateFullBlockPipe(BlockModelGenerators blockModels, Block block)
    {
        var texture = TextureMapping.getBlockTexture(block, "_top");
        var sideTexture = TextureMapping.getBlockTexture(block, "_side");

        TextureMapping texturemapping = new TextureMapping()
                .put(TextureSlot.TOP, texture)
                .put(TextureSlot.BOTTOM, texture)
                .put(TextureSlot.SIDE, sideTexture)
                .put(TextureSlot.PARTICLE, sideTexture);

        var model = ModelTemplates.CUBE_BOTTOM_TOP.create(block, texturemapping, blockModels.modelOutput);
        blockModels.blockStateOutput.accept(
                MultiVariantGenerator
                        .multiVariant(block, Variant.variant().with(VariantProperties.MODEL, model))
                        .with(createPipeFacingDispatch())
        );
    }

    private static PropertyDispatch createPipeFacingDispatch()
    {
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
    }

    private static void generatePipeItem(ItemModelGenerators itemModels, Block block) {
        var model = createPipeItemModel(itemModels, block);
        itemModels.itemModelOutput.accept(block.asItem(), ItemModelUtils.plainModel(model));
    }

    private static ResourceLocation createPipeConnectionsModel(BlockModelGenerators blockModels, Block block) {
        return Templates.createPipeConnections(block).create(block, new TextureMapping(), blockModels.modelOutput);
    }

    private static ResourceLocation createPipeCenterModel(BlockModelGenerators blockModels, Block block) {
        return Templates.PIPE_CENTER.createWithSuffix(block, "_center", new TextureMapping().put(TextureSlot.TEXTURE, ModelLocationUtils.getModelLocation(block)), blockModels.modelOutput);
    }

    private static ResourceLocation createPipeItemModel(ItemModelGenerators itemModels, Block block) {
        return Templates.PIPE_ITEM.create(block.asItem(), new TextureMapping().put(TextureSlot.TEXTURE, ModelLocationUtils.getModelLocation(block)), itemModels.modelOutput);
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
