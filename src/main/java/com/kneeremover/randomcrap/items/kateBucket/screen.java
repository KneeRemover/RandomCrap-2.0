package com.kneeremover.randomcrap.items.kateBucket;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.awt.*;

// * The Screen is drawn in several layers, most importantly:
//         * Background - renderBackground() - eg a grey fill
//         * Background texture - drawGuiContainerBackgroundLayer() (eg the frames for the slots)
//         * Foreground layer - typically text labels
//         * renderHoveredToolTip - for tool tips when the mouse is hovering over something of interest

public class screen extends ContainerScreen<com.kneeremover.randomcrap.items.kateBucket.container> {

    public screen(com.kneeremover.randomcrap.items.kateBucket.container container, PlayerInventory playerInv, ITextComponent title) {
        super(container, playerInv, title);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
        final float PLAYER_LABEL_XPOS = 8;
        final float PLAYER_LABEL_DISTANCE_FROM_BOTTOM = (96 - 2);

        final float BUCKET_LABEL_YPOS = -50;
        TranslationTextComponent redBucket = new TranslationTextComponent("randomcrap.gui.katebucket");
        float BUCKET_LABEL_XPOS = 8;                  // centre the label
        this.font.drawString(matrixStack, redBucket.getString(), BUCKET_LABEL_XPOS, BUCKET_LABEL_YPOS, Color.darkGray.getRGB());            //this.font.drawString;

        float PLAYER_LABEL_YPOS = ySize - PLAYER_LABEL_DISTANCE_FROM_BOTTOM;
        this.font.drawString(matrixStack, this.playerInventory.getDisplayName().getString(),                              //this.font.drawString;
                PLAYER_LABEL_XPOS, PLAYER_LABEL_YPOS, Color.darkGray.getRGB());
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(TEXTURE);                //this.minecraft
        // width and height are the size provided to the window when initialised after creation.
        // xSize, ySize are the expected size of the texture-? usually seems to be left as a defaul.t.
        // The code below is typical for vanilla containers, so I've just copied that- it appears to centre the texture within
        //  the available window
        int edgeSpacingX = (this.width - 176) / 2;
        int edgeSpacingY = (this.height - 279) / 2;
        this.blit(matrixStack, edgeSpacingX, edgeSpacingY, 0, 0, 256, 256);
    }

    // This is the resource location for the background image
    private static final ResourceLocation TEXTURE = new ResourceLocation("randomcrap", "textures/gui/katebucket.png");

}
