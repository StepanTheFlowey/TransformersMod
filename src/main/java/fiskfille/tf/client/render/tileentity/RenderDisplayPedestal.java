package fiskfille.tf.client.render.tileentity;

import fiskfille.tf.TransformersAPI;
import fiskfille.tf.client.displayable.Displayable;
import fiskfille.tf.common.tileentity.TileEntityDisplayPedestal;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class RenderDisplayPedestal extends TileEntitySpecialRenderer {
	public void renderModelAt(TileEntityDisplayPedestal displayPillar, double x, double y, double z, float partialTicks) {
		final ItemStack displayItem = displayPillar.getDisplayItem();
		if(displayItem == null) {
			return;
		}

		final Displayable displayable = TransformersAPI.getDisplayableFor(displayItem.getItem());
		if(displayable == null) {
			return;
		}

		GL11.glPushMatrix();
		GL11.glTranslated(x + 0.5D, y + 1.5D, z + 0.5D);
		GL11.glScalef(1F, -1F, -1F);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);

		displayable.render(displayItem);

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		GL11.glColor3f(1F, 1F, 1F);
		GL11.glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTicks) {
		renderModelAt((TileEntityDisplayPedestal) tileentity, x, y, z, partialTicks);
	}
}
