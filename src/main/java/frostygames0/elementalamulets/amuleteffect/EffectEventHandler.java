package frostygames0.elementalamulets.amuleteffect;

import frostygames0.elementalamulets.ElementalAmulets;
import frostygames0.elementalamulets.core.init.ModItems;
import frostygames0.elementalamulets.items.CursedAmulet;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombieVillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.model.MultiLayerModel;
import net.minecraftforge.common.BasicTrade;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import top.theillusivec4.curios.api.CuriosApi;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
/*
 * This event handler is for custom amulet effects(not potion effects)
 */
public class EffectEventHandler {

    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent event) {
        JumpAmuletEffect.onLivingJump(event);
    }
    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        JumpAmuletEffect.onLivingHurt(event);
        //FireAmuletEffect.onLivingHurt(event);
    }
    @SubscribeEvent
    public static void onAttack(LivingAttackEvent event) {
        JumpAmuletEffect.onLivingAttack(event);
        //FireAmuletEffect.onLivingAttack(event);
    }

    @SubscribeEvent
    public static void villageTrades(VillagerTradesEvent event) {
        if(event.getType() == VillagerProfession.FISHERMAN) {
            List<VillagerTrades.ITrade> level5 = event.getTrades().get(5);
            level5.add(new BasicTrade(new ItemStack(Items.SALMON, 10), new ItemStack(ModItems.JUMP_AMULET.get()), 2, 10, 0.1f));
            level5.add(new BasicTrade(new ItemStack(Items.COD, 15), new ItemStack(ModItems.FIRE_AMULET.get()), 1, 20, 0.1f));
            level5.add(new BasicTrade(new ItemStack(Items.TROPICAL_FISH, 5), new ItemStack(ModItems.SPEED_AMULET.get()), 2, 30, 0.1f));
            level5.add(new BasicTrade(new ItemStack(Items.PUFFERFISH, 10), new ItemStack(ModItems.INVISIBILITY_AMULET.get()), 1, 40, 0.1f));
        }
    }
}
