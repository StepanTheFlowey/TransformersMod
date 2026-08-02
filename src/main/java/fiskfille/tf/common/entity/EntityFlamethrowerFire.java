package fiskfille.tf.common.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import java.util.concurrent.ThreadLocalRandom;

public class EntityFlamethrowerFire extends EntityThrowable {
	protected final int particleMaxAge = (int) (8D / (ThreadLocalRandom.current().nextDouble() * 0.8D + 0.2D)) + 2;

	public EntityFlamethrowerFire(World world) {
		super(world);
		noClip = false;
	}

	public EntityFlamethrowerFire(World world, EntityLivingBase entity) {
		super(world, entity);
		noClip = false;
	}

	public EntityFlamethrowerFire(World world, double x, double y, double z) {
		super(world, x, y, z);
		noClip = false;
	}

	@Override
	protected float getGravityVelocity() {
		return 0;
	}

	@Override
	protected float func_70182_d() {
		return 1;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(isEntityAlive()) {
			if(ticksExisted >= particleMaxAge) {
				setDead();
			}
		}
	}

	@Override
	protected void onImpact(MovingObjectPosition mop) {
		if(mop.entityHit != null) {
			final float multiplier = (float) (particleMaxAge - ticksExisted) / particleMaxAge;

			mop.entityHit.setFire((int) (20F * multiplier));

			if(getThrower() instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) getThrower();
				mop.entityHit.attackEntityFrom(DamageSource.causePlayerDamage(player), 5F * multiplier);
			}
		}

		if(rand.nextInt(10) == 0) {
			if(!worldObj.isRemote) {
				if(!setFire(worldObj, mop.blockX, mop.blockY, mop.blockZ, mop.sideHit)) {
					motionX *= 0.25D;
					motionY *= 0.25D;
					motionZ *= 0.25D;
				}
			}
		}
		else {
			motionX *= 0.25D;
			motionY *= 0.25D;
			motionZ *= 0.25D;
		}
	}

	public boolean setFire(World world, int x, int y, int z, int sideHit) {
		switch(sideHit) {
			case 0:
				--y;
				break;

			case 1:
				++y;
				break;

			case 2:
				--z;
				break;

			case 3:
				++z;
				break;

			case 4:
				--x;
				break;

			case 5:
				++x;
				break;
		}

		if(world.isAirBlock(x, y, z)) {
			world.setBlock(x, y, z, Blocks.fire);
		}

		return true;
	}
}
