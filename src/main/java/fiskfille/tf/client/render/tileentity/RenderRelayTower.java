package fiskfille.tf.client.render.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.model.tileentity.ModelRelayTorch;
import fiskfille.tf.client.model.tileentity.ModelRelayTower;
import fiskfille.tf.common.tileentity.TileEntityRelayTorch;
import fiskfille.tf.common.tileentity.TileEntityRelayTower;
import fiskfille.tf.helper.TFRenderHelper;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public final class RenderRelayTower extends TileEntitySpecialRenderer {
	private final ModelRelayTower modelTower = new ModelRelayTower();
	private final ModelRelayTorch modelTorch = new ModelRelayTorch();

	public void render(final TileEntityRelayTower tower, final double x, final double y, final double z, final float partialTicks) {
		final World world = tower.getWorldObj();
		int metadata = 0;

		if(world != null) {
			metadata = tower.getBlockMetadata();
		}

		if(tower.isValid(metadata)) {
			GL11.glPushMatrix();
			GL11.glTranslated(x + 0.5D, y + 0.5D, z + 0.5D);
			GL11.glScalef(1, -1, -1);

			if(tower instanceof TileEntityRelayTorch && world != null) {
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
						GL11.glRotatef(90 * rotations[(metadata - 2) % 4], 0, 1, 0);
						GL11.glRotatef(90, 1, 0, 0);
						GL11.glTranslatef(0, -1, 0);
						break;
				}
			}
			else {
				GL11.glRotatef(metadata * 90, 0, 1, 0);
				GL11.glTranslatef(0, -1, 0);
			}

			bindTexture(new ResourceLocation(TransformersMod.MODID, String.format("textures/models/tiles/relay_%s.png", tower instanceof TileEntityRelayTorch ? "torch" : "tower")));
			final ModelRelayTower model = getModel(tower);

			model.render(tower, partialTicks);

			bindTexture(new ResourceLocation(TransformersMod.MODID, String.format("textures/models/tiles/relay_%s_lights.png", tower instanceof TileEntityRelayTorch ? "torch" : "tower")));
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			TFRenderHelper.setLighting(TFRenderHelper.LIGHTING_LUMINOUS);

			model.render(tower, partialTicks);

			TFRenderHelper.resetLighting();
			GL11.glEnable(GL11.GL_LIGHTING);

			if(world != null) {
				final int progress = TFRenderHelper.getBlockDestroyProgress(world, tower.xCoord, tower.yCoord, tower.zCoord);

				if(progress >= 0) {
					bindTexture(new ResourceLocation(String.format("textures/blocks/destroy_stage_%s.png", progress)));
					GL11.glColor4f(1, 1, 1, 0.5F);

					GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT);
					GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
					GL11.glEnable(GL11.GL_ALPHA_TEST);
					GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
					OpenGlHelper.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR, GL11.GL_ONE, GL11.GL_ZERO);
					model.setBreaking(true);

					model.render(tower, partialTicks);

					model.setBreaking(false);
					GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
					GL11.glPopAttrib();
				}
			}

			GL11.glDisable(GL11.GL_BLEND);
			GL11.glPopMatrix();

			if(world != null) {
				TFRenderHelper.renderEnergyTransmissions(tower, x, y, z);
			}
		}
	}

	private ModelRelayTower getModel(final TileEntityRelayTower tower) {
		if(tower instanceof TileEntityRelayTorch) {
			return modelTorch;
		}

		return modelTower;
	}

	@Override
	public void renderTileEntityAt(final TileEntity tileentity, final double x, final double y, final double z, final float partialTicks) {
		render((TileEntityRelayTower) tileentity, x, y, z, partialTicks);
	}
}
