package fiskfille.tf.common.entity;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

public class EntityTankShell extends EntityThrowable implements IEntityAdditionalSpawnData {
	public boolean allowExplosions;

	public EntityTankShell(final World world) {
		super(world);
	}

	public EntityTankShell(final World world, final EntityLivingBase entity, final boolean explosions) {
		super(world, entity);
		allowExplosions = explosions;
	}

	public EntityTankShell(final World world, final double x, final double y, final double z) {
		super(world, x, y, z);
	}

	@Override
	protected float getGravityVelocity() {
		return 0.04F;
	}

	@Override
	protected float func_70182_d() {
		return 4;
	}

	@Override
	protected void onImpact(final MovingObjectPosition mop) {
		if(!worldObj.isRemote) {
			if(mop.typeOfHit == MovingObjectType.BLOCK) {
				explode(mop.blockX, mop.blockY, mop.blockZ, mop.sideHit);
			}
			else if(mop.typeOfHit == MovingObjectType.ENTITY) {
				worldObj.createExplosion(null, mop.entityHit.posX, mop.entityHit.posY, mop.entityHit.posZ, 1F, allowExplosions);
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

		worldObj.createExplosion(null, x + 0.5F, y + 0.5F, z + 0.5F, 1F, allowExplosions);
	}

	@Override
	public void writeEntityToNBT(final NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setBoolean("Explosions", allowExplosions);
	}

	@Override
	public void readEntityFromNBT(final NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		allowExplosions = nbt.getBoolean("Explosions");
	}

	@Override
	public void writeSpawnData(final ByteBuf buf) {
		buf.writeBoolean(allowExplosions);
	}

	@Override
	public void readSpawnData(final ByteBuf buf) {
		allowExplosions = buf.readBoolean();
	}
}
