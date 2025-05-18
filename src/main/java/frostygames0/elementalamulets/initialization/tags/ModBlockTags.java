package frostygames0.elementalamulets.initialization.tags;

import frostygames0.elementalamulets.ElementalAmulets;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModBlockTags {
    public static final TagKey<Block> ELEMENTUM_CRYSTAL_ORES = createTagKey("elementum_crystal_ores");

    private static TagKey<Block> createTagKey(String name) {
        return TagKey.create(Registries.BLOCK, ElementalAmulets.id(name));
    }
}
