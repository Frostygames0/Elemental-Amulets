package frostygames0.elementalamulets.core.init;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.recipes.ElementalSeparation;
import frostygames0.elementalamulets.recipes.ElementalSeparationSerializer;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModRecipes {

    public static final IRecipeType<ElementalSeparation> ELEMENTAL_SEPARATION_RECIPE = IRecipeType.register("elemental_separation");

    public static final DeferredRegister<IRecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, ElementalAmulets.MOD_ID);

    public static final RegistryObject<IRecipeSerializer<ElementalSeparation>> ELEMENTAL_SEPARATION = SERIALIZERS.register("elemental_separation",
            () -> new ElementalSeparationSerializer<>(ElementalSeparation::new));
}
