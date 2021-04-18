package frostygames0.elementalamulets.items.interfaces;

public interface IAmuletItem {
    /**
     * Gets damage that amulet will receive on it's use
     * @return Damage
     */
    public int getDamageOnUse();

    /**
     * Gets tier of amulet.
     * @return any int value(Negative values are not recommended!)
     */
    public int getTier();
}
