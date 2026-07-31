package fiskfille.tf.client.model.item;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelBassBlaster extends ModelBase {
	private final ModelRenderer weaponbase;

	public ModelBassBlaster() {
		textureWidth = textureHeight = 64;

		final ModelRenderer vent3 = new ModelRenderer(this, 0, 8);
		vent3.setRotationPoint(-3F, -1F, 0.4F);
		vent3.addBox(-1.6F, 0F, -0.3F, 2, 2, 1, 0F);
		setRotateAngle(vent3, 0F, -0.20943951023931953F, 0F);

		final ModelRenderer vent2 = new ModelRenderer(this, 0, 8);
		vent2.setRotationPoint(-3F, -1F, 2.2F);
		vent2.addBox(-1.6F, 0F, -0.3F, 2, 2, 1, 0F);
		setRotateAngle(vent2, 0F, -0.20943951023931953F, 0F);

		final ModelRenderer vent1 = new ModelRenderer(this, 0, 8);
		vent1.setRotationPoint(-3F, -1F, -1.6F);
		vent1.addBox(-1.6F, 0F, -0.3F, 2, 2, 1, 0F);
		setRotateAngle(vent1, 0F, -0.20943951023931953F, 0F);

		final ModelRenderer lowerflap2 = new ModelRenderer(this, 0, 24);
		lowerflap2.setRotationPoint(0F, -1F, 0F);
		lowerflap2.addBox(-1.5F, -2.5F, 0F, 3, 4, 1, 0F);
		setRotateAngle(lowerflap2, 0.7330382858376184F, 0F, 0F);

		final ModelRenderer lowerflap1 = new ModelRenderer(this, 0, 11);
		lowerflap1.setRotationPoint(-0.2F, 1.5F, -3F);
		lowerflap1.addBox(-1F, -1.2F, 0F, 2, 2, 1, 0F);
		lowerflap1.addChild(lowerflap2);
		setRotateAngle(lowerflap1, 0.8726646259971648F, 0F, 15.707963267948966F);

		final ModelRenderer wepbody6 = new ModelRenderer(this, 0, 0);
		wepbody6.setRotationPoint(1.5F, -1.5F, -2F);
		wepbody6.addBox(0F, 0F, 0F, 1, 3, 5, 0F);

		final ModelRenderer wepbody5 = new ModelRenderer(this, 0, 0);
		wepbody5.setRotationPoint(-3F, -1.5F, -2F);
		wepbody5.addBox(0F, 0F, 0F, 1, 3, 5, 0F);

		final ModelRenderer wepbody4 = new ModelRenderer(this, 10, 8);
		wepbody4.setRotationPoint(0F, 2.4F, -2F);
		wepbody4.addBox(-3F, -1F, 0F, 3, 1, 5, 0F);
		setRotateAngle(wepbody4, 0F, 0F, 0.3141592653589793F);

		final ModelRenderer wepbody3 = new ModelRenderer(this, 10, 8);
		wepbody3.setRotationPoint(0F, -2.4F, -2F);
		wepbody3.addBox(-3F, 0F, 0F, 3, 1, 5, 0F);
		setRotateAngle(wepbody3, 0F, 0F, -0.3141592653589793F);

		final ModelRenderer wepbody2 = new ModelRenderer(this, 0, 18);
		wepbody2.setRotationPoint(-1.7F, -1.5F, -3F);
		wepbody2.addBox(0F, 0F, 0F, 3, 3, 3, 0F);

		final ModelRenderer wepbody1 = new ModelRenderer(this, 0, 8);
		wepbody1.setRotationPoint(0F, -2F, 0F);
		wepbody1.addBox(-0.5F, 0F, -2F, 2, 4, 6, 0F);

		final ModelRenderer upperflap2 = new ModelRenderer(this, 0, 24);
		upperflap2.setRotationPoint(0F, -1F, 0F);
		upperflap2.addBox(-1.5F, -2.5F, 0F, 3, 4, 1, 0F);
		setRotateAngle(upperflap2, 0.7330382858376184F, 0F, 0F);

		final ModelRenderer upperflap1 = new ModelRenderer(this, 0, 11);
		upperflap1.setRotationPoint(-0.2F, -1.5F, -3F);
		upperflap1.addBox(-1F, -1.2F, 0F, 2, 2, 1, 0F);
		upperflap1.addChild(upperflap2);
		setRotateAngle(upperflap1, 0.8726646259971648F, 0F, 0F);

		final ModelRenderer cable2 = new ModelRenderer(this, 12, 0);
		cable2.setRotationPoint(0.4F, -3F, -1.5F);
		cable2.addBox(0F, 0F, 0F, 1, 1, 5, 0F);
		setRotateAngle(cable2, -0.03490658503988659F, -0.06981317007977318F, 0.15707963267948966F);

		final ModelRenderer cable1 = new ModelRenderer(this, 12, 0);
		cable1.setRotationPoint(0.4F, 3F, -1.5F);
		cable1.addBox(0F, -1F, 0F, 1, 1, 5, 0F);
		setRotateAngle(cable1, 0.03490658503988659F, -0.06981317007977318F, -0.15707963267948966F);

		weaponbase = new ModelRenderer(this, 0, 0);
		weaponbase.setRotationPoint(0F, 0F, 0F);
		weaponbase.addBox(-1.2F, -1F, -3.1F, 2, 2, 1, 0F);
		weaponbase.addChild(vent3);
		weaponbase.addChild(lowerflap1);
		weaponbase.addChild(vent2);
		weaponbase.addChild(wepbody2);
		weaponbase.addChild(cable1);
		weaponbase.addChild(wepbody3);
		weaponbase.addChild(wepbody1);
		weaponbase.addChild(wepbody4);
		weaponbase.addChild(upperflap1);
		weaponbase.addChild(vent1);
		weaponbase.addChild(cable2);
		weaponbase.addChild(wepbody5);
		weaponbase.addChild(wepbody6);
	}

	public void render() {
		weaponbase.render(0.0625F);
	}

	private void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
