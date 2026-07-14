package fiskfille.tf.asm;

import fiskfille.tf.helper.TFHelper;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

import static fiskfille.tf.TransformersMod.mc;

public class ASMHooksClient {
	public static int getBrightnessForRender(Entity entity) {
		if(entity instanceof EntityPlayer) {
			final float scale = TFHelper.getHeight((EntityPlayer) entity) / 1.8F;

			return MathHelper.floor_double(entity.boundingBox.minY + scale * 1.62F);
		}

		final double d0 = (entity.boundingBox.maxY - entity.boundingBox.minY) * 0.66D;
		return MathHelper.floor_double(entity.posY - entity.yOffset + d0);
	}

	public static void applyPlayerRenderTranslation(RenderPlayer render, AbstractClientPlayer player, double x, double y, double z) {
		if(player == mc.thePlayer) {
			GL11.glTranslatef(0F, player.yOffset - 1.62F, 0F);
		}

		GL11.glTranslated(x, y, z);
	}

	public static double getScaledSneakOffset(EntityPlayer player) {
		return 0.125D;
	}

	public static void renderSlotPre(GuiContainer gui, Slot slot) {}

	public static void renderSlotPost(GuiContainer gui, Slot slot) {
		OpenGlHelper.glBlendFunc(770, 771, 1, 0);
	}
}
