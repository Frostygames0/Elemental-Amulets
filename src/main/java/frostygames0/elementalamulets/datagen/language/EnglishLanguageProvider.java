package frostygames0.elementalamulets.datagen.language;

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
    }

    private void addCommandsOutputs() {
        addCommand("click_to_see_element", "Click to get information about the element!");
        addCommand("found_no_elements", "No elements registered! Something probably went wrong!");
        addCommand("found_n_elements", "%s elements registered:");
        addCommand("no_registry_error", "Unable to find registry %s! Something went terribly wrong!");
        addCommand("no_element_error", "No such element: %s");
        addCommand("get_element.name", "Name: %s");
        addCommand("get_element.description", "Description: %s");
        addCommand("get_element.is_primordial", "Is Primordial?: %s");
        addCommand("get_element.composition", "Composed of:");

        addCommand("storage.add.fail", "add %s to");
        addCommand("storage.add.success", "added %s x%s to");

        addCommand("storage.take.fail", "take %s from");
        addCommand("storage.take.success", "Taken %s x%s from");

        addCommand("storage.block_success", "Successfully %s the storage at %s");
        addCommand("storage.block_fail", "Unable to %s the storage at %s");
        addCommand("storage.block_empty", "Storage at %s is empty!");
        addCommand("storage.block_query", "Storage at %s %s");

        addCommand("no_block_storage", "Target block has no element storage capability!");

        addCommand("storage.get.all_stored_element_types", "Element types stored in the storage: %s");
        addCommand("storage.get.max_distinct_elements_stored", "Max distinct elements allowed by the storage: %s");
        addCommand("storage.get.distinct_elements_amount", "Number of distinct elements stored in the storage: %s");
        addCommand("storage.get.total_amount", "Total amount of all elements in the storage: %s");
        addCommand("storage.get.max_capacity", "Max capacity of the storage: %s");
        addCommand("storage.get.element", "contains %s x%s");
        addCommand("storage.get.element.none", "doesn't contain %s");
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

    private void addCommand(String name, String description) {
        add("command.elementalamulets." + name, description);
    }
}
