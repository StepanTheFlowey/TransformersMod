package fiskfille.tf.common.tileentity;

import fiskfille.tf.common.energon.power.EnergyStorage;
import fiskfille.tf.common.energon.power.EnergyStorageInventory;
import fiskfille.tf.common.energon.power.IEnergyReceiver;
import fiskfille.tf.common.energon.power.ReceiverHandler;
import fiskfille.tf.common.item.TFItems;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

public class TileEntityColumn extends TileEntityMachineContainer implements IEnergyReceiver, IMultiTile {
	public final ReceiverHandler receiverHandler = new ReceiverHandler(this);
	public final EnergyStorage storage = new EnergyStorageInventory(this, this);

	@Override
	public void updateEntity() {
		if(getBlockMetadata() < 4) {
			super.updateEntity();
			storage.calculateUsage();
		}
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return super.getRenderBoundingBox().addCoord(0, 1, 0).expand(0.25F, 0, 0.25F);
	}

	@Override
	public int getSizeInventory() {
		return 6;
	}

	@Override
	public void readCustomNBT(final NBTTagCompound nbt) {
		super.readCustomNBT(nbt);
		storage.readFromNBT(nbt);
	}

	@Override
	public void writeCustomNBT(final NBTTagCompound nbt) {
		super.writeCustomNBT(nbt);
		storage.writeToNBT(nbt);
	}

	@Override
	public float receiveEnergy(final float amount, final boolean simulate) {
		if(!canActivate()) {
			return 0;
		}

		return storage.add(amount, simulate);
	}

	@Override
	public float extractEnergy(final float amount, final boolean simulate) {
		if(!canActivate()) {
			return 0;
		}

		return storage.remove(amount, simulate);
	}

	@Override
	public float getEnergy() {
		return storage.getEnergy();
	}

	@Override
	public float getMaxEnergy() {
		return storage.getMaxEnergy();
	}

	@Override
	public float getEnergyUsage() {
		return storage.getUsage();
	}

	@Override
	public ReceiverHandler getReceiverHandler() {
		return receiverHandler;
	}

	@Override
	public boolean canReceiveEnergy(final TileEntity from) {
		return getBlockMetadata() < 4;
	}

	@Override
	public Vec3 getEnergyInputOffset() {
		return Vec3.createVectorHelper(0, 1.5D, 0);
	}

	@Override
	public int getMapColor() {
		return 0xFF0000;
	}

	@Override
	public String getInventoryName() {
		return "gui.energy_column";
	}

	@Override
	public boolean isItemValidForSlot(final int slot, final ItemStack itemstack) {
		return itemstack.getItem() == TFItems.powerCanister;
	}

	@Override
	public int[] getBaseOffsets(final int metadata) {
		return new int[]{0, -metadata / 4, 0};
	}
}
