package fiskfille.tf.common.energon.power;

import fiskfille.tf.helper.TFFormatHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static net.minecraft.util.EnumChatFormatting.*;

public class EnergyStorage {
	protected final int maxEnergy;
	protected float energy;
	protected float energyUsage;
	protected float lastEnergy;

	public EnergyStorage(final int max) {
		maxEnergy = max;
	}

	public EnergyStorage copy() {
		final EnergyStorage storage = new EnergyStorage(getMaxEnergy());
		storage.energy = energy;
		storage.energyUsage = energyUsage;
		storage.lastEnergy = lastEnergy;
		return storage;
	}

	public void toBytes(final ByteBuf buf) {
		buf.writeFloat(energy);
		buf.writeFloat(energyUsage);
	}

	public void fromBytes(final ByteBuf buf) {
		energy = buf.readFloat();
		energyUsage = buf.readFloat();
		lastEnergy = energy - energyUsage;
	}

	public void readFromNBT(final NBTTagCompound nbt) {
		final NBTTagCompound storage = nbt.getCompoundTag("EmB");
		energy = storage.getFloat("Energy");
		energyUsage = storage.getFloat("Usage");
		lastEnergy = energy - energyUsage;
	}

	public void writeToNBT(final NBTTagCompound nbt) {
		final NBTTagCompound storage = nbt.getCompoundTag("EmB");
		storage.setFloat("Energy", energy);
		storage.setFloat("Usage", energyUsage);
		nbt.setTag("EmB", storage);
	}

	public float remove(final float amount, final boolean simulate) {
		final float actual = Math.min(amount, getEnergy());

		if(!simulate) {
			energy -= actual;
		}

		return actual;
	}

	public float add(final float amount, final boolean simulate) {
		final float actual = Math.max(Math.min(amount, getMaxEnergy() - getEnergy()), 0);

		if(!simulate) {
			energy += actual;
		}

		return actual;
	}

	public float getEnergy() {
		if(energy <= 1E-16) {
			energy = 0;
		}

		return energy;
	}

	public int getMaxEnergy() {
		return maxEnergy;
	}

	public void set(final float amount) {
		energy = Math.min(getMaxEnergy(), Math.max(0, amount));
	}

	public float getEnergyUsage() {
		return energyUsage;
	}

	public void setUsage(final float usage) {
		energyUsage = usage;
		lastEnergy = energy;
	}

	public void calculateUsage() {
		energyUsage = energy - lastEnergy;
		lastEnergy = energy;
	}

	public List<IChatComponent> format() {
		final float usage = getEnergyUsage();

		final IChatComponent rate = new ChatComponentText(TFFormatHelper.formatNumberPrecise(Math.abs(usage)));
		final IChatComponent prefix;
		if(usage > 0) {
			prefix = new ChatComponentText("+").setChatStyle(new ChatStyle().setColor(GREEN));
		}
		else if(usage < 0) {
			prefix = new ChatComponentText("-").setChatStyle(new ChatStyle().setColor(RED));
		}
		else {
			prefix = new ChatComponentText("").setChatStyle(new ChatStyle().setColor(GRAY));
		}

		final IChatComponent rateFormetted = new ChatComponentTranslation("gui.emb.rate", prefix.appendSibling(rate)).setChatStyle(new ChatStyle().setColor(GRAY));
		final float maxEnergy = getMaxEnergy();
		if(maxEnergy > 0) {
			return Arrays.asList(new ChatComponentTranslation("gui.emb.storage", TFFormatHelper.formatNumber(getEnergy()), TFFormatHelper.formatNumber(maxEnergy)), rateFormetted);
		}

		return Collections.singletonList(rateFormetted);
	}
}
