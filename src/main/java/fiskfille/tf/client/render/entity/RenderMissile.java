package fiskfille.tf.client.render.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.model.ModelMissile;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderMissile extends Render {
	public final ModelMissile model = new ModelMissile();
	public final ResourceLocation texture = new ResourceLocation(TransformersMod.MODID, "textures/models/weapons/missile.png");

	@Override
	public void doRender(Entity entity, double x, double y, double z, float par8, float par9) {
		bindEntityTexture(entity);
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		GL11.glRotatef(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * par9 + 180, 0F, 1F, 0F);
		GL11.glRotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * par9, 1F, 0F, 0F);
		GL11.glScalef(0.5F, 0.5F, 0.5F);

		model.render();

		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return texture;
	}
}
