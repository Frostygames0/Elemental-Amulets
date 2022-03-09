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

import com.google.common.collect.ImmutableList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.world.StructureSpawnListGatherEvent;


import java.util.List;
import java.util.Optional;

import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

public class CultTempleStructure extends StructureFeature<JigsawConfiguration> {

    public static final Lazy<List<MobSpawnSettings.SpawnerData>> MONSTERS = Lazy.of(() -> ImmutableList.of(new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 100, 1, 2)));
    public static final Lazy<List<MobSpawnSettings.SpawnerData>> CREATURES = Lazy.of(() -> ImmutableList.of(new MobSpawnSettings.SpawnerData(EntityType.BAT, 100, 1, 4)));

    public CultTempleStructure() {
        super(JigsawConfiguration.CODEC, CultTempleStructure::createPiecesGenerator);
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }


    private static boolean isFeatureChunk(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        BlockPos centerOfChunk = context.chunkPos().getWorldPosition();

        ChunkGenerator chunkGen = context.chunkGenerator();
        int landHeight = chunkGen.getFirstOccupiedHeight(centerOfChunk.getX(), centerOfChunk.getZ(), Heightmap.Types.WORLD_SURFACE_WG, context.heightAccessor());

        NoiseColumn columnOfBlocks = chunkGen.getBaseColumn(centerOfChunk.getX(), centerOfChunk.getZ(), context.heightAccessor());
        BlockState topBlock = columnOfBlocks.getBlock(landHeight);

        return topBlock.getFluidState().isEmpty() && landHeight <= 65; // This should prevent spawning it on water
    }

    public static void addMobsToSpawn(StructureSpawnListGatherEvent event) {
        if (event.getStructure() == ModStructures.CULT_TEMPLE.get()) {
            event.addEntitySpawns(MobCategory.MONSTER, MONSTERS.get());
            event.addEntitySpawns(MobCategory.AMBIENT, CREATURES.get());
        }
    }

    public static Optional<PieceGenerator<JigsawConfiguration>> createPiecesGenerator(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        if (!isFeatureChunk(context)) return Optional.empty();

        JigsawConfiguration config = new JigsawConfiguration(() -> context.registryAccess().ownedRegistryOrThrow(Registry.TEMPLATE_POOL_REGISTRY).get(modPrefix("cult_temple/entrance")), 12);

        PieceGeneratorSupplier.Context<JigsawConfiguration> newContext = new PieceGeneratorSupplier.Context<>(context.chunkGenerator(), context.biomeSource(), context.seed(), context.chunkPos(), config, context.heightAccessor(), context.validBiome(), context.structureManager(), context.registryAccess());

        BlockPos blockpos = context.chunkPos().getMiddleBlockPosition(0);

        return JigsawPlacement.addPieces(
                newContext,
                PoolElementStructurePiece::new,
                blockpos,
                false,
                true
        );

    }
}
