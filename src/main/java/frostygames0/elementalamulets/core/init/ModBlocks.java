package frostygames0.elementalamulets.core.init;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.blocks.ElementalCombinator;
import frostygames0.elementalamulets.blocks.ElementalFocus;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = ElementalAmulets.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ElementalAmulets.MOD_ID);

    public static final RegistryObject<Block> ELEMENTAL_COMBINATOR = BLOCKS.register("elemental_combinator",
            () -> new ElementalCombinator(AbstractBlock.Properties.of(Material.STONE, DyeColor.RED).strength(3.5f).sound(SoundType.STONE).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(0)
                    .lightLevel(state -> state.getValue(ElementalCombinator.COMBINING) ? 15 : 5).isRedstoneConductor(ModBlocks::never)));

    public static final RegistryObject<Block> CELESTIAL_FOCUS = BLOCKS.register("celestial_focus",
            () -> new ElementalFocus(AbstractBlock.Properties.of(Material.WOOD).strength(2f).sound(SoundType.WOOD).requiresCorrectToolForDrops().harvestTool(ToolType.AXE).harvestLevel(0)
            .noOcclusion().isViewBlocking(ModBlocks::never).isSuffocating(ModBlocks::never)));

    public static final RegistryObject<Block> ELEMENTAL_STONE = BLOCKS.register("elemental_ore",
            () -> new Block(AbstractBlock.Properties.of(Material.STONE).strength(3.5f).sound(SoundType.STONE).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(1)));

    private static boolean never(BlockState blockState, IBlockReader iBlockReader, BlockPos blockPos) {
        return false;
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void oreGeneration(BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder gen = event.getGeneration();
        if(!event.getCategory().equals(Biome.Category.NETHER) && !event.getCategory().equals(Biome.Category.THEEND)) {
            gen.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, ModFeatures.ELEMENTAL_ORE);
        }
    }
}
