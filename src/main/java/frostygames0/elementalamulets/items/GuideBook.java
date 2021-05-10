package frostygames0.elementalamulets.items;

import frostygames0.elementalamulets.ElementalAmulets;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.ModList;
import vazkii.patchouli.api.PatchouliAPI;

public class GuideBook extends Item {
    public static final ResourceLocation PATCHOULI_BOOK_ID = new ResourceLocation(ElementalAmulets.MOD_ID, "guidebook");

    public GuideBook(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if(!worldIn.isRemote() && playerIn instanceof ServerPlayerEntity) {
            if(ModList.get().isLoaded("patchouli")) {
                PatchouliAPI.get().openBookGUI((ServerPlayerEntity) playerIn, PATCHOULI_BOOK_ID);
                return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
            } else {
                playerIn.sendStatusMessage(new StringTextComponent("Patchouli is not detected! Please install it in order to get access to the guide").mergeStyle(TextFormatting.RED), true);
                return ActionResult.resultFail(playerIn.getHeldItem(handIn));
            }
        }
        return ActionResult.resultConsume(playerIn.getHeldItem(handIn));
    }
}
