package fiskfille.tf.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.TransformersMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiButtonFlat extends GuiButton {
	public static final ResourceLocation tfButtonTextures = new ResourceLocation(TransformersMod.MODID, "textures/gui/widgets.png");

	public GuiButtonFlat(final int id, final int x, final int y, final int width, final String s) {
		super(id, x, y, width, 13, s);
	}

	@Override
	public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
		if(!visible) {
			return;
		}

		mc.getTextureManager().bindTexture(tfButtonTextures);

		GL11.glColor3f(1, 1, 1);
		field_146123_n = new Rectangle(xPosition, yPosition, width, height).contains(mouseX, mouseY);
		final int hoverState = getHoverState(field_146123_n);

		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		mouseDragged(mc, mouseX, mouseY);

		final FontRenderer fontrenderer = mc.fontRenderer;
		final int color = enabled ? 0x373737 : 0x7F7F7F;
		if(width % 2 == 0) {
			drawTexturedModalRect(xPosition, yPosition, 60, hoverState * 13, width / 2, height);
			drawTexturedModalRect(xPosition + width / 2, yPosition, 60 + 150 - width / 2, hoverState * 13, width / 2, height);
			fontrenderer.drawString(displayString, xPosition + width / 2 - fontrenderer.getStringWidth(displayString) / 2, yPosition + (height - 6) / 2, color);
		}
		else {
			drawTexturedModalRect(xPosition, yPosition, 60, hoverState * 13, width / 2 + 1, height);
			drawTexturedModalRect(xPosition + width / 2 + 1, yPosition, 60 + 150 - width / 2, hoverState * 13, width / 2, height);
			fontrenderer.drawString(displayString, xPosition + width / 2 - fontrenderer.getStringWidth(displayString) / 2 + 1, yPosition + (height - 6) / 2, color);
		}
	}

	public List<String> getHoverText() {
		return Collections.emptyList();
	}
}
