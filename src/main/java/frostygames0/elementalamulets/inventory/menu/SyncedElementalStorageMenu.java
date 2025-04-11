package frostygames0.elementalamulets.inventory.menu;

import frostygames0.elementalamulets.element.ElementalComposition;
import frostygames0.elementalamulets.element.storage.IElementStorage;
import frostygames0.elementalamulets.network.SyncElementStorageWithClientMenu;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

public abstract class SyncedElementalStorageMenu extends AbstractContainerMenu {
    protected final Player player;

    private final IElementStorage stored;

    private ElementalComposition oldValue = ElementalComposition.EMPTY;

    protected SyncedElementalStorageMenu(@Nullable MenuType<?> menuType, int containerId, Player player, IElementStorage storage) {
        super(menuType, containerId);

        this.player = player;
        this.stored = storage;
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

        var currentValue = this.stored.getStored();
        if (!currentValue.equals(this.oldValue)) {
            if (this.player instanceof ServerPlayer serverPlayer) {
                PacketDistributor.sendToPlayer(serverPlayer, new SyncElementStorageWithClientMenu(this.containerId, currentValue));
            }

            this.oldValue = currentValue;
        }
    }

    public void setStored(ElementalComposition storage) {
        this.stored.setStored(storage);
    }

    public IElementStorage getStored() {
        return this.stored;
    }
}
