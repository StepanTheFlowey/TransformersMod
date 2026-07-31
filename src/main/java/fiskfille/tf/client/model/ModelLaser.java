package fiskfille.tf.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import org.lwjgl.opengl.GL11;

public class ModelLaser extends ModelBase {
	private final ModelRenderer laserInner;

	public ModelLaser() {
		textureWidth = 64;
		textureHeight = 16;

		final ModelRenderer laserOuter = new ModelRenderer(this, 17, 0);
		laserOuter.addBox(-1F, -1F, -4F, 2, 2, 8);

		laserInner = new ModelRenderer(this, 0, 0);
		laserInner.addBox(-0.5F, -0.5F, -3.5F, 1, 1, 7);
		laserInner.addChild(laserOuter);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		laserInner.render(f5);
	}
}
