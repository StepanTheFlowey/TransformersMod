package fiskfille.tf.common.energon.power;

import fiskfille.tf.common.tileentity.TileEntityMachineContainer;
import net.minecraft.item.ItemStack;

public class EnergyStorageInventory extends EnergyStorage {
	protected final TileEntityMachineContainer owner;

	public EnergyStorageInventory(final TileEntityMachineContainer owner) {
		super(0);
		this.owner = owner;
	}

	@Override
	public EnergyStorageInventory copy() {
		return new EnergyStorageInventory(owner);
	}

	@Override
	public float remove(final float amount, final boolean simulate) {
		if(amount <= 0) {
			return 0;
		}

		float max = Math.min(amount, getEnergy());
		float removed = 0;

		for(int i = 0; i < owner.getSizeInventory(); ++i) {
			final ItemStack stack = owner.getStackInSlot(i);

			if(stack != null && stack.getItem() instanceof IEnergyContainerItem) {
				final IEnergyContainerItem container = (IEnergyContainerItem) stack.getItem();
				float extracted = container.extractEnergy(stack, max, true);

				extracted = Math.min(extracted, max);
				removed += container.extractEnergy(stack, extracted, simulate || owner.getWorldObj().isRemote);
				max -= extracted;

				if(max <= 0) {
					break;
				}
			}
		}

		energy -= removed;

		return removed;
	}

	@Override
	public float add(final float amount, final boolean simulate) {
		if(amount <= 0) {
			return 0;
		}

		float max = Math.max(Math.min(amount, getMaxEnergy() - getEnergy()), 0);
		float added = 0;

		for(int i = 0; i < owner.getSizeInventory(); ++i) {
			final ItemStack stack = owner.getStackInSlot(i);

			if(stack != null && stack.getItem() instanceof IEnergyContainerItem) {
				final IEnergyContainerItem container = (IEnergyContainerItem) stack.getItem();
				float extracted = container.receiveEnergy(stack, max, true);

				extracted = Math.min(extracted, max);
				added += container.receiveEnergy(stack, extracted, simulate || owner.getWorldObj().isRemote);
				max -= extracted;

				if(max <= 0) {
					break;
				}
			}
		}

		energy += added;

		return added;
	}

	@Override
	public float getEnergy() {
		energy = 0;

		for(int i = 0; i < owner.getSizeInventory(); ++i) {
			final ItemStack stack = owner.getStackInSlot(i);

			if(stack != null && stack.getItem() instanceof IEnergyContainerItem) {
				energy += ((IEnergyContainerItem) stack.getItem()).getEnergyStored(stack);
			}
		}

		return energy;
	}

	@Override
	public int getMaxEnergy() {
		int max = 0;

		for(int i = 0; i < owner.getSizeInventory(); ++i) {
			final ItemStack stack = owner.getStackInSlot(i);

			if(stack != null && stack.getItem() instanceof IEnergyContainerItem) {
				max += ((IEnergyContainerItem) stack.getItem()).getEnergyCapacity(stack);
			}
		}

		return max;
	}

	@Override
	public void set(final float amount) {
		energy = Math.min(getMaxEnergy(), Math.max(0, amount));
	}

	@Override
	public void setUsage(final float usage) {
		energyUsage = usage;
		lastEnergy = getEnergy();
	}
}
