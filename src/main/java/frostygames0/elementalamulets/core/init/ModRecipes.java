package frostygames0.elementalamulets.core.init;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.recipes.ElementalCombination;
import frostygames0.elementalamulets.recipes.ElementalCombinationSerializer;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class ModRecipes {

    public static final IRecipeType<ElementalCombination> ELEMENTAL_COMBINATION_TYPE = IRecipeType.register(ElementalAmulets.MOD_ID+":elemental_combination");

    public static final DeferredRegister<IRecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ElementalAmulets.MOD_ID);

    public static final RegistryObject<IRecipeSerializer<ElementalCombination>> ELEMENTAL_COMBINATION = SERIALIZERS.register("elemental_combination",
            ElementalCombinationSerializer::new);

    public static List<ElementalCombination> getRecipes(World world) {
        return world.getRecipeManager().getRecipesForType(ELEMENTAL_COMBINATION_TYPE);
    }
}
