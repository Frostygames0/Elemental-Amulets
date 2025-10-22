package frostygames0.elementalamulets.initialization.command;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.command.ElementalCompositionArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModArgumentTypes {
    public static final DeferredRegister<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPES = DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE, ElementalAmulets.MOD_ID);

    public static final DeferredHolder<ArgumentTypeInfo<?, ?>, SingletonArgumentInfo<ElementalCompositionArgument>> ELEMENTAL_COMPOSITION =
            COMMAND_ARGUMENT_TYPES.register("elemental_composition",
                    () -> ArgumentTypeInfos.registerByClass(
                            ElementalCompositionArgument.class,
                            SingletonArgumentInfo.contextAware(ElementalCompositionArgument::composition)
                    )
            );

    public static void register(IEventBus modBus) {
        COMMAND_ARGUMENT_TYPES.register(modBus);
    }
}
