package fiskfille.tf.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelMissile extends ModelBase {
	public final ModelRenderer body1;
	public final ModelRenderer body2;
	public final ModelRenderer body3;
	public final ModelRenderer body4;
	public final ModelRenderer body5;
	public final ModelRenderer body6;
	public final ModelRenderer body7;
	public final ModelRenderer body8;
	public final ModelRenderer bodyLeft;
	public final ModelRenderer bodyLower;
	public final ModelRenderer bodyRight;
	public final ModelRenderer bodyUpper;
	public final ModelRenderer engine;
	public final ModelRenderer finLeft;
	public final ModelRenderer finLow;
	public final ModelRenderer finRight;
	public final ModelRenderer finUp;
	public final ModelRenderer missileBase;
	public final ModelRenderer noseLeft;
	public final ModelRenderer noseLower;
	public final ModelRenderer noseRight;
	public final ModelRenderer noseUpper;
	public final ModelRenderer rearfin1;
	public final ModelRenderer rearfin2;
	public final ModelRenderer rearfin3;
	public final ModelRenderer rearfin4;
	public final ModelRenderer rearfin5;
	public final ModelRenderer rearfin6;
	public final ModelRenderer rearfin7;
	public final ModelRenderer rearfin8;

	public ModelMissile() {
		textureWidth = 64;
		textureHeight = 32;

		noseUpper = new ModelRenderer(this, 0, 23);
		noseUpper.setRotationPoint(0F, -2F, 0F);
		noseUpper.addBox(-1F, 0F, -6F, 2, 1, 6, 0F);
		noseUpper.rotateAngleX = 0.15707963267948966F;

		rearfin7 = new ModelRenderer(this, 17, 0);
		rearfin7.setRotationPoint(0F, 0F, 4F);
		rearfin7.addBox(-0.5F, -2F, 2F, 1, 3, 4, 0F);
		rearfin7.rotateAngleX = 0.6981317007977318F;
		rearfin7.rotateAngleZ = 4.71238898038469F;

		rearfin8 = new ModelRenderer(this, 35, 0);
		rearfin8.addBox(-0.5F, -2.8F, -5.8F, 1, 1, 7, 0F);
		rearfin8.rotateAngleX = -0.5585053606381855F;

		bodyLower = new ModelRenderer(this, 21, 18);
		bodyLower.addBox(-1F, -2F, 0F, 2, 1, 11, 0F);
		bodyLower.rotateAngleZ = 3.141592653589793F;

		bodyRight = new ModelRenderer(this, 21, 18);
		bodyRight.addBox(-1F, -2F, 0F, 2, 1, 11, 0F);
		bodyRight.rotateAngleZ = -1.5707963267948966F;

		noseLower = new ModelRenderer(this, 0, 23);
		noseLower.setRotationPoint(0F, -2F, 0F);
		noseLower.addBox(-1F, 0F, -6F, 2, 1, 6, 0F);
		noseLower.rotateAngleX = 0.15707963267948966F;

		body6 = new ModelRenderer(this, 36, 23);
		body6.setRotationPoint(0F, -1.9F, 0F);
		body6.addBox(-0.5F, 0F, -5F, 1, 1, 5, 0F);
		body6.rotateAngleX = 0.15707963267948966F;

		rearfin5 = new ModelRenderer(this, 17, 0);
		rearfin5.setRotationPoint(0F, 0F, 4F);
		rearfin5.addBox(-0.5F, -2F, 2F, 1, 3, 4, 0F);
		rearfin5.rotateAngleX = 0.6981317007977318F;
		rearfin5.rotateAngleZ = 3.141592653589793F;

		body7 = new ModelRenderer(this, 21, 6);
		body7.addBox(0.9F, -0.5F, 0F, 1, 1, 11, 0F);
		body7.rotateAngleZ = -2.356194490192345F;

		body3 = new ModelRenderer(this, 21, 6);
		body3.addBox(0.9F, -0.5F, 0F, 1, 1, 11, 0F);
		body3.rotateAngleZ = -0.7853981633974483F;

		rearfin2 = new ModelRenderer(this, 35, 0);
		rearfin2.addBox(-0.5F, -2.8F, -5.8F, 1, 1, 7, 0F);
		rearfin2.rotateAngleX = -0.5585053606381855F;

		finRight = new ModelRenderer(this, 0, 17);
		finRight.setRotationPoint(0F, -2F, 0F);
		finRight.addBox(-0.5F, 0F, 0F, 1, 2, 4, 0F);
		finRight.rotateAngleX = 0.45378560551852565F;

		rearfin6 = new ModelRenderer(this, 35, 0);
		rearfin6.addBox(-0.5F, -2.8F, -5.8F, 1, 1, 7, 0F);
		rearfin6.rotateAngleX = -0.5585053606381855F;

		noseRight = new ModelRenderer(this, 0, 23);
		noseRight.setRotationPoint(0F, -2F, 0F);
		noseRight.addBox(-1F, 0F, -6F, 2, 1, 6, 0F);
		noseRight.rotateAngleX = 0.15707963267948966F;

		finUp = new ModelRenderer(this, 0, 17);
		finUp.setRotationPoint(0F, -2F, 0F);
		finUp.addBox(-0.5F, 0F, 0F, 1, 2, 4, 0F);
		finUp.rotateAngleX = 0.45378560551852565F;

		noseLeft = new ModelRenderer(this, 0, 23);
		noseLeft.setRotationPoint(0F, -2F, 0F);
		noseLeft.addBox(-1F, 0F, -6F, 2, 1, 6, 0F);
		noseLeft.rotateAngleX = 0.15707963267948966F;

		bodyUpper = new ModelRenderer(this, 21, 18);
		bodyUpper.addBox(-1F, -2F, 0F, 2, 1, 11, 0F);

		finLow = new ModelRenderer(this, 0, 17);
		finLow.setRotationPoint(0F, -2F, 0F);
		finLow.addBox(-0.5F, 0F, 0F, 1, 2, 4, 0F);
		finLow.rotateAngleX = 0.45378560551852565F;

		body8 = new ModelRenderer(this, 36, 23);
		body8.setRotationPoint(0F, -1.9F, 0F);
		body8.addBox(-0.5F, 0F, -5F, 1, 1, 5, 0F);
		body8.rotateAngleX = 0.15707963267948966F;

		missileBase = new ModelRenderer(this, 0, 13);
		missileBase.setRotationPoint(0F, 0F, -9F);
		missileBase.addBox(-1F, -1F, -5.9F, 2, 2, 17, 0F);

		rearfin4 = new ModelRenderer(this, 35, 0);
		rearfin4.addBox(-0.5F, -2.8F, -5.8F, 1, 1, 7, 0F);
		rearfin4.rotateAngleX = -0.5585053606381855F;

		finLeft = new ModelRenderer(this, 0, 17);
		finLeft.setRotationPoint(0F, -2F, 0F);
		finLeft.addBox(-0.5F, 0F, 0F, 1, 2, 4, 0F);
		finLeft.rotateAngleX = 0.45378560551852565F;

		bodyLeft = new ModelRenderer(this, 21, 18);
		bodyLeft.addBox(-1F, -2F, 0F, 2, 1, 11, 0F);
		bodyLeft.rotateAngleZ = 1.5707963267948966F;

		body5 = new ModelRenderer(this, 21, 6);
		body5.addBox(0.9F, -0.5F, 0F, 1, 1, 11, 0F);
		body5.rotateAngleZ = 2.356194490192345F;

		body2 = new ModelRenderer(this, 36, 23);
		body2.setRotationPoint(0F, -1.9F, 0F);
		body2.addBox(-0.5F, 0F, -5F, 1, 1, 5, 0F);
		body2.rotateAngleX = 0.15707963267948966F;

		rearfin1 = new ModelRenderer(this, 17, 0);
		rearfin1.setRotationPoint(0F, 0F, 4F);
		rearfin1.addBox(-0.5F, -2F, 2F, 1, 3, 4, 0F);
		rearfin1.rotateAngleX = 0.6981317007977318F;

		body1 = new ModelRenderer(this, 21, 6);
		body1.addBox(0.9F, -0.5F, 0F, 1, 1, 11, 0F);
		body1.rotateAngleZ = 0.7853981633974483F;

		body4 = new ModelRenderer(this, 36, 23);
		body4.setRotationPoint(0F, -1.9F, 0F);
		body4.addBox(-0.5F, 0F, -5F, 1, 1, 5, 0F);
		body4.rotateAngleX = 0.15707963267948966F;

		rearfin3 = new ModelRenderer(this, 17, 0);
		rearfin3.setRotationPoint(0F, 0F, 4F);
		rearfin3.addBox(-0.5F, -2F, 2F, 1, 3, 4, 0F);
		rearfin3.rotateAngleX = 0.6981317007977318F;
		rearfin3.rotateAngleZ = 1.5707963267948966F;

		engine = new ModelRenderer(this, 0, 0);
		engine.rotationPointZ = 11F;
		engine.addBox(-2F, -2F, 0F, 4, 4, 8, 0F);

		bodyUpper.addChild(noseUpper);
		engine.addChild(rearfin7);
		rearfin7.addChild(rearfin8);
		missileBase.addChild(bodyLower);
		missileBase.addChild(bodyRight);
		bodyLower.addChild(noseLower);
		body5.addChild(body6);
		engine.addChild(rearfin5);
		missileBase.addChild(body7);
		missileBase.addChild(body3);
		rearfin1.addChild(rearfin2);
		bodyRight.addChild(finRight);
		rearfin5.addChild(rearfin6);
		bodyRight.addChild(noseRight);
		bodyUpper.addChild(finUp);
		bodyLeft.addChild(noseLeft);
		missileBase.addChild(bodyUpper);
		bodyLower.addChild(finLow);
		body7.addChild(body8);
		rearfin3.addChild(rearfin4);
		bodyLeft.addChild(finLeft);
		missileBase.addChild(bodyLeft);
		missileBase.addChild(body5);
		body1.addChild(body2);
		engine.addChild(rearfin1);
		missileBase.addChild(body1);
		body3.addChild(body4);
		engine.addChild(rearfin3);
		missileBase.addChild(engine);
	}

	public void render() {
		missileBase.render(0.0625F);
	}
}
