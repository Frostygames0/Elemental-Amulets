package frostygames0.elementalamulets.mixin.accessors;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
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
    <C extends Container, T extends Recipe<C>> Map<ResourceLocation, T> callByType(RecipeType<T> pRecipeType);
}
