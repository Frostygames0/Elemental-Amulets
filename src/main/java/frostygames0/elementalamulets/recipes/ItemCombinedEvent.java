package frostygames0.elementalamulets.recipes;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.items.IItemHandlerModifiable;

/**
 * @author Frostygames0
 * @date 02.06.2021 3:08
 */
public class ItemCombinedEvent extends PlayerEvent {

    public static void eventPost(PlayerEntity crafter, IItemHandlerModifiable handler) {
        MinecraftForge.EVENT_BUS.post(new ItemCombinedEvent(crafter, handler));
    }

    private final IItemHandlerModifiable handler;
    public ItemCombinedEvent(PlayerEntity player, IItemHandlerModifiable handler) {
        super(player);
        this.handler = handler;
    }

    public IItemHandlerModifiable getHandler() {
        return this.handler;
    }
}
