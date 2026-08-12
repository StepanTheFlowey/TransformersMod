package fiskfille.tf.asm;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.helper.TFHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public final class ASMHooksClient {
	public static int getBrightnessForRender(final Entity entity) {
		if(entity instanceof EntityPlayer) {
			final float scale = TFHelper.getHeight((EntityPlayer) entity) / 1.8F;
			return MathHelper.floor_double(entity.boundingBox.minY + scale * 1.62F);
		}

		final double offset = (entity.boundingBox.maxY - entity.boundingBox.minY) * 0.66D;
		return MathHelper.floor_double(entity.posY - entity.yOffset + offset);
	}

	public static void applyPlayerRenderTranslation(final RenderPlayer render, final AbstractClientPlayer player, final double x, final double y, final double z) {
		if(player == Minecraft.getMinecraft().thePlayer) {
			GL11.glTranslatef(0, player.yOffset - 1.62F, 0);
		}

		GL11.glTranslated(x, y, z);
	}

	public static double getScaledSneakOffset(final EntityPlayer player) {
		return 0.125D;
	}

	public static void renderSlotPost(final GuiContainer gui, final Slot slot) {
		OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
	}
}
