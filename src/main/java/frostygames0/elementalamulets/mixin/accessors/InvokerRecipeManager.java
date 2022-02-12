package frostygames0.elementalamulets.mixin.accessors;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;


import java.util.Map;

/**
 * @author Frostygames0
 * @date 12.02.2022 22:34
 */
@Mixin(RecipeManager.class)
public interface InvokerRecipeManager {

    @Invoker
    <C extends IInventory, T extends IRecipe<C>> Map<ResourceLocation, T> callByType(IRecipeType<T> pRecipeType);
}
