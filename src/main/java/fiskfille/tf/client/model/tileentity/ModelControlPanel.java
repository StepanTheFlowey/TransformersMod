package fiskfille.tf.client.model.tileentity;

import fiskfille.tf.client.model.tools.ModelRendererBreakable;
import fiskfille.tf.client.model.tools.MowzieModelBase;
import fiskfille.tf.client.model.tools.MowzieModelRenderer;
import fiskfille.tf.common.groundbridge.DataCore;
import fiskfille.tf.common.tileentity.TileEntityControlPanel;
import fiskfille.tf.helper.TFHelper;

public class ModelControlPanel extends MowzieModelBase {
	public final MowzieModelRenderer compass1;
	public final MowzieModelRenderer compass2;
	public final MowzieModelRenderer compass3;
	public final MowzieModelRenderer compass4;
	public final MowzieModelRenderer dimPanel1;
	public final MowzieModelRenderer dimPanel2;
	public final MowzieModelRenderer dimPanel3;
	public final MowzieModelRenderer dimPanel4;
	public final MowzieModelRenderer dimPanel6;
	public final MowzieModelRenderer dimPanel7;
	public final MowzieModelRenderer screen1;
	public final MowzieModelRenderer screen2;
	public final MowzieModelRenderer table1;
	public final MowzieModelRenderer table2;
	public final MowzieModelRenderer table6;
	private final MowzieModelRenderer lever;
	private final MowzieModelRenderer leverCover1;
	private final MowzieModelRenderer leverCover2;
	private final MowzieModelRenderer needle;
	private final MowzieModelRenderer switchBottom1;
	private final MowzieModelRenderer switchBottom2;
	private final MowzieModelRenderer switchBottom3;
	private final MowzieModelRenderer switchBottom4;
	private final MowzieModelRenderer switchMiddle1;
	private final MowzieModelRenderer switchMiddle2;
	private final MowzieModelRenderer switchMiddle3;
	private final MowzieModelRenderer switchMiddle4;
	private final MowzieModelRenderer switchTop1;
	private final MowzieModelRenderer switchTop2;
	private final MowzieModelRenderer switchTop3;
	private final MowzieModelRenderer switchTop4;

	public ModelControlPanel() {
		textureWidth = textureHeight = 128;

		switchMiddle1 = new ModelRendererBreakable(this, false, 0, 9);
		switchMiddle1.setRotationPoint(0.2F, 0.5F, 1F);
		switchMiddle1.addBox(0F, -2F, -0.5F, 2, 2, 1, 0F);

		switchBottom4 = new ModelRendererBreakable(this, false, 0, 9);
		switchBottom4.setRotationPoint(6.8F, 0.5F, 1F);
		switchBottom4.addBox(0F, -2F, -0.5F, 2, 2, 1, 0F);

		switchBottom3 = new ModelRendererBreakable(this, false, 0, 9);
		switchBottom3.setRotationPoint(4.6F, 0.5F, 1F);
		switchBottom3.addBox(0F, -2F, -0.5F, 2, 2, 1, 0F);

		switchBottom2 = new ModelRendererBreakable(this, false, 0, 9);
		switchBottom2.setRotationPoint(2.4F, 0.5F, 1F);
		switchBottom2.addBox(0F, -2F, -0.5F, 2, 2, 1, 0F);

		MowzieModelRenderer compass_7 = new ModelRendererBreakable(this, 0, 58);
		compass_7.setRotationPoint(0F, 0F, 0F);
		compass_7.addBox(-1.5F, -0.5F, -3.6F, 3, 1, 1, 0F);
		setRotateAngle(compass_7, -0.017453292519943295F, 2.356194490192345F, 0F);

		lever = new ModelRendererBreakable(this, 0, 96);
		lever.setRotationPoint(1F, 0F, 5F);
		lever.addBox(0F, -1F, 0F, 1, 2, 4, 0F);
		setRotateAngle(lever, 0.17453292519943295F, 0F, 0F);

		compass1 = new ModelRendererBreakable(this, 0, 46);
		compass1.setRotationPoint(0F, 0F, 0F);
		compass1.addBox(-1.5F, -0.5F, -3.6F, 3, 1, 1, 0F);
		setRotateAngle(compass1, -0.017453292519943295F, 0F, 0F);

		dimPanel7 = new ModelRendererBreakable(this, false, 84, 12);
		dimPanel7.setRotationPoint(0F, 0F, 0.1F);
		dimPanel7.addBox(0F, 0F, -0.7F, 3, 3, 1, 0F);
		setRotateAngle(dimPanel7, 0.05235987755982988F, 0F, 0F);

		switchMiddle4 = new ModelRendererBreakable(this, false, 0, 9);
		switchMiddle4.setRotationPoint(6.8F, 0.5F, 1F);
		switchMiddle4.addBox(0F, -2F, -0.5F, 2, 2, 1, 0F);

		MowzieModelRenderer table5 = new ModelRendererBreakable(this, 0, 65);
		table5.setRotationPoint(1F, -2.3F, 4F);
		table5.addBox(0F, 0F, 0F, 9, 1, 2, 0F);

		MowzieModelRenderer needle_2 = new ModelRendererBreakable(this, 8, 50);
		needle_2.setRotationPoint(0F, 0F, 0F);
		needle_2.addBox(-1F, -0.8F, -1F, 2, 1, 2, 0F);

		switchMiddle2 = new ModelRendererBreakable(this, false, 0, 9);
		switchMiddle2.setRotationPoint(2.4F, 0.5F, 1F);
		switchMiddle2.addBox(0F, -2F, -0.5F, 2, 2, 1, 0F);

		compass3 = new ModelRendererBreakable(this, 0, 52);
		compass3.setRotationPoint(0F, 0F, 0F);
		compass3.addBox(-1.5F, -0.5F, -3.6F, 3, 1, 1, 0F);
		setRotateAngle(compass3, -0.017453292519943295F, 3.141592653589793F, 0F);

		MowzieModelRenderer table22 = new ModelRendererBreakable(this, false, 37, 0);
		table22.setRotationPoint(8F, 1F, 13.7F);
		table22.addBox(0F, 0F, 0F, 4, 4, 1, 0F);

		dimPanel2 = new ModelRendererBreakable(this, false, 70, 5);
		dimPanel2.setRotationPoint(0F, 0F, 0.1F);
		dimPanel2.addBox(0F, 0F, -0.5F, 5, 3, 1, 0F);
		setRotateAngle(dimPanel2, -0.017453292519943295F, 0F, 0F);

		MowzieModelRenderer table3 = new ModelRendererBreakable(this, 0, 65);
		table3.setRotationPoint(1F, -2.3F, 12F);
		table3.addBox(0F, 0F, 0F, 9, 1, 2, 0F);

		MowzieModelRenderer table21 = new ModelRendererBreakable(this, false, 37, 0);
		table21.setRotationPoint(3F, 1F, 13.7F);
		table21.addBox(0F, 0F, 0F, 4, 4, 1, 0F);

		dimPanel1 = new ModelRendererBreakable(this, 70, 0);
		dimPanel1.setRotationPoint(10.2F, 4.5F, 5.5F);
		dimPanel1.addBox(0F, 0F, 0.7F, 5, 3, 2, 0F);
		setRotateAngle(dimPanel1, -0.10471975511965977F, 0.05235987755982988F, 0F);

		switchTop4 = new ModelRendererBreakable(this, false, 0, 9);
		switchTop4.setRotationPoint(6.8F, 0.5F, 1F);
		switchTop4.addBox(0F, -2F, -0.5F, 2, 2, 1, 0F);

		MowzieModelRenderer dimPanel8 = new ModelRendererBreakable(this, 70, 14);
		dimPanel8.setRotationPoint(0F, 0F, 0F);
		dimPanel8.addBox(0.5F, 0.5F, 0F, 2, 3, 1, 0F);

		leverCover1 = new ModelRendererBreakable(this, 7, 96);
		leverCover1.setRotationPoint(2F, 0.3F, 9F);
		leverCover1.addBox(0F, -2F, -0.5F, 4, 2, 1, 0F);
		setRotateAngle(leverCover1, 0.45378560551852565F, 0F, 0F);

		MowzieModelRenderer table19 = new ModelRendererBreakable(this, false, 0, 5);
		table19.setRotationPoint(20.5F, 2.3F, -0.8F);
		table19.addBox(0F, 0F, 0F, 3, 3, 1, 0F);

		MowzieModelRenderer screen3 = new ModelRendererBreakable(this, false, 0, 28);
		screen3.mirror = true;
		screen3.setRotationPoint(0.1F, -6.5F, 0.75F);
		screen3.addBox(0F, 0F, 0F, 1, 7, 1, 0F);

		table2 = new ModelRendererBreakable(this, 0, 46);
		table2.setRotationPoint(0F, -4F, 0F);
		table2.addBox(0F, -2F, 0F, 32, 2, 16, 0F);
		setRotateAngle(table2, 0.08726646259971647F, 0F, 0F);

		compass4 = new ModelRendererBreakable(this, 0, 55);
		compass4.setRotationPoint(0F, 0F, 0F);
		compass4.addBox(-1.5F, -0.5F, -3.6F, 3, 1, 1, 0F);
		setRotateAngle(compass4, -0.017453292519943295F, -1.5707963267948966F, 0F);

		MowzieModelRenderer dimPanel11 = new ModelRendererBreakable(this, 92, 5);
		dimPanel11.setRotationPoint(-1F, 0.5F, 1.7F);
		dimPanel11.addBox(-0.7F, 0F, 0.5F, 1, 1, 3, 0F);
		setRotateAngle(dimPanel11, -1.0821041362364843F, 0F, 0F);

		MowzieModelRenderer table14 = new ModelRendererBreakable(this, 22, 7);
		table14.setRotationPoint(27.5F, 1F, 1F);
		table14.addBox(0F, 0F, 0F, 2, 5, 3, 0F);

		final MowzieModelRenderer dimPanel15 = new ModelRendererBreakable(this, 59, 5);
		dimPanel15.setRotationPoint(0F, 2F, 0F);
		dimPanel15.addBox(-1.8F, 0F, -1F, 3, 2, 2, 0F);
		setRotateAngle(dimPanel15, 0.15707963267948966F, 0F, 0F);

		final MowzieModelRenderer table16 = new ModelRendererBreakable(this, 11, 6);
		table16.setRotationPoint(27.5F, 0F, 10F);
		table16.addBox(0F, 0F, 0F, 2, 6, 3, 0F);

		final MowzieModelRenderer table20 = new ModelRendererBreakable(this, false, 0, 5);
		table20.setRotationPoint(24F, 2.3F, -0.8F);
		table20.addBox(0F, 0F, 0F, 3, 3, 1, 0F);

		final MowzieModelRenderer table8 = new ModelRendererBreakable(this, 0, 106);
		table8.setRotationPoint(0.2F, -1F, 4F);
		table8.addBox(0F, 0F, 0F, 2, 1, 2, 0F);

		final MowzieModelRenderer lever3 = new ModelRendererBreakable(this, 0, 96);
		lever3.setRotationPoint(5F, 0F, 0F);
		lever3.addBox(0F, -1F, 0F, 1, 2, 4, 0F);

		compass2 = new ModelRendererBreakable(this, 0, 49);
		compass2.setRotationPoint(0F, 0F, 0F);
		compass2.addBox(-1.5F, -0.5F, -3.6F, 3, 1, 1, 0F);
		setRotateAngle(compass2, -0.017453292519943295F, 1.5707963267948966F, 0F);

		dimPanel4 = new ModelRendererBreakable(this, false, 85, 3);
		dimPanel4.setRotationPoint(2.7F, 0.2F, -0.8F);
		dimPanel4.addBox(0F, 0F, 0F, 2, 2, 1, 0F);

		switchTop1 = new ModelRendererBreakable(this, false, 0, 9);
		switchTop1.setRotationPoint(0.2F, 0.5F, 1F);
		switchTop1.addBox(0F, -2F, -0.5F, 2, 2, 1, 0F);

		screen2 = new ModelRendererBreakable(this, false, 35, 11);
		screen2.setRotationPoint(0.5F, -6.9F, 0.5F);
		screen2.addBox(0F, 0F, 0F, 14, 7, 1, 0F);

		final MowzieModelRenderer dimPanel12 = new ModelRendererBreakable(this, 92, 10);
		dimPanel12.setRotationPoint(0F, -0.3F, 2.8F);
		dimPanel12.addBox(-1.1F, 0.3F, -0.2F, 2, 4, 1, 0F);
		setRotateAngle(dimPanel12, 0.5410520681182421F, 0F, 0F);

		final MowzieModelRenderer table13 = new ModelRendererBreakable(this, 22, 7);
		table13.setRotationPoint(-1.5F, 1F, 5.5F);
		table13.addBox(0F, 0F, 0F, 2, 5, 3, 0F);

		switchTop2 = new ModelRendererBreakable(this, false, 0, 9);
		switchTop2.setRotationPoint(2.4F, 0.5F, 1F);
		switchTop2.addBox(0F, -2F, -0.5F, 2, 2, 1, 0F);

		final MowzieModelRenderer table12 = new ModelRendererBreakable(this, 11, 6);
		table12.setRotationPoint(-1.5F, 0F, 10F);
		table12.addBox(0F, 0F, 0F, 2, 6, 3, 0F);

		needle = new ModelRendererBreakable(this, 6, 53);
		needle.setRotationPoint(0F, 0.3F, 0F);
		needle.addBox(-0.5F, -1F, -0.5F, 1, 1, 4, 0F);

		screen1 = new ModelRendererBreakable(this, false, 0, 16);
		screen1.setRotationPoint(11F, -3F, 13F);
		screen1.addBox(0F, 0F, 0F, 15, 1, 2, 0F);
		setRotateAngle(screen1, -0.15707963267948966F, 0F, 0F);

		final MowzieModelRenderer dimPanel13 = new ModelRendererBreakable(this, 63, 0);
		dimPanel13.setRotationPoint(4.5F, 0.5F, 0F);
		dimPanel13.addBox(0.5F, 0F, 0F, 1, 2, 2, 0F);

		leverCover2 = new ModelRendererBreakable(this, 19, 96);
		leverCover2.setRotationPoint(0F, -2F, 0.5F);
		leverCover2.addBox(0F, -2F, -1F, 4, 2, 1, 0F);
		setRotateAngle(leverCover2, 1.4311699866353502F, 0F, 0F);

		final MowzieModelRenderer table15 = new ModelRendererBreakable(this, 22, 7);
		table15.setRotationPoint(27.5F, 1F, 5.5F);
		table15.addBox(0F, 0F, 0F, 2, 5, 3, 0F);

		switchTop3 = new ModelRendererBreakable(this, false, 0, 9);
		switchTop3.setRotationPoint(4.6F, 0.5F, 1F);
		switchTop3.addBox(0F, -2F, -0.5F, 2, 2, 1, 0F);

		final MowzieModelRenderer lever2 = new ModelRendererBreakable(this, 0, 103);
		lever2.setRotationPoint(0F, 0F, 0F);
		lever2.addBox(1F, -0.5F, 2.5F, 4, 1, 1, 0F);

		final MowzieModelRenderer table9 = new ModelRendererBreakable(this, 0, 106);
		table9.setRotationPoint(5.9F, -1F, 4F);
		table9.addBox(0F, 0F, 0F, 2, 1, 2, 0F);

		final MowzieModelRenderer compass_6 = new ModelRendererBreakable(this, 0, 58);
		compass_6.setRotationPoint(0F, 0F, 0F);
		compass_6.addBox(-1.5F, -0.5F, -3.6F, 3, 1, 1, 0F);
		setRotateAngle(compass_6, -0.017453292519943295F, 0.7853981633974483F, 0F);

		switchMiddle3 = new ModelRendererBreakable(this, false, 0, 9);
		switchMiddle3.setRotationPoint(4.6F, 0.5F, 1F);
		switchMiddle3.addBox(0F, -2F, -0.5F, 2, 2, 1, 0F);

		final MowzieModelRenderer table10 = new ModelRendererBreakable(this, 10, 65);
		table10.setRotationPoint(2F, -5.8F, 1F);
		table10.addBox(0F, 0F, 0F, 28, 6, 14, 0F);

		final MowzieModelRenderer table11 = new ModelRendererBreakable(this, 22, 7);
		table11.setRotationPoint(-1.5F, 1F, 1F);
		table11.addBox(0F, 0F, 0F, 2, 5, 3, 0F);

		switchBottom1 = new ModelRendererBreakable(this, false, 0, 9);
		switchBottom1.setRotationPoint(0.2F, 0.5F, 1F);
		switchBottom1.addBox(0F, -2F, -0.5F, 2, 2, 1, 0F);

		dimPanel3 = new ModelRendererBreakable(this, false, 85, 0);
		dimPanel3.setRotationPoint(0.3F, 0.2F, -0.8F);
		dimPanel3.addBox(0F, 0F, 0F, 2, 2, 1, 0F);

		final MowzieModelRenderer table18 = new ModelRendererBreakable(this, false, 0, 5);
		table18.setRotationPoint(17F, 2.3F, -0.8F);
		table18.addBox(0F, 0F, 0F, 3, 3, 1, 0F);

		dimPanel6 = new ModelRendererBreakable(this, 84, 7);
		dimPanel6.setRotationPoint(1F, -3.1F, 0F);
		dimPanel6.addBox(0F, 0F, 0.7F, 3, 3, 1, 0F);

		final MowzieModelRenderer compass_8 = new ModelRendererBreakable(this, 0, 58);
		compass_8.setRotationPoint(0F, 0F, 0F);
		compass_8.addBox(-1.5F, -0.5F, -3.6F, 3, 1, 1, 0F);
		setRotateAngle(compass_8, -0.017453292519943295F, -2.356194490192345F, 0F);

		final MowzieModelRenderer dimPanel10 = new ModelRendererBreakable(this, 92, 0);
		dimPanel10.mirror = true;
		dimPanel10.setRotationPoint(2.7F, 2F, 1F);
		dimPanel10.addBox(-1F, 0F, 0F, 1, 2, 2, 0F);

		final MowzieModelRenderer compass_5 = new ModelRendererBreakable(this, 0, 58);
		compass_5.setRotationPoint(0F, 0F, 0F);
		compass_5.addBox(-1.5F, -0.5F, -3.6F, 3, 1, 1, 0F);
		setRotateAngle(compass_5, -0.017453292519943295F, -0.7853981633974483F, 0F);

		final MowzieModelRenderer dimPanel14 = new ModelRendererBreakable(this, 58, 0);
		dimPanel14.setRotationPoint(0.3F, 1F, 1F);
		dimPanel14.addBox(0F, 0F, -0.5F, 1, 3, 1, 0F);

		final MowzieModelRenderer table7 = new ModelRendererBreakable(this, 0, 86);
		table7.setRotationPoint(23F, -2.5F, 2F);
		table7.addBox(0F, 0F, 2F, 8, 1, 8, 0F);

		final MowzieModelRenderer table17 = new ModelRendererBreakable(this, false, 12, 0);
		table17.setRotationPoint(1F, 2.3F, -0.2F);
		table17.addBox(0F, 0F, 0F, 9, 3, 1, 0F);

		final MowzieModelRenderer dimPanel5 = new ModelRendererBreakable(this, 70, 10);
		dimPanel5.setRotationPoint(0F, 0F, 0F);
		dimPanel5.addBox(0.5F, 0.5F, 0F, 4, 2, 1, 0F);

		table1 = new ModelRendererBreakable(this, 0, 21);
		table1.setRotationPoint(-16F, 16F, -8F);
		table1.addBox(0F, 0F, 0F, 32, 8, 16, 0F);

		table6 = new ModelRendererBreakable(this, 0, 69);
		table6.setRotationPoint(16F, -2.2F, 8F);
		table6.addBox(-2.5F, -0.1F, -2.5F, 5, 1, 5, 0F);

		final MowzieModelRenderer table4 = new ModelRendererBreakable(this, 0, 65);
		table4.setRotationPoint(1F, -2.3F, 8F);
		table4.addBox(0F, 0F, 0F, 9, 1, 2, 0F);

		final MowzieModelRenderer screen4 = new ModelRendererBreakable(this, false, 0, 28);
		screen4.setRotationPoint(13.9F, -6.5F, 0.75F);
		screen4.addBox(0F, 0F, 0F, 1, 7, 1, 0F);

		final MowzieModelRenderer dimPanel9 = new ModelRendererBreakable(this, 92, 0);
		dimPanel9.setRotationPoint(0.3F, 2F, 1F);
		dimPanel9.addBox(0F, 0F, 0F, 1, 2, 2, 0F);

		table4.addChild(switchMiddle1);
		table5.addChild(switchBottom4);
		table5.addChild(switchBottom3);
		table5.addChild(switchBottom2);
		table6.addChild(compass_7);
		table7.addChild(lever);
		table6.addChild(compass1);
		dimPanel6.addChild(dimPanel7);
		table4.addChild(switchMiddle4);
		table2.addChild(table5);
		needle.addChild(needle_2);
		table4.addChild(switchMiddle2);
		table6.addChild(compass3);
		table10.addChild(table22);
		dimPanel1.addChild(dimPanel2);
		table2.addChild(table3);
		table10.addChild(table21);
		table3.addChild(switchTop4);
		dimPanel6.addChild(dimPanel8);
		table7.addChild(leverCover1);
		table10.addChild(table19);
		screen1.addChild(screen3);
		table1.addChild(table2);
		table6.addChild(compass4);
		dimPanel10.addChild(dimPanel11);
		table10.addChild(table14);
		dimPanel14.addChild(dimPanel15);
		table10.addChild(table16);
		table10.addChild(table20);
		table7.addChild(table8);
		lever.addChild(lever3);
		table6.addChild(compass2);
		dimPanel2.addChild(dimPanel4);
		table3.addChild(switchTop1);
		screen1.addChild(screen2);
		dimPanel11.addChild(dimPanel12);
		table10.addChild(table13);
		table3.addChild(switchTop2);
		table10.addChild(table12);
		table6.addChild(needle);
		table2.addChild(screen1);
		dimPanel1.addChild(dimPanel13);
		leverCover1.addChild(leverCover2);
		table10.addChild(table15);
		table3.addChild(switchTop3);
		lever.addChild(lever2);
		table7.addChild(table9);
		table6.addChild(compass_6);
		table4.addChild(switchMiddle3);
		table1.addChild(table10);
		table10.addChild(table11);
		table5.addChild(switchBottom1);
		dimPanel2.addChild(dimPanel3);
		table10.addChild(table18);
		dimPanel1.addChild(dimPanel6);
		table6.addChild(compass_8);
		dimPanel6.addChild(dimPanel10);
		table6.addChild(compass_5);
		dimPanel13.addChild(dimPanel14);
		table2.addChild(table7);
		table10.addChild(table17);
		dimPanel1.addChild(dimPanel5);
		table2.addChild(table6);
		table2.addChild(table4);
		screen1.addChild(screen4);
		dimPanel6.addChild(dimPanel9);

		setInitPose();
	}

	public void render(TileEntityControlPanel tile, float partialTicks) {
		setToInitPose();

		if(tile != null && tile.getWorldObj() != null) {
			final MowzieModelRenderer[][] models = {{switchTop1, switchTop2, switchTop3, switchTop4}, {switchMiddle1, switchMiddle2, switchMiddle3, switchMiddle4}, {switchBottom1, switchBottom2, switchBottom3, switchBottom4}};

			for(int i = 0; i < models.length; ++i) {
				for(int j = 0; j < models[i].length; ++j) {
					models[i][j].rotateAngleX = -(float) tile.switches[i][j] / 10;
				}
			}

			needle.rotateAngleY = (float) Math.PI / 2 * TFHelper.median(tile.animPortalDirection, tile.prevAnimPortalDirection, partialTicks);
			leverCover1.rotateAngleX = 0.45378560551852565F - 1.9F * TFHelper.median(tile.activationLeverCoverTimer, tile.prevActivationLeverCoverTimer, partialTicks);
			leverCover2.rotateAngleX = 1.4311699866353502F - 0.5F * TFHelper.median(tile.activationLeverCoverTimer, tile.prevActivationLeverCoverTimer, partialTicks);
			lever.rotateAngleX = 0.17453292519943295F + 2.5F * TFHelper.median(tile.activationLeverTimer, tile.prevActivationLeverTimer, partialTicks);
		}

		table1.render(0.0625F);

		if(tile.hasUpgrade(DataCore.spaceBridge)) {
			dimPanel1.render(0.0625F);
		}
	}
}
