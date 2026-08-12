package fiskfille.tf.common.energon.power;

import fiskfille.tf.common.item.ItemCSD.DimensionalCoords;
import fiskfille.tf.helper.TFEnergyHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Handles all receivers this transmitter is transmitting to
 */
public class TransmissionHandler {
	private final HashSet<ReceiverEntry> receivers = new HashSet<>();
	private final ArrayDeque<ReceiverEntry> queuedReceivers = new ArrayDeque<>();

	private NetworkEntry owner;
	private boolean needsUpdate = false;

	public void toBytes(final ByteBuf buf) {
		buf.writeInt(receivers.size());
		for(final ReceiverEntry receiver : receivers) {
			receiver.toBytes(buf);
		}
	}

	public void fromBytes(final ByteBuf buf) {
		final int receiverCount = buf.readInt();
		for(int i = 0; i < receiverCount; i++) {
			receivers.add(ReceiverEntry.fromBytes(buf));
		}
	}

	public void onUpdate(final World world) {
		if(owner == null) {
			return;
		}

		for(final Iterator<ReceiverEntry> iterator = receivers.iterator(); iterator.hasNext(); ) {
			final ReceiverEntry receiver = iterator.next();
			TileEntity receiverTile = receiver.getTile();

			if(receiverTile == null) {
				receiver.load(world);
				receiverTile = receiver.getTile();
			}

			if(receiverTile instanceof IEnergyReceiver) {
				final boolean invalid = world.getChunkProvider().chunkExists(receiverTile.xCoord >> 4, receiverTile.zCoord >> 4) && (receiverTile.isInvalid() || !exists(world, receiverTile));
				final boolean outRange = !TFEnergyHelper.isInRange(owner.getTile(), receiverTile);
				final boolean destroyed = !invalid && !exists(world, receiverTile);

				if(!invalid && !outRange && !destroyed) {
					final boolean flag = receiver.canReach();
					receiver.setCanReach(canPowerReach(receiver));

					if(flag != receiver.canReach()) {
						needsUpdate = true;
					}

					continue;
				}
			}

			iterator.remove();
			needsUpdate = true;
		}

		while(!queuedReceivers.isEmpty()) {
			final ReceiverEntry receiver = queuedReceivers.poll();
			needsUpdate |= processQueue(world, receiver);
		}

		if(needsUpdate) {
			owner.getTile().markDirty();
		}
	}

	private boolean canPowerReach(final ReceiverEntry entry) {
		final DimensionalCoords coords = entry.getCoords();
		final Vec3 receiver = entry.getReceiver().getEnergyInputOffset().addVector(coords.posX + 0.5F, coords.posY + 0.5F, coords.posZ + 0.5F);

		final TileEntity tile = owner.getTile();
		final Vec3 transmitter = owner.getTransmitter().getEnergyOutputOffset().addVector(tile.xCoord + 0.5F, tile.yCoord + 0.5F, tile.zCoord + 0.5F);

		final double deltaScale = 1 / receiver.distanceTo(transmitter);
		transmitter.xCoord += (receiver.xCoord - transmitter.xCoord) * deltaScale;
		transmitter.yCoord += (receiver.yCoord - transmitter.yCoord) * deltaScale;
		transmitter.zCoord += (receiver.zCoord - transmitter.zCoord) * deltaScale;

		final MovingObjectPosition result = TFEnergyHelper.rayTraceBlocks(tile.getWorldObj(), transmitter, receiver);
		if(result == null || result.typeOfHit == MovingObjectPosition.MovingObjectType.MISS) {
			return true;
		}

		return result.hitVec.distanceTo(receiver) < 0.0625D;
	}

	public void kill() {
		for(final ReceiverEntry entry : receivers) {
			entry.getReceiver().getReceiverHandler().remove(owner);
		}
	}

	private boolean processQueue(final World world, final ReceiverEntry receiver) {
		if(!receivers.contains(receiver)) {
			receiver.load(world);
			final TileEntity tile = receiver.getTile();

			if(tile instanceof IEnergyTransmitter && TFEnergyHelper.getDescendants(receiver.getTransmitter()).contains(owner.getCoords())) {
				return false;
			}
			else if(tile instanceof IEnergyReceiver && ((IEnergyReceiver) tile).canReceiveEnergy(owner.getTile())) {
				add(receiver);
				return true;
			}
		}

		return false;
	}

	public void add(final ReceiverEntry receiver) {
		if(!receivers.contains(receiver)) {
			receivers.add(receiver);

			if(receiver.getTile() != null) {
				final IEnergyReceiver energyReceiver = receiver.getReceiver();
				energyReceiver.getReceiverHandler().add(owner);
			}

			needsUpdate = true;
		}
	}

	public void remove(final ReceiverEntry entry) {
		if(receivers.contains(entry)) {
			receivers.remove(entry);

			if(entry.getTile() != null) {
				final IEnergyReceiver receiver = entry.getReceiver();
				receiver.getReceiverHandler().remove(owner);
			}

			needsUpdate = true;
		}
	}

	private boolean exists(final World world, final TileEntity tile) {
		return world.getBlock(tile.xCoord, tile.yCoord, tile.zCoord) == tile.getBlockType();
	}

	public void readFromNBT(final NBTTagCompound nbt) {
		receivers.clear();
		queuedReceivers.clear();

		final NBTTagCompound energy = nbt.getCompoundTag("EmB");
		final NBTTagList receiverList = energy.getTagList("Receivers", NBT.TAG_COMPOUND);

		for(int i = 0; i < receiverList.tagCount(); ++i) {
			final NBTTagCompound receiverTag = receiverList.getCompoundTagAt(i);
			queue(ReceiverEntry.readFromNBT(receiverTag));
		}
	}

	public void writeToNBT(final NBTTagCompound nbt) {
		final NBTTagList receiverList = new NBTTagList();

		for(final ReceiverEntry receiver : receivers) {
			writeReceiver(receiverList, receiver);
		}

		for(final ReceiverEntry receiver : queuedReceivers) {
			writeReceiver(receiverList, receiver);
		}

		final NBTTagCompound energy = nbt.getCompoundTag("EmB");
		energy.setTag("Receivers", receiverList);
		nbt.setTag("EmB", energy);
	}

	private void writeReceiver(final NBTTagList list, final ReceiverEntry receiver) {
		final NBTTagCompound tag = new NBTTagCompound();
		receiver.writeToNBT(tag);
		list.appendTag(tag);
	}

	public void queue(final ReceiverEntry receiver) {
		queuedReceivers.add(receiver);
	}

	public void reset(final World world, final Set<ReceiverEntry> newReceivers) {
		for(final ReceiverEntry receiver : receivers) {
			final ReceiverHandler receiverHandler = TFEnergyHelper.getReceiverHandler(receiver.getTile());

			if(receiverHandler != null) {
				receiverHandler.remove(owner);
			}
		}

		receivers.clear();
		queuedReceivers.clear();

		for(final ReceiverEntry receiver : newReceivers) {
			processQueue(world, receiver);
		}

		needsUpdate = true;
	}

	public HashSet<ReceiverEntry> getReceivers() {
		return receivers;
	}

	public ReceiverEntry getReceiver(final DimensionalCoords coords) {
		for(final ReceiverEntry receiver : receivers) {
			if(receiver.getCoords().equals(coords)) {
				return receiver;
			}
		}

		return null;
	}

	public NetworkEntry getOwner() {
		return owner;
	}

	public void setOwner(final TileEntity tile) {
		owner = new NetworkEntry(tile);
	}

	public boolean needsUpdate() {
		return needsUpdate;
	}

	public void setNeedsUpdate(final boolean flag) {
		needsUpdate = flag;
	}
}
