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
public abstract class TransformerCar extends Transformer {
	public TransformerCar(final String name) {
		super(name);
	}

	@Override
	public float fall(final EntityPlayer player, final float distance, final int altMode) {
		return TFHelper.isFullyTransformed(player) ? distance / 2 : super.fall(player, distance, altMode);
	}

	@Override
	public boolean hasStealthForce() {
		return true;
	}

	@Override
	public boolean canJumpAsVehicle(final EntityPlayer player) {
		return TFHelper.isInStealthMode(player);
	}

	@Override
	public void updateMovement(final EntityPlayer player) {
		TFMotionManager.motion(player, 60, 100, 20, 20, true, false, TFHelper.isInStealthMode(player));
	}

	@Override
	public boolean canUseNitro(final EntityPlayer player) {
		return !TFHelper.isInStealthMode(player);
	}

	@Override
	public boolean canShoot(final EntityPlayer player) {
		return TFHelper.isInStealthMode(player);
	}

	@Override
	public Item getShootItem() {
		return TFItems.missile;
	}

	@Override
	public Entity getShootEntity(final EntityPlayer player) {
		return new EntityMissile(player.worldObj, player, TFConfig.allowMissileExplosions, TFHelper.isInStealthMode(player));
	}

	@Override
	public int getShots() {
		return 8;
	}

	@Override
	public void doNitroParticles(final EntityPlayer player) {
		final ThreadLocalRandom random = ThreadLocalRandom.current();

		for(int i = 0; i < 4; ++i) {
			final Vec3 side = TFVectorHelper.getBackSideCoords(player, 0.15D, i < 2, -1.4, false);
			player.worldObj.spawnParticle("smoke", side.xCoord, side.yCoord, side.zCoord, random.nextDouble() / 20, random.nextDouble() / 20, random.nextDouble() / 20);
		}

		for(int i = 0; i < 10; ++i) {
			final Vec3 side = TFVectorHelper.getBackSideCoords(player, 0.15D, i < 2, -1.4, false);
			player.worldObj.spawnParticle("smoke", side.xCoord, side.yCoord, side.zCoord, random.nextDouble() / 10, random.nextDouble() / 10 + 0.05D, random.nextDouble() / 10);
		}
	}
}
