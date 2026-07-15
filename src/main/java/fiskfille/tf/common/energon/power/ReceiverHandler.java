package fiskfille.tf.common.energon.power;

import fiskfille.tf.common.item.ItemCSD.DimensionalCoords;
import net.minecraft.tileentity.TileEntity;

import java.util.HashSet;
import java.util.Set;

/**
 * Handles all transmitters transmitting energy to this tile
 */
public class ReceiverHandler {
	private final Set<NetworkEntry> transmitters = new HashSet<>();
	private final NetworkEntry owner;

	public ReceiverHandler(TileEntity tile) {
		owner = new NetworkEntry(tile);
	}

	public void add(NetworkEntry transmitter) {
		transmitters.add(transmitter);
	}

	public Set<NetworkEntry> getTransmitters() {
		return transmitters;
	}

	public void remove(DimensionalCoords coords) {
		remove(new NetworkEntry(coords, null));
	}

	public void remove(NetworkEntry transmitter) {
		transmitters.remove(transmitter);
	}

	public NetworkEntry getOwner() {
		return owner;
	}
}
