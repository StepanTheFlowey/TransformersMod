package fiskfille.tf.client.model.item;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelBassBlaster extends ModelBase {
	private final ModelRenderer base;

	public ModelBassBlaster() {
		textureWidth = textureHeight = 64;

		final ModelRenderer vent3 = new ModelRenderer(this, 0, 8);
		vent3.setRotationPoint(-3, -1, 0.4F);
		vent3.addBox(-1.6F, 0, -0.3F, 2, 2, 1, 0);
		vent3.rotateAngleY = -0.20943951023931953F;

		final ModelRenderer vent2 = new ModelRenderer(this, 0, 8);
		vent2.setRotationPoint(-3, -1, 2.2F);
		vent2.addBox(-1.6F, 0, -0.3F, 2, 2, 1, 0);
		vent2.rotateAngleY = -0.20943951023931953F;

		final ModelRenderer vent1 = new ModelRenderer(this, 0, 8);
		vent1.setRotationPoint(-3, -1, -1.6F);
		vent1.addBox(-1.6F, 0, -0.3F, 2, 2, 1, 0);
		vent1.rotateAngleY = -0.20943951023931953F;

		final ModelRenderer lowerflap2 = new ModelRenderer(this, 0, 24);
		lowerflap2.setRotationPoint(0, -1, 0);
		lowerflap2.addBox(-1.5F, -2.5F, 0, 3, 4, 1, 0);
		lowerflap2.rotateAngleX = 0.7330382858376184F;

		final ModelRenderer lowerflap1 = new ModelRenderer(this, 0, 11);
		lowerflap1.setRotationPoint(-0.2F, 1.5F, -3);
		lowerflap1.addBox(-1, -1.2F, 0, 2, 2, 1, 0);
		lowerflap1.addChild(lowerflap2);
		setRotateAngle(lowerflap1, 0.8726646259971648F, 0, 15.707963267948966F);

		final ModelRenderer wepbody6 = new ModelRenderer(this, 0, 0);
		wepbody6.setRotationPoint(1.5F, -1.5F, -2);
		wepbody6.addBox(0F, 0, 0, 1, 3, 5, 0);

		final ModelRenderer wepbody5 = new ModelRenderer(this, 0, 0);
		wepbody5.setRotationPoint(-3, -1.5F, -2);
		wepbody5.addBox(0, 0, 0, 1, 3, 5, 0);

		final ModelRenderer wepbody4 = new ModelRenderer(this, 10, 8);
		wepbody4.setRotationPoint(0, 2.4F, -2);
		wepbody4.addBox(-3, -1, 0, 3, 1, 5, 0);
		setRotateAngle(wepbody4, 0, 0, 0.3141592653589793F);

		final ModelRenderer wepbody3 = new ModelRenderer(this, 10, 8);
		wepbody3.setRotationPoint(0, -2.4F, -2);
		wepbody3.addBox(-3, 0, 0, 3, 1, 5, 0);
		setRotateAngle(wepbody3, 0, 0, -0.3141592653589793F);

		final ModelRenderer wepbody2 = new ModelRenderer(this, 0, 18);
		wepbody2.setRotationPoint(-1.7F, -1.5F, -3);
		wepbody2.addBox(0, 0, 0, 3, 3, 3, 0);

		final ModelRenderer wepbody1 = new ModelRenderer(this, 0, 8);
		wepbody1.rotationPointY = -2;
		wepbody1.addBox(-0.5F, 0, -2, 2, 4, 6, 0);

		final ModelRenderer upperflap2 = new ModelRenderer(this, 0, 24);
		upperflap2.setRotationPoint(0, -1, 0);
		upperflap2.addBox(-1.5F, -2.5F, 0, 3, 4, 1, 0);
		upperflap2.rotateAngleX = 0.7330382858376184F;

		final ModelRenderer upperflap1 = new ModelRenderer(this, 0, 11);
		upperflap1.setRotationPoint(-0.2F, -1.5F, -3);
		upperflap1.addBox(-1, -1.2F, 0, 2, 2, 1, 0);
		upperflap1.addChild(upperflap2);
		upperflap1.rotateAngleX = 0.8726646259971648F;

		final ModelRenderer cable2 = new ModelRenderer(this, 12, 0);
		cable2.setRotationPoint(0.4F, -3, -1.5F);
		cable2.addBox(0, 0, 0, 1, 1, 5, 0);
		setRotateAngle(cable2, -0.03490658503988659F, -0.06981317007977318F, 0.15707963267948966F);

		final ModelRenderer cable1 = new ModelRenderer(this, 12, 0);
		cable1.setRotationPoint(0.4F, 3, -1.5F);
		cable1.addBox(0, -1, 0, 1, 1, 5, 0);
		setRotateAngle(cable1, 0.03490658503988659F, -0.06981317007977318F, -0.15707963267948966F);

		base = new ModelRenderer(this, 0, 0);
		base.setRotationPoint(0, 0, 0);
		base.addBox(-1.2F, -1, -3.1F, 2, 2, 1, 0);
		base.addChild(vent3);
		base.addChild(lowerflap1);
		base.addChild(vent2);
		base.addChild(wepbody2);
		base.addChild(cable1);
		base.addChild(wepbody3);
		base.addChild(wepbody1);
		base.addChild(wepbody4);
		base.addChild(upperflap1);
		base.addChild(vent1);
		base.addChild(cable2);
		base.addChild(wepbody5);
		base.addChild(wepbody6);
	}

	public void render() {
		base.render(0.0625F);
	}

	private void setRotateAngle(final ModelRenderer modelRenderer, final float x, final float y, final float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
