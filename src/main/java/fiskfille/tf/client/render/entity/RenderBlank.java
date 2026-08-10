package fiskfille.tf.client.render.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public final class RenderBlank extends Render {
	public RenderBlank() {
		shadowSize = 0F;
	}

	@Override
	protected ResourceLocation getEntityTexture(final Entity entity) {
		return null;
	}

	@Override
	public void doRender(final Entity entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {}
}
