package fiskfille.tf.common.block;

import fiskfille.tf.client.gui.GuiHandlerTF.TFGui;
import fiskfille.tf.common.tileentity.TileEntityTransmitter;
import fiskfille.tf.helper.TFTileHelper;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class BlockTransmitter extends BlockMachineBase {
	public BlockTransmitter() {
		super(Material.rock);
		setHardness(5F);
		setResistance(10F);
	}

	@Override
	public int getBlockHeight() {
		return 3;
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
	public void addCollisionBoxesToList(final World world, final int x, final int y, final int z, final AxisAlignedBB aabb, final List list, final Entity entity) {
		final int metadata = world.getBlockMetadata(x, y, z);
		final float f = 0.0625F;

		if(metadata < 4) {
			addBox(0, 0, 0, 1, f * 4, 1, x, y, z, aabb, list);

			for(int i = 0; i < 26; ++i) {
				final float width = 1 - 0.6F * ((float) i / 26);
				final float f1 = 1 - width;
				addBox(f1 / 2, f * (i + 4), f1 / 2, 1 - f1 / 2, f * (i + 5), 1 - f1 / 2, x, y, z, aabb, list);
			}
		}
		else if(metadata < 8) {
			addBox(0, f * 14, 0, 1, 1, 1, x, y, z, aabb, list);
		}
		else {
			addBox(0, 0, 0, 1, 1, 1, x, y, z, aabb, list);
		}

		setBlockBoundsBasedOnState(world, x, y, z);
	}

	@Override
	public void setBlockBoundsBasedOnState(final IBlockAccess world, final int x, final int y, final int z) {
		final int metadata = world.getBlockMetadata(x, y, z);

		if(metadata < 4) {
			setBlockBounds(0, 0, 0, 1, 3, 1);
		}
		else if(metadata < 8) {
			setBlockBounds(0, -1, 0, 1, 2, 1);
		}
		else {
			setBlockBounds(0, -2, 0, 1, 1, 1);
		}
	}

	@Override
	public boolean onBlockActivated(final World world, final int x, final int y, final int z, final EntityPlayer player, final int side, float hitX, float hitY, final float hitZ) {
		if(super.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ)) {
			return true;
		}

		if(!player.isSneaking()) {
			final TileEntity tile = world.getTileEntity(x, y, z);
			final TileEntity tileBase = TFTileHelper.getTileBase(tile);
			final int metadata = world.getBlockMetadata(x, y, z);
			final int direction = metadata % 4;
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
					switch(side) {
						case 2:
							face = 0;
							break;

						case 3:
							face = 1;
							break;

						case 4:
							face = 2;
							break;

						case 5:
							face = 3;
							break;
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
					case 0:
					case 1:
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

					case 2:
						hitX = 1 - hitX;
						hitY = 1 - hitY;
						break;

					case 3:
						hitY = 1 - hitY;
						break;

					case 4:
						hitX = hitZ;
						hitY = 1 - hitY;
						break;
					case 5:
						hitX = 1 - hitZ;
						hitY = 1 - hitY;
						break;
				}

				if(face == 1) {
					hitX = 1 - hitX;
				}

				if(face < 4) {
					hitY = 1 - hitY;
					hitY += TFTileHelper.getTileBaseOffsets(tile, metadata)[1];
				}

				if(tileBase instanceof TileEntityTransmitter) {
					if(onRightClick(world, tileBase.xCoord, tileBase.yCoord, tileBase.zCoord, (TileEntityTransmitter) tileBase, player, face, hitX, hitY)) {
						return true;
					}
				}
			}

			if(tileBase instanceof TileEntityTransmitter) {
				TFGui.ENERGON_TRANSMITTER.open(player, tileBase);
			}

			return true;
		}

		return false;
	}

	public boolean onRightClick(final World world, final int x, final int y, final int z, final TileEntityTransmitter tile, final EntityPlayer player, final int face, final float hitX, final float hitY) {
		// 0 = back, 1 = front, 2 = left, 3 = right, 4 = top, 5 = bottom
		if(face == 1) {
			final float f = 0.0625F;
			if(hitX > f * 5.5F && hitX < f * 10.5F && hitY > f * 10 && hitY < f * 20) {
				TFGui.RECEIVER_NETWORK.open(player, tile);
				return true;
			}
		}

		return false;
	}

	@Override
	public void registerBlockIcons(final IIconRegister iconRegister) {
		blockIcon = iconRegister.registerIcon("stone");
	}
}
