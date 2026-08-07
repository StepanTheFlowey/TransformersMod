package fiskfille.tf.common.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.common.groundbridge.DataCore;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class ItemDataCore extends Item {
	@SideOnly(Side.CLIENT) private IIcon overlay;

	public ItemDataCore() {
		setMaxStackSize(1);
		setHasSubtypes(true);
	}

	@Override
	public void addInformation(final ItemStack itemstack, final EntityPlayer player, final List list, final boolean flag) {
		list.add(DataCore.get(itemstack.getItemDamage()).getTranslatedName());
	}

	@Override
	public void getSubItems(final Item item, final CreativeTabs tab, final List subItems) {
		for(int i = 0; i < DataCore.dataCores.size(); ++i) {
			subItems.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public IIcon getIcon(final ItemStack stack, final int pass) {
		return pass == 1 ? overlay : super.getIcon(stack, pass);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(final ItemStack itemstack, final int pass) {
		return pass == 1 ? DataCore.get(itemstack.getItemDamage()).getColor() : super.getColorFromItemStack(itemstack, pass);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(final IIconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon(TransformersMod.MODID + ":data_core");
		overlay = iconRegister.registerIcon(TransformersMod.MODID + ":data_core_overlay");
	}
}
