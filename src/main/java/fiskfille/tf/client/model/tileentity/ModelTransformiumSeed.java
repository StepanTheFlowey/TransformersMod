package fiskfille.tf.client.model.tileentity;

import fiskfille.tf.common.entity.EntityTransformiumSeed;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import org.lwjgl.opengl.GL11;

public class ModelTransformiumSeed extends ModelBase {
	private final ModelRenderer antenna;
	private final ModelRenderer shape1;
	private final ModelRenderer shape2;
	private final ModelRenderer shape3;
	private final ModelRenderer shape4;
	private final ModelRenderer shape5;
	private final ModelRenderer shape6;
	private final ModelRenderer shape7;
	private final ModelRenderer wingA;
	private final ModelRenderer wingB;
	private final ModelRenderer wingC;
	private final ModelRenderer wingD;

	public ModelTransformiumSeed() {
		textureWidth = 64;
		textureHeight = 32;

		antenna = new ModelRenderer(this, 14, 7);
		antenna.setRotationPoint(0F, 12.5F, 0F);
		antenna.addBox(-0.5F, 0F, -0.5F, 1, 4, 1, 0F);

		shape1 = new ModelRenderer(this, 0, 0);
		shape1.setRotationPoint(0F, 4F, 0F);
		shape1.addBox(-1F, 0F, -1F, 2, 1, 2, 0F);

		shape2 = new ModelRenderer(this, 0, 3);
		shape2.setRotationPoint(0F, 5F, 0F);
		shape2.addBox(-1.5F, 0F, -1.5F, 3, 6, 3, 0F);

		shape3 = new ModelRenderer(this, 0, 12);
		shape3.setRotationPoint(0F, 11F, 0F);
		shape3.addBox(-1F, 0F, -1F, 2, 5, 2, 0F);

		shape4 = new ModelRenderer(this, 0, 19);
		shape4.setRotationPoint(0F, 8F, 0F);
		shape4.addBox(-2F, 0F, -2F, 4, 1, 4, 0F);

		shape5 = new ModelRenderer(this, 0, 19);
		shape5.setRotationPoint(0F, 6F, 0F);
		shape5.addBox(-2F, 0F, -2F, 4, 1, 4, 0F);

		shape6 = new ModelRenderer(this, 8, 8);
		shape6.setRotationPoint(0F, 14F, 0F);
		shape6.addBox(-0.5F, -0.5F, -2F, 1, 2, 4, 0F);
		shape6.rotateAngleY = 1.5707963267948966F;

		shape7 = new ModelRenderer(this, 8, 8);
		shape7.setRotationPoint(0F, 14F, 0F);
		shape7.addBox(-0.5F, -0.5F, -2F, 1, 2, 4, 0F);

		wingA = new ModelRenderer(this, 12, 0);
		wingA.setRotationPoint(0F, 14F, -1.5F);
		wingA.addBox(-1F, -5F, -0.5F, 2, 6, 1, 0F);
		wingA.rotateAngleX = 0.06981317007977318F;

		wingB = new ModelRenderer(this, 18, 0);
		wingB.setRotationPoint(-1.5F, 14F, 0F);
		wingB.addBox(-0.5F, -5F, -1F, 1, 6, 2, 0F);
		wingB.rotateAngleZ = -0.06981317007977318F;

		wingC = new ModelRenderer(this, 24, 0);
		wingC.setRotationPoint(0F, 14F, 1.5F);
		wingC.addBox(-1F, -5F, -0.5F, 2, 6, 1, 0F);
		wingC.rotateAngleX = -0.06981317007977318F;

		wingD = new ModelRenderer(this, 30, 0);
		wingD.setRotationPoint(1.5F, 14F, 0F);
		wingD.addBox(-0.5F, -5F, -1F, 1, 6, 2, 0F);
		wingD.rotateAngleZ = 0.06981317007977318F;
	}

	public void render(EntityTransformiumSeed seed) {
		setRotationAngles(seed);
		GL11.glPushMatrix();
		GL11.glTranslatef(0F, -0.85F, 0F);
		GL11.glScalef(1.3F, 1.3F, 1.3F);

		shape1.render(0.0625F);
		shape2.render(0.0625F);
		shape5.render(0.0625F);
		antenna.render(0.0625F);
		shape6.render(0.0625F);
		shape7.render(0.0625F);
		wingA.render(0.0625F);
		wingB.render(0.0625F);
		shape4.render(0.0625F);
		shape3.render(0.0625F);
		wingC.render(0.0625F);
		wingD.render(0.0625F);

		GL11.glPopMatrix();
	}

	private void setRotation(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	public void setRotationAngles(EntityTransformiumSeed seed) {
		super.setRotationAngles(0, 0, 0, 0, 0, 0, seed);

		final float t = (float) (Math.min(seed.ticksExisted, 50)) / 50;
		antenna.rotationPointY = 12.5F + t * 3.5F;

		final float v = 0.06981317007977318F * (1F - t) + (float) Math.PI / 2 * t;
		wingA.rotateAngleX = v;
		wingB.rotateAngleZ = -v;
		wingC.rotateAngleX = -v;
		wingD.rotateAngleZ = v;
	}
}
