package fiskfille.tf.client.render.tileentity;

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

public class RenderDisplayStation extends TileEntitySpecialRenderer {
	private final ModelDisplayStation model = new ModelDisplayStation();
	private final ResourceLocation texture = new ResourceLocation(TransformersMod.modid, "textures/models/tiles/display_station.png");
	private final ResourceLocation textureLamp = new ResourceLocation(TransformersMod.modid, "textures/models/tiles/display_station_lamp.png");

	public void render(TileEntityDisplayStation tile, double x, double y, double z, float partialTicks) {
		int metadata = 0;

		if(tile.getWorldObj() != null) {
			metadata = tile.getBlockMetadata();
		}

		GL11.glPushMatrix();
		GL11.glTranslated(x + 0.5D, y + 1.5D, z + 0.5D);
		GL11.glScalef(1F, -1F, -1F);
		GL11.glRotatef(metadata * 90F, 0F, 1F, 0F);

		if(metadata < 4) {
			bindTexture(texture);
			model.setBreaking(false);
			model.render();

			bindTexture(textureLamp);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			TFRenderHelper.setLighting(TFRenderHelper.LIGHTING_LUMINOUS);

			model.render();

			TFRenderHelper.resetLighting();
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_BLEND);

			if(tile.getWorldObj() != null) {
				final int progress = TFRenderHelper.getBlockDestroyProgress(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord);

				if(progress >= 0) {
					bindTexture(new ResourceLocation(String.format("textures/blocks/destroy_stage_%s.png", progress)));
					GL11.glPushMatrix();
					OpenGlHelper.glBlendFunc(774, 768, 1, 0);
					GL11.glColor4f(1F, 1F, 1F, 0.5F);
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
					GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
					GL11.glEnable(GL11.GL_ALPHA_TEST);

					model.setBreaking(true);
					model.render();

					GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
					GL11.glDisable(GL11.GL_BLEND);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					GL11.glPopMatrix();
				}
			}

			try {
				final EntityPlayer entity = tile.fakePlayer;

				if(entity != null && entity.experience != -0.0085F) {
					entity.width = 0.6F;
					entity.height = 1.8F;
					entity.yOffset = 1.62F;
					entity.capabilities.isFlying = true;
					entity.rotationYawHead = 0;
					entity.experience = -0.0085F;
					entity.setInvisible(true);
					entity.setDead();
					entity.setLocationAndAngles(tile.xCoord + 0.5F, tile.yCoord, tile.zCoord + 0.5F, 0, 0);
				}

				if(entity != null) {
					GL11.glRotatef(180F, 1F, 0F, 0F);
					GL11.glTranslatef(0F, 0.0625F * 3F, 0F);
					RenderManager.instance.renderEntityWithPosYaw(entity, 0, 0, 0, 0, 1);
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}

		GL11.glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double d, double d1, double d2, float f) {
		render((TileEntityDisplayStation) tileentity, d, d1, d2, f);
	}
}
