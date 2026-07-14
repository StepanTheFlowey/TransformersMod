package fiskfille.tf.client.displayable;

import fiskfille.tf.common.tick.ClientTickHandler;
import fiskfille.tf.common.tileentity.TileEntityTransformiumSeed;
import fiskfille.tf.helper.TFRenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

public class DisplayableTransformiumSeed extends Displayable {
	private static final TileEntityTransformiumSeed tileentity = new TileEntityTransformiumSeed();

	@Override
	public void render(ItemStack itemstack) {
		TFRenderHelper.renderTag(StatCollector.translateToLocalFormatted("tile.display_pedestal.amount", itemstack.stackSize), 0, 0.05F, 0);

		final float f1 = MathHelper.sin((mc.thePlayer.ticksExisted + ClientTickHandler.renderTick) / 15F) * 0.07F;
		GL11.glRotatef((mc.thePlayer.ticksExisted + ClientTickHandler.renderTick) * 0.75F, 0F, 1F, 0F);
		GL11.glTranslatef(0, 0.6F + f1, 0);
		GL11.glRotatef(180, 1, 0, 0);
		final float f = 0.5F;
		GL11.glScalef(f, f, f);

		TileEntityRendererDispatcher.instance.renderTileEntityAt(tileentity, -0.5F, -0.5F, -0.5F, 0F);
	}
}
