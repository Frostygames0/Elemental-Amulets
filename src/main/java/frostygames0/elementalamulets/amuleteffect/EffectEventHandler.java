package frostygames0.elementalamulets.amuleteffect;

import frostygames0.elementalamulets.ElementalAmulets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombieVillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
        if(event.getSource().isFireDamage() && event.getEntity() instanceof PlayerEntity) {
            ElementalAmulets.LOGGER.debug(event.getAmount());
        }
    }
    @SubscribeEvent
    public static void onAttack(LivingAttackEvent event) {
        JumpAmuletEffect.onLivingAttack(event);
    }

    /*
    @SubscribeEvent
    public static void onEntitySpawn(LivingSpawnEvent event){
        if(event.getEntity() instanceof ZombieVillagerEntity) {
            ZombieVillagerEntity zombie = (ZombieVillagerEntity) event.getEntity();
            zombie.setCustomName(new StringTextComponent("MEGA Lesha Akakiy"));
            zombie.setCustomNameVisible(true);
            zombie.setItemStackToSlot(EquipmentSlotType.HEAD, new ItemStack(Items.END_ROD));
            zombie.setItemStackToSlot(EquipmentSlotType.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
            zombie.setItemStackToSlot(EquipmentSlotType.LEGS, new ItemStack(Items.CHAINMAIL_LEGGINGS));
            zombie.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.DIRT));

        }
    }*/
}
