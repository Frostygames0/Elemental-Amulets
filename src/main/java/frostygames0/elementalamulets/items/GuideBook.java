package frostygames0.elementalamulets.items;

import frostygames0.elementalamulets.advancements.triggers.ModCriteriaTriggers;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import vazkii.patchouli.api.PatchouliAPI;


import javax.annotation.Nullable;
import java.util.List;

import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

public class GuideBook extends Item {
    public static final ResourceLocation BOOK_ID = modPrefix("guide_book");

    public GuideBook(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if(ModList.get().isLoaded("patchouli")) tooltip.add(((IFormattableTextComponent)PatchouliAPI.get().getSubtitle(BOOK_ID)).withStyle(TextFormatting.GOLD));
        tooltip.add(new TranslationTextComponent("item.elementalamulets.guide_book.subtitle").withStyle(TextFormatting.GRAY));
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if(playerIn instanceof ServerPlayerEntity) {
            if(!FMLEnvironment.production) { // Isn't not finished, so locking it for players
                if (ModList.get().isLoaded("patchouli")) {
                    PatchouliAPI.get().openBookGUI((ServerPlayerEntity) playerIn, BOOK_ID);
                    ModCriteriaTriggers.SUCCESS_USE.trigger((ServerPlayerEntity) playerIn, playerIn.getItemInHand(handIn));
                    return ActionResult.success(playerIn.getItemInHand(handIn));
                } else {
                    playerIn.displayClientMessage(new TranslationTextComponent("patchouli.elementalamulets.not_present").withStyle(TextFormatting.RED), true);
                    return ActionResult.fail(playerIn.getItemInHand(handIn));
                }
            } else {
                playerIn.displayClientMessage(new TranslationTextComponent("patchouli.elementalamulets.no_ide").withStyle(TextFormatting.GOLD), true);
            }
        }
        return ActionResult.consume(playerIn.getItemInHand(handIn));
    }
}
