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
public abstract class TransformerJet extends Transformer {
	public TransformerJet(final String name) {
		super(name);
	}

	@Override
	public float fall(final EntityPlayer player, final float distance, final int altMode) {
		return 0;
	}

	@Override
	public float getThirdPersonDistance(final EntityPlayer player) {
		return 4F;
	}

	@Override
	public void updateMovement(final EntityPlayer player) {
		TFMotionManager.motionJet(player, 100, 140, 20);
	}

	@Override
	public boolean canShoot(final EntityPlayer player) {
		return true;
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
	public void doNitroParticles(final EntityPlayer player) {
		final ThreadLocalRandom random = ThreadLocalRandom.current();

		for(int i = 0; i < 4; ++i) {
			final Vec3 side = TFVectorHelper.getBackSideCoords(player, 0.15F, i < 2, -2, true);
			player.worldObj.spawnParticle("flame", side.xCoord, side.yCoord + 0.3F, side.zCoord, random.nextFloat() / 20, -0.2F + random.nextFloat() / 20, random.nextFloat() / 20);
		}
	}
}
