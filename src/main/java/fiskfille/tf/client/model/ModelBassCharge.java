package fiskfille.tf.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelBassCharge extends ModelBase {
	private final ModelRenderer shape1;

	public ModelBassCharge() {
		textureWidth = 16;
		textureHeight = 8;

		final ModelRenderer shape8 = new ModelRenderer(this);
		shape8.addBox(-2.5F, -6, -0.5F, 5, 1, 1, 0);
		shape8.rotateAngleZ = (float) (Math.PI * 1.75D);

		final ModelRenderer shape7 = new ModelRenderer(this);
		shape7.addBox(-2.5F, -6, -0.5F, 5, 1, 1, 0);
		shape7.rotateAngleZ = (float) (Math.PI * 1.5D);

		final ModelRenderer shape6 = new ModelRenderer(this);
		shape6.addBox(-2.5F, -6, -0.5F, 5, 1, 1, 0);
		shape6.rotateAngleZ = (float) (Math.PI * 1.25D);

		final ModelRenderer shape5 = new ModelRenderer(this);
		shape5.addBox(-2.5F, -6, -0.5F, 5, 1, 1, 0);
		shape5.rotateAngleZ = (float) Math.PI;

		final ModelRenderer shape4 = new ModelRenderer(this);
		shape4.addBox(-2.5F, -6, -0.5F, 5, 1, 1, 0);
		shape4.rotateAngleZ = (float) (Math.PI * 0.75D);

		final ModelRenderer shape3 = new ModelRenderer(this);
		shape3.addBox(-2.5F, -6, -0.5F, 5, 1, 1, 0);
		shape3.rotateAngleZ = (float) (Math.PI * 0.5D);

		final ModelRenderer shape2 = new ModelRenderer(this);
		shape2.addBox(-2.5F, -6, -0.5F, 5, 1, 1, 0);
		shape2.rotateAngleZ = (float) (Math.PI * 0.25D);

		shape1 = new ModelRenderer(this);
		shape1.addBox(-2.5F, -6, -0.5F, 5, 1, 1, 0);

		shape1.addChild(shape2);
		shape1.addChild(shape3);
		shape1.addChild(shape4);
		shape1.addChild(shape5);
		shape1.addChild(shape6);
		shape1.addChild(shape7);
		shape1.addChild(shape8);
	}

	public void render() {
		shape1.render(0.0625F);
	}

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
