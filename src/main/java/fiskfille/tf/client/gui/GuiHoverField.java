package fiskfille.tf.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiHoverField extends GuiButtonFlat {
	private final List<String> hoverText;

	public GuiHoverField(final int x, final int y, final int width, final int height, final List<String> text) {
		super(-1, x, y, width, "");
		this.height = height;
		hoverText = text;
	}

	@Override
	public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
		field_146123_n = new Rectangle(xPosition, yPosition, width, height).contains(mouseX, mouseY);
	}

	@Override
	public boolean mousePressed(final Minecraft mc, final int mouseX, final int mouseY) {
		return false;
	}

	@Override
	public List<String> getHoverText() {
		return hoverText;
	}

	public String colorFormat(final int color) {
		return String.format("&<0x%s>", color);
	}
}
