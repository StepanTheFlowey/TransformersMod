package fiskfille.tf.common.tileentity;

import fiskfille.tf.common.block.BlockMachineBase;
import fiskfille.tf.common.container.ContainerEmpty;
import fiskfille.tf.common.energon.power.IEnergyContainer;
import fiskfille.tf.common.network.MessageTileTrigger.ITileDataCallback;
import fiskfille.tf.helper.TFEnergyHelper;
import fiskfille.tf.helper.TFTileHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class TileEntityMachine extends TileEntityTF implements ITileDataCallback {
	public final EnumIO[] io;
	public EnumRedstone redstoneMode = EnumRedstone.IGNORE;
	public EnumDistribution distribution = EnumDistribution.QUEUED;

	public TileEntityMachine() {
		io = new EnumIO[ForgeDirection.VALID_DIRECTIONS.length];
		Arrays.fill(io, EnumIO.NONE);
	}

	@Override
	public void updateEntity() {
		if(!worldObj.isRemote) {
			if(this instanceof IEnergyContainer && TFTileHelper.getTileBase(this) == this) {
				final IEnergyContainer container = (IEnergyContainer) this;
				final ArrayList<ForgeDirection> list = new ArrayList<>();

				for(int i = 0; i < io.length; ++i) {
					final ForgeDirection dir = ForgeDirection.getOrientation(i);

					if(io[i].ordinal() > 0 && container.getMaxEnergy() > 0 && (io[i] != EnumIO.PUSH || container.getEnergy() > 0) && canTransfer(dir)) {
						final TileEntity tile = TFTileHelper.getTileBase(worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY + (dir.offsetY > 0 ? getBlockType().getBlockHeight() - 1 : 0), zCoord + dir.offsetZ));

						if(tile instanceof IEnergyContainer) {
							list.add(dir);
						}
					}
				}

				for(final ForgeDirection dir : list) {
					final IEnergyContainer receiver = (IEnergyContainer) TFTileHelper.getTileBase(worldObj.getTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY + (dir.offsetY > 0 ? getBlockType().getBlockHeight() - 1 : 0), zCoord + dir.offsetZ));
					float rate = getTransferRate(dir, io[dir.ordinal()]);

					if(distribution == EnumDistribution.SPREAD) {
						rate /= list.size();
					}

					switch(io[dir.ordinal()]) {
						case PULL:
							TFEnergyHelper.transferEnergy(container, receiver, rate, false);
							break;
						case PUSH:
							TFEnergyHelper.transferEnergy(receiver, container, rate, false);
							break;
						default:
							break;
					}
				}
			}
		}
	}

	@Override
	public BlockMachineBase getBlockType() {
		return (BlockMachineBase) super.getBlockType();
	}

	@Override
	public void readCustomNBT(final NBTTagCompound nbt) {
		if(nbt.hasKey("ConfigDataTF", NBT.TAG_COMPOUND)) {
			final NBTTagCompound config = nbt.getCompoundTag("ConfigDataTF");
			final NBTTagList nbttaglist = config.getTagList("IO", NBT.TAG_COMPOUND);

			for(int i = 0; i < nbttaglist.tagCount(); ++i) {
				final NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
				io[nbttagcompound.getByte("side") % io.length] = EnumIO.values()[nbttagcompound.getByte("mode") % EnumIO.values().length];
			}

			redstoneMode = EnumRedstone.values()[config.getByte("Redstone") % EnumRedstone.values().length];
			distribution = EnumDistribution.values()[config.getByte("Distribution") % EnumDistribution.values().length];
		}
	}

	@Override
	public void writeCustomNBT(final NBTTagCompound nbt) {
		if(isConfigured()) {
			final NBTTagCompound config = nbt.getCompoundTag("ConfigDataTF");
			final NBTTagList nbttaglist = new NBTTagList();

			for(int i = 0; i < io.length; ++i) {
				final NBTTagCompound nbttagcompound = new NBTTagCompound();
				nbttagcompound.setByte("side", (byte) i);
				nbttagcompound.setByte("mode", (byte) io[i].ordinal());
				nbttaglist.appendTag(nbttagcompound);
			}

			config.setByte("Redstone", (byte) redstoneMode.ordinal());
			config.setByte("Distribution", (byte) distribution.ordinal());
			config.setTag("IO", nbttaglist);
			nbt.setTag("ConfigDataTF", config);
		}
	}

	public boolean isConfigured() {
		for(final EnumIO enumIO : io) {
			if(enumIO.ordinal() > 0) {
				return true;
			}
		}

		return redstoneMode.ordinal() > 0 || distribution.ordinal() > 0;
	}

	public EnumIO getInOutMode(final ForgeDirection dir) {
		if(dir.ordinal() < io.length) {
			return io[dir.ordinal()];
		}

		return EnumIO.NONE;
	}

	public boolean canActivate() {
		boolean isPowered = worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord);
		int y = 0;

		while(++y < getBlockType().getBlockHeight() && TFTileHelper.getTileBase(worldObj.getTileEntity(xCoord, yCoord + y, zCoord)) == TFTileHelper.getTileBase(this)) {
			isPowered |= worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord + y, zCoord);
		}

		switch(redstoneMode) {
			case WITH:
				return isPowered;
			case WITHOUT:
				return !isPowered;
			default:
				return true;
		}
	}

	public float getTransferRate(final ForgeDirection dir, final EnumIO mode) {
		return 10;
	}

	public boolean canTransfer(final ForgeDirection dir) {
		final int x = xCoord + dir.offsetX;
		final int y = yCoord + dir.offsetY + (dir.offsetY > 0 ? getBlockType().getBlockHeight() - 1 : 0);
		final int z = zCoord + dir.offsetZ;
		final float f = 0.001F;

		final Block block = worldObj.getBlock(x, y, z);
		final AxisAlignedBB aabb = block.getCollisionBoundingBoxFromPool(worldObj, x, y, z);
		final AxisAlignedBB aabb1 = getBlockType().getCollisionBoundingBoxFromPool(worldObj, xCoord, yCoord, zCoord);

		if(aabb == null || aabb1 == null) {
			return false;
		}

		return aabb1.addCoord(dir.offsetX * f, dir.offsetY * f, dir.offsetZ * f).intersectsWith(aabb);
	}

	@Override
	public void receive(final EntityPlayer player, final int action) {
		if(action < 0) {
			if(action >= -io.length) {
				final int id = -action - 1;
				io[id] = EnumIO.values()[(io[id].ordinal() + 1) % EnumIO.values().length];
			}
			else if(action == -io.length - 1) {
				player.openContainer = new ContainerEmpty(player.inventory, 16);
			}
			else if(action == -io.length - 2) {
				redstoneMode = EnumRedstone.values()[(redstoneMode.ordinal() + 1) % EnumRedstone.values().length];
			}
			else if(action == -io.length - 3) {
				distribution = EnumDistribution.values()[(distribution.ordinal() + 1) % EnumDistribution.values().length];
			}
		}
	}

	public enum EnumIO {
		NONE, PULL, PUSH
	}

	public enum EnumRedstone {
		IGNORE, WITH, WITHOUT
	}

	public enum EnumDistribution {
		QUEUED, SPREAD
	}
}
