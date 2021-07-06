package frostygames0.elementalamulets.recipes.ingredient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import frostygames0.elementalamulets.core.util.NBTUtil;
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

    public AmuletIngredient(ItemStack stack) {
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

    public ItemStack getMatchingStack() {
        return this.stack;
    }

    private boolean compareAmuletTiers(ItemStack stack, ItemStack other) {
        return NBTUtil.getInteger(stack, AmuletItem.TIER_TAG) == NBTUtil.getInteger(other, AmuletItem.TIER_TAG);
    }

    @Override
    public JsonElement serialize() {
        JsonObject json = new JsonObject();
        json.addProperty("item", stack.getItem().getRegistryName().toString());
        if(stack.getItem() instanceof AmuletItem) {
            AmuletItem item = (AmuletItem) stack.getItem();
            if(item.hasTier()) {
                json.addProperty("tier", item.getTier(stack));
            }
        }
        return json;
    }

    private static ItemStack getAmuletFromJson(JsonObject json) {
        String itemName = JSONUtils.getString(json, "item");

        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));
        if (item == null) throw new JsonSyntaxException("Item: "+itemName+" does not exist!");
        if (item instanceof AmuletItem) {
            int tier = JSONUtils.getInt(json, "tier", 1);
            if (tier > 4 || tier < 0) {
                throw new JsonSyntaxException("Incorrect Tier! Can't be higher than 4 and lower than 0! Your tier is "+tier);
            }
            return AmuletItem.getStackWithTier(new ItemStack(item), tier);
        }
        return new ItemStack(item, 1);
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return Serializer.INSTANCE;
    }

    public static class Serializer implements IIngredientSerializer<AmuletIngredient> {
        public static final Serializer INSTANCE = new Serializer();

        @Override
        public AmuletIngredient parse(PacketBuffer buffer) {
                return new AmuletIngredient(buffer.readItemStack());
            }
            @Override
            public AmuletIngredient parse(JsonObject json) {
                return new AmuletIngredient(getAmuletFromJson(json));
            }
            @Override
            public void write(PacketBuffer buffer, AmuletIngredient ingredient) {
                buffer.writeItemStack(ingredient.stack);
            }
        }
    }

