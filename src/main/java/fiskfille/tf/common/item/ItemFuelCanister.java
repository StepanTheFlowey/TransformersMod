package fiskfille.tf.common.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.TransformersAPI;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.common.energon.Energon;
import fiskfille.tf.common.fluid.FluidEnergon;
import fiskfille.tf.common.fluid.FluidTankTF;
import fiskfille.tf.common.fluid.TFFluids;
import fiskfille.tf.helper.TFFormatHelper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.ItemFluidContainer;

import java.util.List;

public class ItemFuelCanister extends ItemFluidContainer {
	public static final String[] unlocalizedNames = new String[]{"", "creative_"};
	public IIcon[] icons;

	@SideOnly(Side.CLIENT) private IIcon[] overlays;

	public ItemFuelCanister() {
		super(0, 2000);
		setHasSubtypes(true);
		setMaxDamage(0);
	}

	public static boolean isEmpty(final ItemStack itemstack) {
		return getFluidAmount(itemstack) <= 0;
	}

	public static boolean isFull(final ItemStack itemstack) {
		return getFluidAmount(itemstack) >= getContainerCapacity(itemstack);
	}

	public static int getFluidAmount(final ItemStack itemstack) {
		if(itemstack.stackTagCompound == null || !itemstack.stackTagCompound.hasKey("Fluid")) {
			return 0;
		}

		return itemstack.stackTagCompound.getCompoundTag("Fluid").getInteger("Amount");
	}

	public static FluidStack getContainerFluid(final ItemStack itemstack) {
		if(itemstack.stackTagCompound == null || !itemstack.stackTagCompound.hasKey("Fluid")) {
			return null;
		}

		return FluidStack.loadFluidStackFromNBT(itemstack.stackTagCompound.getCompoundTag("Fluid"));
	}

	public static int getContainerCapacity(final ItemStack itemstack) {
		if(itemstack.getItem() instanceof IFluidContainerItem) {
			return ((IFluidContainerItem) itemstack.getItem()).getCapacity(itemstack);
		}

		return 0;
	}

	@Override
	public int fill(final ItemStack container, final FluidStack resource, final boolean doFill) {
		if(resource == null || resource.getFluid() != TFFluids.energon) {
			return 0;
		}

		return super.fill(container, resource, doFill);
	}

	@Override
	public FluidStack drain(final ItemStack container, final int maxDrain, final boolean doDrain) {
		return super.drain(container, maxDrain, container.getItemDamage() == 0 && doDrain);
	}

	@Override
	public void addInformation(final ItemStack itemstack, final EntityPlayer player, final List list, final boolean p_77624_4_) {
		final FluidTankTF tank = new FluidTankTF(getCapacity(itemstack));
		tank.setFluid(getFluid(itemstack));

		list.addAll(TFFormatHelper.toString(tank.format()));
	}

	@Override
	public String getUnlocalizedName(final ItemStack itemstack) {
		final int i = MathHelper.clamp_int(itemstack.getItemDamage(), 0, unlocalizedNames.length - 1);
		return "item." + unlocalizedNames[i] + "fuel_canister";
	}

	@Override
	public void getSubItems(final Item item, final CreativeTabs tab, final List list) {
		for(int i = 0; i < unlocalizedNames.length; ++i) {
			list.add(new ItemStack(item, 1, i));

			for(final Energon energon : TransformersAPI.getEnergonTypes()) {
				final ItemStack itemstack = new ItemStack(item, 1, i);
				fill(itemstack, FluidEnergon.create(energon, capacity), true);
				list.add(itemstack);
			}
		}
	}

	@Override
	public IIcon getIcon(final ItemStack itemstack, final int pass) {
		if(pass > 0) {
			final FluidStack fluidStack = getFluid(itemstack);

			if(fluidStack != null && fluidStack.amount > 0) {
				final int i = Math.round((float) fluidStack.amount / capacity * 4);
				return overlays[i % overlays.length];
			}
		}

		return super.getIcon(itemstack, pass);
	}

	@Override
	public IIcon getIconFromDamage(final int damage) {
		final int i = MathHelper.clamp_int(damage, 0, unlocalizedNames.length - 1);
		return icons[i];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(final ItemStack itemstack, final int pass) {
		if(pass == 1) {
			final FluidStack fluidStack = getFluid(itemstack);

			if(fluidStack != null && fluidStack.amount > 0) {
				return FluidEnergon.getLiquidColor(fluidStack);
			}
		}

		return super.getColorFromItemStack(itemstack, pass);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses() {
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(final IIconRegister iconRegister) {
		icons = new IIcon[unlocalizedNames.length];
		overlays = new IIcon[5];

		for(int i = 0; i < icons.length; ++i) {
			icons[i] = iconRegister.registerIcon(TransformersMod.MODID + ":" + unlocalizedNames[i] + "fuel_canister");
		}

		for(int i = 0; i < overlays.length; ++i) {
			overlays[i] = iconRegister.registerIcon(TransformersMod.MODID + ":fuel_canister_overlay_" + i);
		}
	}
}
