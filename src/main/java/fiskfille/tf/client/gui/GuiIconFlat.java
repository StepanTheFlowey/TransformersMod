package fiskfille.tf.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

public class GuiIconFlat extends GuiButtonFlat {
	public final IButtonRenderCallback callback;

	public GuiIconFlat(final int id, final int x, final int y, final IButtonRenderCallback renderCallback) {
		super(id, x, y, 20, "");
		callback = renderCallback;
		height = 20;
	}

	@Override
	public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
		if(visible) {
			mc.getTextureManager().bindTexture(GuiButtonFlat.tfButtonTextures);

			GL11.glColor3f(1F, 1F, 1F);
			field_146123_n = new Rectangle(xPosition, yPosition, width, height).contains(mouseX, mouseY);

			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			mouseDragged(mc, mouseX, mouseY);

			drawTexturedModalRect(xPosition, yPosition, 210, getHoverState(field_146123_n) * height, width, height);

			GL11.glPushMatrix();
			GL11.glTranslatef(xPosition, yPosition, 0);
			callback.render(this, mouseX, mouseY);
			GL11.glPopMatrix();
		}
	}

	@Override
	public List<String> getHoverText() {
		return callback.getHoverText(this);
	}

	public interface IButtonRenderCallback {
		void render(GuiButton button, int mouseX, int mouseY);

		List<String> getHoverText(GuiButton button);
	}
}
