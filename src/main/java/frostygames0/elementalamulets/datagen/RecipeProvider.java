package frostygames0.elementalamulets.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;

import java.util.function.Consumer;

public class RecipeProvider extends net.minecraft.data.RecipeProvider {
    public RecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
    }
}
