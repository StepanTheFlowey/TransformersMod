package fiskfille.tf.client.model.item;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelFlamethrower extends ModelBase {
	private final ModelRenderer box01;
	private final ModelRenderer box02;
	private final ModelRenderer box03;
	private final ModelRenderer box04;
	private final ModelRenderer box05;
	private final ModelRenderer box06;
	private final ModelRenderer box07;
	private final ModelRenderer box08;
	private final ModelRenderer box09;
	private final ModelRenderer box10;
	private final ModelRenderer box11;
	private final ModelRenderer box12;
	private final ModelRenderer box13;
	private final ModelRenderer box14;
	private final ModelRenderer box15;
	private final ModelRenderer box16;
	private final ModelRenderer box17;

	public ModelFlamethrower() {
		textureWidth = textureHeight = 64;

		box01 = new ModelRenderer(this, 0, 7);
		box01.addBox(-3F, -3F, -1.5F, 6, 3, 3, 0F);
		box01.setRotationPoint(0F, 0F, 0F);
		box01.setTextureSize(512, 512);
		box01.mirror = true;
		setRotation(box01, 0F, 0F);

		box02 = new ModelRenderer(this, 0, 13);
		box02.addBox(3F, -1F, -1F, 6, 2, 2, 0F);
		box02.setRotationPoint(0F, 0F, 0F);
		box02.setTextureSize(512, 512);
		box02.mirror = true;
		setRotation(box02, 0F, 0F);

		box03 = new ModelRenderer(this, 0, 17);
		box03.addBox(-1F, -0.2F, -0.5F, 5, 2, 1, 0F);
		box03.setRotationPoint(0F, 0F, 0F);
		box03.setTextureSize(512, 512);
		box03.mirror = true;
		setRotation(box03, 0F, -0.19198622F);

		box04 = new ModelRenderer(this, 0, 20);
		box04.addBox(-2F, -3.5F, -0.5F, 12, 4, 1, 0F);
		box04.setRotationPoint(0F, 0F, 0F);
		box04.setTextureSize(512, 512);
		box04.mirror = true;
		setRotation(box04, 0F, 0F);

		box05 = new ModelRenderer(this, 0, 25);
		box05.addBox(6.35F, -3F, -1.5F, 3, 1, 3, 0F);
		box05.setRotationPoint(0F, 0F, 0F);
		box05.setTextureSize(512, 512);
		box05.mirror = true;
		setRotation(box05, 0F, 0F);

		box06 = new ModelRenderer(this, 0, 29);
		box06.addBox(-1F, -2F, -3F, 3, 1, 1, 0F);
		box06.setRotationPoint(0F, 0F, 0F);
		box06.setTextureSize(512, 512);
		box06.mirror = true;
		setRotation(box06, -0.59341195F, 0F);

		box07 = new ModelRenderer(this, 0, 29);
		box07.addBox(-1F, -2F, 2F, 3, 1, 1, 0F);
		box07.setRotationPoint(0F, 0F, 0F);
		box07.setTextureSize(512, 512);
		box07.mirror = true;
		setRotation(box07, 0.59341195F, 0F);

		box08 = new ModelRenderer(this, 0, 29);
		box08.addBox(-3F, -2F, 0F, 3, 1, 1, 0F);
		box08.setRotationPoint(0F, 0F, 0F);
		box08.setTextureSize(512, 512);
		box08.mirror = true;
		setRotation(box08, 0.59341195F, 0F);

		box09 = new ModelRenderer(this, 0, 29);
		box09.addBox(-3F, -2F, -1F, 3, 1, 1, 0F);
		box09.setRotationPoint(0F, 0F, 0F);
		box09.setTextureSize(512, 512);
		box09.mirror = true;
		setRotation(box09, -0.59341195F, 0F);

		box10 = new ModelRenderer(this, 0, 31);
		box10.addBox(9.35F, -3F, -1.5F, 1, 3, 3, 0F);
		box10.setRotationPoint(0F, 0F, 0F);
		box10.setTextureSize(512, 512);
		box10.mirror = true;
		setRotation(box10, 0F, 0F);

		box11 = new ModelRenderer(this, 6, 29);
		box11.addBox(10.35F, -2.5F, -1F, 1, 2, 2, 0F);
		box11.setRotationPoint(0F, 0F, 0F);
		box11.setTextureSize(512, 512);
		box11.mirror = true;
		setRotation(box11, 0F, 0F);

		box12 = new ModelRenderer(this, 8, 0);
		box12.addBox(1F, 1.8F, -1.5F, 3, 4, 3, 0F);
		box12.setRotationPoint(0F, 0F, 0F);
		box12.setTextureSize(512, 512);
		box12.mirror = true;
		setRotation(box12, 0F, -0.19198622F);

		box13 = new ModelRenderer(this, 12, 25);
		box13.addBox(-1F, 0F, -0.5F, 2, 4, 1, 0F);
		box13.setRotationPoint(0F, 0F, 0F);
		box13.setTextureSize(512, 512);
		box13.mirror = true;
		setRotation(box13, 0F, 0.83775804F);

		box14 = new ModelRenderer(this, 0, 2);
		box14.addBox(-4F, -4F, -1F, 2, 3, 2, 0F);
		box14.setRotationPoint(0F, 0F, 0F);
		box14.setTextureSize(512, 512);
		box14.mirror = true;
		setRotation(box14, 0F, 0F);

		box15 = new ModelRenderer(this, 14, 15);
		box15.addBox(8.5F, -4F, -1F, 1, 2, 2, 0F);
		box15.setRotationPoint(0F, 0F, 0F);
		box15.setTextureSize(512, 512);
		box15.mirror = true;
		setRotation(box15, 0F, 0F);

		box16 = new ModelRenderer(this, 0, 0);
		box16.addBox(10F, -0.5F, -0.5F, 3, 1, 1, 0F);
		box16.setRotationPoint(0F, 0F, 0F);
		box16.setTextureSize(512, 512);
		box16.mirror = true;
		setRotation(box16, 0F, 0F);

		box17 = new ModelRenderer(this, 17, 0);
		box17.addBox(12F, -0.5F, -0.5F, 1, 1, 1, 0F);
		box17.setRotationPoint(0F, 0F, 0F);
		box17.setTextureSize(512, 512);
		box17.mirror = true;
		setRotation(box17, 0F, -0.05235988F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);

		box01.render(f5);
		box02.render(f5);
		box03.render(f5);
		box04.render(f5);
		box05.render(f5);
		box06.render(f5);
		box07.render(f5);
		box08.render(f5);
		box09.render(f5);
		box10.render(f5);
		box11.render(f5);
		box12.render(f5);
		box13.render(f5);
		box14.render(f5);
		box15.render(f5);
		box16.render(f5);
		box17.render(f5);
	}

	public void render() {
		final float f5 = 0.0625F;

		box01.render(f5);
		box02.render(f5);
		box03.render(f5);
		box04.render(f5);
		box05.render(f5);
		box06.render(f5);
		box07.render(f5);
		box08.render(f5);
		box09.render(f5);
		box10.render(f5);
		box11.render(f5);
		box12.render(f5);
		box13.render(f5);
		box14.render(f5);
		box15.render(f5);
		box16.render(f5);
		box17.render(f5);
	}

	private void setRotation(ModelRenderer model, float y, float z) {
		model.rotateAngleX = 0;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}
}
