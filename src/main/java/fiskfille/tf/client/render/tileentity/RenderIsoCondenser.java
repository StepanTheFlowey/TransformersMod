package fiskfille.tf.client.render.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.model.tileentity.ModelIsoCondenser;
import fiskfille.tf.common.tileentity.TileEntityIsoCondenser;
import fiskfille.tf.helper.TFRenderHelper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

import java.util.Map;

@SideOnly(Side.CLIENT)
public final class RenderIsoCondenser extends TileEntitySpecialRenderer {
	private final ModelIsoCondenser model = new ModelIsoCondenser();
	private final ResourceLocation texture = new ResourceLocation(TransformersMod.MODID, "textures/models/tiles/isotopic_condenser.png");
	private final ResourceLocation textureLights = new ResourceLocation(TransformersMod.MODID, "textures/models/tiles/isotopic_condenser_lights.png");

	private void render(final TileEntityIsoCondenser tile, final double x, final double y, final double z) {
		bindTexture(texture);
		GL11.glPushMatrix();
		GL11.glTranslated(x + 0.5D, y + 1.5D, z + 0.5D);
		GL11.glScalef(1, -1, -1);

		model.render(tile, false);

		bindTexture(textureLights);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		TFRenderHelper.setLighting(TFRenderHelper.LIGHTING_LUMINOUS);

		model.render(tile, true);

		TFRenderHelper.resetLighting();
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

				model.render(tile, false);

				model.setBreaking(false);
				GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
				GL11.glPopAttrib();
			}
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();

		GL11.glPushMatrix();
		GL11.glTranslated(x + 0.5D, y + 0.5D, z + 0.5D);

		for(final Map.Entry<ForgeDirection, Block> e : tile.providers.entrySet()) {
			final ForgeDirection dir = e.getKey();
			final Block block = e.getValue();
			final float f = tile.animationTimer.get(dir) == null ? 0 : tile.animationTimer.get(dir);

			if(!block.isOpaqueCube() && f == 1) {
				block.setBlockBoundsBasedOnState(tile.getWorldObj(), tile.xCoord + dir.offsetX, tile.yCoord, tile.zCoord + dir.offsetZ);

				final Vec3 src = Vec3.createVectorHelper(-dir.offsetX * 0.5F, 0.2F, -dir.offsetZ * 0.5F);
				final Vec3 dst = Vec3.createVectorHelper(-dir.offsetX + 0.5F - (block.getBlockBoundsMinX() + block.getBlockBoundsMaxX()) / 2, 0.5F - (block.getBlockBoundsMinY() + block.getBlockBoundsMaxY()) / 2, -dir.offsetZ + 0.5F - (block.getBlockBoundsMinZ() + block.getBlockBoundsMaxZ()) / 2);
				TFRenderHelper.renderEnergyStatic(src, dst, 1F / 64, 0.5F, 8, e.getKey().hashCode());
			}
		}

		GL11.glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(final TileEntity tileentity, final double x, final double y, final double z, final float partialTicks) {
		render((TileEntityIsoCondenser) tileentity, x, y, z);
	}
}
