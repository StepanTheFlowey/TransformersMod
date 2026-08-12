package fiskfille.tf.common.item;

import fiskfille.tf.common.energon.power.IEnergyContainerItem;
import fiskfille.tf.helper.TFFormatHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import java.util.List;

public class ItemEnergyContainer extends Item implements IEnergyContainerItem {
	protected final int capacity;

	public ItemEnergyContainer(final int max) {
		setMaxStackSize(1);
		capacity = max;
	}

	@Override
	public void addInformation(final ItemStack itemstack, final EntityPlayer player, final List list, final boolean flag) {
		list.add(StatCollector.translateToLocalFormatted("gui.emb.storage", TFFormatHelper.formatNumber(getEnergyStored(itemstack)), TFFormatHelper.formatNumber(getEnergyCapacity(itemstack))));
	}

	@Override
	public float receiveEnergy(final ItemStack itemstack, final float amount, final boolean simulate) {
		if(!itemstack.hasTagCompound()) {
			itemstack.setTagCompound(new NBTTagCompound());
		}

		final NBTTagCompound tagCompound = itemstack.getTagCompound();
		final float energy = tagCompound.getFloat("Energy");
		final float energyReceived = Math.min(getEnergyCapacity(itemstack) - energy, amount);
		if(!simulate) {
			tagCompound.setFloat("Energy", energy + energyReceived);
		}

		return energyReceived;
	}

	@Override
	public float extractEnergy(final ItemStack itemstack, final float amount, final boolean simulate) {
		if(!itemstack.hasTagCompound()) {
			return 0;
		}

		final NBTTagCompound tagCompound = itemstack.getTagCompound();
		if(!tagCompound.hasKey("Energy")) {
			return 0;
		}

		final float energy = tagCompound.getFloat("Energy");
		final float energyExtracted = Math.min(energy, amount);
		if(!simulate) {
			tagCompound.setFloat("Energy", energy - energyExtracted);
		}

		return energyExtracted;
	}

	@Override
	public float getEnergyStored(final ItemStack itemstack) {
		if(!itemstack.hasTagCompound()) {
			return 0;
		}

		final NBTTagCompound tagCompound = itemstack.getTagCompound();
		if(!tagCompound.hasKey("Energy")) {
			return 0;
		}

		float energy = tagCompound.getFloat("Energy");
		if(energy <= 1E-16) {
			tagCompound.setFloat("Energy", energy = 0);
		}

		return energy;
	}

	@Override
	public int getEnergyCapacity(final ItemStack itemstack) {
		return capacity;
	}
}
