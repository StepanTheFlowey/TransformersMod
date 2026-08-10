package fiskfille.tf.client.render.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.model.tileentity.ModelEnergyPort;
import fiskfille.tf.common.tileentity.TileEntityEnergyPort;
import fiskfille.tf.helper.TFRenderHelper;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public final class RenderEnergyPort extends TileEntitySpecialRenderer {
	private final ModelEnergyPort model = new ModelEnergyPort();
	private final ResourceLocation texture = new ResourceLocation(TransformersMod.MODID, "textures/models/tiles/energy_port.png");
	private final ResourceLocation textureOff = new ResourceLocation(TransformersMod.MODID, "textures/models/tiles/energy_port_overlay_off.png");
	private final ResourceLocation textureOn = new ResourceLocation(TransformersMod.MODID, "textures/models/tiles/energy_port_overlay_on.png");

	private void render(final TileEntityEnergyPort tile, final double x, final double y, final double z) {
		bindTexture(texture);
		GL11.glPushMatrix();
		GL11.glTranslated(x + 0.5D, y + 0.5D, z + 0.5D);
		GL11.glScalef(1, -1, -1);
		adjustRotation(tile);

		model.render();

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		if(tile.getWorldObj() != null && tile.getEnergy() > 0) {
			bindTexture(textureOn);
			GL11.glDisable(GL11.GL_LIGHTING);
			TFRenderHelper.setLighting(TFRenderHelper.LIGHTING_LUMINOUS);

			model.render();

			TFRenderHelper.resetLighting();
			GL11.glEnable(GL11.GL_LIGHTING);
		}
		else {
			bindTexture(textureOff);
			model.render();
		}

		if(tile.getWorldObj() != null) {
			final int progress = TFRenderHelper.getBlockDestroyProgress(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord);

			if(progress >= 0) {
				bindTexture(new ResourceLocation(String.format("textures/blocks/destroy_stage_%s.png", progress)));
				GL11.glColor4f(1, 1, 1, 0.5F);

				GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT);
				GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
				OpenGlHelper.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR, GL11.GL_ONE, GL11.GL_ZERO);
				model.setBreaking(true);

				model.render();

				model.setBreaking(false);
				GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
				GL11.glPopAttrib();
			}
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	private void adjustRotation(final TileEntityEnergyPort tile) {
		int metadata = 0;

		if(tile.getWorldObj() != null) {
			metadata = tile.getBlockMetadata();
		}

		switch(ForgeDirection.getOrientation(metadata)) {
			case UP:
				GL11.glTranslatef(0, 1, 0);
				GL11.glRotatef(180, 0, 0, 1);
				break;

			case DOWN:
				GL11.glTranslatef(0, -1, 0);
				break;

			default:
				final int[] rotations = {0, 2, 3, 1};
				GL11.glRotatef(90 * rotations[metadata - 2], 0, 1, 0);
				GL11.glRotatef(90, 1, 0, 0);
				GL11.glTranslatef(0, -1, 0);
				break;
		}

		GL11.glTranslatef(0, -0.00103125F, 0);
	}

	@Override
	public void renderTileEntityAt(final TileEntity tileentity, final double x, final double y, final double z, final float partialTicks) {
		render((TileEntityEnergyPort) tileentity, x, y, z);
	}
}
