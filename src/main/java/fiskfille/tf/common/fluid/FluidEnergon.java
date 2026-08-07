package fiskfille.tf.common.fluid;

import com.google.common.collect.Maps;
import fiskfille.tf.TransformersAPI;
import fiskfille.tf.common.energon.Energon;
import fiskfille.tf.common.energon.IEnergon;
import fiskfille.tf.helper.TFHelper;
import fiskfille.tf.helper.TFTextureHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.Map;

public class FluidEnergon extends Fluid {
	public FluidEnergon(final String fluidName) {
		super(fluidName);
	}

	public static void refreshNBT(final FluidStack stack) {
		if(stack.tag == null) {
			stack.tag = new NBTTagCompound();
		}
	}

	public static FluidStack create(final ItemStack crystal) {
		final IEnergon energon = (IEnergon) (crystal.getItem() instanceof ItemBlock ? Block.getBlockFromItem(crystal.getItem()) : crystal.getItem());

		return create(energon.getEnergonType(), energon.getMass());
	}

	public static FluidStack create(final Energon energon, final int amount) {
		final FluidStack stack = new FluidStack(TFFluids.energon, amount);
		final Map<String, Float> ratios = Maps.newHashMap();

		for(final Energon energon1 : TransformersAPI.getEnergonTypes()) {
			ratios.put(energon1.getId(), energon.getId().equals(energon1.getId()) ? 1F : 0);
		}

		setRatios(stack, ratios);

		return stack;
	}

	public static void merge(final FluidStack stack1, final FluidStack stack2, final int amount) {
		Map<String, Float> ratios1 = getRatios(stack1);
		final Map<String, Float> ratios2 = getRatios(stack2);

		if(stack1.amount == 0) {
			ratios1 = ratios2;
		}
		else {
			for(final Map.Entry<String, Float> e : ratios1.entrySet()) {
				final float f = (float) amount / stack1.amount;
				e.setValue(e.getValue() * (1 - f) + ratios2.get(e.getKey()) * f);
			}
		}

		setRatios(stack1, ratios1);
	}

	public static void setRatios(final FluidStack stack, final Map<String, Float> ratios) {
		final NBTTagCompound nbttagcompound = new NBTTagCompound();

		for(final Energon energon : TransformersAPI.getEnergonTypes()) {
			if(!ratios.containsKey(energon.getId())) {
				ratios.put(energon.getId(), 0F);
			}
		}

		for(final Map.Entry<String, Float> e : ratios.entrySet()) {
			nbttagcompound.setFloat(e.getKey(), e.getValue());
		}

		refreshNBT(stack);
		stack.tag.setTag("Ratio", nbttagcompound);

		calculateLiquidColor(stack);
	}

	public static HashMap<String, Float> getRatios(final FluidStack stack) {
		refreshNBT(stack);

		final NBTTagCompound nbttagcompound = stack.tag.getCompoundTag("Ratio");
		final HashMap<String, Float> map = new HashMap<>();
		for(final Energon energon : TransformersAPI.getEnergonTypes()) {
			map.put(energon.getId(), nbttagcompound.getFloat(energon.getId()));
		}

		return map;
	}

	public static void calculateLiquidColor(final FluidStack stack) {
		final Map<String, Float> ratios = getRatios(stack);
		int liquidColor = -1;

		for(final Map.Entry<String, Float> e : ratios.entrySet()) {
			final Energon energon = TransformersAPI.getEnergonTypeByName(e.getKey());

			if(energon != null) {
				if(liquidColor == -1) {
					liquidColor = energon.getColor();
				}
				else {
					liquidColor = TFHelper.blend(liquidColor, energon.getColor(), e.getValue());
				}
			}
		}

		setLiquidColor(stack, liquidColor);
	}

	public static void setLiquidColor(final FluidStack stack, final int color) {
		refreshNBT(stack);
		stack.tag.setInteger("Color", color);
	}

	public static int getLiquidColor(final FluidStack stack) {
		return stack.tag != null && stack.tag.hasKey("Color") ? stack.tag.getInteger("Color") : -1;
	}

	@Override
	public IIcon getStillIcon() {
		return TFTextureHelper.energonStillIcon;
	}

	@Override
	public IIcon getFlowingIcon() {
		return TFTextureHelper.energonFlowingIcon;
	}

	@Override
	public int getColor(final FluidStack stack) {
		return getLiquidColor(stack);
	}
}
