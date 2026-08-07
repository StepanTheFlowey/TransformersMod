package fiskfille.tf.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class GuiButtonSwapColors extends GuiButton {
	public GuiButtonSwapColors(final int id, final int x, final int y) {
		super(id, x, y, 20, 20, "");
	}

	@Override
	public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
		if(!visible) {
			return;
		}

		mc.getTextureManager().bindTexture(GuiButtonFlat.tfButtonTextures);
		GL11.glColor3f(1F, 1F, 1F);
		final boolean flag = new Rectangle(xPosition, yPosition, width, height).contains(mouseX, mouseY);
		drawTexturedModalRect(xPosition, yPosition, 0, 200 + (flag ? height : 0), width, height);
	}
}
