package fiskfille.tf.common.data.tile;

import fiskfille.tf.common.fluid.FluidTankTF;
import fiskfille.tf.helper.TFHelper;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fluids.FluidStack;

public class TileDataEnergonTank extends TileData {
	public FluidTankTF tank;

	public TileDataEnergonTank() {
	}

	public TileDataEnergonTank(final FluidTankTF fluidTank) {
		tank = fluidTank;
	}

	public TileDataEnergonTank(final int max) {
		this(new FluidTankTF(max));
	}

	public TileDataEnergonTank(final TileDataEnergonTank data) {
		super(data);
		tank = data.tank.copy();
	}

	@Override
	public void toBytes(final ByteBuf buf) {
		super.toBytes(buf);
		buf.writeInt(getCapacity());
		tank.toBytes(buf);
	}

	@Override
	public void fromBytes(final ByteBuf buf) {
		super.fromBytes(buf);
		tank = new FluidTankTF(buf.readInt());
		tank.fromBytes(buf);
	}

	@Override
	public void serverTick() {
		tank.calculateUsage();
		super.serverTick();
	}

	@Override
	public void clientTick() {
		TFHelper.applyFluidUsage(tank);
	}

	public FluidStack getFluid() {
		return tank.getFluid();
	}

	public void setFluid(final FluidStack fluid) {
		tank.setFluid(fluid);
	}

	public int getFluidAmount() {
		return tank.getFluidAmount();
	}

	public int getUsage() {
		return tank.getUsage();
	}

	public int getCapacity() {
		return tank.getCapacity();
	}

	@Override
	public boolean matches(final TileData tileData) {
		if(tileData instanceof TileDataEnergonTank) {
			final TileDataEnergonTank data = (TileDataEnergonTank) tileData;

			return getUsage() == data.getUsage() && FluidStack.areFluidStackTagsEqual(getFluid(), data.getFluid());
		}

		return false;
	}
}
