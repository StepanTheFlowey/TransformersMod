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
	public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean flag) {
		list.add(DataCore.get(itemstack.getItemDamage()).getTranslatedName());
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List subItems) {
		for(int i = 0; i < DataCore.dataCores.size(); ++i) {
			subItems.add(new ItemStack(this, 1, i));
		}
	}

	@Override
	public IIcon getIcon(ItemStack stack, int pass) {
		return pass == 1 ? overlay : super.getIcon(stack, pass);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack itemstack, int pass) {
		return pass == 1 ? DataCore.get(itemstack.getItemDamage()).getColor() : super.getColorFromItemStack(itemstack, pass);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon(TransformersMod.MODID + ":data_core");
		overlay = iconRegister.registerIcon(TransformersMod.MODID + ":data_core_overlay");
	}
}
