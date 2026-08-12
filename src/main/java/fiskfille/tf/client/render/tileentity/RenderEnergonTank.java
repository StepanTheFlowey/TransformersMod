package fiskfille.tf.client.render.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.common.tileentity.TileEntityEnergonTank;
import fiskfille.tf.helper.TFFluidRenderHelper;
import fiskfille.tf.helper.TFRenderHelper;
import fiskfille.tf.helper.TFTileHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public final class RenderEnergonTank extends TileEntitySpecialRenderer {
	private RenderBlocks renderBlocks = RenderBlocks.getInstance();

	private void render(final TileEntityEnergonTank tile, final double x, final double y, final double z) {
		final FluidStack stack = tile.data.getFluid();
		if(stack == null || stack.getFluid() == null || stack.amount <= 0) {
			return;
		}

		final int[] displayList = TFFluidRenderHelper.getFluidDisplayLists(renderBlocks, stack, tile.getWorldObj(), false);
		if(displayList == null) {
			return;
		}

		bindTexture(TextureMap.locationBlocksTexture);
		TFRenderHelper.glColor(stack.getFluid().getColor(stack));
		GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_LIGHTING_BIT);
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		float scaleY = 0.99F;
		float scaleOffset = 0;
		final World world = tile.getWorldObj();
		if(world != null) {
			final TileEntityEnergonTank tileBase = TFTileHelper.getTileBase(tile);
			boolean connectAbove = false;
			boolean connectBelow = false;

			if(tileBase == TFTileHelper.getTileBase(world.getTileEntity(tile.xCoord, tile.yCoord + 1, tile.zCoord))) {
				connectAbove = true;
			}

			if(tileBase == TFTileHelper.getTileBase(world.getTileEntity(tile.xCoord, tile.yCoord - 1, tile.zCoord))) {
				connectBelow = true;
			}

			if(connectAbove && connectBelow) {
				scaleY = 1;
			}
			else if(connectAbove) {
				final float diff = (1 - scaleY) / 2;
				scaleY += diff;
				scaleOffset = 0.5F;
			}
			else if(connectBelow) {
				final float diff = (1 - scaleY) / 2;
				scaleY += diff;
				scaleOffset = -0.5F;
			}
		}

		GL11.glTranslated(x, y, z);
		GL11.glTranslatef(0.5F, 0.5F + scaleOffset, 0.5F);
		GL11.glScalef(0.99F, scaleY, 0.99F);
		GL11.glTranslatef(-0.5F, -0.5F - scaleOffset, -0.5F);

		final int dl = (int) ((float) stack.amount / tile.data.getCapacity() * (TFFluidRenderHelper.DISPLAY_STAGES - 1));
		GL11.glCallList(displayList[MathHelper.clamp_int(dl, 0, TFFluidRenderHelper.DISPLAY_STAGES - 1)]);
		GL11.glPopMatrix();
		GL11.glPopAttrib();
	}

	@Override
	public void func_147496_a(final World world) {
		renderBlocks = new RenderBlocks(world);
	}

	@Override
	public void renderTileEntityAt(final TileEntity tileentity, final double x, final double y, final double z, final float partialTicks) {
		render((TileEntityEnergonTank) tileentity, x, y, z);
	}
}
