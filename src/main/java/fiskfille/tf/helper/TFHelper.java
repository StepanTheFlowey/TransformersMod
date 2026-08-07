package fiskfille.tf.helper;

import fiskfille.tf.TransformersMod;
import fiskfille.tf.common.data.TFData;
import fiskfille.tf.common.fluid.FluidTankTF;
import fiskfille.tf.common.item.armor.ItemTransformerArmor;
import fiskfille.tf.common.transformer.base.Transformer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author FiskFille, gegy1000
 */
public final class TFHelper {
	public static boolean isTransformer(final EntityLivingBase entity) {
		return isTransformer(entity.getEquipmentInSlot(4), entity.getEquipmentInSlot(3), entity.getEquipmentInSlot(2), entity.getEquipmentInSlot(1));
	}

	public static boolean isTransformer(final ItemStack... itemstacks) {
		ItemStack itemstack = itemstacks[0];

		for(int i = 1; i < itemstacks.length; ++i) {
			if(getTransformerFromArmor(itemstack) == null || getTransformerFromArmor(itemstack) != getTransformerFromArmor(itemstacks[i])) {
				return false;
			}

			itemstack = itemstacks[i];
		}

		return true;

//        Transformer helmet = getTransformerFromArmor(itemstacks[0]);
//        Transformer chest = getTransformerFromArmor(itemstacks[1]);
//        Transformer legs = getTransformerFromArmor(itemstacks[2]);
//        Transformer feet = getTransformerFromArmor(itemstacks[3]);
//
//        if ((helmet == null || helmet != chest) && chest != null && chest.getHelmet() == null)
//        {
//            return itemstacks[0] == null && chest == legs && legs == feet;
//        }
//
//        return helmet != null && helmet == chest && chest == legs && legs == feet;
	}

	public static Transformer getTransformer(final EntityLivingBase entity) {
		if(entity != null && isTransformer(entity)) {
			return getTransformerFromArmor(entity, 0);
		}

		return null;
	}

	public static Transformer getTransformer(final ItemStack... itemstacks) {
		if(isTransformer(itemstacks)) {
			return getTransformerFromArmor(itemstacks[0]);
		}

		return null;
	}

	public static Transformer getTransformerFromArmor(final EntityLivingBase entity, final int slot) {
		return getTransformerFromArmor(entity.getEquipmentInSlot(slot + 1));
	}

	public static Transformer getTransformerFromArmor(final ItemStack itemstack) {
		if(itemstack != null) {
			final Item item = itemstack.getItem();

			if(item instanceof ItemTransformerArmor) {
				return ((ItemTransformerArmor) item).getTransformer();
			}
		}

		return null;
	}

	public static boolean isFullyTransformed(final EntityPlayer player) {
		return isTransformer(player) && getTransformationTimer(player) == 1;
	}

	public static boolean isInRobotMode(final EntityPlayer player) {
		return isTransformer(player) && getTransformationTimer(player) == 0;
	}

	public static boolean isInStealthMode(final EntityPlayer player) {
		final Transformer transformer = TFHelper.getTransformer(player);
		int altMode = TFData.ALT_MODE.get(player);

		if(altMode == -1) {
			altMode = TFData.PREV_ALT_MODE.get(player);
		}

		return transformer != null && transformer.hasStealthForce() && altMode != -1 && getStealthModeTimer(player) > 0;
	}

	public static float getTransformationTimer(final EntityPlayer player) {
		return median(TFData.TRANSFORM_PROGRESS.get(player), TFData.PREV_TRANSFORM_PROGRESS.get(player), TransformersMod.proxy.getRenderTick());
	}

	public static float getStealthModeTimer(final EntityPlayer player) {
		return median(TFData.STEALTH_FORCE_PROGRESS.get(player), TFData.PREV_STEALTH_FORCE_PROGRESS.get(player), TransformersMod.proxy.getRenderTick());
	}

	public static void applyFluidUsage(final FluidTankTF tank) {
		final FluidStack fluidStack = tank.getFluid();

		if(fluidStack != null) {
			fluidStack.amount += tank.getUsage();

			if(fluidStack.amount < 0) {
				fluidStack.amount = 0;
			}
			else if(fluidStack.amount > tank.getCapacity()) {
				fluidStack.amount = tank.getCapacity();
			}
		}
	}

	public static float median(final float curr, final float prev, final float partialTicks) {
		return prev + (curr - prev) * partialTicks;
	}

	public static double median(final double curr, final double prev, final float partialTicks) {
		return prev + (curr - prev) * partialTicks;
	}

	public static float getWidth(final EntityPlayer player) {
		return 0.6F;
	}

	public static float getHeight(final EntityPlayer player) {
		return 1.8F + getCameraYOffset(player);
	}

	public static float getScale(final EntityPlayer player) {
		return 1;
	}

	public static float getCameraYOffset(final EntityPlayer player) {
		final Transformer transformer = TFHelper.getTransformer(player);

		if(transformer != null) {
			final int altMode = TFData.ALT_MODE.get(player);

			return TFHelper.median(transformer.getVehicleHeightOffset(), transformer.getHeightOffset(), TFHelper.getTransformationTimer(player));
		}

		return 0;
	}

	public static boolean shouldOverrideScale(final EntityPlayer player) {
		if(getTransformer(player) == null && TFData.PREV_TRANSFORMER.get(player) != null) {
			return true;
		}

		return !player.isEntityAlive() || (getTransformer(player) != null || TFData.PREV_TRANSFORMER.get(player) != null) && (getHeight(player) != player.height || getWidth(player) != player.width);
	}

	public static int blend(final int a, final int b, float ratio) {
		if(ratio > 1F) {
			ratio = 1F;
		}
		else if(ratio < 0F) {
			ratio = 0F;
		}

		final float iRatio = 1F - ratio;

		final int aA = a >> 24 & 0xff;
		final int aR = (a & 0xff0000) >> 16;
		final int aG = (a & 0xff00) >> 8;
		final int aB = a & 0xff;

		final int bA = b >> 24 & 0xff;
		final int bR = (b & 0xff0000) >> 16;
		final int bG = (b & 0xff00) >> 8;
		final int bB = b & 0xff;

		final int A = (int) (aA * iRatio + bA * ratio);
		final int R = (int) (aR * iRatio + bR * ratio);
		final int G = (int) (aG * iRatio + bG * ratio);
		final int B = (int) (aB * iRatio + bB * ratio);

		return A << 24 | R << 16 | G << 8 | B;
	}
}
