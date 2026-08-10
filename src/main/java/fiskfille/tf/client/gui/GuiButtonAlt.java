package fiskfille.tf.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class GuiButtonAlt extends GuiButton {
	public GuiButtonAlt(final int id, final int x, final int y, final int width, final int height, final String s) {
		super(id, x, y, width, height, s);
	}

	@Override
	public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
		if(!visible) {
			return;
		}

		mc.getTextureManager().bindTexture(buttonTextures);

		GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glColor3f(1, 1, 1);
		final int k = getHoverState(field_146123_n = new Rectangle(xPosition, yPosition, width, height).contains(mouseX, mouseY));
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		drawTexturedModalRect(xPosition, yPosition, 0, 46 + k * 20, width / 2, height / 2);
		drawTexturedModalRect(xPosition, yPosition + height / 2, 0, 66 - height / 2 + k * 20, width / 2, height / 2);
		drawTexturedModalRect(xPosition + width / 2, yPosition, 200 - width / 2, 46 + k * 20, width / 2, height / 2);
		drawTexturedModalRect(xPosition + width / 2, yPosition + height / 2, 200 - width / 2, 66 - height / 2 + k * 20, width / 2, height / 2);

		mouseDragged(mc, mouseX, mouseY);

		int color = 0xE0E0E0;
		if(packedFGColour != 0) {
			color = packedFGColour;
		}
		else if(!enabled) {
			color = 0xA0A0A0;
		}
		else if(field_146123_n) {
			color = 0xFFFFA0;
		}

		drawCenteredString(mc.fontRenderer, displayString, xPosition + width / 2, yPosition + (height - 8) / 2, color);
		GL11.glPopAttrib();
	}
}
