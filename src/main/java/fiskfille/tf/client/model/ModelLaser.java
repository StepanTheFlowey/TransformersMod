package fiskfille.tf.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelLaser extends ModelBase {
	private final ModelRenderer laserInner;

	public ModelLaser() {
		textureWidth = 64;
		textureHeight = 16;

		final ModelRenderer laserOuter = new ModelRenderer(this, 17, 0);
		laserOuter.addBox(-1, -1, -4, 2, 2, 8);

		laserInner = new ModelRenderer(this, 0, 0);
		laserInner.addBox(-0.5F, -0.5F, -3.5F, 1, 1, 7);
		laserInner.addChild(laserOuter);
	}

	@Override
	public void render(final Entity entity, final float f, final float f1, final float f2, final float f3, final float f4, final float f5) {
		laserInner.render(f5);
	}
}
