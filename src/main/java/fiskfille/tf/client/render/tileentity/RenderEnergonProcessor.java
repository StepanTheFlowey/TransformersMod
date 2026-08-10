package fiskfille.tf.client.render.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.model.tileentity.ModelEnergonProcessor;
import fiskfille.tf.common.tileentity.TileEntityEnergonProcessor;
import fiskfille.tf.helper.TFRenderHelper;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public final class RenderEnergonProcessor extends TileEntitySpecialRenderer {
	private final ModelEnergonProcessor model = new ModelEnergonProcessor();
	private final ResourceLocation texture = new ResourceLocation(TransformersMod.MODID, "textures/models/tiles/energon_processor.png");
	private final ResourceLocation textureLights = new ResourceLocation(TransformersMod.MODID, "textures/models/tiles/energon_processor_lights.png");

	private void render(final TileEntityEnergonProcessor tileentity, final double x, final double y, final double z) {
		int metadata = 0;

		if(tileentity.getWorldObj() != null) {
			metadata = tileentity.getBlockMetadata();
		}

		bindTexture(texture);
		GL11.glPushMatrix();
		GL11.glTranslated(x + 0.5D, y + 1.5D, z + 0.5D);
		GL11.glScalef(1, -1, -1);
		GL11.glRotatef(metadata * 90, 0, 1, 0);

		model.render(tileentity);

		bindTexture(textureLights);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		TFRenderHelper.setLighting(TFRenderHelper.LIGHTING_LUMINOUS);

		model.render(tileentity);

		TFRenderHelper.resetLighting();
		GL11.glEnable(GL11.GL_LIGHTING);

		if(tileentity.getWorldObj() != null) {
			final int progress = TFRenderHelper.getBlockDestroyProgress(tileentity.getWorldObj(), tileentity.xCoord, tileentity.yCoord, tileentity.zCoord);

			if(progress >= 0) {
				bindTexture(new ResourceLocation(String.format("textures/blocks/destroy_stage_%s.png", progress)));
				GL11.glColor4f(1, 1, 1, 0.5F);

				GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT);
				GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
				OpenGlHelper.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR, GL11.GL_ONE, GL11.GL_ZERO);
				model.setBreaking(true);

				model.render(tileentity);

				model.setBreaking(false);
				GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
				GL11.glPopAttrib();
			}
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(final TileEntity tileentity, final double x, final double y, final double z, final float partialTicks) {
		render((TileEntityEnergonProcessor) tileentity, x, y, z);
	}
}
