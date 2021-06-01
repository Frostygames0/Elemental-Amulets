package frostygames0.elementalamulets.datagen;

import frostygames0.elementalamulets.datagen.recipes.RecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        if(event.includeClient()) {
            generator.addProvider(new ItemModelProvider(generator, event.getExistingFileHelper()));
            generator.addProvider(new BlockStateProvider(generator, event.getExistingFileHelper()));
        }
        if(event.includeServer()) {
            generator.addProvider(new TagProvider(generator, event.getExistingFileHelper()));
            generator.addProvider(new RecipeProvider(generator));
        }
    }
}
