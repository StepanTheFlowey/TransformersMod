package fiskfille.tf.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiSliderBase extends GuiButton {
	public float percentage;
	public boolean dragging;

	public GuiSliderBase(final int id, final int x, final int y, final int width, final int height, final String s) {
		super(id, x, y, width, height, s);
		percentage = 1F;
	}

	@Override
	public int getHoverState(final boolean b) {
		return 0;
	}

	@Override
	protected void mouseDragged(final Minecraft mc, final int mouseX, final int mouseY) {
		if(!visible) {
			return;
		}

		if(dragging) {
			percentage = (float) (mouseX - (xPosition + 4)) / (float) (width - 8);

			if(percentage < 0) {
				percentage = 0;
			}

			if(percentage > 1) {
				percentage = 1;
			}
		}

		GL11.glColor3f(1F, 1F, 1F);
		drawTexturedModalRect(xPosition + (int) (percentage * (width - 8)), yPosition, 0, 66, 4, 20);
		drawTexturedModalRect(xPosition + (int) (percentage * (width - 8)) + 4, yPosition, 196, 66, 4, 20);
	}

	@Override
	public boolean mousePressed(final Minecraft mc, final int mouseX, final int mouseY) {
		if(super.mousePressed(mc, mouseX, mouseY)) {
			percentage = (float) (mouseX - (xPosition + 4)) / (float) (width - 8);

			if(percentage < 0) {
				percentage = 0;
			}

			if(percentage > 1) {
				percentage = 1;
			}

			dragging = true;
			return true;
		}

		return false;
	}

	@Override
	public void mouseReleased(final int mouseX, final int mouseY) {
		dragging = false;
	}
}
