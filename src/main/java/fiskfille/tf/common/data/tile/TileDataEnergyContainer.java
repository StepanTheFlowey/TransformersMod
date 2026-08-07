package fiskfille.tf.common.data.tile;

import fiskfille.tf.common.energon.power.EnergyStorage;
import fiskfille.tf.helper.TFEnergyHelper;
import io.netty.buffer.ByteBuf;

public class TileDataEnergyContainer extends TileData {
	public EnergyStorage storage;

	public TileDataEnergyContainer() {
	}

	public TileDataEnergyContainer(final EnergyStorage energyStorage) {
		storage = energyStorage;
	}

	public TileDataEnergyContainer(final float max) {
		this(new EnergyStorage(max));
	}

	public TileDataEnergyContainer(final TileDataEnergyContainer data) {
		super(data);
		storage = data.storage.copy();
	}

	@Override
	public void toBytes(final ByteBuf buf) {
		super.toBytes(buf);
		buf.writeFloat(getMaxEnergy());
		storage.toBytes(buf);
	}

	@Override
	public void fromBytes(final ByteBuf buf) {
		super.fromBytes(buf);
		storage = new EnergyStorage(buf.readFloat());
		storage.fromBytes(buf);
	}

	@Override
	public void serverTick() {
		storage.calculateUsage();
		super.serverTick();
	}

	@Override
	public void clientTick() {
		TFEnergyHelper.applyEnergyUsage(storage);
	}

	public float getEnergy() {
		return storage.getEnergy();
	}

	public float getEnergyUsage() {
		return storage.getUsage();
	}

	public float getMaxEnergy() {
		return storage.getMaxEnergy();
	}

	@Override
	public boolean matches(final TileData tileData) {
		if(tileData instanceof TileDataEnergyContainer) {
			final TileDataEnergyContainer data = (TileDataEnergyContainer) tileData;

			return Math.abs(getEnergyUsage() - data.getEnergyUsage()) <= 0.001F;
		}

		return false;
	}
}
