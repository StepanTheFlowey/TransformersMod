package fiskfille.tf.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.TransformersMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiVerticalSlider extends GuiSliderBase {
	private final ResourceLocation buttonTextures = new ResourceLocation(TransformersMod.MODID, "textures/gui/widgets.png");

	public GuiVerticalSlider(final int id, final int x, final int y, final int height) {
		super(id, x, y, 20, height, "");
	}

	@Override
	public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
		if(!visible) {
			return;
		}

		mc.getTextureManager().bindTexture(buttonTextures);
		GL11.glColor3f(1F, 1F, 1F);
		field_146123_n = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
		GL11.glEnable(GL11.GL_BLEND);
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		final int k = getHoverState(field_146123_n);
		drawTexturedModalRect(xPosition, yPosition, k * 20, 0, width, height / 2);
		drawTexturedModalRect(xPosition, yPosition + height / 2, k * 20, 200 - height / 2, width, height / 2);
		mouseDragged(mc, mouseX, mouseY);
	}

	@Override
	public boolean mousePressed(final Minecraft mc, final int mouseX, final int mouseY) {
		if(enabled && visible && mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height) {
			percentage = (float) (mouseY - (yPosition + 4)) / (float) (height - 8);

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
	protected void mouseDragged(final Minecraft mc, final int mouseX, final int mouseY) {
		if(!visible) {
			return;
		}

		if(dragging) {
			percentage = (float) (mouseY - (yPosition + 4)) / (float) (height - 8);

			if(percentage < 0) {
				percentage = 0;
			}

			if(percentage > 1) {
				percentage = 1;
			}
		}

		GL11.glColor3f(1F, 1F, 1F);
		drawTexturedModalRect(xPosition, yPosition + (int) (percentage * (height - 8)), 20, 0, 20, 4);
		drawTexturedModalRect(xPosition, yPosition + (int) (percentage * (height - 8)) + 4, 20, 196, 20, 4);
	}
}
