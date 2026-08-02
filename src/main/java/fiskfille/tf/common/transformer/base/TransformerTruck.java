package fiskfille.tf.common.transformer.base;

import fiskfille.tf.common.entity.EntityMissile;
import fiskfille.tf.common.item.TFItems;
import fiskfille.tf.common.motion.TFMotionManager;
import fiskfille.tf.config.TFConfig;
import fiskfille.tf.helper.TFHelper;
import fiskfille.tf.helper.TFVectorHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.Vec3;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author gegy1000
 */
public abstract class TransformerTruck extends Transformer {
	public TransformerTruck(String name) {
		super(name);
	}

	@Override
	public float fall(EntityPlayer player, float distance, int altMode) {
		return TFHelper.isFullyTransformed(player) ? distance / 4 : super.fall(player, distance, altMode);
	}

	@Override
	public boolean hasStealthForce() {
		return true;
	}

	@Override
	public boolean canJumpAsVehicle(EntityPlayer player) {
		return TFHelper.isInStealthMode(player);
	}

	@Override
	public float getHeightOffset() {
		return -1F;
	}

	@Override
	public boolean canUseNitro(EntityPlayer player) {
		return !TFHelper.isInStealthMode(player);
	}

	@Override
	public void updateMovement(EntityPlayer player) {
		TFMotionManager.motion(player, 40, 60, 20, 10, false, true, TFHelper.isInStealthMode(player));
	}

	@Override
	public boolean canShoot(EntityPlayer player) {
		return TFHelper.isInStealthMode(player);
	}

	@Override
	public Item getShootItem() {
		return TFItems.missile;
	}

	@Override
	public Entity getShootEntity(EntityPlayer player) {
		return new EntityMissile(player.worldObj, player, TFConfig.allowMissileExplosions, TFHelper.isInStealthMode(player));
	}

	@Override
	public int getShots() {
		return 8;
	}

	@Override
	public void doNitroParticles(EntityPlayer player) {
		final ThreadLocalRandom random = ThreadLocalRandom.current();

		for(int i = 0; i < 4; ++i) {
			final Vec3 side = TFVectorHelper.getBackSideCoords(player, 0.15F, i < 2, -0.9, false);
			player.worldObj.spawnParticle("smoke", side.xCoord, side.yCoord, side.zCoord, random.nextFloat() / 20, random.nextFloat() / 20, random.nextFloat() / 20);
		}

		for(int i = 0; i < 10; ++i) {
			final Vec3 side = TFVectorHelper.getBackSideCoords(player, 0.15F, i < 2, -0.9, false);
			player.worldObj.spawnParticle("smoke", side.xCoord, side.yCoord, side.zCoord, random.nextFloat() / 10, random.nextFloat() / 10 + 0.05F, random.nextFloat() / 10);
		}
	}
}
