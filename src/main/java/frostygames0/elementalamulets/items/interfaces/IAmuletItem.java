package frostygames0.elementalamulets.items.interfaces;

/**
 * Interface with all amulet related methods
 * Used to identify amulets
 */
public interface IAmuletItem {
    int getDamageOnUse();

    default int getTier() {
        return 1;
    }
}
