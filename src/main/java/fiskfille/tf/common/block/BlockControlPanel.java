package fiskfille.tf.common.block;

import fiskfille.tf.common.groundbridge.DataCore;
import fiskfille.tf.common.item.ItemCSD.DimensionalCoords;
import fiskfille.tf.common.network.MessageTileTrigger;
import fiskfille.tf.common.network.base.TFNetworkManager;
import fiskfille.tf.common.tileentity.TileEntityControlPanel;
import fiskfille.tf.helper.TFTileHelper;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockControlPanel extends BlockMachineBase {
	public BlockControlPanel() {
		super(Material.iron);
		setHardness(4F);
		setResistance(10F);
		setStepSound(soundTypeMetal);
	}

	public static boolean isBlockLeftSideOfPanel(final int metadata) {
		return metadata < 4;
	}

	public static boolean isBlockTopOfPanel(final int metadata) {
		return metadata >= 8;
	}

	public static int getDirection(final int metadata) {
		return metadata % 4;
	}

	@Override
	public int getPlacedRotation(final EntityLivingBase entity) {
		return 0;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public void setBlockBoundsBasedOnState(final IBlockAccess world, final int x, final int y, final int z) {
		final TileEntity tile = TFTileHelper.getTileBase(world.getTileEntity(x, y, z));
		final int metadata = world.getBlockMetadata(x, y, z);
		final int direction = getDirection(metadata);
		final float f = 0.0625F;
		final float f1 = 0.9575F;

		if(isBlockTopOfPanel(metadata)) {
			if(tile instanceof TileEntityControlPanel && ((TileEntityControlPanel) tile).hasUpgrade(DataCore.spaceBridge)) {
				final float width = f * 6;
				final float depth = f * 4;
				final float height = f * 8;

				if(direction == 0) {
					setBlockBounds(0, f1 - 1, 1 - depth, width, f1 - 1 + height, 1);
				}
				else if(direction == 1) {
					setBlockBounds(0, f1 - 1, 0, depth, f1 - 1 + height, width);
				}
				else if(direction == 2) {
					setBlockBounds(1 - width, f1 - 1, 0, 1, f1 - 1 + height, depth);
				}
				else if(direction == 3) {
					setBlockBounds(1 - depth, f1 - 1, 1 - width, 1, f1 - 1 + height, 1);
				}
			}
			else {
				setBlockBounds(0, 0, 0, 0, 0, 0);
			}
		}
		else {
			setBlockBounds(0, 0, 0, 1, f1, 1);
		}
	}

	@Override
	public int getMobilityFlag() {
		return 1;
	}

	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, float hitX, float hitY, final float hitZ) {
		if(super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ)) {
			return true;
		}

		final int metadata = world.getBlockMetadata(x, y, z);
		final int direction = getDirection(metadata);
		int face = -1;

		switch(side) {
			case 0:
				face = 5;
				break;

			case 1:
				face = 4;
				break;
		}

		switch(direction) {
			case 0:
				if(side == 2) {
					face = 0;
				}
				else if(side == 3) {
					face = 1;
				}
				else if(side == 4) {
					face = 2;
				}
				else if(side == 5) {
					face = 3;
				}
				break;

			case 1:
				switch(side) {
					case 5:
						face = 0;
						break;

					case 4:
						face = 1;
						break;

					case 2:
						face = 2;
						break;

					case 3:
						face = 3;
						break;
				}
				break;

			case 2:
				switch(side) {
					case 3:
						face = 0;
						break;

					case 2:
						face = 1;
						break;

					case 5:
						face = 2;
						break;

					case 4:
						face = 3;
						break;
				}
				break;
			case 3:
				switch(side) {
					case 4:
						face = 0;
						break;

					case 5:
						face = 1;
						break;

					case 3:
						face = 2;
						break;

					case 2:
						face = 3;
						break;
				}
				break;
		}

		if(face != -1) {
			switch(side) {
				case 1:
				case 0:
					switch(direction) {
						case 0:
							hitX = 1 - hitX;
							hitY = (side == 1 ? 1 : hitZ * 2) - hitZ;
							break;

						case 1:
							hitY = (side == 0 ? 1 : hitX * 2) - hitX;
							hitX = 1 - hitZ;
							break;

						case 2:
							hitY = (side == 0 ? 1 : hitZ * 2) - hitZ;
							break;

						case 3:
							hitY = (side == 1 ? 1 : hitX * 2) - hitX;
							hitX = hitZ;
							break;
					}
					break;

				case 3:
					hitY = 1 - hitY;
					break;

				case 2:
					hitX = 1 - hitX;
					hitY = 1 - hitY;
					break;

				case 5:
					hitX = 1 - hitZ;
					hitY = 1 - hitY;
					break;

				case 4:
					hitX = hitZ;
					hitY = 1 - hitY;
					break;
			}

			if(face == 1) {
				hitX = 1 - hitX;
			}

			final boolean isSide = !isBlockLeftSideOfPanel(metadata);
			final boolean isTop = isBlockTopOfPanel(metadata);

			if(isSide && face != 2 && face != 3) {
				++hitX;
			}

			if(isTop && face != 4 && face != 5) {
				--hitY;
			}

			final TileEntity tile = TFTileHelper.getTileBase(world.getTileEntity(x, y, z));

			if(tile instanceof TileEntityControlPanel) {
				return onRightClick(world, (TileEntityControlPanel) tile, player, face, hitX, hitY);
			}
		}

		return false;
	}

	public boolean onRightClick(final World world, final TileEntityControlPanel tile, final EntityPlayer player, final int face, final float hitX, final float hitY) {
		// 0 = front, 1 = back, 2 = right, 3 = left, 4 = top, 5 = bottom
		final float f = 0.0625F;

		if(world.isRemote) {
			if(face == 0) {
				if(hitY > f * 4 && hitY <= f * 7.5F) {
					for(int i = 0; i < 3; ++i) {
						if(player.getHeldItem() != null && tile.isItemValidForSlot(i, player.getHeldItem()) || tile.getStackInSlot(i) != null && player.getHeldItem() == null) {
							if(hitX > f * (19 + i * 3.5F) && hitX <= f * (22 + i * 3.5F)) {
								sendActionPacket(tile, player, 15 + i);
								return true;
							}
						}
					}
				}

				if(hitY >= f * -3.7F && hitY < f * -1.7F) {
					if(hitX >= f * 26.5F && hitX < f * 28.5F) {
						sendActionPacket(tile, player, 18);
						return true;
					}
					else if(hitX >= f * 28.7F && hitX < f * 30.7F) {
						sendActionPacket(tile, player, 19);
						return true;
					}
				}
			}
			else if(face == 4) {
				if(hitY > f * 2 && hitY <= f * 4.5F) {
					if(hitX > f * 1.15F && hitX <= f * 3.21F) {
						sendActionPacket(tile, player, 1);
						return true;
					}

					if(hitX > f * 3.25F && hitX <= f * 5.39F) {
						sendActionPacket(tile, player, 2);
						return true;
					}

					if(hitX > f * 5.55F && hitX <= f * 7.6F) {
						sendActionPacket(tile, player, 3);
						return true;
					}

					if(hitX > f * 7.6F && hitX <= f * 10F) {
						sendActionPacket(tile, player, 4);
						return true;
					}
				}

				if(hitY > f * 6.2F && hitY <= f * 8.8F) {
					if(hitX > f * 1.15F && hitX <= f * 3.21F) {
						sendActionPacket(tile, player, 5);
						return true;
					}

					if(hitX > f * 3.25F && hitX <= f * 5.39F) {
						sendActionPacket(tile, player, 6);
						return true;
					}

					if(hitX > f * 5.55F && hitX <= f * 7.6F) {
						sendActionPacket(tile, player, 7);
						return true;
					}

					if(hitX > f * 7.6F && hitX <= f * 10F) {
						sendActionPacket(tile, player, 8);
						return true;
					}
				}

				if(hitY > f * 10F && hitY <= f * 13F) {
					if(hitX > f * 1.15F && hitX <= f * 3.21F) {
						sendActionPacket(tile, player, 9);
						return true;
					}

					if(hitX > f * 3.25F && hitX <= f * 5.39F) {
						sendActionPacket(tile, player, 10);
						return true;
					}

					if(hitX > f * 5.55F && hitX <= f * 7.6F) {
						sendActionPacket(tile, player, 11);
						return true;
					}

					if(hitX > f * 7.6F && hitX <= f * 10F) {
						sendActionPacket(tile, player, 12);
						return true;
					}
				}

				if(hitX > f * 13.5F && hitX <= f * 18.5F && hitY > f * 5.65F && hitY <= f * 10.85F) {
					if(!tile.data.activationLeverState) {
						sendActionPacket(tile, player, 13);
					}

					return true;
				}

				if(hitX > f * 23F && hitX <= f * 31F && hitY > f * 4F && hitY <= f * 12.75F) {
					if(tile.activationLeverCoverState && (tile.activationLeverTimer == 0 || tile.activationLeverTimer == 1)) {
						sendActionPacket(tile, player, 14);
					}

					return true;
				}
			}
		}

		return false;
	}

	public void sendActionPacket(final TileEntityControlPanel tile, final EntityPlayer player, final int action) {
		if(player.worldObj.isRemote) {
			TFNetworkManager.networkWrapper.sendToServer(new MessageTileTrigger(new DimensionalCoords(tile), player, action));
		}
	}

	@Override
	public void registerBlockIcons(final IIconRegister iconRegister) {
		blockIcon = iconRegister.registerIcon("iron_block");
	}
}
