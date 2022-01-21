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
import frostygames0.elementalamulets.datagen.loottables.LootTableProvider;
import frostygames0.elementalamulets.datagen.recipes.RecipeProvider;
import frostygames0.elementalamulets.datagen.tags.BlockTagsProvider;
import frostygames0.elementalamulets.datagen.tags.ItemTagsProvider;
import frostygames0.elementalamulets.init.ModItems;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = ElementalAmulets.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        ModItems.lookupAmulets(); // Looking them up as common setup is not running while datagen is happening
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        if (event.includeClient()) {
            generator.addProvider(new ItemModelProvider(generator, helper));
            generator.addProvider(new BlockStateProvider(generator, helper));
        }
        if (event.includeServer()) {
            BlockTagsProvider blockTagsProvider = new BlockTagsProvider(generator, helper);
            generator.addProvider(blockTagsProvider);
            generator.addProvider(new ItemTagsProvider(generator, blockTagsProvider, helper));

            generator.addProvider(new RecipeProvider(generator));
            generator.addProvider(new LootTableProvider(generator));
        }
    }
}
