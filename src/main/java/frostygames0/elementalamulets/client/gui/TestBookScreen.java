package frostygames0.elementalamulets.client.gui;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class TestBookScreen extends Screen {
    private static final ResourceLocation ENCHANTING_BOOK_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/enchanting_table_book.png");

    private BookModel bookModel;
    private float openAnimState;
    private float oOpen;

    public TestBookScreen() {
        super(Component.empty());
    }

    @Override
    protected void init() {
        super.init();
        bookModel = new BookModel(minecraft.getEntityModels().bakeLayer(ModelLayers.BOOK));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        renderBook(guiGraphics, width / 2, height / 2 + 90, partialTick);
    }

    @Override
    public void tick() {
        super.tick();
        tickBook();
    }

    private void tickBook() {
        oOpen = openAnimState;

        openAnimState += 0.2f;
        openAnimState = Mth.clamp(openAnimState, 0.0F, 1.0F);
    }

    private void renderBook(GuiGraphics guiGraphics, int x, int y, float partialTick) {
        guiGraphics.flush();
        Lighting.setupForEntityInInventory();
        guiGraphics.pose().pushPose();

        guiGraphics.pose().translate(x, y, 100.0F);
        guiGraphics.pose().scale(-100.0F, 100.0F, 10.0F);
        guiGraphics.pose().mulPose(Axis.XP.rotationDegrees(60F));
        guiGraphics.pose().mulPose(Axis.YP.rotationDegrees(-90.0F));

        var pepe = Mth.lerp(partialTick, oOpen, openAnimState);
        bookModel.setupAnim(0f, 1, 1, pepe);
//
//        guiGraphics.drawSpecial(multiBufferSource -> {
//            VertexConsumer vertexconsumer = multiBufferSource.getBuffer(RenderType.solid());
//            ShapeRenderer.renderLineBox(guiGraphics.pose(), vertexconsumer, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1);
//        });

        guiGraphics.drawSpecial(multiBufferSource -> {
            var vertexConsumer = multiBufferSource.getBuffer(bookModel.renderType(ENCHANTING_BOOK_LOCATION));
            bookModel.renderToBuffer(guiGraphics.pose(), vertexConsumer, 15728880, OverlayTexture.NO_OVERLAY);
        });

        guiGraphics.flush();
        guiGraphics.pose().popPose();

//        float f = Mth.lerp(partialTick, this.oOpen, this.open);
//        float f1 = Mth.lerp(partialTick, this.oFlip, this.flip);
//        guiGraphics.flush();
//        Lighting.setupForEntityInInventory();
//        guiGraphics.pose().pushPose();
//        guiGraphics.pose().translate((float)x + 33.0F, (float)y + 31.0F, 100.0F);
//        float f2 = 40.0F;
//        guiGraphics.pose().scale(-40.0F, 40.0F, 40.0F);
//        guiGraphics.pose().mulPose(Axis.XP.rotationDegrees(25.0F));
//        guiGraphics.pose().translate((1.0F - f) * 0.2F, (1.0F - f) * 0.1F, (1.0F - f) * 0.25F);
//        float f3 = -(1.0F - f) * 90.0F - 90.0F;
//        guiGraphics.pose().mulPose(Axis.YP.rotationDegrees(f3));
//        guiGraphics.pose().mulPose(Axis.XP.rotationDegrees(180.0F));
//        float f4 = Mth.clamp(Mth.frac(f1 + 0.25F) * 1.6F - 0.3F, 0.0F, 1.0F);
//        float f5 = Mth.clamp(Mth.frac(f1 + 0.75F) * 1.6F - 0.3F, 0.0F, 1.0F);
//        this.bookModel.setupAnim(0.0F, f4, f5, f);
//        guiGraphics.drawSpecial(p_371383_ -> {
//            VertexConsumer vertexconsumer = p_371383_.getBuffer(this.bookModel.renderType(ENCHANTING_BOOK_LOCATION));
//            this.bookModel.renderToBuffer(guiGraphics.pose(), vertexconsumer, 15728880, OverlayTexture.NO_OVERLAY);
//        });
//        guiGraphics.flush();
//        guiGraphics.pose().popPose();
//        Lighting.setupFor3DItems();
    }
}
