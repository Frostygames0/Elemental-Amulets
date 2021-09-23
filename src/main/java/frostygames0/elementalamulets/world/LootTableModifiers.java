package frostygames0.elementalamulets.world;

import com.google.gson.JsonObject;
import frostygames0.elementalamulets.core.init.ModItems;
import frostygames0.elementalamulets.items.amulets.AmuletItem;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.event.RegistryEvent;


import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

/**
 * @author Frostygames0
 * @date 21.09.2021 15:47
 */
public class LootTableModifiers{

    public static void registerLootModifierSerializer(RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        event.getRegistry().register(new TreasureLoot.Serializer().setRegistryName(modPrefix("desert_pyramid_loot")));
    }

    public static class TreasureLoot extends LootModifier {
        public TreasureLoot(ILootCondition[] conditionsIn) {
            super(conditionsIn);
        }

        @Nonnull
        @Override
        protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
            List<AmuletItem> AMULETS = ModItems.getAmulets();
            Random rand = new Random();
            if (LootTables.DESERT_PYRAMID.equals(context.getQueriedLootTableId())) {
                generatedLoot.add(AmuletItem.getStackWithTier(new ItemStack(AMULETS.get(rand.nextInt(AMULETS.size()))), 1));
            } else if(LootTables.BURIED_TREASURE.equals(context.getQueriedLootTableId())) {
                generatedLoot.add(new ItemStack(ModItems.AETHER_ELEMENT.get(), 2));
            } else if(LootTables.SHIPWRECK_TREASURE.equals(context.getQueriedLootTableId())) {
                generatedLoot.add(new ItemStack(ModItems.WATER_ELEMENT.get(), 5));
            }
            return generatedLoot;
        }

        public static class Serializer extends GlobalLootModifierSerializer<TreasureLoot> {

            @Override
            public TreasureLoot read(ResourceLocation location, JsonObject object, ILootCondition[] ailootcondition) {
                return new TreasureLoot(ailootcondition);
            }

            @Override
            public JsonObject write(TreasureLoot instance) {
                return makeConditions(instance.conditions);
            }
        }
    }
}
