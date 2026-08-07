package fiskfille.tf.common.energon.power;

import fiskfille.tf.common.item.ItemCSD.DimensionalCoords;
import net.minecraft.tileentity.TileEntity;

import java.util.HashSet;

/**
 * Handles all transmitters transmitting energy to this tile
 */
public class ReceiverHandler {
	private final HashSet<NetworkEntry> transmitters = new HashSet<>();
	private final NetworkEntry owner;

	public ReceiverHandler(final TileEntity tile) {
		owner = new NetworkEntry(tile);
	}

	public void add(final NetworkEntry transmitter) {
		transmitters.add(transmitter);
	}

	public HashSet<NetworkEntry> getTransmitters() {
		return transmitters;
	}

	public void remove(final DimensionalCoords coords) {
		remove(new NetworkEntry(coords, null));
	}

	public void remove(final NetworkEntry transmitter) {
		transmitters.remove(transmitter);
	}

	public NetworkEntry getOwner() {
		return owner;
	}
}
