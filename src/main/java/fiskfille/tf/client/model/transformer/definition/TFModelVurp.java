package fiskfille.tf.client.model.transformer.definition;

import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.model.tools.ModelRendererTF;
import fiskfille.tf.client.model.transformer.ModelTransformerBase;
import fiskfille.tf.client.model.transformer.ModelVurp;
import fiskfille.tf.client.model.transformer.stealth.ModelVurpStealth;
import fiskfille.tf.client.model.transformer.vehicle.ModelVehicleBase;
import fiskfille.tf.client.model.transformer.vehicle.ModelVurpVehicle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

public class TFModelVurp extends TransformerModel {
	private final ModelVurp model = new ModelVurp();
	private final ModelVurp modelItem = new ModelVurp();
	private final ModelVurpVehicle vehicle = new ModelVurpVehicle();
	private final ModelVurpStealth stealth = new ModelVurpStealth();

	@Override
	public ModelTransformerBase getMainModel() {
		return model;
	}

	@Nonnull
	@Override
	public ModelVehicleBase getVehicleModel() {
		return vehicle;
	}

	@Override
	public ModelVehicleBase getStealthModel() {
		return stealth;
	}

	@Override
	public ModelRendererTF[] getFeet() {
		return new ModelRendererTF[]{model.footbaseL, model.footbaseR};
	}

	@Override
	public ModelRendererTF[] getLegs() {
		return new ModelRendererTF[]{model.upperLegL, model.upperLegR};
	}

	@Override
	public ModelRendererTF getLowerArm() {
		return model.lowerArmR1;
	}

	@Override
	public ModelRendererTF getUpperArm() {
		return model.armbaseR1;
	}

	@Override
	public ModelRendererTF getBody() {
		return model.torsobase;
	}

	@Override
	public ModelRendererTF getHead() {
		return model.head;
	}

	@Override
	public float getFootHeight() {
		return 2;
	}

	@Override
	public void renderItem(final EntityPlayer player, final ItemStack stack) {
		GL11.glTranslatef(0.05F, -0F, 0.1F);
	}

	@Override
	public void renderCape(final EntityPlayer player) {
		GL11.glTranslatef(0F, -0.2F, 0.1F);
	}

	@Override
	public void renderFirstPersonArm(final EntityPlayer player) {
		GL11.glScalef(1.1F, 1.1F, 1.1F);
	}

	@Override
	public ResourceLocation getTexture(final Entity entity, final String suffix) {
		return new ResourceLocation(TransformersMod.MODID, String.format("textures/models/vurp/vurp%s.png", suffix));
	}

	@Override
	public ModelTransformerBase getItemInventoryModel() {
		return modelItem;
	}
}
