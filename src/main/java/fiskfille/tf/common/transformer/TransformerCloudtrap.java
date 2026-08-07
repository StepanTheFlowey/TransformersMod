package fiskfille.tf.common.transformer;

import fiskfille.tf.common.item.TFItems;
import fiskfille.tf.common.motion.TFMotionManager;
import fiskfille.tf.common.transformer.base.TransformerJet;
import fiskfille.tf.common.transformer.cloudtrap.CloudtrapJetpackManager;
import fiskfille.tf.helper.TFVectorHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.Vec3;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author gegy1000
 */
public class TransformerCloudtrap extends TransformerJet {
	public TransformerCloudtrap() {
		super("Cloudtrap");
	}

	@Override
	public Item getHelmet() {
		return TFItems.cloudtrapHelmet;
	}

	@Override
	public Item getChestplate() {
		return TFItems.cloudtrapChestplate;
	}

	@Override
	public Item getLeggings() {
		return TFItems.cloudtrapLeggings;
	}

	@Override
	public Item getBoots() {
		return TFItems.cloudtrapBoots;
	}

	@Override
	public float getHeightOffset() {
		return -0.1F;
	}

	@Override
	public float getVehicleHeightOffset() {
		return -1.5F;
	}

	@Override
	public void tick(final EntityPlayer player, final float timer) {
		if(timer < 0.5F) {
			if(player.worldObj.isRemote) {
				CloudtrapJetpackManager.cloudtrapTick(player);
			}
		}

		if(timer == 0) {
			if(!player.capabilities.isFlying) {
				if(player.motionY < 0D) {
					player.motionY *= 0.975;
				}
			}
		}
	}

	@Override
	public boolean onJump(final EntityPlayer player) {
		return !player.isSneaking();
	}

	@Override
	public void updateMovement(final EntityPlayer player) {
		TFMotionManager.motionJet(player, 140, 200, 50);
	}

	@Override
	public void doNitroParticles(final EntityPlayer player) {
		final ThreadLocalRandom random = ThreadLocalRandom.current();

		for(int i = 0; i < 4; ++i) {
			final Vec3 side = TFVectorHelper.getBackSideCoords(player, 0.135F, i < 2, -2.5, true);
			player.worldObj.spawnParticle("flame", side.xCoord, side.yCoord + 0.25F, side.zCoord, (random.nextFloat() - 0.5F) / 20, (random.nextFloat() - 0.5F) / 20, (random.nextFloat() - 0.5F) / 20);
		}
	}
}
