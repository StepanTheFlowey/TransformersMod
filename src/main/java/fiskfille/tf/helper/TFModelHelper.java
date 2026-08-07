package fiskfille.tf.helper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.client.model.player.ModelBipedPartial;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gegy1000, FiskFille
 */
@SideOnly(Side.CLIENT)
public class TFModelHelper {
	public static final ModelBipedPartial modelBipedPartial = new ModelBipedPartial();
	private static final Map<Entity, ModelOffset> offsets = new HashMap<>();

	/**
	 * @returns the model offsets for the specified entity.
	 */
	public static ModelOffset getOffsets(final Entity entity) {
		return offsets.computeIfAbsent(entity, k -> new ModelOffset(false));
	}

	/**
	 * Hooks into {@link ModelBiped#render}<br>
	 * Called after {@link ModelBiped#setRotationAngles}, but before rendering
	 *
	 * @param model
	 * @param entity
	 * @param limbSwing
	 * @param limbSwingAmount
	 * @param ticks
	 * @param rotationYaw
	 * @param rotationPitch
	 * @param scale
	 */
	public static void renderBipedPre(final ModelBiped model, final Entity entity, final float limbSwing, final float limbSwingAmount, final float ticks, final float rotationYaw, final float rotationPitch, final float scale) {
		ModelOffset offset = TFModelHelper.getOffsets(entity);

		if(!offset.isInitialized()) {
			offset = new ModelOffset(true);
			offset.headOffsetX = model.bipedHead.rotationPointX;
			offset.headOffsetY = model.bipedHead.rotationPointY;
			offset.headOffsetZ = model.bipedHead.rotationPointZ;

			offsets.put(entity, offset);
		}

		if(offset.isInitialized()) {
			model.bipedHead.rotationPointY = offset.headOffsetY;
			model.bipedHeadwear.rotationPointY = offset.headOffsetY;
			model.bipedHead.rotationPointX = offset.headOffsetX;
			model.bipedHeadwear.rotationPointX = offset.headOffsetX;
			model.bipedHead.rotationPointZ = offset.headOffsetZ;
			model.bipedHeadwear.rotationPointZ = offset.headOffsetZ;
		}
	}
}
