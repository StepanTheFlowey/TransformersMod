package fiskfille.tf.client.render.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.render.shader.PortalShader;
import fiskfille.tf.common.block.TFBlocks;
import fiskfille.tf.common.data.tile.TileDataControlPanel;
import fiskfille.tf.common.tileentity.TileEntityGroundBridgeTeleporter;
import fiskfille.tf.config.TFConfig;
import fiskfille.tf.helper.TFRenderHelper;
import fiskfille.tf.helper.TFTileHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public final class RenderGroundBridgeTeleporter extends TileEntitySpecialRenderer {
	private final ResourceLocation texture = new ResourceLocation(TransformersMod.MODID, "textures/misc/portal_effect.png");
	private PortalShader shader;

	public RenderGroundBridgeTeleporter() {
		try {
			shader = new PortalShader();
		}
		catch(final Exception e) {
			e.printStackTrace();
		}
	}

	private void render(final TileEntityGroundBridgeTeleporter tileentity, final double x, final double y, final double z, final float partialTicks) {
		int metadata = 0;

		if(tileentity.getWorldObj() != null) {
			metadata = tileentity.getBlockMetadata();
		}

		if((metadata & 1) == 1) {
			GL11.glPushMatrix();
			GL11.glTranslated(x + 0.5D, y + 0.5D, z + 0.5D);
			GL11.glScalef(1, -1, -1);

			if(tileentity.controlPanel != null) {
				final TileDataControlPanel data = (TileDataControlPanel) TFTileHelper.getTileData(tileentity.controlPanel);

				if(data != null) {
					if(tileentity.isReturnPortal(metadata)) {
						GL11.glRotatef(90 * data.direction, 0, 1, 0);
					}
					else {
						GL11.glRotatef(90 * data.frameDirection, 0, 1, 0);
					}
				}
			}

			float f1 = 1 - (tileentity.lastUpdate > 0 ? tileentity.lastUpdate + partialTicks - 1 : 0) / 6;

			if(tileentity.lastUpdate == 0) {
				f1 = MathHelper.clamp_float(tileentity.ticks + partialTicks, 0, 6) / 6;
			}

			f1 = MathHelper.clamp_float(f1, 0, 1);

			GL11.glScalef(f1, f1, 1);
			GL11.glColor3f(1, 1, 1);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
			TFRenderHelper.setLighting(TFRenderHelper.LIGHTING_LUMINOUS);

			if(shader == null || TFConfig.oldPortalRender) {
				bindTexture(TextureMap.locationBlocksTexture);
				drawPortalOld(false);
				drawPortalOld(true);
			}
			else {
				bindTexture(texture);
				GL11.glPushAttrib(GL11.GL_TEXTURE_BIT);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
				shader.start();
				shader.setTime(tileentity.ticks + partialTicks);
				GL11.glRotatef((tileentity.ticks + partialTicks) * 2, 0, 0, 1);

				drawPortal(false);
				drawPortal(true);

				shader.stop();
				GL11.glPopAttrib();
			}

			TFRenderHelper.resetLighting();
			GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glPopMatrix();
		}
	}

	private void drawPortal(final boolean invert) {
		GL11.glPushMatrix();
		GL11.glRotatef(180, 0, 0, 1);
		GL11.glTranslatef(0, 0, 0.6F);
		GL11.glScalef(1.9F, 1.9F, 1);

		final Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawing(GL11.GL_TRIANGLES);

		final float corners = 45;
		final float angle = 360F / corners;
		float offset = 0;

		for(int i = 0; i < 3; ++i) {
			final float radius;
			float innerRadius = 0;
			float dent = 0;

			if(i == 0) {
				radius = 2;
				innerRadius = 1;
				dent = -1;
			}
			else if(i == 1) {
				radius = 1;
				innerRadius = 0.25F;
				dent = -0.5F;
			}
			else {
				radius = 0.25F;
			}

			for(float f = 0; f < corners; ++f) {
				final float f1 = f - 1;

				if(f == corners - 1) {
					f += 0.001F;
				}

				final Vec3 pos1 = Vec3.createVectorHelper(0, radius, 0);
				final Vec3 pos2 = Vec3.createVectorHelper(0, radius, 0);
				pos1.rotateAroundZ(angle * f * (float) Math.PI / 180F);
				pos2.rotateAroundZ(angle * f1 * (float) Math.PI / 180F);
				final Vec3 pos3 = Vec3.createVectorHelper((pos1.xCoord + pos2.xCoord) / 2, (pos1.yCoord + pos2.yCoord) / 2, (pos1.zCoord + pos2.zCoord) / 2);

				final Vec3 pos4 = Vec3.createVectorHelper(0, innerRadius, 0);
				final Vec3 pos5 = Vec3.createVectorHelper(0, innerRadius, 0);
				pos4.rotateAroundZ(angle * f * (float) Math.PI / 180F);
				pos5.rotateAroundZ(angle * f1 * (float) Math.PI / 180F);

				if(!invert) {
					tessellator.addVertex(pos3.xCoord, pos3.yCoord, offset);
					tessellator.addVertex(pos2.xCoord, pos2.yCoord, offset);
					tessellator.addVertex(pos5.xCoord, pos5.yCoord, dent + offset);
					tessellator.addVertex(pos1.xCoord, pos1.yCoord, offset);
					tessellator.addVertex(pos3.xCoord, pos3.yCoord, offset);
					tessellator.addVertex(pos4.xCoord, pos4.yCoord, dent + offset);
					tessellator.addVertex(pos3.xCoord, pos3.yCoord, offset);
					tessellator.addVertex(pos5.xCoord, pos5.yCoord, dent + offset);
					tessellator.addVertex(pos4.xCoord, pos4.yCoord, dent + offset);
				}
				else {
					tessellator.addVertex(pos5.xCoord, pos5.yCoord, dent + offset);
					tessellator.addVertex(pos2.xCoord, pos2.yCoord, offset);
					tessellator.addVertex(pos3.xCoord, pos3.yCoord, offset);
					tessellator.addVertex(pos1.xCoord, pos1.yCoord, offset);
					tessellator.addVertex(pos4.xCoord, pos4.yCoord, dent + offset);
					tessellator.addVertex(pos3.xCoord, pos3.yCoord, offset);
					tessellator.addVertex(pos3.xCoord, pos3.yCoord, offset);
					tessellator.addVertex(pos4.xCoord, pos4.yCoord, dent + offset);
					tessellator.addVertex(pos5.xCoord, pos5.yCoord, dent + offset);
				}
			}

			offset += dent;
		}

		tessellator.draw();
		GL11.glPopMatrix();
	}

	private void drawPortalOld(final boolean invert) {
		GL11.glPushMatrix();
		GL11.glRotatef(180, 0, 0, 1);
		GL11.glScalef(1.9425F, 1.9425F, 1);

		final Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawing(GL11.GL_TRIANGLES);

		final IIcon icon = TFBlocks.groundBridgeTeleporter.getIcon(0, 0);
		final float indent = 1;
		final float corners = 60;
		final float angle = 360F / corners;
		final float zoom = 46 * (16F / icon.getIconWidth());

		for(int j = 0; j <= corners; ++j) {
			final Vec3 pos1 = Vec3.createVectorHelper(0, 1.5, 0);
			final Vec3 pos2 = Vec3.createVectorHelper(0, 1.5, 0);
			pos1.rotateAroundZ(angle * j * (float) Math.PI / 180F);
			pos2.rotateAroundZ(angle * (j - 1) * (float) Math.PI / 180F);

			final float minX = -icon.getInterpolatedU(8);
			final float minY = icon.getInterpolatedV(8);

			Vec3 tex1 = Vec3.createVectorHelper(0.5F, 0.5F, 0);
			Vec3 tex2 = Vec3.createVectorHelper(0.5F, 0.5F, 0);
			tex1.rotateAroundZ((angle * j + 135) * (float) Math.PI / 180F);
			tex2.rotateAroundZ((angle * (j - 1) + 135) * (float) Math.PI / 180F);
			tex1.xCoord /= zoom;
			tex1.yCoord /= zoom;
			tex1.zCoord /= zoom;
			tex2.xCoord /= zoom;
			tex2.yCoord /= zoom;
			tex2.zCoord /= zoom;
			tex1 = tex1.addVector(minX, minY, 0);
			tex2 = tex2.addVector(minX, minY, 0);

			if(!invert) {
				tessellator.addVertexWithUV(pos1.xCoord, pos1.yCoord, 0, -tex1.xCoord, tex1.yCoord);
				tessellator.addVertexWithUV(pos2.xCoord, pos2.yCoord, 0, -tex2.xCoord, tex2.yCoord);
				tessellator.addVertexWithUV(0, 0, -indent, -minX, minY);
			}
			else {
				tessellator.addVertexWithUV(pos2.xCoord, pos2.yCoord, 0, -tex2.xCoord, tex2.yCoord);
				tessellator.addVertexWithUV(pos1.xCoord, pos1.yCoord, 0, -tex1.xCoord, tex1.yCoord);
				tessellator.addVertexWithUV(0, 0, -indent, -minX, minY);
			}
		}

		tessellator.draw();
		GL11.glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(final TileEntity tileentity, final double x, final double y, final double z, final float partialTicks) {
		render((TileEntityGroundBridgeTeleporter) tileentity, x, y, z, partialTicks);
	}
}
