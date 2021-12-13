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

package frostygames0.elementalamulets.datagen;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.blocks.ElementalCombinator;
import frostygames0.elementalamulets.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;


import java.util.function.Function;
import java.util.stream.Collectors;

public class BlockStateProvider extends net.minecraftforge.client.model.generators.BlockStateProvider {
    public BlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, ElementalAmulets.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockstateBlock(ModBlocks.ELEMENTAL_COMBINATOR.get(), state -> {
            if (state.getValue(ElementalCombinator.COMBINING)) {
                return models().cubeBottomTop(name(ModBlocks.ELEMENTAL_COMBINATOR.get()) + "_on",
                        modLoc("block/" + name(ModBlocks.ELEMENTAL_COMBINATOR.get()) + "_side"),
                        mcLoc("block/furnace_top"),
                        modLoc("block/" + name(ModBlocks.ELEMENTAL_COMBINATOR.get()) + "_up_on"));
            } else {
                return models().cubeBottomTop(name(ModBlocks.ELEMENTAL_COMBINATOR.get()),
                        modLoc("block/" + name(ModBlocks.ELEMENTAL_COMBINATOR.get()) + "_side"),
                        mcLoc("block/furnace_top"),
                        modLoc("block/" + name(ModBlocks.ELEMENTAL_COMBINATOR.get()) + "_up_off"));
            }
        });

        // Elemental Stone
        simpleBlock(ModBlocks.ELEMENTAL_ORE.get());
        simpleBlock(ModBlocks.CELESTIAL_FOCUS.get(), models().getExistingFile(modLoc("block/celestial_focus")));

        simpleBlock(ModBlocks.ELEMENTAL_SHARDS_BLOCK.get());
        simpleBlock(ModBlocks.WATER_SHARDS_BLOCK.get());
        simpleBlock(ModBlocks.EARTH_SHARDS_BLOCK.get());
        simpleBlock(ModBlocks.AIR_SHARDS_BLOCK.get());
        simpleBlock(ModBlocks.FIRE_SHARDS_BLOCK.get());

        for (Block block : ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList())) {
            simpleBlockItem(block, models().getExistingFile(modLoc("block/" + name(block))));
        }
    }

    private void blockstateBlock(Block block, Function<BlockState, ModelFile> func) {
        getVariantBuilder(block).forAllStates(state -> ConfiguredModel.builder().modelFile(func.apply(state)).build());
    }

    private String name(Block block) {
        return block.getRegistryName().getPath();
    }
}
