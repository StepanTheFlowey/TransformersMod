package fiskfille.tf.common.energon.power;

import fiskfille.tf.helper.TFFormatHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;

import java.util.Arrays;
import java.util.List;

import static net.minecraft.util.EnumChatFormatting.*;

public class EnergyStorage {
	protected final float maxEnergy;
	protected float energy;
	protected float energyUsage;
	protected float lastEnergy;

	public EnergyStorage(float max) {
		maxEnergy = max;
	}

	public EnergyStorage copy() {
		final EnergyStorage storage = new EnergyStorage(getMaxEnergy());
		storage.energy = energy;
		storage.energyUsage = energyUsage;
		storage.lastEnergy = lastEnergy;
		return storage;
	}

	public void toBytes(ByteBuf buf) {
		buf.writeFloat(energy);
		buf.writeFloat(energyUsage);
	}

	public void fromBytes(ByteBuf buf) {
		energy = buf.readFloat();
		energyUsage = buf.readFloat();
		lastEnergy = energy - energyUsage;
	}

	public void readFromNBT(NBTTagCompound nbt) {
		final NBTTagCompound storage = nbt.getCompoundTag("EmB");
		energy = storage.getFloat("Energy");
		energyUsage = storage.getFloat("Usage");
		lastEnergy = energy - energyUsage;
	}

	public void writeToNBT(NBTTagCompound nbt) {
		final NBTTagCompound storage = nbt.getCompoundTag("EmB");
		storage.setFloat("Energy", energy);
		storage.setFloat("Usage", energyUsage);
		nbt.setTag("EmB", storage);
	}

	public float remove(float amount, boolean simulate) {
		final float actual = Math.min(amount, getEnergy());

		if(!simulate) {
			energy -= actual;
		}

		return actual;
	}

	public float add(float amount, boolean simulate) {
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

	public float getMaxEnergy() {
		return maxEnergy;
	}

	public void set(float amount) {
		energy = Math.min(getMaxEnergy(), Math.max(0F, amount));
	}

	public float getUsage() {
		return energyUsage;
	}

	public void setUsage(float usage) {
		energyUsage = usage;
		lastEnergy = energy;
	}

	public void calculateUsage() {
		energyUsage = energy - lastEnergy;
		lastEnergy = energy;
	}

	public List<IChatComponent> format() {
		final float usage = getUsage();

		IChatComponent gain = new ChatComponentText("+").setChatStyle(new ChatStyle().setColor(GREEN));
		IChatComponent loss = new ChatComponentText("-").setChatStyle(new ChatStyle().setColor(RED));
		IChatComponent rate = new ChatComponentText(TFFormatHelper.formatNumberPrecise(Math.abs(usage)));
		IChatComponent prefix = new ChatComponentText("").setChatStyle(new ChatStyle().setColor(GRAY));
		prefix = usage > 0 ? gain : usage < 0 ? loss : prefix;

		return Arrays.asList(
						new ChatComponentTranslation("gui.emb.storage", TFFormatHelper.formatNumber(getEnergy()), TFFormatHelper.formatNumber(getMaxEnergy())),
						new ChatComponentTranslation("gui.emb.rate", prefix.appendSibling(rate)).setChatStyle(new ChatStyle().setColor(GRAY))
		);
	}
}
