package frostygames0.elementalamulets.recipes.ingredient;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import frostygames0.elementalamulets.items.amulets.AmuletItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.stream.Stream;

public class AmuletIngredient extends Ingredient {
    private final ItemStack stack;

    protected AmuletIngredient(ItemStack stack) {
        super(Stream.of(new Ingredient.SingleItemList(stack)));
        this.stack = stack;
    }

    @Override
    public boolean test(@Nullable ItemStack test) {
        if (test == null) return false;
        if(test.getItem() instanceof AmuletItem) {
            return this.stack.getItem() == test.getItem() && compareAmuletTiers(stack, test);
        }
        return this.stack.getItem() == test.getItem();
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return Serializer.INSTANCE;
    }

    private boolean compareAmuletTiers(ItemStack stack, ItemStack other) {
        if (stack.hasTag() && stack.getTag().contains(AmuletItem.TIER_TAG) && other.hasTag() && other.getTag().contains(AmuletItem.TIER_TAG)) {
            int tier1 = stack.getTag().getInt(AmuletItem.TIER_TAG);
            int tier2 = other.getTag().getInt(AmuletItem.TIER_TAG);
            return tier1 == tier2;
        }
        return false;
    }

    private static ItemStack getAmuletFromJson(JsonObject json) {
        String itemName = JSONUtils.getString(json, "item");

        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));
        if (item == null) throw new JsonSyntaxException("Unknown item '" + itemName + "'");
        if (item instanceof AmuletItem) {
            int tier = JSONUtils.getInt(json, "tier", 1);
            if (tier > 4 || tier < 0) {
                throw new JsonSyntaxException("Incorrect Tier! Can't be higher than 4 and lower than 0! Your tier is "+tier);
            }
            return AmuletItem.getStackWithTier(new ItemStack(item), tier);
        }
        return new ItemStack(item, 1);
    }

        public static class Serializer implements IIngredientSerializer<AmuletIngredient> {
            public static final Serializer INSTANCE = new Serializer();

            // Use this to read it from buffer;
            @Override
            public AmuletIngredient parse(PacketBuffer buffer) {
                return new AmuletIngredient(buffer.readItemStack());
            }

            // Use this to read it from json;
            @Override
            public AmuletIngredient parse(JsonObject json) {
                return new AmuletIngredient(getAmuletFromJson(json));
            }

            // Use this to write to buffer
            @Override
            public void write(PacketBuffer buffer, AmuletIngredient ingredient) {
                buffer.writeItemStack(ingredient.stack);
            }
        }
    }

