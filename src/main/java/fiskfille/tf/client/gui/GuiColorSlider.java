package fiskfille.tf.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import org.lwjgl.opengl.GL11;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class GuiColorSlider extends GuiSliderBase {
	public final int sliderId;

	public GuiColorSlider(final int id, final int x, final int y, final int sliderId, final String s) {
		super(id, x, y, 150, 20, s);
		this.sliderId = sliderId;
	}

	@Override
	public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
		if(visible) {
			mc.getTextureManager().bindTexture(buttonTextures);
			GL11.glColor3f(1, 1, 1);
			field_146123_n = mouseX >= xPosition && mouseY >= yPosition && mouseX < xPosition + width && mouseY < yPosition + height;
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			final int k = getHoverState(field_146123_n);
			drawTexturedModalRect(xPosition, yPosition, 0, 46 + k * 20, width / 2, height);
			drawTexturedModalRect(xPosition + width / 2, yPosition, 200 - width / 2, 46 + k * 20, width / 2, height);

			final Color color = sliderId == 1 ? Color.GREEN : sliderId == 2 ? Color.BLUE : Color.RED;
			final Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.setColorOpaque_I(0);
			tessellator.addVertex(0, height - 2, zLevel);
			tessellator.setColorOpaque_I(color.getRGB());
			tessellator.addVertex(width - 2, height - 2, zLevel);
			tessellator.addVertex(width - 2, 0, zLevel);
			tessellator.setColorOpaque_I(0);
			tessellator.addVertex(0, 0, zLevel);

			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glTranslatef(xPosition + 1, yPosition + 1, 0);

			tessellator.draw();

			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glPopMatrix();

			mouseDragged(mc, mouseX, mouseY);

			int l = 14737632;
			if(packedFGColour != 0) {
				l = packedFGColour;
			}
			else if(!enabled) {
				l = 10526880;
			}
			else if(field_146123_n) {
				l = 16777120;
			}

			drawCenteredString(mc.fontRenderer, I18n.format("gui.display_station.color.amount", displayString, (int) (percentage * 100)), xPosition + width / 2, yPosition + (height - 8) / 2, l);
		}
	}
}
