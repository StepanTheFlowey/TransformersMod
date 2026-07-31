package fiskfille.tf.client.render.tileentity;

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

public class RenderTransmitter extends TileEntitySpecialRenderer {
	private final ModelTransmitter model = new ModelTransmitter();
	private final ResourceLocation texture = new ResourceLocation(TransformersMod.MODID, "textures/models/tiles/transmitter.png");
	private final ResourceLocation textureLights = new ResourceLocation(TransformersMod.MODID, "textures/models/tiles/transmitter_lights.png");

	public void render(TileEntityTransmitter transmitter, double x, double y, double z, float partialTicks) {
		final World world = transmitter.getWorldObj();
		int metadata = 0;

		if(world != null) {
			metadata = transmitter.getBlockMetadata();
		}

		if(metadata < 4) {
			GL11.glPushMatrix();
			GL11.glTranslated(x + 0.5D, y + 1.5D, z + 0.5D);
			GL11.glScalef(1, -1, -1);
			GL11.glRotatef(metadata * 90, 0, 1, 0);

			bindTexture(texture);
			model.setBreaking(false);
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
					OpenGlHelper.glBlendFunc(774, 768, 1, 0);
					bindTexture(new ResourceLocation(String.format("textures/blocks/destroy_stage_%s.png", progress)));
					GL11.glColor4f(1, 1, 1, 0.5F);
					GL11.glPushMatrix();
					GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
					GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
					GL11.glEnable(GL11.GL_ALPHA_TEST);
					model.setBreaking(true);
					model.render(transmitter, partialTicks);
					GL11.glDisable(GL11.GL_ALPHA_TEST);
					GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
					GL11.glEnable(GL11.GL_ALPHA_TEST);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					GL11.glPopMatrix();
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
	public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f) {
		render((TileEntityTransmitter) tileentity, d, d1, d2, f);
	}
}
