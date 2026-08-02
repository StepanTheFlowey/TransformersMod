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
		box01.addBox(-3, -3, -1.5F, 6, 3, 3, 0);
		box01.setTextureSize(512, 512);
		box01.mirror = true;

		box02 = new ModelRenderer(this, 0, 13);
		box02.addBox(3, -1, -1, 6, 2, 2, 0);
		box02.setTextureSize(512, 512);
		box02.mirror = true;

		box03 = new ModelRenderer(this, 0, 17);
		box03.addBox(-1, -0.2F, -0.5F, 5, 2, 1, 0);
		box03.setTextureSize(512, 512);
		box03.mirror = true;
		box03.rotateAngleZ = -0.19198622F;

		box04 = new ModelRenderer(this, 0, 20);
		box04.addBox(-2, -3.5F, -0.5F, 12, 4, 1, 0);
		box04.setTextureSize(512, 512);
		box04.mirror = true;

		box05 = new ModelRenderer(this, 0, 25);
		box05.addBox(6.35F, -3F, -1.5F, 3, 1, 3, 0);
		box05.setTextureSize(512, 512);
		box05.mirror = true;

		box06 = new ModelRenderer(this, 0, 29);
		box06.addBox(-1, -2, -3, 3, 1, 1, 0);
		box06.setTextureSize(512, 512);
		box06.mirror = true;
		box06.rotateAngleY = -0.59341195F;

		box07 = new ModelRenderer(this, 0, 29);
		box07.addBox(-1, -2, 2, 3, 1, 1, 0);
		box07.setTextureSize(512, 512);
		box07.mirror = true;
		box07.rotateAngleY = 0.59341195F;

		box08 = new ModelRenderer(this, 0, 29);
		box08.addBox(-3, -2, 0, 3, 1, 1, 0);
		box08.setTextureSize(512, 512);
		box08.mirror = true;
		box08.rotateAngleY = 0.59341195F;

		box09 = new ModelRenderer(this, 0, 29);
		box09.addBox(-3, -2, -1, 3, 1, 1, 0);
		box09.setTextureSize(512, 512);
		box09.mirror = true;
		box09.rotateAngleY = -0.59341195F;

		box10 = new ModelRenderer(this, 0, 31);
		box10.addBox(9.35F, -3, -1.5F, 1, 3, 3, 0);
		box10.setTextureSize(512, 512);
		box10.mirror = true;

		box11 = new ModelRenderer(this, 6, 29);
		box11.addBox(10.35F, -2.5F, -1, 1, 2, 2, 0);
		box11.setTextureSize(512, 512);
		box11.mirror = true;

		box12 = new ModelRenderer(this, 8, 0);
		box12.addBox(1, 1.8F, -1.5F, 3, 4, 3, 0);
		box12.setTextureSize(512, 512);
		box12.mirror = true;
		box12.rotateAngleZ = -0.19198622F;

		box13 = new ModelRenderer(this, 12, 25);
		box13.addBox(-1, 0, -0.5F, 2, 4, 1, 0);
		box13.setTextureSize(512, 512);
		box13.mirror = true;
		box13.rotateAngleZ = 0.83775804F;

		box14 = new ModelRenderer(this, 0, 2);
		box14.addBox(-4, -4, -1, 2, 3, 2, 0);
		box14.setTextureSize(512, 512);
		box14.mirror = true;

		box15 = new ModelRenderer(this, 14, 15);
		box15.addBox(8.5F, -4, -1, 1, 2, 2, 0);
		box15.setTextureSize(512, 512);
		box15.mirror = true;

		box16 = new ModelRenderer(this, 0, 0);
		box16.addBox(10, -0.5F, -0.5F, 3, 1, 1, 0);
		box16.setTextureSize(512, 512);
		box16.mirror = true;

		box17 = new ModelRenderer(this, 17, 0);
		box17.addBox(12, -0.5F, -0.5F, 1, 1, 1, 0);
		box17.setTextureSize(512, 512);
		box17.mirror = true;
		box17.rotateAngleZ = -0.05235988F;
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float scale) {
		super.render(entity, f, f1, f2, f3, f4, scale);
		setRotationAngles(f, f1, f2, f3, f4, scale, entity);

		box01.render(scale);
		box02.render(scale);
		box03.render(scale);
		box04.render(scale);
		box05.render(scale);
		box06.render(scale);
		box07.render(scale);
		box08.render(scale);
		box09.render(scale);
		box10.render(scale);
		box11.render(scale);
		box12.render(scale);
		box13.render(scale);
		box14.render(scale);
		box15.render(scale);
		box16.render(scale);
		box17.render(scale);
	}

	public void render() {
		box01.render(0.0625F);
		box02.render(0.0625F);
		box03.render(0.0625F);
		box04.render(0.0625F);
		box05.render(0.0625F);
		box06.render(0.0625F);
		box07.render(0.0625F);
		box08.render(0.0625F);
		box09.render(0.0625F);
		box10.render(0.0625F);
		box11.render(0.0625F);
		box12.render(0.0625F);
		box13.render(0.0625F);
		box14.render(0.0625F);
		box15.render(0.0625F);
		box16.render(0.0625F);
		box17.render(0.0625F);
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
	}
}
