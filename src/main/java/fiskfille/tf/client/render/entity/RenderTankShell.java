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
public class RenderTankShell extends Render {
	public final ModelTankShell model = new ModelTankShell();
	public final ResourceLocation texture = new ResourceLocation(TransformersMod.MODID, "textures/models/weapons/tank_shell.png");

	@Override
	public void doRender(Entity entity, double x, double y, double z, float par8, float par9) {
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		GL11.glRotatef(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * par9 + 180, 0F, 1F, 0F);
		GL11.glRotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * par9, 1F, 0F, 0F);
		GL11.glScalef(1.75F, 1.75F, 1.75F);
		bindEntityTexture(entity);

		model.render();

		GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return texture;
	}
}
