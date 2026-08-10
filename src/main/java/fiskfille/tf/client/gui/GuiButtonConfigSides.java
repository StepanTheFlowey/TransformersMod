package fiskfille.tf.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Collections;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiButtonConfigSides extends GuiButtonFlat {
	public GuiButtonConfigSides(final int id, final int x, final int y) {
		super(id, x, y, 13, "");
	}

	@Override
	public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
		if(!visible) {
			return;
		}

		mc.getTextureManager().bindTexture(tfButtonTextures);
		GL11.glColor3f(1, 1, 1);
		field_146123_n = new Rectangle(xPosition, yPosition, width, height).contains(mouseX, mouseY);
		drawTexturedModalRect(xPosition, yPosition, 230 + (field_146123_n ? width : 0), 0, width, height);
	}

	@Override
	public List<String> getHoverText() {
		return Collections.singletonList(I18n.format("gui.tf.io.desc"));
	}
}
