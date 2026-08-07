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
	protected float capacity;

	public ItemEnergyContainer(final float max) {
		setMaxStackSize(1);
		capacity = max;
	}

	public ItemEnergyContainer setCapacity(final float max) {
		capacity = max;
		return this;
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

		float energy = itemstack.getTagCompound().getFloat("Energy");
		final float energyReceived = Math.min(getEnergyCapacity(itemstack) - energy, amount);

		if(!simulate) {
			energy += energyReceived;
			itemstack.getTagCompound().setFloat("Energy", energy);
		}

		return energyReceived;
	}

	@Override
	public float extractEnergy(final ItemStack itemstack, final float amount, final boolean simulate) {
		if(!itemstack.hasTagCompound() || !itemstack.getTagCompound().hasKey("Energy")) {
			return 0;
		}

		float energy = itemstack.getTagCompound().getFloat("Energy");
		final float energyExtracted = Math.min(energy, amount);

		if(!simulate) {
			energy -= energyExtracted;
			itemstack.getTagCompound().setFloat("Energy", energy);
		}

		return energyExtracted;
	}

	@Override
	public float getEnergyStored(final ItemStack itemstack) {
		if(!itemstack.hasTagCompound() || !itemstack.getTagCompound().hasKey("Energy")) {
			return 0;
		}

		float energy = itemstack.getTagCompound().getFloat("Energy");

		if(energy <= 1E-16) {
			itemstack.getTagCompound().setFloat("Energy", energy = 0);
		}

		return energy;
	}

	@Override
	public float getEnergyCapacity(final ItemStack itemstack) {
		return capacity;
	}
}
