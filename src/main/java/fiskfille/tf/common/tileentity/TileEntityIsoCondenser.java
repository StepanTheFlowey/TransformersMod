package fiskfille.tf.common.tileentity;

import fiskfille.tf.common.data.tile.TileData;
import fiskfille.tf.common.data.tile.TileDataEnergyContainer;
import fiskfille.tf.common.energon.Energon;
import fiskfille.tf.common.energon.IEnergon;
import fiskfille.tf.common.energon.power.IEnergyContainer;
import fiskfille.tf.common.item.ItemCSD.DimensionalCoords;
import fiskfille.tf.helper.TFTileHelper;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.HashMap;

public final class TileEntityIsoCondenser extends TileEntityMachine implements IEnergyContainer {
	public final HashMap<ForgeDirection, Block> providers = new HashMap<>();
	public final HashMap<ForgeDirection, Float> animationTimer = new HashMap<>();
	public final HashMap<ForgeDirection, Float> prevAnimationTimer = new HashMap<>();
	public TileDataEnergyContainer data = new TileDataEnergyContainer(8000);

	@Override
	public void updateEntity() {
		super.updateEntity();

		prevAnimationTimer.putAll(animationTimer);

		if(!data.isInitialized()) {
			data.initialize(this);
		}

		providers.clear();

		for(final ForgeDirection dir : new ForgeDirection[]{ForgeDirection.NORTH, ForgeDirection.EAST, ForgeDirection.SOUTH, ForgeDirection.WEST}) {
			final Block block = worldObj.getBlock(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
			final float f = animationTimer.get(dir) == null ? 0 : animationTimer.get(dir);

			boolean active = false;

			if(block instanceof IEnergon && ((IEnergon) block).getMass() > 0) {
				providers.put(dir, block);
				active = canActivate();
			}

			if(active) {
				animationTimer.put(dir, MathHelper.clamp_float(f + 1F / 10, 0, 1));
			}
			else {
				animationTimer.put(dir, MathHelper.clamp_float(f - 1F / 10, 0, 1));
			}
		}

		if(!worldObj.isRemote) {
			if(canActivate()) {
				for(final HashMap.Entry<ForgeDirection, Block> e : providers.entrySet()) {
					receiveEnergy(getGenerationRate(((IEnergon) e.getValue()).getMass()), false);
				}
			}

			data.serverTick();
		}

		final TileData prevData = TFTileHelper.getTileData(new DimensionalCoords(this));
		if(prevData instanceof TileDataEnergyContainer) {
			data = new TileDataEnergyContainer((TileDataEnergyContainer) prevData);
		}
	}

	public float getGenerationRate(final int mass) {
		if(!canActivate()) {
			return 0;
		}

		return (float) mass / Energon.CRYSTAL_BLOCK * 0.1F;
	}

	@Override
	public void invalidate() {
		super.invalidate();

		if(!worldObj.isRemote) {
			data.kill();
		}
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		return super.getRenderBoundingBox().expand(1, 1, 1);
	}

	@Override
	public void readCustomNBT(final NBTTagCompound nbt) {
		super.readCustomNBT(nbt);

		if(nbt.hasKey("ConfigDataTF", NBT.TAG_COMPOUND)) {
			final NBTTagCompound config = nbt.getCompoundTag("ConfigDataTF");
			data.storage.readFromNBT(config);
		}
	}

	@Override
	public void writeCustomNBT(final NBTTagCompound nbt) {
		super.writeCustomNBT(nbt);

		if(data.storage.getEnergy() > 0) {
			final NBTTagCompound config = nbt.getCompoundTag("ConfigDataTF");
			data.storage.writeToNBT(config);
			nbt.setTag("ConfigDataTF", config);
		}
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
		return data.getEnergy();
	}

	@Override
	public int getMaxEnergy() {
		return data.getMaxEnergy();
	}

	@Override
	public float getEnergyUsage() {
		return data.storage.getEnergyUsage();
	}
}
