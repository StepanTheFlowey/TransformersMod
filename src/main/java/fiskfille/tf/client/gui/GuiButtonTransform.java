package fiskfille.tf.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Collections;
import java.util.List;

public class GuiButtonTransform extends GuiButtonFlat {
	public GuiButtonTransform(final int id, final int x, final int y) {
		super(id, x, y, 24, "");
		height = 6;
	}

	@Override
	public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
		if(!visible) {
			return;
		}

		mc.getTextureManager().bindTexture(tfButtonTextures);
		GL11.glColor3f(1F, 1F, 1F);
		field_146123_n = new Rectangle(xPosition, yPosition, width, height).contains(mouseX, mouseY);
		drawTexturedModalRect(xPosition, yPosition, 78, 104 + getHoverState(field_146123_n) * height, width, height);
	}

	@Override
	public List<String> getHoverText() {
		if(enabled) {
			return Collections.singletonList(I18n.format("gui.display_station.transform"));
		}

		return Collections.emptyList();
	}
}
