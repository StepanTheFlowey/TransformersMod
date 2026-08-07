package fiskfille.tf.client.render.tileentity;

import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.model.tileentity.ModelAssemblyTable;
import fiskfille.tf.common.tileentity.TileEntityAssemblyTable;
import fiskfille.tf.helper.TFRenderHelper;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderAssemblyTable extends TileEntitySpecialRenderer {
	private final ModelAssemblyTable model = new ModelAssemblyTable();
	private final ResourceLocation texture = new ResourceLocation(TransformersMod.MODID, "textures/models/tiles/assembly_table.png");
	private final ResourceLocation textureLights = new ResourceLocation(TransformersMod.MODID, "textures/models/tiles/assembly_table_lights.png");

	public void render(final TileEntityAssemblyTable tileentity, final double x, final double y, final double z, final float partialTicks) {
		GL11.glPushMatrix();
		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);
		GL11.glScalef(1F, -1F, -1F);
		bindTexture(texture);
		model.setBreaking(false);
		model.render();

		bindTexture(textureLights);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		TFRenderHelper.setLighting(TFRenderHelper.LIGHTING_LUMINOUS);
		model.render();
		TFRenderHelper.resetLighting();
		GL11.glEnable(GL11.GL_LIGHTING);

		if(tileentity.getWorldObj() != null) {
			final int progress = TFRenderHelper.getBlockDestroyProgress(tileentity.getWorldObj(), tileentity.xCoord, tileentity.yCoord, tileentity.zCoord);

			if(progress >= 0) {
				OpenGlHelper.glBlendFunc(774, 768, 1, 0);
				bindTexture(new ResourceLocation(String.format("textures/blocks/destroy_stage_%s.png", progress)));
				GL11.glColor4f(1, 1, 1, 0.5F);
				GL11.glPushMatrix();
				GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
				GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
				GL11.glEnable(GL11.GL_ALPHA_TEST);

				model.setBreaking(true);
				model.render();

				GL11.glDisable(GL11.GL_ALPHA_TEST);
				GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glPopMatrix();
			}
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(final TileEntity tileentity, final double d, final double d1, final double d2, final float f) {
		render((TileEntityAssemblyTable) tileentity, d, d1, d2, f);
	}
}
