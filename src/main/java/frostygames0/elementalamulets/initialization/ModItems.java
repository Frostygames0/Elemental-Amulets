package frostygames0.elementalamulets.initialization;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.element.ElementalComposition;
import frostygames0.elementalamulets.item.ElementalSenseRingItem;
import frostygames0.elementalamulets.item.ElementumShardItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    private ModItems() {
    }

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ElementalAmulets.MOD_ID);

    public static final DeferredItem<ElementumShardItem> ELEMENTUM_SHARD =
            ITEMS.registerItem("elementum_shard",
                    (props) -> new ElementumShardItem(props.component(ModDataComponents.ELEMENTAL_COMPOSITION, ElementalComposition.EMPTY)));
    public static final DeferredItem<ElementalSenseRingItem> RING_OF_ELEMENTAL_SENSE =
            ITEMS.registerItem("elemental_sense_ring",
                    ElementalSenseRingItem::new,
                    new Item.Properties()
                            .stacksTo(1)
                            .rarity(Rarity.UNCOMMON));

    public static final DeferredItem<BlockItem> ELEMENTAL_EXTRACTOR =
            ITEMS.registerSimpleBlockItem(ModBlocks.PRIMITIVE_ELEMENTAL_EXTRACTOR);
    public static final DeferredItem<BlockItem> ELEMENTUM_CRYSTAL_ORE =
            ITEMS.registerSimpleBlockItem(ModBlocks.ELEMENTUM_CRYSTAL_ORE);
    public static final DeferredItem<BlockItem> ELEMENTUM_CRYSTAL_DEEPSLATE_ORE =
            ITEMS.registerSimpleBlockItem(ModBlocks.ELEMENTUM_CRYSTAL_DEEPSLATE_ORE);
    public static final DeferredItem<BlockItem> ELEMENTAL_PIPE =
            ITEMS.registerSimpleBlockItem(ModBlocks.ELEMENTAL_PIPE);
    public static final DeferredItem<BlockItem> EXTRACTOR_PIPE =
            ITEMS.registerSimpleBlockItem(ModBlocks.PRESSURIZER_PIPE);

    public static final DeferredItem<BlockItem> SIMPLE_STORAGE =
            ITEMS.registerSimpleBlockItem(ModBlocks.SIMPLE_STORAGE);

    public static final DeferredItem<BlockItem> NEW_PIPE = ITEMS.registerSimpleBlockItem(ModBlocks.NEW_PIPE);

    public static void register(IEventBus bus) {
        ITEMS.addAlias(ElementalAmulets.id("penis_smazhka"), ELEMENTUM_SHARD.getId());

        ITEMS.register(bus);
    }
}
