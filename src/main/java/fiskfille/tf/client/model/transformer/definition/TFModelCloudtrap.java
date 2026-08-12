package fiskfille.tf.client.model.transformer.definition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.model.tools.ModelRendererTF;
import fiskfille.tf.client.model.transformer.ModelCloudtrap;
import fiskfille.tf.client.model.transformer.ModelTransformerBase;
import fiskfille.tf.client.model.transformer.vehicle.ModelCloudtrapVehicle;
import fiskfille.tf.client.model.transformer.vehicle.ModelVehicleBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

@SideOnly(Side.CLIENT)
public class TFModelCloudtrap extends TransformerModel {
	private final ModelCloudtrap model = new ModelCloudtrap();
	private final ModelCloudtrap modelItem = new ModelCloudtrap();
	private final ModelCloudtrapVehicle vehicle = new ModelCloudtrapVehicle();

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
	public ModelRendererTF[] getFeet() {
		return new ModelRendererTF[]{model.feetbaseL, model.feetbaseR};
	}

	@Override
	public ModelRendererTF[] getLegs() {
		return new ModelRendererTF[]{model.upperLegL, model.upperLegR};
	}

	@Override
	public ModelRendererTF getLowerArm() {
		return model.lowerArmR;
	}

	@Override
	public ModelRendererTF getUpperArm() {
		return model.upperArmR;
	}

	@Override
	public ModelRendererTF getBody() {
		return model.back1;
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
		GL11.glTranslatef(0.075F, -0.18F, -0.02F);
		GL11.glRotatef(10, 1, 0, 0);
	}

	@Override
	public void renderCape(final EntityPlayer player) {
		GL11.glRotatef(20, 1, 0, 0);
		GL11.glTranslatef(0, -0.05F, 0F);
	}

	@Override
	public void renderFirstPersonArm(final EntityPlayer player) {
		GL11.glScalef(1.3F, 1.3F, 1.3F);
		GL11.glTranslatef(0.2F, -0.1F, 0.2F);
	}

	@Override
	public ResourceLocation getTexture(final Entity entity, final String suffix) {
		return new ResourceLocation(TransformersMod.MODID, String.format("textures/models/cloudtrap/cloudtrap%s.png", suffix));
	}

	@Override
	public ModelTransformerBase getItemInventoryModel() {
		return modelItem;
	}
}
