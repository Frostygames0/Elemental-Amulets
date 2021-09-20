package frostygames0.elementalamulets.client.models;

// Made with Blockbench 3.9.2
// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import frostygames0.elementalamulets.items.amulets.AmuletItem;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;


import static frostygames0.elementalamulets.ElementalAmulets.modPrefix;

public class AmuletModel extends EntityModel<Entity> {
    private final ModelRenderer jewel;
    private final ModelRenderer string;

    public AmuletModel() {
        texWidth = 16;
        texHeight = 16;

        jewel = new ModelRenderer(this);
        jewel.setPos(1.0F, 28.75F, -1.0F);
        jewel.texOffs(0, 1).addBox(-3.0F, -24.0F, -3.0F, 4.0F, 4.0F, 1.0F, 0.0F, false);
        jewel.texOffs(0, 8).addBox(-4.0F, -23.0F, -3.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        jewel.texOffs(5, 6).addBox(-2.0F, -25.0F, -3.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
        jewel.texOffs(0, 11).addBox(1.0F, -23.0F, -3.0F, 1.0F, 2.0F, 1.0F, 0.0F, false);
        jewel.texOffs(5, 9).addBox(-2.0F, -20.0F, -3.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);

        string = new ModelRenderer(this);
        string.setPos(0.0F, -25.0F, 0.0F);
        jewel.addChild(string);
        string.texOffs(5, 8).addBox(0.0F, -1.0F, -2.0F, 2.0F, 1.0F, 0.0F, 0.0F, false);
        string.texOffs(0, 7).addBox(2.0F, -2.0F, -2.0F, 1.0F, 1.0F, 0.0F, 0.0F, false);
        string.texOffs(3, 7).addBox(3.0F, -4.0F, -2.0F, 1.0F, 2.0F, 0.0F, 0.0F, false);
        string.texOffs(0, 6).addBox(3.0F, -4.0F, -2.0F, 1.0F, 0.0F, 3.0F, 0.0F, false);
        string.texOffs(0, 0).addBox(-4.0F, -4.0F, 2.0F, 6.0F, 0.0F, 1.0F, 0.0F, false);
        string.texOffs(0, 6).addBox(2.0F, -4.0F, 1.0F, 1.0F, 0.0F, 1.0F, 0.0F, false);
        string.texOffs(0, 6).addBox(-5.0F, -4.0F, 1.0F, 1.0F, 0.0F, 1.0F, 0.0F, false);
        string.texOffs(0, 6).addBox(-6.0F, -4.0F, -2.0F, 1.0F, 0.0F, 3.0F, 0.0F, false);
        string.texOffs(3, 7).addBox(-6.0F, -4.0F, -2.0F, 1.0F, 2.0F, 0.0F, 0.0F, false);
        string.texOffs(0, 7).addBox(-5.0F, -2.0F, -2.0F, 1.0F, 1.0F, 0.0F, 0.0F, false);
        string.texOffs(5, 8).addBox(-4.0F, -1.0F, -2.0F, 2.0F, 1.0F, 0.0F, 0.0F, false);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
        //previously the render function, render code was moved to a method below
    }

    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
        jewel.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }

    public static ResourceLocation getTexture(AmuletItem item, ItemStack stack) {
        int tier = MathHelper.clamp(item.getTier(stack), 0, AmuletItem.MAX_TIER);
        return modPrefix("textures/entity/amulets/tier_"+tier+"/"+item.getRegistryName().getPath()+".png");
    }
}
