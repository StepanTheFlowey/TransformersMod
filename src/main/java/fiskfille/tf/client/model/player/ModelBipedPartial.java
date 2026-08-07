package fiskfille.tf.client.model.player;

import fiskfille.tf.client.model.tools.ModelRendererPartial;
import net.minecraft.client.model.ModelBiped;

public class ModelBipedPartial extends ModelBiped {
	public ModelBipedPartial() {
		this(0, 0, 64, 32);
	}

	public ModelBipedPartial(final float scale, final float offset, final int texWidth, final int texHeight) {
		super(scale, offset, texWidth, texHeight);

		bipedCloak = new ModelRendererPartial(this, 0, 0);
		bipedCloak.addBox(-5F, 0F, -1F, 10, 16, 1, scale);

		bipedEars = new ModelRendererPartial(this, 24, 0);
		bipedEars.addBox(-3F, -6F, -1F, 6, 6, 1, scale);

		bipedHead = new ModelRendererPartial(this, 0, 0);
		bipedHead.addBox(-4F, -8F, -4F, 8, 8, 8, scale);
		bipedHead.rotationPointY = offset;

		bipedHeadwear = new ModelRendererPartial(this, 32, 0);
		bipedHeadwear.addBox(-4F, -8F, -4F, 8, 8, 8, scale + 0.5F);
		bipedHeadwear.rotationPointY = offset;

		bipedBody = new ModelRendererPartial(this, 16, 16);
		bipedBody.addBox(-4F, 0F, -2F, 8, 12, 4, scale);
		bipedBody.rotationPointY = offset;

		bipedRightArm = new ModelRendererPartial(this, 40, 16);
		bipedRightArm.addBox(-3F, -2F, -2F, 4, 12, 4, scale);
		bipedRightArm.rotationPointX = -5F;
		bipedRightArm.rotationPointY = 2F + offset;

		bipedLeftArm = new ModelRendererPartial(this, 40, 16);
		bipedLeftArm.mirror = true;
		bipedLeftArm.addBox(-1F, -2F, -2F, 4, 12, 4, scale);
		bipedLeftArm.rotationPointX = 5F;
		bipedLeftArm.rotationPointY = 2F + offset;

		bipedRightLeg = new ModelRendererPartial(this, 0, 16);
		bipedRightLeg.addBox(-2F, 0F, -2F, 4, 12, 4, scale);
		bipedRightLeg.rotationPointX = -1.9F;
		bipedRightLeg.rotationPointY = 12F + offset;

		bipedLeftLeg = new ModelRendererPartial(this, 0, 16);
		bipedLeftLeg.mirror = true;
		bipedLeftLeg.addBox(-2F, 0F, -2F, 4, 12, 4, scale);
		bipedLeftLeg.rotationPointX = 1.9F;
		bipedLeftLeg.rotationPointY = 12F + offset;
	}
}
