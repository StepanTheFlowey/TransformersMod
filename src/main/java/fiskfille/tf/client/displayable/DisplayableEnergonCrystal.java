package fiskfille.tf.client.displayable;

import fiskfille.tf.client.model.tileentity.ModelCrystal;
import fiskfille.tf.common.block.BlockEnergonCrystal;
import fiskfille.tf.common.tick.ClientTickHandler;
import fiskfille.tf.helper.TFRenderHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

public class DisplayableEnergonCrystal extends Displayable {
	private final ModelCrystal model = new ModelCrystal();

	@Override
	public void render(final ItemStack itemstack) {
		TFRenderHelper.renderTag(StatCollector.translateToLocalFormatted("tile.display_pedestal.amount", itemstack.stackSize), 0F, 0.1F, 0F);

		GL11.glRotatef((Minecraft.getMinecraft().thePlayer.ticksExisted + ClientTickHandler.renderTick) * 0.75F, 0F, 1F, 0F);
		final float f1 = MathHelper.sin((Minecraft.getMinecraft().thePlayer.ticksExisted + ClientTickHandler.renderTick) / 15F) * 0.07F;
		GL11.glTranslatef(0, -0.3F + f1, 0);
		GL11.glScalef(0.75F, 0.75F, 0.75F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		final float[] color = TFRenderHelper.hexToRGB(((BlockEnergonCrystal) Block.getBlockFromItem(itemstack.getItem())).getEnergonType().getColor());
		GL11.glColor4f(color[0], color[1], color[2], 0.5F);
		TFRenderHelper.setLighting(TFRenderHelper.LIGHTING_LUMINOUS);

		model.render();

		TFRenderHelper.resetLighting();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
}
