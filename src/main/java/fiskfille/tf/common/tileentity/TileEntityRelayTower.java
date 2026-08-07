package fiskfille.tf.common.tileentity;

import com.google.common.collect.Maps;
import fiskfille.tf.common.chunk.ForcedChunk;
import fiskfille.tf.common.chunk.SubTicket;
import fiskfille.tf.common.chunk.TFChunkManager;
import fiskfille.tf.common.data.tile.TileData;
import fiskfille.tf.common.data.tile.TileDataRelay;
import fiskfille.tf.common.energon.power.*;
import fiskfille.tf.common.item.ItemCSD.DimensionalCoords;
import fiskfille.tf.helper.TFEnergyHelper;
import fiskfille.tf.helper.TFTileHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.util.Constants.NBT;

import java.util.Map;
import java.util.Set;

public class TileEntityRelayTower extends TileEntityTF implements IEnergyTransmitter, IEnergyReceiver, IChunkLoaderTile, IMultiTile {
	public final ReceiverHandler receiverHandler = new ReceiverHandler(this);
	public final EnergyStorage storage = new EnergyStorageRelay(this);
	public final Map<DimensionalCoords, Float> netEnergyTransfer = Maps.newHashMap();
	public TileDataRelay data = new TileDataRelay();
	public float energyTransfer;
	public float energyReceived;
	public float energyExtracted;

	public Ticket chunkTicket;

	@Override
	public void updateEntity() {
		super.updateEntity();

		if(!data.isInitialized()) {
			data.initialize(this);
		}

		if(isValid(getBlockMetadata())) {
			if(!worldObj.isRemote) {
				if(chunkTicket == null) {
					final Ticket ticket = TFChunkManager.getTicketForChunk(ForcedChunk.fromTile(this));

					if(ticket != null) {
						final SubTicket subTicket = SubTicket.fromTile(this);
						forceChunks(subTicket.assign(ticket));
					}
				}

				data.serverTickPre();
				data.isPowered = energyTransfer > 0 || TFEnergyHelper.canPowerChainReach(this);
				data.invertCurrent.clear();

				for(final Map.Entry<DimensionalCoords, Float> e : netEnergyTransfer.entrySet()) {
					if(e.getValue() < 0) {
						data.invertCurrent.add(e.getKey());
					}
				}

				data.serverTick();
			}

			final TileData prevData = TFTileHelper.getTileData(new DimensionalCoords(this));

			if(prevData instanceof TileDataRelay) {
				data = new TileDataRelay((TileDataRelay) prevData);
			}
		}

		energyTransfer = 0;
		energyReceived = 0;
		energyTransfer = 0;
		netEnergyTransfer.clear();
	}

	public float getNetTransfer(final DimensionalCoords coords) {
		return netEnergyTransfer.containsKey(coords) ? netEnergyTransfer.get(coords) : 0;
	}

	public boolean isValid(final int metadata) {
		return metadata < 4;
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1).addCoord(0, 1.25F, 0);

		if(isValid(getBlockMetadata())) {
			final Set<ReceiverEntry> receivers = data.transmissionHandler.getReceivers();

			for(final ReceiverEntry entry : receivers) {
				final TileEntity tile = entry.getTile();

				if(tile != null) {
					bounds = bounds.func_111270_a(tile.getRenderBoundingBox());
				}
			}
		}

		return bounds;
	}

	@Override
	public void readCustomNBT(final NBTTagCompound nbt) {
		if(nbt.getBoolean("Base")) {
			if(nbt.hasKey("ConfigDataTF", NBT.TAG_COMPOUND)) {
				final NBTTagCompound config = nbt.getCompoundTag("ConfigDataTF");
				data.transmissionHandler.readFromNBT(config);
			}
		}
	}

	@Override
	public void writeCustomNBT(final NBTTagCompound nbt) {
		final boolean base = isValid(getBlockMetadata());
		nbt.setBoolean("Base", base);

		if(base && !data.transmissionHandler.getReceivers().isEmpty()) {
			final NBTTagCompound config = nbt.getCompoundTag("ConfigDataTF");
			data.transmissionHandler.writeToNBT(config);
			nbt.setTag("ConfigDataTF", config);
		}
	}

	@Override
	public ReceiverHandler getReceiverHandler() {
		return receiverHandler;
	}

	@Override
	public TransmissionHandler getTransmissionHandler() {
		return data.transmissionHandler;
	}

	@Override
	public int getTransmissionRate() {
		return 50;
	}

	@Override
	public int getRange() {
		return 15;
	}

	@Override
	public boolean canReceiveEnergy(final TileEntity from) {
		return isValid(getBlockMetadata());
	}

	@Override
	public Vec3 getEnergyOutputOffset() {
		return getEnergyInputOffset();
	}

	@Override
	public Vec3 getEnergyInputOffset() {
		return Vec3.createVectorHelper(0, 1.5F, 0);
	}

	@Override
	public int getMapColor() {
		return 0x0077CC;
	}

	@Override
	public float receiveEnergy(float amount, final boolean simulate) {
		amount = Math.min(amount, getTransmissionRate() - energyReceived);

		if(amount <= 0) {
			return 0;
		}

		final float f = storage.add(amount, simulate);
		if(!simulate) {
			energyTransfer += f;
			energyReceived += f;
		}

		return f;
	}

	@Override
	public float extractEnergy(float amount, final boolean simulate) {
		amount = Math.min(amount, getTransmissionRate() - energyExtracted);

		if(amount <= 0) {
			return 0;
		}

		final float f = storage.remove(amount, simulate);

		if(!simulate) {
			energyTransfer += f;
			energyExtracted += f;
		}

		return f;
	}

	@Override
	public float getEnergy() {
		return storage.getEnergy();
	}

	@Override
	public float getMaxEnergy() {
		return storage.getMaxEnergy();
	}

	@Override
	public float getEnergyUsage() {
		return storage.getUsage();
	}

	@Override
	public void invalidate() {
		super.invalidate();

		if(!worldObj.isRemote) {
			data.kill();
			releaseChunks();
		}
	}

	@Override
	public void forceChunks(final SubTicket subTicket) {
		releaseChunks();
		chunkTicket = subTicket.owner;
		TFChunkManager.forceChunk(subTicket.owner, ForcedChunk.fromTile(this));
	}

	public void releaseChunks() {
		if(chunkTicket != null) {
			TFChunkManager.releaseChunk(SubTicket.fromTile(chunkTicket, this), ForcedChunk.fromTile(this));
			chunkTicket = null;
		}
	}

	@Override
	public int[] getBaseOffsets(final int metadata) {
		return new int[]{0, -metadata / 4, 0};
	}
}
