package frostygames0.elementalamulets.datagen.language;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.element.ElementType;
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
        addCommand("click_to_see_element", "Click to get the information about the element!");
        addCommand("found_no_elements", "No elements registered! Something probably went wrong!");
        addCommand("found_n_elements", "%s elements registered:");
        addCommand("no_registry_error", "Unable to find the registry %s! Something went terribly wrong!");
        addCommand("no_element_error", "No such element: %s");
        addCommand("get_element.name", "Name: %s");
        addCommand("get_element.description", "Description: %s");
        addCommand("get_element.is_primordial", "Is Primordial?: %s");
        addCommand("get_element.composition", "Composed of:");

        addCommand("storage.add.fail", "add %s to");
        addCommand("storage.add.success", "added %s x%s to");

        addCommand("storage.take.fail", "take %s from");
        addCommand("storage.take.success", "taken %s x%s from");

        addCommand("storage.block_success", "Successfully %s the storage at %s.");
        addCommand("storage.block_fail", "Unable to %s the storage at %s.");
        addCommand("storage.block_empty", "The storage at %s is empty!");
        addCommand("storage.block_query", "The storage at %s %s");

        addCommand("no_block_storage", "The target block at %s has no element storage capability!");

        addCommand("storage.get.all_stored_element_types", "stores %s different element types inside.");
        addCommand("storage.get.max_distinct_elements_stored", "allows %s distinct elements to be stored inside.");
        addCommand("storage.get.distinct_elements_amount", "contains %s distinct elements");
        addCommand("storage.get.total_amount", "has %s elements (regardless of their type) inside");
        addCommand("storage.get.max_capacity", "has the max capacity of %s.");
        addCommand("storage.get.element", "contains %s x%s.");
        addCommand("storage.get.element.none", "doesn't contain %s");
        addCommand("storage.set.not_modifiable", "directly set contents of");
        addCommand("storage.set.success", "set the contents of");

        addCommandArgument("elemental_composition.invalid", "Invalid Elemental Composition: %s");

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
        addKeyMapping("show_composition", "Show the elemental composition.");
    }

    private void addKeyMapping(String id, String name) {
        add("key.elementalamulets." + id, name);
    }

    private void addElement(ResourceKey<ElementType> key, String name) {
        add(ElementType.getNameTranslationKey(key.location()), name);
    }

    private void addElement(ResourceKey<ElementType> key, String name, String description) {
        addElement(key, name);
        add(ElementType.getDescriptionTranslationKey(key.location()), description);
    }

    private void addCommand(String name, String description) {
        add("command.elementalamulets." + name, description);
    }

    private void addCommandArgument(String name, String description) {
        add("argument.elementalamulets." + name, description);
    }
}
