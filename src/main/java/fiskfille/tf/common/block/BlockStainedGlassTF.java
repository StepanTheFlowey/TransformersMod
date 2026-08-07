package fiskfille.tf.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.common.item.ItemDyeTF;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

import java.util.List;

public class BlockStainedGlassTF extends BlockStainedGlass {
	private static IIcon[] icons;

	public BlockStainedGlassTF() {
		super(Material.glass);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(final IBlockAccess world, final int x, final int y, final int z, final int side) {
		final Block block = world.getBlock(x, y, z);

		if(this == TFBlocks.stainedGlass) {
			if(world.getBlockMetadata(x, y, z) != world.getBlockMetadata(x - Facing.offsetsXForSide[side], y - Facing.offsetsYForSide[side], z - Facing.offsetsZForSide[side])) {
				return true;
			}

			if(block == this) {
				return false;
			}
		}

		return super.shouldSideBeRendered(world, x, y, z, side);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(final int side, final int metadata) {
		return icons[MathHelper.clamp_int(metadata, 0, icons.length - 1)];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(final Item item, final CreativeTabs tab, final List list) {
		for(int i = 0; i < ItemDyeTF.dyes.length; ++i) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(final IIconRegister iconRegister) {
		icons = new IIcon[ItemDyeTF.dyes.length];

		for(int i = 0; i < icons.length; ++i) {
			icons[i] = iconRegister.registerIcon(getTextureName() + "_" + ItemDyeTF.dyes[i]);
		}
	}
}
