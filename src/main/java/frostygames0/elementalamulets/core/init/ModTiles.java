package frostygames0.elementalamulets.core.init;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.blocks.tiles.ElementalCrafterTile;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.ObjectUtils;

public class ModTiles {
    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, ElementalAmulets.MOD_ID);

    public static final RegistryObject<TileEntityType<ElementalCrafterTile>> ELEMENTAL_CRAFTER_TILE = TILES.register("elemental_crafter",
            () -> TileEntityType.Builder.create(ElementalCrafterTile::new, ModBlocks.ELEMENTAL_CRAFTER.get()).build(null));
}
