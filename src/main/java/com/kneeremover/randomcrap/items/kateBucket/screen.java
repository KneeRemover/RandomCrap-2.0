package com.kneeremover.randomcrap.items.kateBucket;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;

// * The Screen is drawn in several layers, most importantly:
//         * Background - renderBackground() - eg a grey fill
//         * Background texture - drawGuiContainerBackgroundLayer() (eg the frames for the slots)
//         * Foreground layer - typically text labels
//         * renderHoveredToolTip - for tool tips when the mouse is hovering over something of interest

public class screen extends ContainerScreen<com.kneeremover.randomcrap.items.kateBucket.container> {
	@Override
	protected void init() {
		super.init();
		this.imageWidth = 176;
		this.imageHeight = 222;
		this.leftPos = (this.width - this.imageWidth) / 2;
		this.topPos = (this.height - this.imageHeight) / 2;
	}

	public screen(com.kneeremover.randomcrap.items.kateBucket.container container, PlayerInventory playerInv, ITextComponent title) {
		super(container, playerInv, title);
	}

	@Override
	public void render(@NotNull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(matrixStack);
		super.render(matrixStack, mouseX, mouseY, partialTicks);
		this.renderTooltip(matrixStack, mouseX, mouseY);
	}

	@Override
	protected void renderTooltip(@NotNull MatrixStack matrixStack, int mouseX, int mouseY) {
		TranslationTextComponent redBucket = new TranslationTextComponent("randomcrap.gui.katebucket");
		float LABEL_XPOS = leftPos + 8;
		float PLAYER_LABEL_YPOS = topPos + 100;
		float BUCKET_LABEL_YPOS = topPos - 20;

		this.font.draw(matrixStack, redBucket.getString(), LABEL_XPOS, BUCKET_LABEL_YPOS, Color.darkGray.getRGB());
		this.font.draw(matrixStack, this.inventory.getDisplayName().getString(),
				LABEL_XPOS, PLAYER_LABEL_YPOS, Color.darkGray.getRGB());
	}

	@Override
	protected void renderLabels(MatrixStack pMatrixStack, int pX, int pY) {
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void renderBg(@NotNull MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		Objects.requireNonNull(this.minecraft).getTextureManager().bind(TEXTURE);                //this.minecraft
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
