package fiskfille.tf.client.render.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.model.tileentity.ModelTransformiumSeed;
import fiskfille.tf.common.entity.EntityTransformiumSeed;
import fiskfille.tf.helper.TFRenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public final class RenderTransformiumSeedEntity extends Render {
	private final ModelTransformiumSeed model = new ModelTransformiumSeed();
	private final ResourceLocation texture = new ResourceLocation(TransformersMod.MODID, "textures/models/tiles/transformium_seed.png");
	private final ResourceLocation textureLights = new ResourceLocation(TransformersMod.MODID, "textures/models/tiles/transformium_seed_lights.png");

	public RenderTransformiumSeedEntity() {
		shadowSize = 0.5F;
	}

	public void doRender(final EntityTransformiumSeed seed, final double x, final double y, final double z) {
		bindEntityTexture(seed);
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		GL11.glRotatef(180, 1, 0, 0);

		model.render(seed);

		bindTexture(textureLights);
		GL11.glDisable(GL11.GL_LIGHTING);
		TFRenderHelper.setLighting(TFRenderHelper.LIGHTING_LUMINOUS);

		model.render(seed);

		TFRenderHelper.resetLighting();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(final Entity entity) {
		return texture;
	}

	@Override
	public void doRender(final Entity entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
		this.doRender((EntityTransformiumSeed) entity, x, y, z);
	}
}
