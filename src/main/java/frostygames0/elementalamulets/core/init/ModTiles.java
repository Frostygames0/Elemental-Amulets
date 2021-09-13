package frostygames0.elementalamulets.core.init;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.blocks.tiles.ElementalCombinatorTile;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModTiles {
    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, ElementalAmulets.MOD_ID);

    public static final RegistryObject<TileEntityType<ElementalCombinatorTile>> ELEMENTAL_COMBINATOR_TILE = TILES.register("elemental_combinator",
            () -> TileEntityType.Builder.of(ElementalCombinatorTile::new, ModBlocks.ELEMENTAL_COMBINATOR.get()).build(null));
}
