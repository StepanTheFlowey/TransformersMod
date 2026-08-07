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
public class GuiButtonComponent extends GuiButtonFlat {
	public GuiButtonComponent(final int id, final int x, final int y) {
		super(id, x, y, 6, "");
		height = 18;
	}

	@Override
	public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
		if(!visible) {
			return;
		}

		mc.getTextureManager().bindTexture(tfButtonTextures);
		GL11.glColor3f(1F, 1F, 1F);
		field_146123_n = new Rectangle(xPosition, yPosition, width, height).contains(mouseX, mouseY);

		drawTexturedModalRect(xPosition, yPosition, 60 + getHoverState(field_146123_n) * width, 104, width, height);
	}

	@Override
	public List<String> getHoverText() {
		if(enabled) {
			return Collections.singletonList(I18n.format("gui.display_station.component"));
		}

		return Collections.emptyList();
	}
}
