package frostygames0.elementalamulets.advancements.triggers;

import net.minecraft.advancements.CriteriaTriggers;

/**
 * @author Frostygames0
 * @date 02.06.2021 10:15
 */
public class ModCriteriaTriggers {
    public static final ItemSuccessUseTrigger SUCCESS_USE = new ItemSuccessUseTrigger();
    public static final ItemCombinedTrigger ITEM_COMBINED = new ItemCombinedTrigger();
    public static void register() {
        CriteriaTriggers.register(SUCCESS_USE);
        CriteriaTriggers.register(ITEM_COMBINED);
    }
}
