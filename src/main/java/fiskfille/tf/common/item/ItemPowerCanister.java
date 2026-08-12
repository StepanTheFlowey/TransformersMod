package fiskfille.tf.common.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import java.util.List;

public class ItemPowerCanister extends ItemEnergyContainer {
	public final String[] tiers = {"iron", "gold", "diamond", "emerald"};

	@SideOnly(Side.CLIENT) private IIcon[] icons;

	public ItemPowerCanister() {
		super(0);
		setMaxStackSize(1);
		setHasSubtypes(true);
	}

	@Override
	public int getEnergyCapacity(final ItemStack itemstack) {
		final int tier = Math.min(itemstack.getItemDamage(), tiers.length - 1);
		int i = 4;

		for(int j = 0; j < tier; ++j) {
			i *= 2;
		}

		return i * 1000;
	}

	@Override
	public void getSubItems(final Item item, final CreativeTabs tab, final List subItems) {
		for(int i = 0; i < tiers.length; ++i) {
			final ItemStack itemstack = new ItemStack(this, 1, i);
			subItems.add(itemstack.copy());

			receiveEnergy(itemstack, getEnergyCapacity(itemstack), false);
			subItems.add(itemstack.copy());
		}
	}

	@Override
	public String getItemStackDisplayName(final ItemStack itemstack) {
		final int tier = Math.min(itemstack.getItemDamage(), tiers.length - 1);
		return StatCollector.translateToLocal("item.power_canister_" + tiers[tier] + ".name");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(final int damage) {
		return icons[Math.min(damage, tiers.length - 1)];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamageForRenderPass(final int damage, final int pass) {
		return pass == 1 ? icons[tiers.length] : icons[Math.min(damage, tiers.length - 1)];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(final IIconRegister iconRegister) {
		icons = new IIcon[tiers.length + 1];

		for(int i = 0; i < tiers.length; ++i) {
			icons[i] = iconRegister.registerIcon(getIconString() + "_" + tiers[i]);
		}

		icons[tiers.length] = iconRegister.registerIcon(getIconString() + "_overlay");
	}
}
