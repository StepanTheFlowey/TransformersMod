package fiskfille.tf.client.model.tileentity;

import fiskfille.tf.client.model.tools.ModelRendererBreakable;
import fiskfille.tf.client.model.tools.MowzieModelBase;
import fiskfille.tf.client.model.tools.MowzieModelRenderer;

public class ModelAssemblyTable extends MowzieModelBase {
	private final MowzieModelRenderer base;

	public ModelAssemblyTable() {
		textureWidth = textureHeight = 64;

		final MowzieModelRenderer top5 = new ModelRendererBreakable(this, 36, 26);
		top5.setRotationPoint(10.5F, 1F, -1F);
		top5.addBox(-1F, -2F, 0F, 1, 2, 11, 0F);
		setRotateAngle(top5, 0F, 0F, -0.24434609527920614F);

		final MowzieModelRenderer top16 = new ModelRendererBreakable(this, 0, 16);
		top16.setRotationPoint(0.5F, 3F, 0.5F);
		top16.addBox(0F, 0F, 0F, 2, 3, 2, 0F);
		setRotateAngle(top16, 0.06981317007977318F, 0F, 0F);

		final MowzieModelRenderer top4 = new ModelRendererBreakable(this, 30, 23);
		top4.setRotationPoint(-1F, 1F, -1.5F);
		top4.addBox(0F, -2F, 0F, 11, 2, 1, 0F);
		setRotateAngle(top4, -0.24434609527920614F, 0F, 0F);

		final MowzieModelRenderer base2 = new ModelRendererBreakable(this, 0, 28);
		base2.setRotationPoint(-1F, 1F, -1F);
		base2.addBox(0F, 0F, -1F, 3, 2, 2, 0F);
		setRotateAngle(base2, 0F, -0.7853981633974483F, 0F);

		final MowzieModelRenderer top20 = new ModelRendererBreakable(this, 0, 16);
		top20.setRotationPoint(0.5F, 3F, 0.5F);
		top20.addBox(0F, 0F, 0F, 2, 3, 2, 0F);
		setRotateAngle(top20, 0.06981317007977318F, 0F, 0F);

		base = new ModelRendererBreakable(this, 0, 47);
		base.setRotationPoint(-7F, 21F, -7F);
		base.addBox(0F, 0F, 0F, 14, 3, 14, 0F);

		final MowzieModelRenderer top15 = new ModelRendererBreakable(this, 0, 10);
		top15.setRotationPoint(10F, 1.2F, 14.6F);
		top15.addBox(0F, 0F, 0F, 8, 3, 3, 0F);
		setRotateAngle(top15, 0.3490658503988659F, 3.141592653589793F, 0F);

		final MowzieModelRenderer base4 = new ModelRendererBreakable(this, 0, 28);
		base4.setRotationPoint(-1F, 1F, 15F);
		base4.addBox(0F, 0F, -1F, 3, 2, 2, 0F);
		setRotateAngle(base4, 0F, 0.7853981633974483F, 0F);

		final MowzieModelRenderer top2 = new ModelRendererBreakable(this, 0, 16);
		top2.setRotationPoint(1.5F, -1F, 1.5F);
		top2.addBox(-0.5F, 0F, -0.5F, 10, 1, 10, 0F);

		final MowzieModelRenderer top19 = new ModelRendererBreakable(this, 0, 10);
		top19.setRotationPoint(-2.6F, 1.2F, 10F);
		top19.addBox(0F, 0F, 0F, 8, 3, 3, 0F);
		setRotateAngle(top19, 0.3490658503988659F, 1.5707963267948966F, 0F);

		final MowzieModelRenderer top10 = new ModelRendererBreakable(this, 0, 16);
		top10.setRotationPoint(3F, 3F, 0.5F);
		top10.addBox(0F, 0F, 0F, 2, 3, 2, 0F);
		setRotateAngle(top10, 0.06981317007977318F, 0F, 0F);

		final MowzieModelRenderer top7 = new ModelRendererBreakable(this, 0, 10);
		top7.setRotationPoint(2F, 1.2F, -2.6F);
		top7.addBox(0F, 0F, 0F, 8, 3, 3, 0F);
		setRotateAngle(top7, 0.3490658503988659F, 0F, 0F);

		final MowzieModelRenderer top12 = new ModelRendererBreakable(this, 0, 16);
		top12.setRotationPoint(0.5F, 3F, 0.5F);
		top12.addBox(0F, 0F, 0F, 2, 3, 2, 0F);
		setRotateAngle(top12, 0.06981317007977318F, 0F, 0F);

		final MowzieModelRenderer top1 = new ModelRendererBreakable(this, 0, 27);
		top1.setRotationPoint(1F, -8F, 1F);
		top1.addBox(0F, 0F, 0F, 12, 8, 12, 0F);

		final MowzieModelRenderer top18 = new ModelRendererBreakable(this, 0, 16);
		top18.setRotationPoint(3F, 3F, 0.5F);
		top18.addBox(0F, 0F, 0F, 2, 3, 2, 0F);
		setRotateAngle(top18, 0.06981317007977318F, 0F, 0F);

		final MowzieModelRenderer base9 = new ModelRendererBreakable(this, 0, 0);
		base9.setRotationPoint(11.5F, -7F, 1.5F);
		base9.addBox(0F, 0F, 0F, 1, 7, 1, 0F);

		final MowzieModelRenderer base8 = new ModelRendererBreakable(this, 0, 0);
		base8.setRotationPoint(1.5F, -7F, 11.5F);
		base8.addBox(0F, 0F, 0F, 1, 7, 1, 0F);

		final MowzieModelRenderer top6 = new ModelRendererBreakable(this, 30, 23);
		top6.setRotationPoint(-1F, 1F, 10.5F);
		top6.addBox(0F, -2F, -1F, 11, 2, 1, 0F);
		setRotateAngle(top6, 0.24434609527920614F, 0F, 0F);

		final MowzieModelRenderer base7 = new ModelRendererBreakable(this, 0, 0);
		base7.setRotationPoint(11.5F, -7F, 11.5F);
		base7.addBox(0F, 0F, 0F, 1, 7, 1, 0F);

		final MowzieModelRenderer top8 = new ModelRendererBreakable(this, 0, 16);
		top8.setRotationPoint(0.5F, 3F, 0.5F);
		top8.addBox(0F, 0F, 0F, 2, 3, 2, 0F);
		setRotateAngle(top8, 0.06981317007977318F, 0F, 0F);

		final MowzieModelRenderer top13 = new ModelRendererBreakable(this, 0, 16);
		top13.setRotationPoint(3F, 3F, 0.5F);
		top13.addBox(0F, 0F, 0F, 2, 3, 2, 0F);
		setRotateAngle(top13, 0.06981317007977318F, 0F, 0F);

		final MowzieModelRenderer top14 = new ModelRendererBreakable(this, 0, 16);
		top14.setRotationPoint(5.5F, 3F, 0.5F);
		top14.addBox(0F, 0F, 0F, 2, 3, 2, 0F);
		setRotateAngle(top14, 0.06981317007977318F, 0F, 0F);

		final MowzieModelRenderer top22 = new ModelRendererBreakable(this, 0, 16);
		top22.setRotationPoint(5.5F, 3F, 0.5F);
		top22.addBox(0F, 0F, 0F, 2, 3, 2, 0F);
		setRotateAngle(top22, 0.06981317007977318F, 0F, 0F);

		final MowzieModelRenderer top17 = new ModelRendererBreakable(this, 0, 16);
		top17.setRotationPoint(5.5F, 3F, 0.5F);
		top17.addBox(0F, 0F, 0F, 2, 3, 2, 0F);
		setRotateAngle(top17, 0.06981317007977318F, 0F, 0F);

		final MowzieModelRenderer top11 = new ModelRendererBreakable(this, 0, 10);
		top11.setRotationPoint(14.6F, 1.2F, 2F);
		top11.addBox(0F, 0F, 0F, 8, 3, 3, 0F);
		setRotateAngle(top11, 0.3490658503988659F, -1.5707963267948966F, 0F);

		final MowzieModelRenderer top9 = new ModelRendererBreakable(this, 0, 16);
		top9.setRotationPoint(5.5F, 3F, 0.5F);
		top9.addBox(0F, 0F, 0F, 2, 3, 2, 0F);
		setRotateAngle(top9, 0.06981317007977318F, 0F, 0F);

		final MowzieModelRenderer top21 = new ModelRendererBreakable(this, 0, 16);
		top21.setRotationPoint(3F, 3F, 0.5F);
		top21.addBox(0F, 0F, 0F, 2, 3, 2, 0F);
		setRotateAngle(top21, 0.06981317007977318F, 0F, 0F);

		final MowzieModelRenderer base6 = new ModelRendererBreakable(this, 0, 0);
		base6.setRotationPoint(1.5F, -7F, 1.5F);
		base6.addBox(0F, 0F, 0F, 1, 7, 1, 0F);

		final MowzieModelRenderer top3 = new ModelRendererBreakable(this, 36, 26);
		top3.setRotationPoint(-1.5F, 1F, -1F);
		top3.addBox(0F, -2F, 0F, 1, 2, 11, 0F);
		setRotateAngle(top3, 0F, 0F, 0.24434609527920614F);

		final MowzieModelRenderer base5 = new ModelRendererBreakable(this, 0, 28);
		base5.setRotationPoint(15F, 1F, 15F);
		base5.addBox(0F, 0F, -1F, 3, 2, 2, 0F);
		setRotateAngle(base5, 0F, 2.356194490192345F, 0F);

		final MowzieModelRenderer base3 = new ModelRendererBreakable(this, 0, 28);
		base3.setRotationPoint(15F, 1F, -1F);
		base3.addBox(0F, 0F, -1F, 3, 2, 2, 0F);
		setRotateAngle(base3, 0F, -2.356194490192345F, 0F);

		top2.addChild(top5);
		top15.addChild(top16);
		top2.addChild(top4);
		base.addChild(base2);
		top19.addChild(top20);
		top1.addChild(top15);
		base.addChild(base4);
		top1.addChild(top2);
		top1.addChild(top19);
		top7.addChild(top10);
		top1.addChild(top7);
		top11.addChild(top12);
		base.addChild(top1);
		top15.addChild(top18);
		base.addChild(base9);
		base.addChild(base8);
		top2.addChild(top6);
		base.addChild(base7);
		top7.addChild(top8);
		top11.addChild(top13);
		top11.addChild(top14);
		top19.addChild(top22);
		top15.addChild(top17);
		top1.addChild(top11);
		top7.addChild(top9);
		top19.addChild(top21);
		base.addChild(base6);
		top2.addChild(top3);
		base.addChild(base5);
		base.addChild(base3);

		setInitPose();
	}

	public void render() {
		setToInitPose();
		base.render(0.0625F);
	}
}
