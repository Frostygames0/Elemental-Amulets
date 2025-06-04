package frostygames0.elementalamulets.inventory.menu;

import frostygames0.elementalamulets.element.ElementalComposition;
import frostygames0.elementalamulets.element.storage.IElementStorage;
import frostygames0.elementalamulets.element.storage.IElementStorageModifiable;
import frostygames0.elementalamulets.network.ClientboundElementStorageMenuPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

public abstract class SyncedElementalStorageMenu extends AbstractContainerMenu {
    protected final Player player;

    private final IElementStorage storage;

    private ElementalComposition oldValue = ElementalComposition.EMPTY;

    protected SyncedElementalStorageMenu(@Nullable MenuType<?> menuType, int containerId, Player player, IElementStorage storage) {
        super(menuType, containerId);

        this.player = player;
        this.storage = storage;
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

        var currentValue = storage.getStored();
        if (!currentValue.equals(oldValue)) {
            if (player instanceof ServerPlayer serverPlayer) {
                PacketDistributor.sendToPlayer(serverPlayer, new ClientboundElementStorageMenuPayload(containerId, currentValue));
            }

            oldValue = currentValue;
        }
    }

    // I'd recommend to override this if your storage doesn't implement modifiable, and you don't want an exception
    public void setStorage(ElementalComposition storage) {
        ((IElementStorageModifiable) this.storage).setStored(storage);
    }

    public IElementStorage getStorage() {
        return storage;
    }
}
