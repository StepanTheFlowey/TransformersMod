package fiskfille.tf.waila;

import fiskfille.tf.common.energon.power.EnergyStorage;
import fiskfille.tf.common.energon.power.IEnergyContainer;
import fiskfille.tf.common.fluid.FluidTankTF;
import fiskfille.tf.common.fluid.IFluidHandlerTF;
import fiskfille.tf.helper.TFFormatHelper;
import fiskfille.tf.helper.TFTileHelper;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.List;

public class DataProviderMachine implements IWailaDataProvider {
	public final String key;
	public final Class targetClass;

	public DataProviderMachine(final String s, final Class c) {
		key = s;
		targetClass = c;
	}

	@Override
	public ItemStack getWailaStack(final IWailaDataAccessor accessor, final IWailaConfigHandler config) {
		return null;
	}

	@Override
	public List<String> getWailaHead(final ItemStack itemstack, final List<String> list, final IWailaDataAccessor accessor, final IWailaConfigHandler config) {
		return list;
	}

	@Override
	public List<String> getWailaBody(final ItemStack itemstack, final List<String> list, final IWailaDataAccessor accessor, final IWailaConfigHandler config) {
		final TileEntity tileentity = TFTileHelper.getTileBase(accessor.getTileEntity());

		if(tileentity.getClass() == targetClass && config.getConfig(key, true)) {
			if(tileentity instanceof IEnergyContainer) {
				final IEnergyContainer energyContainer = (IEnergyContainer) tileentity;
				final EnergyStorage storage = new EnergyStorage(energyContainer.getMaxEnergy());
				storage.set(energyContainer.getEnergy());
				storage.setUsage(energyContainer.getEnergyUsage());

				list.addAll(TFFormatHelper.toString(storage.format()));

				if(tileentity instanceof IFluidHandlerTF) {
					list.add(" ");
				}
			}

			if(tileentity instanceof IFluidHandlerTF) {
				final IFluidHandlerTF fluidHandler = (IFluidHandlerTF) tileentity;
				final FluidTankTF tank = getFluid(tileentity, fluidHandler);

				list.addAll(TFFormatHelper.toString(tank.format()));
			}
		}

		return list;
	}

	public FluidTankTF getFluid(final TileEntity tile, final IFluidHandlerTF fluidHandler) {
		return fluidHandler.getTank();
	}

	@Override
	public List<String> getWailaTail(final ItemStack itemstack, final List<String> list, final IWailaDataAccessor accessor, final IWailaConfigHandler config) {
		return list;
	}

	@Override
	public NBTTagCompound getNBTData(final EntityPlayerMP player, final TileEntity tile, final NBTTagCompound nbttagcompound, final World world, final int x, final int y, final int z) {
		return null;
	}
}
