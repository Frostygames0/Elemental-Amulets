/*
 *     Copyright (c) 2021
 *
 *     This file is part of Elemental Amulets, a Minecraft Mod.
 *
 *     Elemental Amulets is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Elemental Amulets is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Elemental Amulets.  If not, see <https://www.gnu.org/licenses/>.
 */

package frostygames0.elementalamulets.init;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.blocks.CelestialFocus;
import frostygames0.elementalamulets.blocks.ElementalCombinator;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ElementalAmulets.MOD_ID);

    public static final RegistryObject<Block> ELEMENTAL_COMBINATOR = BLOCKS.register("elemental_combinator",
            () -> new ElementalCombinator(AbstractBlock.Properties.of(Material.STONE, DyeColor.RED).strength(3.5f).sound(SoundType.STONE).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(0)
                    .lightLevel(state -> state.getValue(ElementalCombinator.COMBINING) ? 15 : 5).isRedstoneConductor(ModBlocks::never)));

    public static final RegistryObject<Block> CELESTIAL_FOCUS = BLOCKS.register("celestial_focus",
            () -> new CelestialFocus(AbstractBlock.Properties.of(Material.WOOD, DyeColor.BROWN).strength(2f).sound(SoundType.WOOD).harvestTool(ToolType.AXE)
                    .noOcclusion().isViewBlocking(ModBlocks::never).isSuffocating(ModBlocks::never)));

    public static final RegistryObject<Block> ELEMENTAL_ORE = BLOCKS.register("elemental_ore",
            () -> new Block(AbstractBlock.Properties.of(Material.STONE).strength(3.5f).sound(SoundType.STONE).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(1)));


    // Shards
    public static final RegistryObject<Block> ELEMENTAL_SHARDS_BLOCK = BLOCKS.register("elemental_shards_block",
            () -> createShardsBlock(MaterialColor.COLOR_CYAN));
    public static final RegistryObject<Block> FIRE_SHARDS_BLOCK = BLOCKS.register("fire_shards_block",
            () -> createShardsBlock(MaterialColor.COLOR_CYAN));
    public static final RegistryObject<Block> WATER_SHARDS_BLOCK = BLOCKS.register("water_shards_block",
            () -> createShardsBlock(MaterialColor.WATER));
    public static final RegistryObject<Block> EARTH_SHARDS_BLOCK = BLOCKS.register("earth_shards_block",
            () -> createShardsBlock(MaterialColor.DIRT));
    public static final RegistryObject<Block> AIR_SHARDS_BLOCK = BLOCKS.register("air_shards_block",
            () -> createShardsBlock(MaterialColor.SNOW));

    private static Block createShardsBlock(MaterialColor color) {
        return new Block(AbstractBlock.Properties.of(Material.GLASS, color).strength(0.8f).sound(SoundType.GLASS).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE));
    }

    private static boolean never(BlockState blockState, IBlockReader iBlockReader, BlockPos blockPos) {
        return false;
    }
}
