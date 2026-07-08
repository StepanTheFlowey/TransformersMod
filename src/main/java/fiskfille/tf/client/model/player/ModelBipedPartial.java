package fiskfille.tf.client.model.player;

import net.minecraft.client.model.ModelBiped;
import fiskfille.tf.client.model.tools.ModelRendererPartial;

public class ModelBipedPartial extends ModelBiped
{
    public ModelBipedPartial()
    {
        this(0F);
    }

    public ModelBipedPartial(float scale)
    {
        this(scale, 0F, 64, 32);
    }

    public ModelBipedPartial(float scale, float offset, int texWidth, int texHeight)
    {
        super(scale, offset, texWidth, texHeight);
        bipedCloak = new ModelRendererPartial(this, 0, 0);
        bipedCloak.addBox(-5F, 0F, -1F, 10, 16, 1, scale);
        bipedEars = new ModelRendererPartial(this, 24, 0);
        bipedEars.addBox(-3F, -6F, -1F, 6, 6, 1, scale);
        bipedHead = new ModelRendererPartial(this, 0, 0);
        bipedHead.addBox(-4F, -8F, -4F, 8, 8, 8, scale);
        bipedHead.setRotationPoint(0F, 0F + offset, 0F);
        bipedHeadwear = new ModelRendererPartial(this, 32, 0);
        bipedHeadwear.addBox(-4F, -8F, -4F, 8, 8, 8, scale + 0.5F);
        bipedHeadwear.setRotationPoint(0F, 0F + offset, 0F);
        bipedBody = new ModelRendererPartial(this, 16, 16);
        bipedBody.addBox(-4F, 0F, -2F, 8, 12, 4, scale);
        bipedBody.setRotationPoint(0F, 0F + offset, 0F);
        bipedRightArm = new ModelRendererPartial(this, 40, 16);
        bipedRightArm.addBox(-3F, -2F, -2F, 4, 12, 4, scale);
        bipedRightArm.setRotationPoint(-5F, 2F + offset, 0F);
        bipedLeftArm = new ModelRendererPartial(this, 40, 16);
        bipedLeftArm.mirror = true;
        bipedLeftArm.addBox(-1F, -2F, -2F, 4, 12, 4, scale);
        bipedLeftArm.setRotationPoint(5F, 2F + offset, 0F);
        bipedRightLeg = new ModelRendererPartial(this, 0, 16);
        bipedRightLeg.addBox(-2F, 0F, -2F, 4, 12, 4, scale);
        bipedRightLeg.setRotationPoint(-1.9F, 12F + offset, 0F);
        bipedLeftLeg = new ModelRendererPartial(this, 0, 16);
        bipedLeftLeg.mirror = true;
        bipedLeftLeg.addBox(-2F, 0F, -2F, 4, 12, 4, scale);
        bipedLeftLeg.setRotationPoint(1.9F, 12F + offset, 0F);
    }
}
