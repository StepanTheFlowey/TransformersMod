package fiskfille.tf.client.render.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.model.tileentity.ModelDisplayStation;
import fiskfille.tf.common.tileentity.TileEntityDisplayStation;
import fiskfille.tf.helper.TFRenderHelper;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public final class RenderDisplayStation extends TileEntitySpecialRenderer {
	private final ModelDisplayStation model = new ModelDisplayStation();
	private final ResourceLocation texture = new ResourceLocation(TransformersMod.MODID, "textures/models/tiles/display_station.png");
	private final ResourceLocation textureLamp = new ResourceLocation(TransformersMod.MODID, "textures/models/tiles/display_station_lamp.png");

	private void render(final TileEntityDisplayStation tile, final double x, final double y, final double z) {
		int metadata = 0;

		if(tile.getWorldObj() != null) {
			metadata = tile.getBlockMetadata();
		}

		if(metadata < 4) {
			bindTexture(texture);
			GL11.glPushMatrix();
			GL11.glTranslated(x + 0.5D, y + 1.5D, z + 0.5D);
			GL11.glScalef(1, -1, -1);
			GL11.glRotatef(metadata * 90, 0, 1, 0);

			model.render();

			bindTexture(textureLamp);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			TFRenderHelper.setLighting(TFRenderHelper.LIGHTING_LUMINOUS);

			model.render();

			TFRenderHelper.resetLighting();
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL11.GL_LIGHTING);

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

			try {
				final EntityPlayer entity = tile.fakePlayer;

				if(entity != null) {
					if(entity.experience != -0.0085F) {
						entity.width = 0.6F;
						entity.height = 1.8F;
						entity.yOffset = 1.62F;
						entity.capabilities.isFlying = true;
						entity.rotationYawHead = 0;
						entity.experience = -0.0085F;
						entity.setInvisible(true);
						entity.setDead();
						entity.setLocationAndAngles(tile.xCoord + 0.5D, tile.yCoord, tile.zCoord + 0.5D, 0, 0);
					}

					GL11.glRotatef(180, 1, 0, 0);
					GL11.glTranslatef(0, 0.0625F * 3, 0);
					RenderManager.instance.renderEntityWithPosYaw(entity, 0, 0, 0, 0, 1);
				}
			}
			catch(final Exception e) {
				e.printStackTrace();
			}

			GL11.glPopMatrix();
		}
	}

	@Override
	public void renderTileEntityAt(final TileEntity tileentity, final double x, final double y, final double z, final float partialTicks) {
		render((TileEntityDisplayStation) tileentity, x, y, z);
	}
}
