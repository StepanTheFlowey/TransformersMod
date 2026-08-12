package fiskfille.tf.common.tileentity;

import fiskfille.tf.common.energon.power.IEnergyContainer;
import fiskfille.tf.common.energon.power.IEnergyReceiver;
import fiskfille.tf.common.energon.power.IReceiverRender;
import fiskfille.tf.common.energon.power.ReceiverHandler;
import fiskfille.tf.helper.TFTileHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;

public final class TileEntityEnergyPort extends TileEntityTF implements IEnergyReceiver, IReceiverRender {
	public final ReceiverHandler receiverHandler = new ReceiverHandler(this);

	public IEnergyContainer getReceiver() {
		final ForgeDirection dir = ForgeDirection.getOrientation(getBlockMetadata());
		final TileEntity tile = TFTileHelper.getTileBase(worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ));

		if(tile instanceof IEnergyContainer && !(tile instanceof TileEntityEnergyPort)) {
			return (IEnergyContainer) tile;
		}

		return null;
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1);
	}

	@Override
	public void readCustomNBT(final NBTTagCompound nbt) {
	}

	@Override
	public void writeCustomNBT(final NBTTagCompound nbt) {
	}

	@Override
	public ReceiverHandler getReceiverHandler() {
		return receiverHandler;
	}

	@Override
	public boolean canReceiveEnergy(final TileEntity from) {
		return true;
	}

	@Override
	public Vec3 getEnergyInputOffset() {
		return getInputVec(3.5F);
	}

	@Override
	public Vec3 getRenderInputOffset() {
		return getInputVec(1.5F);
	}

	public Vec3 getInputVec(final float height) {
		final ForgeDirection dir = ForgeDirection.getOrientation(getBlockMetadata());
		final float f = 0.0625F * height;

		switch(dir) {
			case UP:
				return Vec3.createVectorHelper(0, 0.5F - f, 0);

			case DOWN:
				return Vec3.createVectorHelper(0, -0.5F + f, 0);
		}

		final int[] rotations = {2, 0, 1, 3};
		final float yaw = rotations[dir.ordinal() - 2] * 90;

		final Vec3 vec3 = Vec3.createVectorHelper(0, 0, 0.5F - f);
		vec3.rotateAroundY(-yaw * (float) Math.PI / 180F);
		return vec3;
	}

	@Override
	public int getMapColor() {
		return 0x12547A;
	}

	@Override
	public float receiveEnergy(final float amount, final boolean simulate) {
		final IEnergyContainer container = getReceiver();

		if(container != null) {
			return container.receiveEnergy(amount, simulate);
		}

		return 0;
	}

	@Override
	public float extractEnergy(final float amount, final boolean simulate) {
		final IEnergyContainer container = getReceiver();

		if(container != null) {
			return container.extractEnergy(amount, simulate);
		}

		return 0;
	}

	@Override
	public float getEnergy() {
		final IEnergyContainer container = getReceiver();

		if(container != null) {
			return container.getEnergy();
		}

		return 0;
	}

	@Override
	public int getMaxEnergy() {
		final IEnergyContainer container = getReceiver();

		if(container != null) {
			return container.getMaxEnergy();
		}

		return 0;
	}

	@Override
	public float getEnergyUsage() {
		final IEnergyContainer container = getReceiver();

		if(container != null) {
			return container.getEnergyUsage();
		}

		return 0;
	}
}
