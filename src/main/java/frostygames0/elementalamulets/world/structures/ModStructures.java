/*
 *  Copyright (c) 2021-2022
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

package frostygames0.elementalamulets.world.structures;

import com.google.common.collect.ImmutableMap;
import frostygames0.elementalamulets.ElementalAmulets;
import net.minecraft.util.datafix.fixes.StructuresBecomeConfiguredFix;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModStructures {
    public static final DeferredRegister<StructureFeature<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, ElementalAmulets.MOD_ID);

    public static final RegistryObject<StructureFeature<JigsawConfiguration>> CULT_TEMPLE = STRUCTURES.register("cult_temple", CultTempleStructure::new);

    /*
     * TEMPORARY #8505 FIX, IF FORGE DOESN'T FIX IT BEFORE RELEASE
     * I'm changing data fixer's conversion map to include my structure,
     * as absence of structure here causes exception and chunk regeneration
     */
    public static void fixStructure() {
        String id = ModStructures.CULT_TEMPLE.getId().toString();

        StructuresBecomeConfiguredFix.CONVERSION_MAP = ImmutableMap.<String, StructuresBecomeConfiguredFix.Conversion>builder()
                .putAll(StructuresBecomeConfiguredFix.CONVERSION_MAP)
                .put(id, StructuresBecomeConfiguredFix.Conversion.trivial(id))
                .build();
    }
}
