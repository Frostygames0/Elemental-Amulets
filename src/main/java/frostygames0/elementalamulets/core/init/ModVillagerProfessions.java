package frostygames0.elementalamulets.core.init;

import com.google.common.collect.ImmutableSet;
import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.items.interfaces.IAmuletItem;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.SoundEvents;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.common.BasicTrade;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class ModVillagerProfessions {
    public static final DeferredRegister<VillagerProfession> PROFESSIONS = DeferredRegister.create(ForgeRegistries.PROFESSIONS, ElementalAmulets.MOD_ID);
    public static final DeferredRegister<PointOfInterestType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, ElementalAmulets.MOD_ID);

    public static final RegistryObject<PointOfInterestType> JEWELLER_TABLE = POI_TYPES.register("jeweller_table", () ->
            new PointOfInterestType("jeweller_table", PointOfInterestType.getAllStates(ModBlocks.ELEMENTAL_CRAFTER.get()), 1, 10));
    public static final RegistryObject<VillagerProfession> JEWELLER = PROFESSIONS.register("jeweller", () ->
            new VillagerProfession("jeweller", JEWELLER_TABLE.get(), ImmutableSet.of(), ImmutableSet.of(), SoundEvents.BLOCK_RESPAWN_ANCHOR_DEPLETE));

    public static void villagerTradesEvent(VillagerTradesEvent event) {
        List<VillagerTrades.ITrade> trades = event.getTrades().get(1);
        if(event.getType() == JEWELLER.get()) {
            ModItems.ITEMS.getEntries().stream().map(RegistryObject::get).filter(item -> item instanceof IAmuletItem).forEach(amulet -> {
                trades.add(new BasicTrade(new ItemStack(Items.DIAMOND, 15), new ItemStack(Items.BAKED_POTATO, 5), new ItemStack(amulet), 1, 1, 1.5f));
            });
        }
    }
}
