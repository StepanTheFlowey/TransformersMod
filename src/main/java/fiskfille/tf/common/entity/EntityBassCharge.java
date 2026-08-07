package fiskfille.tf.common.entity;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

public class EntityBassCharge extends EntityThrowable {
	public EntityBassCharge(final World world) {
		super(world);
		setSize(1F, 1F);
	}

	public EntityBassCharge(final World world, final EntityLivingBase entity) {
		super(world, entity);
		setSize(1F, 1F);
	}

	public EntityBassCharge(final World world, final double x, final double y, final double z) {
		super(world, x, y, z);
		setSize(1F, 1F);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(ticksExisted > 20) {
			setDead();
		}
	}

	@Override
	protected float getGravityVelocity() {
		return 0F;
	}

	@Override
	protected float func_70182_d() {
		return 3F;
	}

	@Override
	protected void onImpact(final MovingObjectPosition mop) {
		if(mop.entityHit != null) {
			mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, getThrower()), 2F * (1F - ticksExisted / 20F));
			mop.entityHit.hurtResistantTime = 0;
		}
		else if(mop.typeOfHit == MovingObjectType.BLOCK) {
			final int x = mop.blockX, y = mop.blockY, z = mop.blockZ;
			if(worldObj.getBlock(x, y, z).getMaterial().equals(Material.glass)) {
				worldObj.playAuxSFX(2001, x, y + 1, z, Block.getIdFromBlock(worldObj.getBlock(x, y, z)) + (worldObj.getBlockMetadata(x, y, z) << 12));
				worldObj.setBlockToAir(x, y, z);
			}
		}

		setThrowableHeading(motionX, motionY, motionZ, -0.001F, 0);
	}
}
