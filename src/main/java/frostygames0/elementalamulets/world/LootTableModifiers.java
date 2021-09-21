package frostygames0.elementalamulets.world;

import com.google.gson.JsonObject;
import frostygames0.elementalamulets.ElementalAmulets;
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
        event.getRegistry().register(new DesertLoot.Serializer().setRegistryName(modPrefix("desert_pyramid_loot")));
    }

    public static class DesertLoot extends LootModifier {
        public DesertLoot(ILootCondition[] conditionsIn) {
            super(conditionsIn);
        }

        @Nonnull
        @Override
        protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
            List<AmuletItem> AMULETS = ModItems.getAmulets();
            Random rand = new Random();
            if (LootTables.DESERT_PYRAMID.equals(context.getQueriedLootTableId())) {
                generatedLoot.add(AmuletItem.getStackWithTier(new ItemStack(AMULETS.get(rand.nextInt(AMULETS.size()))), 2));
                ElementalAmulets.LOGGER.debug("Successfully applied modifier to a loot table!");
            }
            return generatedLoot;
        }

        public static class Serializer extends GlobalLootModifierSerializer<DesertLoot> {

            @Override
            public DesertLoot read(ResourceLocation location, JsonObject object, ILootCondition[] ailootcondition) {
                return new DesertLoot(ailootcondition);
            }

            @Override
            public JsonObject write(DesertLoot instance) {
                return makeConditions(instance.conditions);
            }
        }
    }
}
