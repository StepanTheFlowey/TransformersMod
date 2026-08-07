package fiskfille.tf.common.energon.power;

import fiskfille.tf.common.item.ItemCSD.DimensionalCoords;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class ReceiverEntry extends NetworkEntry {
	private boolean canReach;

	public ReceiverEntry(final DimensionalCoords coordinates) {
		super(coordinates, null);
	}

	public ReceiverEntry(final TileEntity tile) {
		super(tile);
	}

	public static ReceiverEntry readFromNBT(final NBTTagCompound compound) {
		final ReceiverEntry entry = new ReceiverEntry(new DimensionalCoords(compound.getInteger("X"), compound.getInteger("Y"), compound.getInteger("Z"), compound.getInteger("Dim")));
		entry.setCanReach(compound.getBoolean("CanReach"));
		return entry;
	}

	public static ReceiverEntry fromBytes(final ByteBuf buf) {
		final ReceiverEntry entry = new ReceiverEntry(new DimensionalCoords().fromBytes(buf));
		entry.setCanReach(buf.readBoolean());
		return entry;
	}

	@Override
	public void writeToNBT(final NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setBoolean("CanReach", canReach);
	}

	@Override
	public void toBytes(final ByteBuf buf) {
		super.toBytes(buf);
		buf.writeBoolean(canReach);
	}

	public boolean canReach() {
		return canReach;
	}

	public void setCanReach(final boolean canReach) {
		this.canReach = canReach;
	}
}
