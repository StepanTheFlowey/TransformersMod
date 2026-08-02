package fiskfille.tf.common.item;

import fiskfille.tf.common.block.BlockControlPanel;
import fiskfille.tf.common.block.TFBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemGroundBridgeControl extends ItemMachine {
	public ItemGroundBridgeControl(Block block) {
		super(block);
	}

	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if(world.isRemote) {
			return true;
		}
		else {
			switch(side) {
				case 0:
					--y;
					break;

				case 1:
					++y;
					break;

				case 2:
					--z;
					break;

				case 3:
					++z;
					break;

				case 4:
					--x;
					break;

				case 5:
					++x;
					break;
			}

			final BlockControlPanel block = (BlockControlPanel) TFBlocks.groundBridgeControlPanel;
			final int direction = MathHelper.floor_double(player.rotationYaw * 4F / 360F + 0.5D) & 3;
			byte x1 = 0;
			byte z1 = 0;

			switch(direction) {
				case 0:
					x1 = -1;
					break;

				case 1:
					z1 = -1;
					break;

				case 2:
					x1 = 1;
					break;

				case 3:
					z1 = 1;
					break;
			}

			if(player.canPlayerEdit(x, y, z, side, itemstack) && player.canPlayerEdit(x + x1, y, z + z1, side, itemstack) && player.canPlayerEdit(x + x1, y + 1, z + z1, side, itemstack)) {
				if(world.isAirBlock(x, y, z) && world.isAirBlock(x + x1, y, z + z1) && world.isAirBlock(x, y + 1, z) && world.isAirBlock(x + x1, y + 1, z + z1)) {
					if(placeBlockAt(itemstack, player, world, x, y, z, side, hitX, hitY, hitZ, direction)) {
						world.setBlock(x + x1, y, z + z1, block, direction + 4, 3);

						if(world.getBlock(x + x1, y, z + z1) == block) {
							world.setBlock(x + x1, y + 1, z + z1, block, direction + 8, 3);
						}

						world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, block.stepSound.func_150496_b(), (block.stepSound.getVolume() + 1F) / 2F, block.stepSound.getPitch() * 0.8F);
						--itemstack.stackSize;
						return true;
					}
				}
			}
		}

		return false;
	}
}
