package fiskfille.tf.common.fluid;

import com.google.common.collect.Lists;
import cpw.mods.fml.common.network.ByteBufUtils;
import fiskfille.tf.TransformersAPI;
import fiskfille.tf.common.energon.Energon;
import fiskfille.tf.helper.TFFormatHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import java.util.List;
import java.util.Map;

import static net.minecraft.util.EnumChatFormatting.*;

public class FluidTankTF extends FluidTank {
	protected int fluidUsage;
	protected int lastFluidAmount;

	public FluidTankTF(final int capacity) {
		super(capacity);
	}

	public FluidTankTF(final FluidStack stack, final int capacity) {
		super(stack, capacity);
	}

	public FluidTankTF(final Fluid fluid, final int amount, final int capacity) {
		super(fluid, amount, capacity);
	}

	public FluidTankTF copy() {
		final FluidTankTF tank = new FluidTankTF(getCapacity());

		if(getFluid() != null) {
			tank.setFluid(getFluid().copy());
		}

		tank.setUsage(getUsage());

		return tank;
	}

	public void toBytes(final ByteBuf buf) {
		final boolean hasFluid = fluid != null;
		buf.writeBoolean(hasFluid);

		if(hasFluid) {
			final NBTTagCompound tag = fluid.writeToNBT(new NBTTagCompound());
			ByteBufUtils.writeTag(buf, tag);
		}

		buf.writeShort(fluidUsage);
	}

	public void fromBytes(final ByteBuf buf) {
		if(buf.readBoolean()) {
			final NBTTagCompound tag = ByteBufUtils.readTag(buf);
			fluid = FluidStack.loadFluidStackFromNBT(tag);
		}

		fluidUsage = buf.readShort();
		lastFluidAmount = getFluidAmount() - fluidUsage;
	}

	@Override
	public FluidTank readFromNBT(final NBTTagCompound nbt) {
		final FluidTank tank = super.readFromNBT(nbt);
		fluidUsage = nbt.getShort("FluidUsage");
		lastFluidAmount = getFluidAmount() - fluidUsage;
		return tank;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt = super.writeToNBT(nbt);
		nbt.setShort("FluidUsage", (short) fluidUsage);
		return nbt;
	}

	public int getUsage() {
		return fluidUsage;
	}

	public void setUsage(final int usage) {
		fluidUsage = usage;
		lastFluidAmount = getFluidAmount();
	}

	public void calculateUsage() {
		final int amount = getFluidAmount();
		fluidUsage = amount - lastFluidAmount;
		lastFluidAmount = amount;
	}

	public List<IChatComponent> format() {
		final List<IChatComponent> list = Lists.newArrayList();
		final FluidStack stack = getFluid();

		if(stack != null && stack.amount > 0) {
			final Map<String, Float> ratios = FluidEnergon.getRatios(stack);
			boolean flag = false;

			for(final Map.Entry<String, Float> e : ratios.entrySet()) {
				final Energon energon = TransformersAPI.getEnergonTypeByName(e.getKey());
				final int percentage = Math.round(e.getValue() * 100);

				if(percentage > 0) {
					final IChatComponent name = new ChatComponentText(energon.getTranslatedName()).setChatStyle(new ChatStyle().setColor(GRAY));
					final IChatComponent ratio = new ChatComponentText(percentage + "").setChatStyle(new ChatStyle().setColor(YELLOW));

					list.add(new ChatComponentTranslation("gui.energon_processor.content", name, ratio).setChatStyle(new ChatStyle().setColor(GRAY)));
					flag = true;
				}
			}

			if(flag) {
				list.add(null);
			}
			else {
				list.add(new ChatComponentTranslation("gui.energon_processor.unidentified").setChatStyle(new ChatStyle().setColor(RED)));
			}
		}

		final IChatComponent amount = new ChatComponentText(TFFormatHelper.formatNumber(getFluidAmount())).setChatStyle(new ChatStyle().setColor(YELLOW));
		final IChatComponent capacity = new ChatComponentText(TFFormatHelper.formatNumber(getCapacity())).setChatStyle(new ChatStyle().setColor(YELLOW));

		list.add(new ChatComponentTranslation("gui.energon_processor.filled", amount, capacity).setChatStyle(new ChatStyle().setColor(GRAY)));

		return list;
	}
}
