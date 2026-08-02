package fiskfille.tf.common.block;

import fiskfille.tf.client.render.block.RenderBlockEnergonOre;
import fiskfille.tf.common.item.TFItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class BlockEnergonOre extends Block {
	public static int renderPass;
	private IIcon[] icons;

	public BlockEnergonOre() {
		super(Material.rock);
		setHardness(3F);
		setResistance(5F);
	}

	@Override
	public int getMixedBrightnessForBlock(IBlockAccess world, int x, int y, int z) {
		if(renderPass > 1) {
			return 0xF000F0;
		}

		return super.getMixedBrightnessForBlock(world, x, y, z);
	}

	@Override
	public int getRenderType() {
		return RenderBlockEnergonOre.renderId;
	}

	@Override
	public Item getItemDropped(int metadata, Random random, int fortune) {
		return TFItems.energonDust;
	}

	@Override
	public int quantityDroppedWithBonus(int fortune, Random random) {
		return quantityDropped(random) + random.nextInt(fortune + 1);
	}

	@Override
	public int quantityDropped(Random random) {
		return 4 + random.nextInt(2);
	}

	@Override
	public int getExpDrop(IBlockAccess world, int metadata, int fortune) {
		final Random random = ThreadLocalRandom.current();

		if(getItemDropped(metadata, random, fortune) != Item.getItemFromBlock(this)) {
			return 1 + random.nextInt(5);
		}

		return 0;
	}

	@Override
	public IIcon getIcon(int side, int metadata) {
		return icons[MathHelper.clamp_int(renderPass, 0, icons.length - 1)];
	}

	@Override
	public void registerBlockIcons(IIconRegister iconRegister) {
		icons = new IIcon[]{
						iconRegister.registerIcon(getTextureName()),
						iconRegister.registerIcon(getTextureName() + "_background"),
						iconRegister.registerIcon(getTextureName() + "_overlay")
		};
	}
}
