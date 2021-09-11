package frostygames0.elementalamulets.core.init;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.blocks.containers.AmuletBeltContainer;
import frostygames0.elementalamulets.blocks.containers.ElementalCombinatorContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IntArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModContainers {
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, ElementalAmulets.MOD_ID);

    public static final RegistryObject<ContainerType<ElementalCombinatorContainer>> ELEMENTAL_COMBINATOR_CONTAINER = CONTAINERS.register("elemental_combinator", () -> IForgeContainerType.create(
            ((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                World world = inv.player.getEntityWorld();
                return new ElementalCombinatorContainer(windowId, world, pos, inv, inv.player, new IntArray(2));
            })
    ));

    public static final RegistryObject<ContainerType<AmuletBeltContainer>> AMULET_BELT_CONTAINER = CONTAINERS.register("amulet_belt", () -> IForgeContainerType.create(
            ((windowId, inv, data) -> {
                ItemStack stack = data.readItemStack();
                World world = inv.player.getEntityWorld();
                return new AmuletBeltContainer(windowId, world, stack, inv, inv.player);
            })
    ));

}
