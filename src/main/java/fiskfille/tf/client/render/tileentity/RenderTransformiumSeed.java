package fiskfille.tf.client.render.tileentity;

import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.model.tileentity.ModelTransformiumSeed;
import fiskfille.tf.common.entity.EntityTransformiumSeed;
import fiskfille.tf.common.tileentity.TileEntityTransformiumSeed;
import fiskfille.tf.helper.TFRenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class RenderTransformiumSeed extends TileEntitySpecialRenderer {
	private final ModelTransformiumSeed model = new ModelTransformiumSeed();
	private final ResourceLocation texture = new ResourceLocation(TransformersMod.MODID, "textures/models/tiles/transformium_seed.png");
	private final ResourceLocation textureLights = new ResourceLocation(TransformersMod.MODID, "textures/models/tiles/transformium_seed_lights.png");

	public void renderModelAt(TileEntityTransformiumSeed seed, double x, double y, double z) {
		final EntityTransformiumSeed entity = new EntityTransformiumSeed(seed.getWorldObj());

		GL11.glPushMatrix();
		GL11.glTranslated(x + 0.5D, y + 0.5D, z + 0.5D);
		GL11.glScalef(1F, -1F, -1F);
		bindTexture(texture);
		model.render(entity);

		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		TFRenderHelper.setLighting(TFRenderHelper.LIGHTING_LUMINOUS);
		bindTexture(textureLights);
		model.render(entity);
		TFRenderHelper.resetLighting();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity tileentity, double x, double y, double z, float partialTicks) {
		renderModelAt((TileEntityTransformiumSeed) tileentity, x, y, z);
	}
}
