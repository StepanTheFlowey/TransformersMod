package fiskfille.tf.common.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.IIcon;

public class ItemBlockWithMetadata extends ItemBlock {
	public ItemBlockWithMetadata(final Block block) {
		super(block);
		setMaxDamage(0);
		setHasSubtypes(true);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(final int damage) {
		return field_150939_a.getIcon(2, damage);
	}

	@Override
	public int getMetadata(final int meta) {
		return meta;
	}
}
