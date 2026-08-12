package fiskfille.tf.common.tileentity;

import fiskfille.tf.TransformersAPI;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.common.chunk.ForcedChunk;
import fiskfille.tf.common.chunk.SubTicket;
import fiskfille.tf.common.chunk.TFChunkManager;
import fiskfille.tf.common.data.tile.TileData;
import fiskfille.tf.common.data.tile.TileDataTransmitter;
import fiskfille.tf.common.energon.Energon;
import fiskfille.tf.common.energon.power.*;
import fiskfille.tf.common.fluid.FluidEnergon;
import fiskfille.tf.common.fluid.FluidTankTF;
import fiskfille.tf.common.fluid.IFluidHandlerTF;
import fiskfille.tf.common.fluid.TFFluids;
import fiskfille.tf.common.item.ItemCSD.DimensionalCoords;
import fiskfille.tf.common.item.ItemFuelCanister;
import fiskfille.tf.helper.TFEnergyHelper;
import fiskfille.tf.helper.TFTileHelper;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidContainerItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public final class TileEntityTransmitter extends TileEntityMachineContainer implements IEnergyTransmitter, IFluidHandlerTF, ISidedInventory, IChunkLoaderTile, IMultiTile, ITransmitterRender {
	public TileDataTransmitter data = new TileDataTransmitter(16000, 6000);
	public Ticket chunkTicket;
	public int animationTimer;

	@Override
	public void updateEntity() {
		super.updateEntity();
		++animationTimer;

		if(!data.isInitialized()) {
			data.initialize(this);
		}

		if(getBlockMetadata() < 4) {
			if(!worldObj.isRemote) {
				if(chunkTicket == null) {
					final Ticket ticket = TFChunkManager.getTicketForChunk(ForcedChunk.fromTile(this));

					if(ticket != null) {
						forceChunks(SubTicket.fromTile(this).assign(ticket));
					}
				}

				data.serverTickPre();

				if(getEnergy() > 0 && canActivate()) {
					final ArrayList<ReceiverEntry> receiversToPower = TFEnergyHelper.getReceiversToPower(this);
					final float f = Math.min(getEnergy(), getTransmissionRate()) / receiversToPower.size();

					for(final ReceiverEntry entry : receiversToPower) {
						final IEnergyReceiver receiver = entry.getReceiver();

						if(receiver.canReceiveEnergy(this)) {
							TFEnergyHelper.transferEnergy(receiver, this, f, false);
						}
					}
				}

				final ItemStack fluidContainer = getStackInSlot(0);
				final FluidStack fluidStack = data.tank.getFluid();

				if(fluidStack != null && fluidStack.amount > 0) {
					final HashMap<String, Float> ratios = FluidEnergon.getRatios(fluidStack);
					final int max = Math.min(10, fluidStack.amount);

					for(final HashMap.Entry<String, Float> e : ratios.entrySet()) {
						final Energon energon = TransformersAPI.getEnergonTypeByName(e.getKey());

						if(energon != null) {
							final int factor = energon.getEnergyValue();
							final float receivedEnergy = receiveEnergy(e.getValue() * factor * max, false);
							drain(ForgeDirection.UNKNOWN, Math.round(receivedEnergy / factor), true);
						}
					}
				}

				if(fluidContainer != null && fluidContainer.getItem() instanceof IFluidContainerItem) {
					final IFluidContainerItem container = (IFluidContainerItem) fluidContainer.getItem();
					final FluidStack fluid = container.getFluid(fluidContainer);

					if(fluid != null && fluid.amount > 0 && fluid.getFluid() == TFFluids.energon) {
						final int amount = Math.min(100, Math.min(ItemFuelCanister.getFluidAmount(fluidContainer), data.getCapacity() - data.getFluidAmount()));
						final int success = fill(ForgeDirection.UNKNOWN, container.drain(fluidContainer, amount, false), true);

						if(success > 0) {
							container.drain(fluidContainer, success, true);
						}
					}
				}

				data.serverTick();
			}

			final TileData prevData = TFTileHelper.getTileData(new DimensionalCoords(this));
			if(prevData instanceof TileDataTransmitter) {
				data = new TileDataTransmitter((TileDataTransmitter) prevData);
			}
		}
	}

	@Override
	public int getSizeInventory() {
		return 1;
	}

	@Override
	public String getInventoryName() {
		return "gui.transmitter";
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1).expand(0.35D, 0, 0.35D).addCoord(0, 2, 0);

		if(getBlockMetadata() < 4) {
			final HashSet<ReceiverEntry> receivers = data.transmissionHandler.getReceivers();

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
		super.readCustomNBT(nbt);

		if(nbt.getBoolean("Base")) {
			if(nbt.hasKey("ConfigDataTF", NBT.TAG_COMPOUND)) {
				final NBTTagCompound config = nbt.getCompoundTag("ConfigDataTF");
				data.transmissionHandler.readFromNBT(config);
				data.storage.readFromNBT(config);
				data.tank.readFromNBT(config);
			}
		}
	}

	@Override
	public void writeCustomNBT(final NBTTagCompound nbt) {
		super.writeCustomNBT(nbt);

		final boolean base = getBlockMetadata() < 4;
		nbt.setBoolean("Base", base);
		if(base) {
			if(data.getEnergy() > 0 || data.getFluidAmount() > 0 || !data.transmissionHandler.getReceivers().isEmpty()) {
				final NBTTagCompound config = nbt.getCompoundTag("ConfigDataTF");
				data.transmissionHandler.writeToNBT(config);
				data.storage.writeToNBT(config);
				data.tank.writeToNBT(config);
				nbt.setTag("ConfigDataTF", config);
			}
		}
	}

	@Override
	public TransmissionHandler getTransmissionHandler() {
		return data.transmissionHandler;
	}

	@Override
	public int getTransmissionRate() {
		return 350;
	}

	@Override
	public int getRange() {
		return 20;
	}

	@Override
	public Vec3 getEnergyOutputOffset() {
		return Vec3.createVectorHelper(0, 2.25F, 0);
	}

	@Override
	public Vec3 getRenderOutputOffset() {
		final float f = animationTimer + TransformersMod.proxy.getRenderTick();
		return Vec3.createVectorHelper(0, 2 + (Math.cos(f / 10) * 2 + 2) / 16, 0);
	}

	@Override
	public float receiveEnergy(final float amount, final boolean simulate) {
		return data.storage.add(amount, simulate);
	}

	@Override
	public float extractEnergy(final float amount, final boolean simulate) {
		return data.storage.remove(amount, simulate);
	}

	@Override
	public float getEnergy() {
		return data.storage.getEnergy();
	}

	@Override
	public int getMaxEnergy() {
		return data.storage.getMaxEnergy();
	}

	@Override
	public float getEnergyUsage() {
		return data.storage.getEnergyUsage();
	}

	@Override
	public int fill(final ForgeDirection from, final FluidStack resource, final boolean doFill) {
		final FluidStack stack = data.tank.getFluid();

		if(stack == null || stack.amount <= 0 || FluidStack.areFluidStackTagsEqual(stack, resource)) {
			return data.tank.fill(resource, doFill);
		}
		else if(stack.getFluid() == TFFluids.energon) {
			final NBTTagCompound prevNBT = resource.tag;

			resource.tag = stack.tag;
			final int amount = data.tank.fill(resource, doFill);
			resource.tag = prevNBT;

			FluidEnergon.merge(stack, resource, amount);

			return amount;
		}

		return 0;
	}

	@Override
	public FluidStack drain(final ForgeDirection from, final FluidStack resource, final boolean doDrain) {
		if(resource == null || !resource.isFluidEqual(data.tank.getFluid())) {
			return null;
		}

		return data.tank.drain(resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(final ForgeDirection from, final int maxDrain, final boolean doDrain) {
		return data.tank.drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(final ForgeDirection from, final Fluid fluid) {
		return getBlockMetadata() < 4 && fluid == TFFluids.energon;
	}

	@Override
	public boolean canDrain(final ForgeDirection from, final Fluid fluid) {
		return getBlockMetadata() < 4;
	}

	@Override
	public FluidTankInfo[] getTankInfo(final ForgeDirection from) {
		return new FluidTankInfo[]{data.tank.getInfo()};
	}

	@Override
	public int[] getAccessibleSlotsFromSide(final int side) {
		return new int[]{0};
	}

	@Override
	public boolean canInsertItem(final int slot, final ItemStack itemstack, final int side) {
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		return isItemValidForSlot(slot, itemstack);
	}

	@Override
	public boolean canExtractItem(final int slot, final ItemStack itemstack, final int side) {
		return ItemFuelCanister.isEmpty(itemstack);
	}

	@Override
	public boolean isItemValidForSlot(final int slot, final ItemStack itemstack) {
		return itemstack.getItem() instanceof IFluidContainerItem && !ItemFuelCanister.isEmpty(itemstack) && ItemFuelCanister.getContainerFluid(itemstack).getFluid() == TFFluids.energon;
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
	public FluidTankTF getTank() {
		return data.tank;
	}

	@Override
	public int[] getBaseOffsets(final int metadata) {
		return new int[]{0, -metadata / 4, 0};
	}
}
