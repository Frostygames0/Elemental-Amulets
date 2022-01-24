/*
 *  Copyright (c) 2021
 *
 *     This file is part of Elemental Amulets, a Minecraft Mod.
 *
 *     Elemental Amulets is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Elemental Amulets is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with Elemental Amulets.  If not, see <https://www.gnu.org/licenses/>.
 */

package frostygames0.elementalamulets;

import frostygames0.elementalamulets.advancements.triggers.ModCriteriaTriggers;
import frostygames0.elementalamulets.client.particles.ModParticles;
import frostygames0.elementalamulets.config.ModConfig;
import frostygames0.elementalamulets.init.*;
import frostygames0.elementalamulets.items.amulets.AmuletItem;
import frostygames0.elementalamulets.network.ModNetworkHandler;
import frostygames0.elementalamulets.world.LootTableModifiers;
import frostygames0.elementalamulets.world.ores.ModOreFeatures;
import frostygames0.elementalamulets.world.structures.CultTempleStructure;
import frostygames0.elementalamulets.world.structures.ModStructures;
import frostygames0.elementalamulets.world.structures.ModStructureFeatures;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;


import java.util.Locale;

@Mod(ElementalAmulets.MOD_ID)
public class ElementalAmulets {

    public static final String MOD_ID = "elementalamulets";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final CreativeModeTab GROUP = new CreativeModeTab(MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            return AmuletItem.getStackWithTier(new ItemStack(ModItems.FIRE_AMULET.get()), 3);
        }
    };

    public ElementalAmulets() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        ModItems.ITEMS.register(bus);
        ModBlocks.BLOCKS.register(bus);
        ModBEs.BLOCK_ENTITIES.register(bus);
        ModMenus.MENUS.register(bus);
        ModRecipes.SERIALIZERS.register(bus);
        ModVillagers.register(bus);
        ModStructures.STRUCTURES.register(bus);
        ModParticles.PARTICLES.register(bus);

        bus.addGenericListener(GlobalLootModifierSerializer.class, LootTableModifiers::registerLootModifierSerializer); // Loot modification register

        ModLoadingContext modCtx = ModLoadingContext.get();
        modCtx.registerConfig(net.minecraftforge.fml.config.ModConfig.Type.SERVER, ModConfig.SERVER_SPEC);
        modCtx.registerConfig(net.minecraftforge.fml.config.ModConfig.Type.CLIENT, ModConfig.CLIENT_SPEC);
        modCtx.registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, ModConfig.COMMON_SPEC, MOD_ID + "-worldgen.toml");

        bus.addListener(this::enqueueIMC);
        bus.addListener(this::commonSetup);

        forgeBus.addListener(ModCommands::registerCommandsEvent);
        forgeBus.addListener(ModVillagers.Structures::addHouses);
        forgeBus.addListener(CultTempleStructure::addMobsToSpawn);
    }

    // use this instead, when you need a ResourceLocation with mod's id
    public static ResourceLocation modPrefix(String name) {
        return new ResourceLocation(MOD_ID, name.toLowerCase(Locale.ROOT));
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.NECKLACE.getMessageBuilder().cosmetic().icon(modPrefix("item/necklace_slot")).build());
        InterModComms.sendTo(CuriosApi.MODID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BELT.getMessageBuilder().build());
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        ModItems.lookupAmulets(); // I'm calling it, so it will be already cached on starting up
        ModNetworkHandler.registerMessages();
        event.enqueueWork(() -> {
            ModStructures.setupStructures();
            ModStructureFeatures.register();

            ModOreFeatures.register();

            ModCriteriaTriggers.register();
            ModStats.registerStats();

            var list = ComposterBlock.COMPOSTABLES;
            list.put(ModItems.EARTH_SHARDS_BLOCK.get(), 1.0f);
            list.put(ModItems.EARTH_ELEMENT.get(), 0.8f);
        });
    }
}
