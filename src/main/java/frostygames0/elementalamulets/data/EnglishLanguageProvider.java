package frostygames0.elementalamulets.data;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.element.Element;
import frostygames0.elementalamulets.registration.Elements;
import frostygames0.elementalamulets.registration.ModBlocks;
import frostygames0.elementalamulets.registration.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class EnglishLanguageProvider extends LanguageProvider {
    public EnglishLanguageProvider(PackOutput output) {
        super(output, ElementalAmulets.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        addElements();
        addItems();
        addBlocks();
        addMiscellaneous();
    }

    private void addElements() {
        addElement(Elements.WATER, "Water");
        addElement(Elements.EARTH, "Earth");
        addElement(Elements.FIRE, "Fire");
        addElement(Elements.AIR, "Air");
        addElementWithDescription(Elements.AETHER, "Aether", "The quintessence of this world! Responsible for all unexplainable and magical things!");
    }

    private void addItems() {
        addItem(ModItems.ELEMENT_SHARD, "Elemental Shard");
        addItem(ModItems.RING_OF_ELEMENTAL_SENSE, "Ring of Elemental Sense");
    }

    private void addBlocks() {
        addBlock(ModBlocks.PRIMITIVE_ELEMENTAL_EXTRACTOR, "Primitive Elemental Extractor");
    }

    private void addMiscellaneous() {
        add("tooltip.elementalamulets.elemental_composition", "Elemental Composition");
        add("tooltip.elementalamulets.elemental_composition.hidden", "Press [SHIFT] to see the elemental composition...");
        add("tooltip.elementalamulets.element_storage_stored", "Stored");
        add("itemGroup.elementalamulets", "Elemental Amulets");
        add("container.elementalamulets.elemental_extractor", "Extractor");

        add("command.elementalamulets.found_no_elements", "No elements registered! Something probably went wrong!");
        add("command.elementalamulets.found_n_elements", "%s elements registered:");
        add("command.elementalamulets.no_registry_error", "Unable to find registry for elements! Something went terribly wrong!");
    }

    private void addElement(ResourceKey<Element> key, String name) {
        add(Element.getNameTranslationKey(key.location()), name);
    }

    private void addElementWithDescription(ResourceKey<Element> key, String name, String description) {
        addElement(key, name);
        add(Element.getDescriptionTranslationKey(key.location()), description);
    }
}
