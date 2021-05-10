package frostygames0.elementalamulets.core.init;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.recipes.ElementalCombination;
import frostygames0.elementalamulets.recipes.ElementalCombinationSerializer;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipes {

    public static final IRecipeType<ElementalCombination> ELEMENTAL_SEPARATION_RECIPE = IRecipeType.register(ElementalAmulets.MOD_ID+":elemental_combination");

    public static final DeferredRegister<IRecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ElementalAmulets.MOD_ID);

    public static final RegistryObject<IRecipeSerializer<ElementalCombination>> ELEMENTAL_SEPARATION = SERIALIZERS.register("elemental_combination",
            () -> new ElementalCombinationSerializer<>(ElementalCombination::new));
}
