package fiskfille.tf.helper;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;

public final class TFArmorDyeHelper {
	public static boolean isDyed(final ItemStack itemstack) {
		return itemstack.hasTagCompound() && itemstack.getTagCompound().hasKey("CustomColor");
	}

	public static void removeColor(final ItemStack itemstack) {
		if(!itemstack.hasTagCompound()) {
			return;
		}

		final NBTTagCompound compound = itemstack.getTagCompound();
		if(compound.hasKey("CustomColor")) {
			compound.removeTag("CustomColor");
		}
		if(compound.hasNoTags()) {
			itemstack.setTagCompound(null);
		}
	}

	public static int getPrimaryColor(final ItemStack itemstack) {
		if(itemstack.hasTagCompound()) {
			final NBTTagCompound nbt = itemstack.getTagCompound().getCompoundTag("CustomColor");
			if(nbt != null) {
				return nbt.getInteger("PrimaryColor");
			}
		}

		return 0;
	}

	public static int getSecondaryColor(final ItemStack itemstack) {
		if(itemstack.hasTagCompound()) {
			final NBTTagCompound nbt = itemstack.getTagCompound().getCompoundTag("CustomColor");
			if(nbt != null) {
				return nbt.getInteger("SecondaryColor");
			}
		}

		return 0;
	}

	public static void setPrimaryColor(final ItemStack itemstack, final int i) {
		if(!itemstack.hasTagCompound()) {
			itemstack.setTagCompound(new NBTTagCompound());
		}

		final NBTTagCompound compound = itemstack.getTagCompound();
		final NBTTagCompound color = compound.getCompoundTag("CustomColor");
		color.setInteger("PrimaryColor", i);
		if(!compound.hasKey("CustomColor", NBT.TAG_COMPOUND)) {
			compound.setTag("CustomColor", color);
		}
	}

	public static void setSecondaryColor(final ItemStack itemstack, final int i) {
		if(!itemstack.hasTagCompound()) {
			itemstack.setTagCompound(new NBTTagCompound());
		}

		final NBTTagCompound compound = itemstack.getTagCompound();
		final NBTTagCompound color = compound.getCompoundTag("CustomColor");
		color.setInteger("SecondaryColor", i);
		if(!compound.hasKey("CustomColor", NBT.TAG_COMPOUND)) {
			compound.setTag("CustomColor", color);
		}
	}

	public static boolean areColorsIdentical(final ItemStack... itemstacks) {
		if(itemstacks.length > 1) {
			final int primary = getPrimaryColor(itemstacks[0]);
			final int secondary = getSecondaryColor(itemstacks[0]);

			for(final ItemStack itemstack : itemstacks) {
				if(itemstack == null || getPrimaryColor(itemstack) != primary || getSecondaryColor(itemstack) != secondary) {
					return false;
				}
			}
		}

		return true;
	}
}
