package fiskfille.tf.client.model.item;

import fiskfille.tf.client.model.tools.MowzieModelBase;
import fiskfille.tf.client.model.tools.MowzieModelRenderer;

public class ModelPowerCanister extends MowzieModelBase {
	private final MowzieModelRenderer canister1;

	public ModelPowerCanister() {
		textureWidth = 32;
		textureHeight = 16;

		final MowzieModelRenderer canister3 = new MowzieModelRenderer(this, 12, 5);
		canister3.rotationPointY = -1.5F;
		canister3.addBox(-2F, 0F, -2F, 4, 1, 4, 0F);

		final MowzieModelRenderer canister2 = new MowzieModelRenderer(this, 12, 0);
		canister2.rotationPointY = -8.5F;
		canister2.addBox(-2F, -1F, -2F, 4, 1, 4, 0F);

		canister1 = new MowzieModelRenderer(this, 0, 0);
		canister1.addBox(-1.5F, -10F, -1.5F, 3, 10, 3, 0F);
		canister1.addChild(canister3);
		canister1.addChild(canister2);

		setInitPose();
	}

	public void render() {
		setToInitPose();
		canister1.render(0.0625F);
	}
}
