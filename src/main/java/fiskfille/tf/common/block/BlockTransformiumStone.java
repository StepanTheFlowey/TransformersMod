package fiskfille.tf.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

import java.util.Random;

public class BlockTransformiumStone extends BlockBasic {
	public BlockTransformiumStone() {
		super(Material.rock);
		setHarvestLvl("pickaxe", 2);
		setHardness(2.5F);
		setResistance(10F);
		setTickRandomly(true);
	}

	@Override
	public int tickRate(final World world) {
		return 1000000000;
	}

	@Override
	public void updateTick(final World world, final int x, final int y, final int z, final Random rand) {
		if(rand.nextInt(100000) == 0 && !world.canBlockSeeTheSky(x, y + 1, z)) {
			world.setBlock(x, y, z, TFBlocks.cosmicRust, 1, 2);
		}

		onBlockAdded(world, x, y, z);
	}

	@Override
	public void onBlockAdded(final World world, final int x, final int y, final int z) {
		world.scheduleBlockUpdate(x, y, z, this, tickRate(world));
	}

	@Override
	public void onNeighborBlockChange(final World world, final int x, final int y, final int z, final Block block) {
		onBlockAdded(world, x, y, z);
	}
}
