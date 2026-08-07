package fiskfille.tf.common.energon.power;

import fiskfille.tf.common.tileentity.TileEntityRelayTower;
import fiskfille.tf.helper.TFEnergyHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class EnergyStorageRelay extends EnergyStorage {
	protected final TileEntityRelayTower relay;

	public EnergyStorageRelay(final TileEntityRelayTower tile) {
		super(0);
		relay = tile;
	}

	@Override
	public EnergyStorageRelay copy() {
		return new EnergyStorageRelay(relay);
	}

	@Override
	public void toBytes(final ByteBuf buf) {}

	@Override
	public void fromBytes(final ByteBuf buf) {}

	@Override
	public void readFromNBT(final NBTTagCompound nbt) {}

	@Override
	public void writeToNBT(final NBTTagCompound nbt) {}

	@Override
	public float remove(final float amount, final boolean simulate) {
		final List<ReceiverEntry> receivers = TFEnergyHelper.getReceiversToPower(relay);
		float removed = 0;

		for(final ReceiverEntry entry : receivers) {
			final float f = entry.getReceiver().extractEnergy(amount / receivers.size(), simulate);
			removed += f;

			relay.netEnergyTransfer.put(entry.getCoords(), relay.getNetTransfer(entry.getCoords()) - f);
		}

		return removed;
	}

	@Override
	public float add(final float amount, final boolean simulate) {
		final List<ReceiverEntry> receivers = TFEnergyHelper.getReceiversToPower(relay);
		float added = 0;

		for(final ReceiverEntry entry : receivers) {
			final float f = entry.getReceiver().receiveEnergy(amount / receivers.size(), simulate);
			added += f;

			relay.netEnergyTransfer.put(entry.getCoords(), relay.getNetTransfer(entry.getCoords()) + f);
		}

		return added;
	}

	@Override
	public float getEnergy() {
		final List<ReceiverEntry> receivers = TFEnergyHelper.getReceiverDescendants(relay);
		float energy = 0;

		for(final ReceiverEntry entry : receivers) {
			energy += entry.getReceiver().getEnergy();
		}

		return energy;
	}

	@Override
	public float getMaxEnergy() {
		final List<ReceiverEntry> receivers = TFEnergyHelper.getReceiverDescendants(relay);
		float maxEnergy = 0;

		for(final ReceiverEntry entry : receivers) {
			maxEnergy += entry.getReceiver().getMaxEnergy();
		}

		return maxEnergy;
	}

	@Override
	public void set(final float amount) {
	}

	@Override
	public void setUsage(final float usage) {
		energyUsage = usage;
		lastEnergy = getEnergy();
	}

	@Override
	public void calculateUsage() {
		final float energy = getEnergy();
		energyUsage = energy - lastEnergy;
		lastEnergy = energy;
	}
}
