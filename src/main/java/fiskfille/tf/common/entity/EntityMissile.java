package fiskfille.tf.common.entity;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import fiskfille.tf.common.achievement.TFAchievements;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

public class EntityMissile extends EntityThrowable implements IEntityAdditionalSpawnData {
	public boolean isInStealthMode;
	public boolean allowExplosions;

	public EntityMissile(final World world) {
		super(world);
	}

	public EntityMissile(final World world, final EntityLivingBase entity, final boolean explosions, final boolean stealthMode) {
		super(world, entity);
		isInStealthMode = stealthMode;
		allowExplosions = explosions;
		setThrowableHeading(motionX, motionY, motionZ, func_70182_d(), 1F);
	}

	public EntityMissile(final World world, final double x, final double y, final double z) {
		super(world, x, y, z);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if(ticksExisted < 5 && !isInStealthMode) {
			posY -= 0.2F;
		}

		for(int i = 0; i < 20; ++i) {
			final float spread = (rand.nextFloat() - 0.5F) / 4;
			worldObj.spawnParticle("smoke", posX + spread, posY + spread, posZ + spread, 0, 0, 0);
		}
	}

	@Override
	protected float getGravityVelocity() {
		return isInStealthMode ? 0.05F : 0.005F;
	}

	@Override
	protected float func_70182_d() {
		return isInStealthMode ? 2 : 4;
	}

	@Override
	protected void onImpact(final MovingObjectPosition mop) {
		if(!worldObj.isRemote) {
			if(mop.typeOfHit == MovingObjectType.BLOCK) {
				explode(mop.blockX, mop.blockY, mop.blockZ, mop.sideHit);
			}
			else if(mop.typeOfHit == MovingObjectType.ENTITY) {
				worldObj.createExplosion(null, mop.entityHit.posX, mop.entityHit.posY, mop.entityHit.posZ, 4, allowExplosions);

				if(mop.entityHit instanceof EntityBat && getThrower() instanceof EntityPlayer) {
					final EntityPlayer player = (EntityPlayer) getThrower();

					if(player.getDistanceSqToEntity(mop.entityHit) >= 25D) {
						player.addStat(TFAchievements.sharpshooter, 1);
					}
				}
			}
		}

		setDead();
	}

	public void explode(int x, int y, int z, final int sideHit) {
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

		worldObj.createExplosion(null, x + 0.5F, y + 0.5F, z + 0.5F, 4, allowExplosions);
	}

	@Override
	public void writeEntityToNBT(final NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setBoolean("Explosions", allowExplosions);
		nbt.setBoolean("StealthForce", isInStealthMode);
	}

	@Override
	public void readEntityFromNBT(final NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		allowExplosions = nbt.getBoolean("Explosions");
		isInStealthMode = nbt.getBoolean("StealthForce");
	}

	@Override
	public void writeSpawnData(final ByteBuf buf) {
		buf.writeBoolean(allowExplosions);
		buf.writeBoolean(isInStealthMode);
	}

	@Override
	public void readSpawnData(final ByteBuf buf) {
		allowExplosions = buf.readBoolean();
		isInStealthMode = buf.readBoolean();
	}
}
