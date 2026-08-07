package fiskfille.tf.common.generator;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

public final class WorldGenCrystal extends WorldGenerator {
	private final Block target;
	private final Material growthMaterial;

	public WorldGenCrystal(final Block block, final Material material) {
		target = block;
		growthMaterial = material;
	}

	@Override
	public boolean generate(final World world, final Random rand, final int x, final int y, final int z) {
		boolean flag = false;

		final int range = 3;
		for(int i = -range; i <= range && !flag; ++i) {
			for(int j = -range; j <= range; ++j) {
				final int xPosition = x >> 4 + i;
				final int zPosition = z >> 4 + j;
				final Random random = new Random(world.getSeed() + ((long) xPosition * xPosition * 0x4c1906) + (xPosition * 0x5ac0dbL) + (long) zPosition * zPosition * 0x4307a7L + (zPosition * 0x5f24fL) ^ 0x3ad8025f);

				if(random.nextInt(300) == 0) {
					flag = true;
					break;
				}
			}
		}

		if(flag && world.getBlock(x, y, z) == Blocks.air) {
			for(final ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
				if(world.getBlock(x + dir.offsetX, y + dir.offsetY, z + dir.offsetZ).getMaterial() == growthMaterial) {
					return world.setBlock(x, y, z, target, dir.getOpposite().ordinal(), 2);
				}
			}
		}

		return false;
	}
}
