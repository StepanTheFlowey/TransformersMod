package fiskfille.tf.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.TransformersMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class BlockCosmicRust extends Block {
	private IIcon coreIcon;

	protected BlockCosmicRust() {
		super(Material.circuits);
		setTickRandomly(true);
	}

	@Override
	public float getBlockHardness(final World world, final int x, final int y, final int z) {
		return world.getBlockMetadata(x, y, z) == 1 ? 2F : blockHardness;
	}

	@Override
	public int damageDropped(final int meta) {
		return meta;
	}

	@Override
	public int quantityDropped(final int meta, final int fortune, final Random random) {
		return meta == 1 ? 1 : 0;
	}

	@Override
	public int tickRate(final World world) {
		return 1000000000;
	}

	@Override
	public void updateTick(final World world, final int x, final int y, final int z, final Random rand) {
		tryRust(world, x + 1, y, z);
		tryRust(world, x - 1, y, z);
		tryRust(world, x, y - 1, z);
		tryRust(world, x, y + 1, z);
		tryRust(world, x, y, z - 1);
		tryRust(world, x, y, z + 1);

		if(world.getBlockMetadata(x, y, z) == 0) {
			world.setBlockToAir(x, y, z);
		}
	}

	private void tryRust(final World world, final int x, final int y, final int z) {
		if(world.getBlock(x, y, z) == TFBlocks.transformiumStone) {
			world.setBlock(x, y, z, this);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(final Item item, final CreativeTabs tab, final List subBlocks) {
		subBlocks.add(new ItemStack(item, 1, 1));
	}

	@Override
	public void onBlockAdded(final World world, final int x, final int y, final int z) {
		world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
	}

	@Override
	public void onNeighborBlockChange(final World world, final int x, final int y, final int z, final Block block) {
		onBlockAdded(world, x, y, z);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(final int side, final int metadata) {
		return metadata == 1 ? coreIcon : blockIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(final IIconRegister iconRegister) {
		blockIcon = iconRegister.registerIcon(TransformersMod.MODID + ":cosmic_rust");
		coreIcon = iconRegister.registerIcon(TransformersMod.MODID + ":cosmic_rust_core");
	}
}
