package frostygames0.elementalamulets.core.init;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.blocks.ElementalCrafter;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ElementalAmulets.MOD_ID);

    public static final RegistryObject<Block> ELEMENTAL_CRAFTER = BLOCKS.register("elemental_crafter",
            () -> new ElementalCrafter(AbstractBlock.Properties.create(Material.IRON)
                    .hardnessAndResistance(10f, 10f)
                    .sound(SoundType.METAL)
                    .harvestLevel(2)));
}
