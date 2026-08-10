package fiskfille.tf.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.common.data.TFEntityData;
import fiskfille.tf.common.data.tile.TileDataControlPanel;
import fiskfille.tf.common.item.ItemCSD.DimensionalCoords;
import fiskfille.tf.common.tileentity.TileEntityControlPanel;
import fiskfille.tf.common.tileentity.TileEntityGroundBridgeTeleporter;
import fiskfille.tf.common.world.TeleporterGroundBridge;
import fiskfille.tf.helper.TFTileHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Random;

import static net.minecraftforge.common.util.ForgeDirection.*;

public class BlockGroundBridgeTeleporter extends BlockBreakable implements ITileEntityProvider {
	public BlockGroundBridgeTeleporter() {
		super(TransformersMod.MODID + ":ground_bridge_teleporter", Material.portal, false);
		setTickRandomly(true);
		// setLightLevel(1);
	}

	public static void spawnTeleporter(final World world, final int x, final int y, final int z, final TileEntityControlPanel tile) {
		if(!tile.data.errors.isEmpty()) {
			return;
		}

		if(isNorthSouthFacingFramePresent(world, x, y, z)) {
			fillNorthFacingFrame(world, x, y, z, TFBlocks.groundBridgeTeleporter, tile, false);
		}
		else if(isEastWestFacingFramePresent(world, x, y, z)) {
			fillEastFacingFrame(world, x, y, z, TFBlocks.groundBridgeTeleporter, tile, false);
		}
	}

	public static void doTeleport(final Entity entity, final TileEntityGroundBridgeTeleporter teleporter) {
		if(!entity.worldObj.isRemote) {
//            TFNetworkManager.networkWrapper.sendToDimension(new MessageGroundBridgeTeleport(entity, teleporter), entity.dimension);
			doTeleportClient(entity, teleporter);
		}
	}

	public static void doTeleportClient(final Entity entity, final TileEntityGroundBridgeTeleporter teleporter) {
		if(teleporter.controlPanel != null) {
			final TileDataControlPanel data = (TileDataControlPanel) TFTileHelper.getTileData(teleporter.controlPanel);

			if(data != null) {
				if(data.framePos != null) {
					double posX = data.destination.posX + 0.5D;
					double posY = data.modifiedDestY;
					double posZ = data.destination.posZ + 0.5D;
					int dimension = data.destination.dimension;
					int srcYaw = data.frameDirection;
					int dstYaw = data.direction;

					if(teleporter.returnPortal()) {
						posX = data.framePos.posX + 0.5D;
						posY = data.framePos.posY + 1D;
						posZ = data.framePos.posZ + 0.5D;
						dimension = data.framePos.dimension;
						srcYaw = data.direction;
						dstYaw = data.frameDirection;
					}

					final float yawDiff = entity.rotationYaw - (srcYaw * 90 + 180);
					final float yaw = dstYaw * 90 + 180 * 90 + yawDiff;

					if(entity instanceof EntityPlayerMP) {
						final EntityPlayerMP playerMP = (EntityPlayerMP) entity;
						final double motionX = entity.motionX;
						final double motionZ = entity.motionZ;

						if(dimension != entity.dimension) {
							playerMP.mcServer.getConfigurationManager().transferPlayerToDimension(playerMP, dimension, new TeleporterGroundBridge(playerMP.mcServer.worldServerForDimension(dimension)));
						}

						playerMP.playerNetServerHandler.setPlayerLocation(posX, posY, posZ, yaw, entity.rotationPitch);
						entity.motionX = motionX;
						entity.motionZ = motionZ;
					}
					else if(!(entity instanceof EntityPlayer) && dimension == entity.dimension) {
						final double motionX = entity.motionX;
						final double motionZ = entity.motionZ;

//                        TODO: Re-enable Ground Bridge functionality for non-player entities
//                        if (dimension != entity.dimension)
//                        {
//                            TFHelper.travelToDimension(entity, dimension, new TeleporterGroundBridge(controlPanel.getDestWorld()));
//                        }

						entity.setLocationAndAngles(posX, posY, posZ, yaw, entity.rotationPitch);
						entity.motionX = motionX;
						entity.motionZ = motionZ;
					}
				}
			}
		}
	}

	public static int func_149999_b(final int meta) {
		return meta & 3;
	}

	public static boolean isReturnPortal(final int metadata) {
		return metadata > 1;
	}

	public static boolean isNorthSouthFacingFramePresent(final IBlockAccess world, final int x, final int y, final int z) {
		final Block b = TFBlocks.groundBridgeFrame;

		return
			world.getBlock(x, y, z) == b &&
			world.getBlock(x - 1, y, z) == b &&
			world.getBlock(x + 1, y, z) == b &&
			world.getBlock(x - 2, y + 1, z) == b &&
			world.getBlock(x + 2, y + 1, z) == b &&
			world.getBlock(x - 3, y + 2, z) == b &&
			world.getBlock(x + 3, y + 2, z) == b &&
			world.getBlock(x - 3, y + 3, z) == b &&
			world.getBlock(x + 3, y + 3, z) == b &&
			world.getBlock(x + 3, y + 4, z) == b &&
			world.getBlock(x + 3, y + 4, z) == b &&
			world.getBlock(x + 2, y + 5, z) == b &&
			world.getBlock(x + 2, y + 5, z) == b &&
			world.getBlock(x - 1, y + 6, z) == b &&
			world.getBlock(x + 1, y + 6, z) == b &&
			world.getBlock(x, y + 6, z) == b;
	}

	public static boolean isEastWestFacingFramePresent(final IBlockAccess world, final int x, final int y, final int z) {
		final Block b = TFBlocks.groundBridgeFrame;

		return
			world.getBlock(x, y, z) == b &&
			world.getBlock(x, y, z - 1) == b &&
			world.getBlock(x, y, z + 1) == b &&
			world.getBlock(x, y + 1, z - 2) == b &&
			world.getBlock(x, y + 1, z + 2) == b &&
			world.getBlock(x, y + 2, z - 3) == b &&
			world.getBlock(x, y + 2, z + 3) == b &&
			world.getBlock(x, y + 3, z - 3) == b &&
			world.getBlock(x, y + 3, z + 3) == b &&
			world.getBlock(x, y + 4, z + 3) == b &&
			world.getBlock(x, y + 4, z + 3) == b &&
			world.getBlock(x, y + 5, z + 2) == b &&
			world.getBlock(x, y + 5, z + 2) == b &&
			world.getBlock(x, y + 6, z - 1) == b &&
			world.getBlock(x, y + 6, z + 1) == b &&
			world.getBlock(x, y + 6, z) == b;
	}

	public static void fillNorthFacingFrame(final World world, final int x, final int y, final int z, final Block block, final TileEntityControlPanel tile, final boolean returnPortal) {
		final DimensionalCoords coords = new DimensionalCoords(tile);
		final int metadata = returnPortal ? 2 : 0;

		for(int i = 0; i < 5; ++i) {
			for(int j = 0; j < 3; ++j) {
				int k = 0;
				int l = 0;

				if(world.getTileEntity(x - 1 + j, y + 1 + i, z) instanceof TileEntityGroundBridgeTeleporter) {
					final TileEntityGroundBridgeTeleporter tileentity = (TileEntityGroundBridgeTeleporter) world.getTileEntity(x - 1 + j, y + 1 + i, z);
					k = tileentity.ticks;
				}
				if(world.getTileEntity(x - 2 + i, y + 2 + j, z) instanceof TileEntityGroundBridgeTeleporter) {
					final TileEntityGroundBridgeTeleporter tileentity = (TileEntityGroundBridgeTeleporter) world.getTileEntity(x - 2 + i, y + 2 + j, z);
					l = tileentity.ticks;
				}

				world.setBlock(x - 1 + j, y + 1 + i, z, block, metadata, 2);
				world.setBlock(x - 2 + i, y + 2 + j, z, block, metadata, 2);

				if(world.getTileEntity(x - 1 + j, y + 1 + i, z) instanceof TileEntityGroundBridgeTeleporter) {
					final TileEntityGroundBridgeTeleporter tileentity = (TileEntityGroundBridgeTeleporter) world.getTileEntity(x - 1 + j, y + 1 + i, z);
					tileentity.controlPanel = coords;
					tileentity.lastUpdate = 0;
					tileentity.ticks = ++k;
				}
				if(world.getTileEntity(x - 2 + i, y + 2 + j, z) instanceof TileEntityGroundBridgeTeleporter) {
					final TileEntityGroundBridgeTeleporter tileentity = (TileEntityGroundBridgeTeleporter) world.getTileEntity(x - 2 + i, y + 2 + j, z);
					tileentity.controlPanel = coords;
					tileentity.lastUpdate = 0;
					tileentity.ticks = l;
				}
			}
		}

		world.setBlockMetadataWithNotify(x, y + 3, z, metadata + 1, 2);
	}

	public static void fillEastFacingFrame(final World world, final int x, final int y, final int z, final Block block, final TileEntityControlPanel tile, final boolean returnPortal) {
		final DimensionalCoords coords = new DimensionalCoords(tile);
		final int metadata = returnPortal ? 2 : 0;

		for(int i = 0; i < 5; ++i) {
			for(int j = 0; j < 3; ++j) {
				int k = 0;
				int l = 0;

				if(world.getTileEntity(x, y + 1 + i, z - 1 + j) instanceof TileEntityGroundBridgeTeleporter) {
					final TileEntityGroundBridgeTeleporter tileentity = (TileEntityGroundBridgeTeleporter) world.getTileEntity(x, y + 1 + i, z - 1 + j);
					k = tileentity.ticks;
				}
				if(world.getTileEntity(x, y + 2 + j, z - 2 + i) instanceof TileEntityGroundBridgeTeleporter) {
					final TileEntityGroundBridgeTeleporter tileentity = (TileEntityGroundBridgeTeleporter) world.getTileEntity(x, y + 2 + j, z - 2 + i);
					l = tileentity.ticks;
				}

				world.setBlock(x, y + 1 + i, z - 1 + j, block, metadata, 2);
				world.setBlock(x, y + 2 + j, z - 2 + i, block, metadata, 2);

				if(world.getTileEntity(x, y + 1 + i, z - 1 + j) instanceof TileEntityGroundBridgeTeleporter) {
					final TileEntityGroundBridgeTeleporter tileentity = (TileEntityGroundBridgeTeleporter) world.getTileEntity(x, y + 1 + i, z - 1 + j);
					tileentity.controlPanel = coords;
					tileentity.lastUpdate = 0;
					tileentity.ticks = ++k;
				}
				if(world.getTileEntity(x, y + 2 + j, z - 2 + i) instanceof TileEntityGroundBridgeTeleporter) {
					final TileEntityGroundBridgeTeleporter tileentity = (TileEntityGroundBridgeTeleporter) world.getTileEntity(x, y + 2 + j, z - 2 + i);
					tileentity.controlPanel = coords;
					tileentity.lastUpdate = 0;
					tileentity.ticks = l;
				}
			}
		}

		world.setBlockMetadataWithNotify(x, y + 3, z, metadata + 1, 2);
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(final World world, final int x, final int y, final int z) {
		return null;
	}

	public boolean canPaneConnectTo(final IBlockAccess world, final int x, final int y, final int z, final ForgeDirection dir) {
		return world.getBlock(x, y, z) == TFBlocks.groundBridgeTeleporter;
	}

	@Override
	public void setBlockBoundsBasedOnState(final IBlockAccess world, final int x, final int y, final int z) {
		final float thickness = 0.125F;
		float f = 0.5F - thickness;
		float f1 = 0.5F + thickness;
		float f2 = 0.5F - thickness;
		float f3 = 0.5F + thickness;
		final boolean flag = canPaneConnectTo(world, x, y, z - 1, NORTH) || canPaneConnectTo(world, x, y, z + 1, SOUTH);
		final boolean flag1 = canPaneConnectTo(world, x - 1, y, z, WEST) || canPaneConnectTo(world, x + 1, y, z, EAST);

		if(!flag && flag1) {
			f = 0;
			f1 = 1;
		}

		if(flag && !flag1) {
			f2 = 0;
			f3 = 1;
		}

		setBlockBounds(f, 0, f2, f1, 1, f3);
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(final IBlockAccess world, final int x, final int y, final int z, final int side) {
		int i1 = 0;

		if(world.getBlock(x, y, z) == this) {
			i1 = func_149999_b(world.getBlockMetadata(x, y, z));

			if(i1 == 0) {
				return false;
			}

			if(i1 == 2 && side != 5 && side != 4) {
				return false;
			}

			if(i1 == 1 && side != 3 && side != 2) {
				return false;
			}
		}

		final boolean flag = world.getBlock(x - 1, y, z) == this && world.getBlock(x - 2, y, z) != this;
		final boolean flag1 = world.getBlock(x + 1, y, z) == this && world.getBlock(x + 2, y, z) != this;
		final boolean flag2 = world.getBlock(x, y, z - 1) == this && world.getBlock(x, y, z - 2) != this;
		final boolean flag3 = world.getBlock(x, y, z + 1) == this && world.getBlock(x, y, z + 2) != this;
		final boolean flag4 = flag || flag1 || i1 == 1;
		final boolean flag5 = flag2 || flag3 || i1 == 2;
		return flag4 && side == 4 || flag4 && side == 5 || flag5 && side == 2 || flag5 && side == 3;
	}

	@Override
	public int quantityDropped(final Random rand) {
		return 0;
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public void onEntityCollidedWithBlock(final World world, final int x, final int y, final int z, final Entity entity) {
		if(!world.isRemote) {
			if(entity.ridingEntity == null && entity.riddenByEntity == null) {
				if(TFEntityData.getData(entity).groundBridgeCooldown == 0) {
					final TileEntityGroundBridgeTeleporter teleporter = (TileEntityGroundBridgeTeleporter) world.getTileEntity(x, y, z);

					if(teleporter != null && teleporter.controlPanel != null) {
						doTeleport(entity, teleporter);
					}
				}

				TFEntityData.getData(entity).groundBridgeCooldown = 10;
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getRenderBlockPass() {
		return 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(final World world, final int x, final int y, final int z) {
		return Item.getItemById(0);
	}

	@Override
	public TileEntity createNewTileEntity(final World world, final int metadata) {
		return new TileEntityGroundBridgeTeleporter();
	}
}
