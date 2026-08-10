package fiskfille.tf.client.render.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.model.tileentity.ModelTransmitter;
import fiskfille.tf.common.tileentity.TileEntityTransmitter;
import fiskfille.tf.helper.TFRenderHelper;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public final class RenderTransmitter extends TileEntitySpecialRenderer {
	private final ModelTransmitter model = new ModelTransmitter();
	private final ResourceLocation texture = new ResourceLocation(TransformersMod.MODID, "textures/models/tiles/transmitter.png");
	private final ResourceLocation textureLights = new ResourceLocation(TransformersMod.MODID, "textures/models/tiles/transmitter_lights.png");

	private void render(final TileEntityTransmitter transmitter, final double x, final double y, final double z, final float partialTicks) {
		final World world = transmitter.getWorldObj();
		int metadata = 0;

		if(world != null) {
			metadata = transmitter.getBlockMetadata();
		}

		if(metadata < 4) {
			bindTexture(texture);
			GL11.glPushMatrix();
			GL11.glTranslated(x + 0.5D, y + 1.5D, z + 0.5D);
			GL11.glScalef(1, -1, -1);
			GL11.glRotatef(metadata * 90, 0, 1, 0);

			model.render(transmitter, partialTicks);

			bindTexture(textureLights);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			TFRenderHelper.setLighting(TFRenderHelper.LIGHTING_LUMINOUS);

			model.render(transmitter, partialTicks);

			TFRenderHelper.resetLighting();
			GL11.glEnable(GL11.GL_LIGHTING);

			if(world != null) {
				final int progress = TFRenderHelper.getBlockDestroyProgress(world, transmitter.xCoord, transmitter.yCoord, transmitter.zCoord);

				if(progress >= 0) {
					bindTexture(new ResourceLocation(String.format("textures/blocks/destroy_stage_%s.png", progress)));
					GL11.glColor4f(1, 1, 1, 0.5F);

					GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT);
					GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
					GL11.glEnable(GL11.GL_ALPHA_TEST);
					GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
					OpenGlHelper.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR, GL11.GL_ONE, GL11.GL_ZERO);
					model.setBreaking(true);

					model.render(transmitter, partialTicks);

					model.setBreaking(false);
					GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
					GL11.glPopAttrib();
				}
			}

			GL11.glDisable(GL11.GL_BLEND);
			GL11.glPopMatrix();

			if(world != null) {
				TFRenderHelper.renderEnergyTransmissions(transmitter, x, y, z);
			}
		}
	}

	@Override
	public void renderTileEntityAt(final TileEntity tileentity, final double x, final double y, final double z, final float partialTicks) {
		render((TileEntityTransmitter) tileentity, x, y, z, partialTicks);
	}
}
