package fiskfille.tf.client.render.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.model.ModelTankShell;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public final class RenderTankShell extends Render {
	public final ModelTankShell model = new ModelTankShell();
	public final ResourceLocation texture = new ResourceLocation(TransformersMod.MODID, "textures/models/weapons/tank_shell.png");

	@Override
	public void doRender(final Entity entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
		bindEntityTexture(entity);
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		GL11.glRotatef(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks + 180, 0, 1, 0);
		GL11.glRotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 1, 0, 0);
		GL11.glScalef(1.75F, 1.75F, 1.75F);

		model.render();

		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(final Entity entity) {
		return texture;
	}
}
