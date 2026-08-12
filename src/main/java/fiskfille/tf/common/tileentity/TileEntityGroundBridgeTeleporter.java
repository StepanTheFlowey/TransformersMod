package fiskfille.tf.common.tileentity;

import cpw.mods.fml.common.network.NetworkRegistry;
import fiskfille.tf.common.block.BlockGroundBridgeTeleporter;
import fiskfille.tf.common.item.ItemCSD.DimensionalCoords;
import fiskfille.tf.common.network.MessageClosePortal;
import fiskfille.tf.common.network.base.TFNetworkManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;

public final class TileEntityGroundBridgeTeleporter extends TileEntityTF {
	public DimensionalCoords controlPanel;
	public int lastUpdate;
	public int ticks;
	public boolean clientClosing;

	@Override
	public void updateEntity() {
		if(!worldObj.isRemote) {
			if(lastUpdate >= 12) {
				worldObj.setBlockToAir(xCoord, yCoord, zCoord);
				markBlockForUpdate();
			}
			else if(lastUpdate == 6) {
				TFNetworkManager.networkWrapper.sendToAllAround(new MessageClosePortal(xCoord, yCoord, zCoord), new NetworkRegistry.TargetPoint(worldObj.provider.dimensionId, xCoord, yCoord, zCoord, 8192));
			}
		}

		if(!worldObj.isRemote || clientClosing) {
			++lastUpdate;
		}

		++ticks;
	}

	public boolean isReturnPortal(final int metadata) {
		return BlockGroundBridgeTeleporter.isReturnPortal(metadata);
	}

	public boolean returnPortal() {
		return isReturnPortal(getBlockMetadata());
	}

	@Override
	public double getMaxRenderDistanceSquared() {
		return super.getMaxRenderDistanceSquared() * 2;
	}

	@Override
	public void readCustomNBT(final NBTTagCompound nbt) {
		if(nbt.hasKey("ControlPanel", NBT.TAG_COMPOUND)) {
			final NBTTagCompound controlPanelLocationNbt = nbt.getCompoundTag("ControlPanel");
			controlPanel = new DimensionalCoords(
				controlPanelLocationNbt.getInteger("x"),
				controlPanelLocationNbt.getInteger("y"),
				controlPanelLocationNbt.getInteger("z"),
				controlPanelLocationNbt.getInteger("dim")
			);
		}
	}

	@Override
	public void writeCustomNBT(final NBTTagCompound nbt) {
		if(controlPanel != null) {
			final NBTTagCompound controlPanelLocationNbt = new NBTTagCompound();
			controlPanelLocationNbt.setInteger("x", controlPanel.posX);
			controlPanelLocationNbt.setInteger("y", controlPanel.posY);
			controlPanelLocationNbt.setInteger("z", controlPanel.posZ);
			controlPanelLocationNbt.setInteger("dim", controlPanel.dimension);
			nbt.setTag("ControlPanel", controlPanelLocationNbt);
		}
	}
}
