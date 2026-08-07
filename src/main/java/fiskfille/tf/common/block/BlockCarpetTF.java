package fiskfille.tf.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.common.item.ItemDyeTF;
import net.minecraft.block.BlockCarpet;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;

public class BlockCarpetTF extends BlockCarpet {
	protected BlockCarpetTF() {
		super();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(final int side, final int metadata) {
		return TFBlocks.wool.getIcon(side, metadata);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(final Item item, final CreativeTabs tab, final List list) {
		for(int i = 0; i < ItemDyeTF.dyes.length; ++i) {
			list.add(new ItemStack(item, 1, i));
		}
	}
}
