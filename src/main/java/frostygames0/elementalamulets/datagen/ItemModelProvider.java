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
import frostygames0.elementalamulets.init.ModItems;
import frostygames0.elementalamulets.items.amulets.AmuletItem;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ItemModelProvider extends net.minecraftforge.client.model.generators.ItemModelProvider {
    public ItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, ElementalAmulets.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (RegistryObject<Item> reg : ModItems.ITEMS.getEntries()) {
            Item item = reg.get();
            if (item instanceof AmuletItem) {
                amuletTexture((AmuletItem) item);
            } else {
                if (item instanceof BlockItem)
                    continue; // BlockItems should be ignored since their models are generated by BlockStateProvider
                singleTexture(name(item), new ResourceLocation("item/generated"), "layer0", modLoc("item/" + name(item)));
            }
        }

    }

    private void amuletTexture(AmuletItem item) {
        if (item.hasTier()) {
            withExistingParent(name(item), "item/generated")
                    .texture("layer0", modLoc("item/" + name(item))).texture("layer1", modLoc("item/amulet_tiers/no_tier"))
                    .override().predicate(modLoc("tier"), 1).model(withExistingParent("item/subtypes/" + addTierSuffix(item, 1), modLoc("item/" + name(item))).texture("layer1", modLoc("item/amulet_tiers/level" + 1))).end()
                    .override().predicate(modLoc("tier"), 2).model(withExistingParent("item/subtypes/" + addTierSuffix(item, 2), modLoc("item/" + name(item))).texture("layer1", modLoc("item/amulet_tiers/level" + 2))).end()
                    .override().predicate(modLoc("tier"), 3).model(withExistingParent("item/subtypes/" + addTierSuffix(item, 3), modLoc("item/" + name(item))).texture("layer1", modLoc("item/amulet_tiers/level" + 3))).end()
                    .override().predicate(modLoc("tier"), 4).model(withExistingParent("item/subtypes/" + addTierSuffix(item, 4), modLoc("item/" + name(item))).texture("layer1", modLoc("item/amulet_tiers/level" + 4))).end();
        } else {
            singleTexture(name(item), mcLoc("item/generated"), "layer0", modLoc("item/" + name(item)));
        }
    }

    private String name(Item item) {
        return item.getRegistryName().getPath();
    }

    private String addTierSuffix(Item item, int tier) {
        String s = name(item);
        return s + "_tier" + tier;
    }
}
