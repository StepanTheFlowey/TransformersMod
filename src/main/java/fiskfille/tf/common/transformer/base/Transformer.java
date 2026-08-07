package fiskfille.tf.common.transformer.base;

import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.model.transformer.definition.TFModelRegistry;
import fiskfille.tf.client.model.transformer.definition.TransformerModel;
import fiskfille.tf.helper.TFHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

/**
 * @author gegy1000, FiskFille
 */
public abstract class Transformer {
	private final String name;

	public Transformer(final String name) {
		this.name = name;
	}

	public abstract Item getHelmet();

	public abstract Item getChestplate();

	public abstract Item getLeggings();

	public abstract Item getBoots();

	/**
	 * Override to specify whether this Transformer can use nitro.
	 *
	 * @param player The player trying to use nitro.
	 * @return whether the player can use nitro.
	 */
	public boolean canUseNitro(final EntityPlayer player) {
		return true;
	}

	/**
	 * @return the model to use for this Transformer.
	 */
	public TransformerModel getModel() {
		return TFModelRegistry.getModel(this);
	}

	/**
	 * @return the name of this Transformer.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Called every tick while wearing the armor.
	 *
	 * @param player The player wearing the armor.
	 * @param timer  The transformation timer.
	 */
	public void tick(final EntityPlayer player, final float timer) {
	}

	public boolean canZoom() {
		return false;
	}

	public float getZoomAmount() {
		return 0.1F;
	}

	public float getHeightOffset() {
		return 0;
	}

	public float getVehicleHeightOffset() {
		return -1.25F;
	}

	public boolean canJumpAsVehicle(final EntityPlayer player) {
		return false;
	}

	public boolean hasStealthForce() {
		return false;
	}

	public abstract void updateMovement(EntityPlayer player);

	public Item getShootItem() {
		return null;
	}

	public Entity getShootEntity(final EntityPlayer playe) {
		return null;
	}

	public String getShootSound() {
		return null;
	}

	public float getShootVolume() {
		return 1;
	}

	public int getShots() {
		return 4;
	}

	public boolean canShoot(final EntityPlayer player) {
		return false;
	}

	public boolean onJump(final EntityPlayer player) {
		return true;
	}

	public float getThirdPersonDistance(final EntityPlayer player) {
		return 4 - TFHelper.getTransformationTimer(player) * 2;
	}

	/**
	 * Called every tick while using nitro on the client side, used to make nitro particles.
	 *
	 * @param player The player making the particles
	 */
	public void doNitroParticles(final EntityPlayer player) {
	}

	/**
	 * Called when this transformer hits the ground.
	 *
	 * @param player   The player who is falling.
	 * @param distance The distance fell.
	 * @return The damage to take.
	 */
	public float fall(final EntityPlayer player, final float distance, final int altMode) {
		return distance;
	}

	public boolean hasRapidFire() {
		return false;
	}

	public boolean disableViewBobbing() {
		return true;
	}

	public boolean disableStepSounds() {
		return true;
	}

	public boolean overrideFirstPerson() {
		return true;
	}

	public boolean renderSpeedAndNitro() {
		return true;
	}

	/**
	 * @return Whether this Transformer can interact with the world while in vehicle mode, i.e. break blocks, damage entities, etc.
	 */
	public boolean canInteractInVehicleMode() {
		return true;
	}

	public int getAltModeCount() {
		return 1;
	}

	public ResourceLocation getTransformationSound(final int altMode) {
		return new ResourceLocation(TransformersMod.MODID, "transform_" + (altMode == -1 ? "robot" : "vehicle"));
	}
}
