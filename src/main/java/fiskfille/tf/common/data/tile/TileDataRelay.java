package fiskfille.tf.common.data.tile;

import fiskfille.tf.common.energon.power.TransmissionHandler;
import fiskfille.tf.common.item.ItemCSD.DimensionalCoords;
import fiskfille.tf.helper.TFTileHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;

import java.util.ArrayList;

public class TileDataRelay extends TileData {
	public TransmissionHandler transmissionHandler = new TransmissionHandler();
	public ArrayList<DimensionalCoords> invertCurrent = new ArrayList<>();
	public boolean isPowered;

	public TileDataRelay() {}

	public TileDataRelay(final TileDataRelay data) {
		super(data);

		transmissionHandler = data.transmissionHandler;
		transmissionHandler.setNeedsUpdate(false);
		invertCurrent.addAll(data.invertCurrent);
		isPowered = data.isPowered;
	}

	@Override
	public <T extends TileEntity> T initialize(final T tile) {
		if(!isInitialized()) {
			transmissionHandler.setOwner(tile);
		}

		return super.initialize(tile);
	}

	@Override
	public void toBytes(final ByteBuf buf) {
		super.toBytes(buf);

		transmissionHandler.toBytes(buf);
		buf.writeBoolean(isPowered);

		buf.writeInt(invertCurrent.size());
		for(final DimensionalCoords coords : invertCurrent) {
			coords.toBytes(buf);
		}
	}

	@Override
	public void fromBytes(final ByteBuf buf) {
		super.fromBytes(buf);

		transmissionHandler.fromBytes(buf);
		isPowered = buf.readBoolean();

		final int size = buf.readInt();
		invertCurrent.ensureCapacity(size);
		for(int i = 0; i < size; ++i) {
			invertCurrent.add(new DimensionalCoords().fromBytes(buf));
		}
	}

	public void serverTickPre() {
		final WorldServer world = MinecraftServer.getServer().worldServerForDimension(getCoords().dimension);

		if(world != null) {
			transmissionHandler.onUpdate(world);
		}
	}

	@Override
	public void kill() {
		if(TFTileHelper.getTileData(getCoords()) != null) {
			transmissionHandler.kill();
		}

		super.kill();
	}

	@Override
	public boolean matches(final TileData tileData) {
		if(tileData instanceof TileDataRelay) {
			final TileDataRelay data = (TileDataRelay) tileData;
			return isPowered == data.isPowered && !transmissionHandler.needsUpdate() && invertCurrent.size() == data.invertCurrent.size() && invertCurrent.containsAll(data.invertCurrent);
		}

		return false;
	}
}
