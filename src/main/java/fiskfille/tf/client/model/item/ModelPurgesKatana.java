package fiskfille.tf.client.model.item;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import org.lwjgl.opengl.GL11;

public class ModelPurgesKatana extends ModelBase {
	private final ModelRenderer base;

	public ModelPurgesKatana() {
		textureWidth = textureHeight = 128;

		base = new ModelRenderer(this, 20, 93);
		base.addBox(-1.5F, 0F, -0.5F, 3, 1, 1, 0F);
		base.rotateAngleY = 1.5707963267948966F;

		final ModelRenderer blade18 = new ModelRenderer(this, 5, 23);
		blade18.setRotationPoint(0F, 4.5F, 0F);
		blade18.addBox(-1F, 0F, -1F, 2, 1, 2, 0F);

		final ModelRenderer blade20 = new ModelRenderer(this, 9, 19);
		blade20.setRotationPoint(-0.6F, 1F, 0F);
		blade20.addBox(-3F, 0F, -1F, 3, 1, 2, 0F);
		setRotateAngle(blade20, 0F, 0F, 0.890117918517108F);

		final ModelRenderer blade10 = new ModelRenderer(this, 11, 96);
		blade10.setRotationPoint(0.5F, -4.5F, 0F);
		blade10.addBox(-1F, -1F, -0.5F, 2, 2, 2, 0F);

		final ModelRenderer blade16 = new ModelRenderer(this, 6, 15);
		blade16.mirror = true;
		blade16.setRotationPoint(-1F, -3F, 0F);
		blade16.addBox(0F, -3F, 0F, 1, 3, 1, 0F);
		setRotateAngle(blade16, 0F, 0F, 0.22689280275926282F);

		final ModelRenderer blade9 = new ModelRenderer(this, 11, 96);
		blade9.setRotationPoint(0.5F, -2F, 0F);
		blade9.addBox(-1F, -1F, -0.5F, 2, 2, 2, 0F);

		final ModelRenderer blade8 = new ModelRenderer(this, 11, 96);
		blade8.setRotationPoint(0.5F, 0.5F, 0F);
		blade8.addBox(-1F, -1F, -0.5F, 2, 2, 2, 0F);

		final ModelRenderer blade14 = new ModelRenderer(this, 1, 10);
		blade14.mirror = true;
		blade14.setRotationPoint(-1.2F, -7F, 0F);
		blade14.addBox(0F, 0F, 0F, 1, 8, 1, 0F);

		final ModelRenderer blade5 = new ModelRenderer(this, 20, 96);
		blade5.setRotationPoint(0.5F, -1.9F, 0.5F);
		blade5.addBox(-0.5F, -2F, -0.5F, 1, 2, 1, 0F);
		setRotateAngle(blade5, 0F, -0.7853981633974483F, 0F);

		final ModelRenderer blade19 = new ModelRenderer(this, 9, 19);
		blade19.mirror = true;
		blade19.setRotationPoint(0.6F, 1F, 0F);
		blade19.addBox(0F, 0F, -1F, 3, 1, 2, 0F);
		setRotateAngle(blade19, 0F, 0F, -0.890117918517108F);

		final ModelRenderer blade7 = new ModelRenderer(this, 1, 20);
		blade7.setRotationPoint(-1.5F, -6.9F, 0.5F);
		blade7.addBox(0F, -0.5F, -0.5F, 2, 1, 1, 0F);
		setRotateAngle(blade7, 0.7853981633974483F, 0F, 0F);

		final ModelRenderer blade12 = new ModelRenderer(this, 6, 10);
		blade12.setRotationPoint(1F, 0F, 0F);
		blade12.addBox(-1F, -3F, 0F, 1, 3, 1, 0F);

		final ModelRenderer blade11 = new ModelRenderer(this, 1, 10);
		blade11.setRotationPoint(1.2F, -7F, 0F);
		blade11.addBox(0F, 0F, 0F, 1, 8, 1, 0F);

		final ModelRenderer blade3 = new ModelRenderer(this, 20, 96);
		blade3.setRotationPoint(-1F, -3F, 0F);
		blade3.addBox(0F, -2F, 0F, 1, 2, 1, 0F);

		final ModelRenderer blade1 = new ModelRenderer(this, 29, 90);
		blade1.setRotationPoint(-0.5F, -1F, -0.5F);
		blade1.addBox(0F, -6F, 0F, 1, 7, 1, 0F);

		final ModelRenderer blade13 = new ModelRenderer(this, 6, 15);
		blade13.setRotationPoint(0F, -3F, 0F);
		blade13.addBox(-1F, -3F, 0F, 1, 3, 1, 0F);
		setRotateAngle(blade13, 0F, 0F, -0.22689280275926282F);

		final ModelRenderer blade2 = new ModelRenderer(this, 24, 96);
		blade2.setRotationPoint(1F, -6F, 0F);
		blade2.addBox(-1F, -3F, 0F, 1, 3, 1, 0F);

		final ModelRenderer blade4 = new ModelRenderer(this, 11, 96);
		blade4.setRotationPoint(0.5F, -1.5F, 0F);
		blade4.addBox(-1F, -1F, -0.5F, 2, 2, 2, 0F);

		final ModelRenderer blade15 = new ModelRenderer(this, 6, 10);
		blade15.mirror = true;
		blade15.setRotationPoint(1F, 0F, 0F);
		blade15.addBox(-1F, -3F, 0F, 1, 3, 1, 0F);

		final ModelRenderer balde6 = new ModelRenderer(this, 11, 96);
		balde6.setRotationPoint(-0.5F, -1.5F, 0F);
		balde6.addBox(-1F, -1F, -0.5F, 2, 2, 2, 0F);

		final ModelRenderer blade17 = new ModelRenderer(this, 0, 23);
		blade17.setRotationPoint(0F, 0F, 0F);
		blade17.addBox(-0.5F, 1F, -0.5F, 1, 4, 1, 0F);
		blade17.addChild(blade18);

		base.addChild(blade20);
		blade1.addChild(blade10);
		blade15.addChild(blade16);
		blade1.addChild(blade9);
		blade1.addChild(blade8);
		blade1.addChild(blade14);
		blade3.addChild(blade5);
		base.addChild(blade19);
		blade2.addChild(blade7);
		blade11.addChild(blade12);
		blade1.addChild(blade11);
		blade2.addChild(blade3);
		base.addChild(blade1);
		blade12.addChild(blade13);
		blade1.addChild(blade2);
		blade3.addChild(blade4);
		blade14.addChild(blade15);
		blade2.addChild(balde6);
		base.addChild(blade17);
	}

	public void render() {
		base.rotateAngleY = (float) Math.toRadians(180);
		base.rotationPointY = -4;
		GL11.glScalef(1F, 1F, 0.75F);
		base.render(0.0625F);
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	private void setRotateAngle(final ModelRenderer modelRenderer, final float x, final float y, final float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
