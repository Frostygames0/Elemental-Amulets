package frostygames0.elementalamulets.data.language;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.element.Element;
import frostygames0.elementalamulets.initialization.ModBlocks;
import frostygames0.elementalamulets.initialization.ModElements;
import frostygames0.elementalamulets.initialization.ModItems;
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
        addContainers();
        addCommandsOutputs();
        addGenericTooltips();
        addCreativeModeTabs();
        addKeyMappings();
        addMiscellaneous();
    }

    private void addElements() {
        addElement(ModElements.WATER, "Water");
        addElement(ModElements.EARTH, "Earth");
        addElement(ModElements.FIRE, "Fire");
        addElement(ModElements.AIR, "Air");
        addElement(ModElements.AETHER, "Aether", "The quintessence of this world! Responsible for all unexplainable and magical things!");
    }

    private void addItems() {
        addItem(ModItems.ELEMENTUM_SHARD, "Elementum Shard");
        addItem(ModItems.RING_OF_ELEMENTAL_SENSE, "Ring of Elemental Sense");
    }

    private void addBlocks() {
        addBlock(ModBlocks.PRIMITIVE_ELEMENTAL_EXTRACTOR, "Primitive Elemental Extractor");
        addBlock(ModBlocks.ELEMENTUM_CRYSTAL_ORE, "Elementum Crystal Ore");
        addBlock(ModBlocks.ELEMENTUM_CRYSTAL_DEEPSLATE_ORE, "Elementum Crystal Deepslate Ore");
        addBlock(ModBlocks.ELEMENTAL_PIPE, "Elemental Pipe");
        addBlock(ModBlocks.PRESSURIZER_PIPE, "Pressurizer Pipe");

        addBlock(ModBlocks.SIMPLE_STORAGE, "Simple Storage");
        addBlock(ModBlocks.SIMPLE_GENERATOR, "Simple Generator");
    }

    private void addCommandsOutputs() {
        add("command.elementalamulets.found_no_elements", "No elements registered! Something probably went wrong!");
        add("command.elementalamulets.found_n_elements", "%s elements registered:");
        add("command.elementalamulets.no_registry_error", "Unable to find registry for elements! Something went terribly wrong!");
        add("command.elementalamulets.get_element.description", "Description: %s");
        add("command.elementalamulets.get_element.is_primordial", "Is Primordial?: %s");
        add("command.elementalamulets.get_element.composition", "Composition:");
    }

    private void addGenericTooltips() {
        add("tooltip.elementalamulets.elemental_composition", "Elemental Composition");
        add("tooltip.elementalamulets.elemental_composition.hidden", "Press [%s] to see the elemental composition...");
        add("tooltip.elementalamulets.element_storage_stored", "Stored");
    }

    private void addContainers() {
        add("container.elementalamulets.elemental_extractor", "Extractor");
    }

    private void addCreativeModeTabs() {
        add("tab.elementalamulets.main", "Elemental Amulets");
    }

    private void addMiscellaneous() {
        add("generic.elementalamulets.round_brackets", "(%s)");
        add("generic.elementalamulets.mixed", "Mixed");
    }

    private void addKeyMappings() {
        addKeyMapping("show_composition", "Show Elemental Composition");
    }

    private void addKeyMapping(String id, String name) {
        add("key.elementalamulets." + id, name);
    }

    private void addElement(ResourceKey<Element> key, String name) {
        add(Element.getNameTranslationKey(key.location()), name);
    }

    private void addElement(ResourceKey<Element> key, String name, String description) {
        addElement(key, name);
        add(Element.getDescriptionTranslationKey(key.location()), description);
    }
}
