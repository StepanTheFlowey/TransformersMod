package fiskfille.tf.client.model.tileentity;

import fiskfille.tf.client.model.tools.MowzieModelBase;
import fiskfille.tf.client.model.tools.MowzieModelRenderer;
import org.lwjgl.opengl.GL11;

public class ModelEnergyPort extends MowzieModelBase {
	private final MowzieModelRenderer base;

	public ModelEnergyPort() {
		textureWidth = 64;
		textureHeight = 32;

		final MowzieModelRenderer shape7 = new MowzieModelRenderer(this, 0, 11);
		shape7.setRotationPoint(0F, -1.06F, 0F);
		shape7.addBox(-2.5F, 1F, 4.31F, 5, 4, 2, 0F);
		setRotateAngle(shape7, 0.4886921905584123F, -0.7853981633974483F, 0F);

		final MowzieModelRenderer shape4 = new MowzieModelRenderer(this, 0, 11);
		shape4.setRotationPoint(0F, -1.06F, 0F);
		shape4.addBox(-2.5F, 1F, 4.31F, 5, 4, 2, 0F);
		setRotateAngle(shape4, 0.4886921905584123F, 1.5707963267948966F, 0F);

		final MowzieModelRenderer shape2 = new MowzieModelRenderer(this, 0, 11);
		shape2.setRotationPoint(0F, -1.06F, 0F);
		shape2.addBox(-2.5F, 1F, 4.31F, 5, 4, 2, 0F);
		setRotateAngle(shape2, 0.4886921905584123F, 0F, 0F);

		final MowzieModelRenderer shape6 = new MowzieModelRenderer(this, 0, 11);
		shape6.setRotationPoint(0F, -1.06F, 0F);
		shape6.addBox(-2.5F, 1F, 4.31F, 5, 4, 2, 0F);
		setRotateAngle(shape6, 0.4886921905584123F, 3.141592653589793F, 0F);

		final MowzieModelRenderer shape9 = new MowzieModelRenderer(this, 0, 11);
		shape9.setRotationPoint(0F, -1.06F, 0F);
		shape9.addBox(-2.5F, 1F, 4.31F, 5, 4, 2, 0F);
		setRotateAngle(shape9, 0.4886921905584123F, -2.356194490192345F, 0F);

		final MowzieModelRenderer shape3 = new MowzieModelRenderer(this, 0, 11);
		shape3.setRotationPoint(0F, -1.06F, 0F);
		shape3.addBox(-2.5F, 1F, 4.31F, 5, 4, 2, 0F);
		setRotateAngle(shape3, 0.4886921905584123F, 0.7853981633974483F, 0F);

		final MowzieModelRenderer shape5 = new MowzieModelRenderer(this, 0, 11);
		shape5.setRotationPoint(0F, -1.06F, 0F);
		shape5.addBox(-2.5F, 1F, 4.31F, 5, 4, 2, 0F);
		setRotateAngle(shape5, 0.4886921905584123F, 2.356194490192345F, 0F);

		final MowzieModelRenderer shape8 = new MowzieModelRenderer(this, 0, 11);
		shape8.setRotationPoint(0F, -1.06F, 0F);
		shape8.addBox(-2.5F, 1F, 4.31F, 5, 4, 2, 0F);
		setRotateAngle(shape8, 0.4886921905584123F, -1.5707963267948966F, 0F);

		base = new MowzieModelRenderer(this, 0, 0);
		base.setRotationPoint(0F, 23.62F, 0F);
		base.addBox(-5F, -1F, -5F, 10, 1, 10, 0F);
		base.addChild(shape7);
		base.addChild(shape4);
		base.addChild(shape2);
		base.addChild(shape6);
		base.addChild(shape9);
		base.addChild(shape3);
		base.addChild(shape5);
		base.addChild(shape8);
	}

	public void render() {
		GL11.glPushMatrix();
		GL11.glTranslatef(base.offsetX, base.offsetY, base.offsetZ);
		GL11.glTranslatef(base.rotationPointX * 0.0625F, base.rotationPointY * 0.0625F, base.rotationPointZ * 0.0625F);
		GL11.glScaled(1.0103125D, 1.0103125D, 1.0103125D);
		GL11.glTranslatef(-base.offsetX, -base.offsetY, -base.offsetZ);
		GL11.glTranslatef(-base.rotationPointX * 0.0625F, -base.rotationPointY * 0.0625F, -base.rotationPointZ * 0.0625F);
		base.render(0.0625F);
		GL11.glPopMatrix();
	}
}
