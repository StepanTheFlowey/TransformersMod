package fiskfille.tf.common.data.tile;

import fiskfille.tf.common.groundbridge.DataCore;
import fiskfille.tf.common.groundbridge.GroundBridgeError.ErrorContainer;
import fiskfille.tf.common.item.ItemCSD.DimensionalCoords;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;

public class TileDataControlPanel extends TileDataEnergyContainer {
	public ArrayList<DataCore> upgrades = new ArrayList<>();
	public ArrayList<ErrorContainer> errors = new ArrayList<>();
	public DimensionalCoords framePos;
	public DimensionalCoords destination = new DimensionalCoords();
	public int modifiedDestY;
	public int direction;
	public int frameDirection;
	public boolean activationLeverState = false;

	public TileDataControlPanel() {
		super(64000);
	}

	public TileDataControlPanel(final TileDataControlPanel data) {
		super(data);
		upgrades = new ArrayList<>(data.upgrades);
		errors = new ArrayList<>(data.errors);
		framePos = DimensionalCoords.copy(data.framePos);
		destination = DimensionalCoords.copy(data.destination);
		modifiedDestY = data.modifiedDestY;
		direction = data.direction;
		frameDirection = data.frameDirection;
		activationLeverState = data.activationLeverState;
	}

	@Override
	public void toBytes(final ByteBuf buf) {
		super.toBytes(buf);
		buf.writeInt(modifiedDestY);
		buf.writeByte(direction);
		buf.writeByte(frameDirection);
		buf.writeBoolean(activationLeverState);

		buf.writeByte(upgrades.size());
		for(final DataCore core : upgrades) {
			buf.writeByte(core.index);
		}

		buf.writeByte(errors.size());
		for(final ErrorContainer container : errors) {
			container.toBytes(buf);
		}

		if(framePos != null) {
			buf.writeBoolean(true);
			framePos.toBytes(buf);
		}
		else {
			buf.writeBoolean(false);
		}

		destination.toBytes(buf);
	}

	@Override
	public void fromBytes(final ByteBuf buf) {
		super.fromBytes(buf);
		modifiedDestY = buf.readInt();
		direction = buf.readByte();
		frameDirection = buf.readByte();
		activationLeverState = buf.readBoolean();

		upgrades = new ArrayList<>();
		final byte upgradeCount = buf.readByte();
		for(byte i = 0; i < upgradeCount; i++) {
			final DataCore core = DataCore.get(buf.readByte());

			if(core != null) {
				upgrades.add(core);
			}
		}

		errors = new ArrayList<>();
		final byte errorCount = buf.readByte();
		for(byte i = 0; i < errorCount; i++) {
			final ErrorContainer container = ErrorContainer.fromBytes(buf);

			if(container != null) {
				errors.add(container);
			}
		}

		if(buf.readBoolean()) {
			framePos = new DimensionalCoords().fromBytes(buf);
		}

		destination.fromBytes(buf);
	}

	public boolean hasUpgrade(final DataCore core) {
		return upgrades.contains(core);
	}

	@Override
	public boolean matches(final TileData tileData) {
		if(tileData instanceof TileDataControlPanel) {
			final TileDataControlPanel data = (TileDataControlPanel) tileData;
			final boolean frameEquals = data.framePos == null && framePos == null || data.framePos != null && data.framePos.equals(framePos);
			final boolean destinationEquals = data.destination.equals(destination) && data.modifiedDestY == modifiedDestY;

			boolean errorsEqual = data.errors.size() == errors.size();
			if(errorsEqual) {
				for(int i = 0; i < data.errors.size(); i++) {
					if(!data.errors.get(i).equals(errors.get(i))) {
						errorsEqual = false;
						break;
					}
				}
			}

			boolean coresEqual = data.upgrades.size() == upgrades.size();
			if(coresEqual) {
				for(int i = 0; i < data.upgrades.size(); i++) {
					if(data.upgrades.get(i).index != upgrades.get(i).index) {
						coresEqual = false;
						break;
					}
				}
			}

			return super.matches(data) && frameEquals && coresEqual && errorsEqual && destinationEquals && data.activationLeverState == activationLeverState && data.direction == direction && data.frameDirection == frameDirection;
		}

		return false;
	}
}
