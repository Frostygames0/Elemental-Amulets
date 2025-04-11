package frostygames0.elementalamulets.registration;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.element.ElementalComposition;
import frostygames0.elementalamulets.item.ElementalSenseRing;
import frostygames0.elementalamulets.item.ElementalShardItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    private ModItems() {
    }

    public static DeferredRegister.Items ITEMS = DeferredRegister.createItems(ElementalAmulets.MOD_ID);

    public static DeferredItem<Item> ELEMENT_SHARD = ITEMS.registerItem("elemental_shard", (props) ->
            new ElementalShardItem(props.component(ModDataComponents.ELEMENTAL_COMPOSITION, ElementalComposition.EMPTY)));

    public static DeferredItem<Item> RING_OF_ELEMENTAL_SENSE = ITEMS.registerItem("elemental_sense_ring", ElementalSenseRing::new, new Item.Properties().stacksTo(1));

    public static DeferredItem<BlockItem> ELEMENTAL_EXTRACTOR = ITEMS.registerSimpleBlockItem(ModBlocks.PRIMITIVE_ELEMENTAL_EXTRACTOR);
}
