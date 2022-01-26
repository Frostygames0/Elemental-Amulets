/*
 *  Copyright (c) 2021
 *
 *     This file is part of Elemental Amulets, a Minecraft Mod.
 *
 *     Elemental Amulets is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Elemental Amulets is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with Elemental Amulets.  If not, see <https://www.gnu.org/licenses/>.
 */

package frostygames0.elementalamulets.init;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.blocks.CelestialFocus;
import frostygames0.elementalamulets.blocks.ElementalCombinator;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.OreBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ElementalAmulets.MOD_ID);

    public static final RegistryObject<ElementalCombinator> ELEMENTAL_COMBINATOR = BLOCKS.register("elemental_combinator",
            () -> new ElementalCombinator(BlockBehaviour.Properties.of(Material.STONE, DyeColor.RED).strength(3.5f).sound(SoundType.STONE).requiresCorrectToolForDrops()
                    .lightLevel(state -> state.getValue(ElementalCombinator.COMBINING) ? 15 : 5).isRedstoneConductor(ModBlocks::never)));

    public static final RegistryObject<CelestialFocus> CELESTIAL_FOCUS = BLOCKS.register("celestial_focus",
            () -> new CelestialFocus(BlockBehaviour.Properties.of(Material.WOOD, DyeColor.BROWN).strength(2f).sound(SoundType.WOOD)
                    .noOcclusion().isViewBlocking(ModBlocks::never).isSuffocating(ModBlocks::never)));

    public static final RegistryObject<OreBlock> ELEMENTAL_ORE = BLOCKS.register("elemental_ore",
            () -> new OreBlock(BlockBehaviour.Properties.of(Material.STONE).strength(3.5f).sound(SoundType.STONE).requiresCorrectToolForDrops(), UniformInt.of(0, 2)));
    public static final RegistryObject<OreBlock> DEEPSLATE_ELEMENTAL_ORE = BLOCKS.register("deepslate_elemental_ore",
            () -> new OreBlock(BlockBehaviour.Properties.of(Material.STONE).strength(4.5f).sound(SoundType.DEEPSLATE).requiresCorrectToolForDrops(), UniformInt.of(1, 3)));


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
        return new Block(BlockBehaviour.Properties.of(Material.GLASS, color).strength(0.8f).sound(SoundType.GLASS).requiresCorrectToolForDrops());
    }

    private static boolean never(BlockState blockState, BlockGetter iBlockReader, BlockPos blockPos) {
        return false;
    }
}
