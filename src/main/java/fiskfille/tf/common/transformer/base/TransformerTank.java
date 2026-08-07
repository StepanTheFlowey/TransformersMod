package fiskfille.tf.common.transformer.base;

import fiskfille.tf.TransformersMod;
import fiskfille.tf.common.entity.EntityTankShell;
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
public abstract class TransformerTank extends Transformer {
	public TransformerTank(final String name) {
		super(name);
	}

	@Override
	public boolean canZoom() {
		return true;
	}

	@Override
	public String getShootSound() {
		return TransformersMod.MODID + ":tankfire";
	}

	@Override
	public float fall(final EntityPlayer player, final float distance, final int altMode) {
		return TFHelper.isFullyTransformed(player) ? 0 : super.fall(player, distance, altMode);
	}

	@Override
	public void updateMovement(final EntityPlayer player) {
		TFMotionManager.motion(player, 20, 30, 0, 20, false, true, false);
	}

	@Override
	public boolean canShoot(final EntityPlayer player) {
		return true;
	}

	@Override
	public Item getShootItem() {
		return TFItems.tankShell;
	}

	@Override
	public Entity getShootEntity(final EntityPlayer player) {
		return new EntityTankShell(player.worldObj, player, TFConfig.allowTankShellExplosions);
	}

	@Override
	public void doNitroParticles(final EntityPlayer player) {
		final ThreadLocalRandom random = ThreadLocalRandom.current();

		for(int i = 0; i < 4; ++i) {
			final Vec3 side = TFVectorHelper.getBackSideCoords(player, 0.15F, i < 2, -0.6, false);
			player.worldObj.spawnParticle("smoke", side.xCoord, side.yCoord, side.zCoord, random.nextFloat() / 20, random.nextFloat() / 20, random.nextFloat() / 20);
		}
	}
}
