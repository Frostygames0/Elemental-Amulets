package frostygames0.elementalamulets.network;

import frostygames0.elementalamulets.blocks.containers.AmuletBeltContainer;
import frostygames0.elementalamulets.core.init.ModItems;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;
import top.theillusivec4.curios.api.CuriosApi;


import java.util.function.Supplier;

/**
 * @author Frostygames0
 * @date 21.09.2021 22:06
 */
public class SOpenAmuletBeltGUIPacket {
    public static void handle(SOpenAmuletBeltGUIPacket msg, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context ctx = supplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayerEntity sender = ctx.getSender();
            ItemStack stack = CuriosApi.getCuriosHelper().findEquippedCurio(ModItems.AMULET_BELT.get(), sender).map(triple -> triple.right).orElse(ItemStack.EMPTY);
            if(!stack.isEmpty()) {
                NetworkHooks.openGui(sender, new SimpleNamedContainerProvider((id, playerInventory, player) -> new AmuletBeltContainer(id, playerInventory, stack), stack.getDisplayName()), buf -> buf.writeItem(stack));
            }
        });
        ctx.setPacketHandled(true);
    }
}
