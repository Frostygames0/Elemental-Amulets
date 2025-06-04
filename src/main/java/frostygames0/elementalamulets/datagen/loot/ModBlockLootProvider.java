package frostygames0.elementalamulets.datagen.loot;

import frostygames0.elementalamulets.initialization.ModBlocks;
import frostygames0.elementalamulets.initialization.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Set;
import java.util.stream.Collectors;

public class ModBlockLootProvider extends BlockLootSubProvider {
    protected ModBlockLootProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, registries);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.PRIMITIVE_ELEMENTAL_EXTRACTOR.get());
        add(ModBlocks.ELEMENTUM_CRYSTAL_ORE.get(), builder -> createElementumCrystalOreDrops(ModBlocks.ELEMENTUM_CRYSTAL_ORE.get()));
        add(ModBlocks.ELEMENTUM_CRYSTAL_DEEPSLATE_ORE.get(), builder -> createElementumCrystalOreDrops(ModBlocks.ELEMENTUM_CRYSTAL_DEEPSLATE_ORE.get()));
        dropSelf(ModBlocks.ELEMENTAL_PIPE.get());
        dropSelf(ModBlocks.PRESSURIZER_PIPE.get());

        dropSelf(ModBlocks.SIMPLE_STORAGE.get());
    }

    protected LootTable.Builder createElementumCrystalOreDrops(Block block) {
        var registrylookup = registries.lookupOrThrow(Registries.ENCHANTMENT);
        return createSilkTouchDispatchTable(
                block,
                applyExplosionDecay(
                        block,
                        LootItem.lootTableItem(ModItems.ELEMENTUM_SHARD)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 6.0F)))
                                .apply(ApplyBonusCount.addOreBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE)))
                )
        );
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries()
                .stream()
                .map(DeferredHolder::value)
                .collect(Collectors.toUnmodifiableList());
    }
}
