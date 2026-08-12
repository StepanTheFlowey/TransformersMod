package fiskfille.tf.helper;

import fiskfille.tf.common.energon.power.*;
import fiskfille.tf.common.item.ItemCSD.DimensionalCoords;
import fiskfille.tf.common.tileentity.TileEntityMachine;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashSet;

public final class TFEnergyHelper {
	public static boolean isInRange(final TileEntity transmitterTile, final TileEntity receiverTile) {
		final IEnergyTransmitter transmitter = (IEnergyTransmitter) transmitterTile;
		final IEnergyReceiver receiver = (IEnergyReceiver) receiverTile;
		final Vec3 src = transmitter.getEnergyOutputOffset().addVector(transmitterTile.xCoord + 0.5D, transmitterTile.yCoord + 0.5D, transmitterTile.zCoord + 0.5D);
		final Vec3 dst = receiver.getEnergyInputOffset().addVector(receiverTile.xCoord + 0.5D, receiverTile.yCoord + 0.5D, receiverTile.zCoord + 0.5D);

		return src.distanceTo(dst) <= transmitter.getRange();
	}

	public static boolean isInRange(final TileEntity transmitterTile, final ChunkCoordinates dstCoords) {
		final IEnergyTransmitter transmitter = (IEnergyTransmitter) transmitterTile;
		final Vec3 src = transmitter.getEnergyOutputOffset().addVector(transmitterTile.xCoord + 0.5D, transmitterTile.yCoord + 0.5D, transmitterTile.zCoord + 0.5D);
		final Vec3 dst = Vec3.createVectorHelper(dstCoords.posX + 0.5D, dstCoords.posY + 0.5D, dstCoords.posZ + 0.5D);

		return src.distanceTo(dst) <= transmitter.getRange();
	}

	public static TransmissionHandler getTransmissionHandler(final TileEntity tile) {
		if(tile instanceof IEnergyTransmitter) {
			return ((IEnergyTransmitter) tile).getTransmissionHandler();
		}

		return null;
	}

	public static ReceiverHandler getReceiverHandler(final TileEntity tile) {
		if(tile instanceof IEnergyReceiver) {
			return ((IEnergyReceiver) tile).getReceiverHandler();
		}

		return null;
	}

	public static ArrayList<DimensionalCoords> getDescendants(final IEnergyTransmitter transmitter) {
		return getDescendants(transmitter, new ArrayList<>());
	}

	private static ArrayList<DimensionalCoords> getDescendants(final IEnergyTransmitter transmitter, final ArrayList<DimensionalCoords> list) {
		final TransmissionHandler handler = transmitter.getTransmissionHandler();

		for(final ReceiverEntry entry : handler.getReceivers()) {
			list.add(entry.getCoords());

			if(entry.getTile() instanceof IEnergyTransmitter) {
				getDescendants(entry.getTransmitter(), list);
			}
		}

		return list;
	}

	public static MovingObjectPosition rayTraceBlocks(final World world, final Vec3 src, final Vec3 dst) {
		return rayTraceBlocks(world, src, dst, false, false, false);
	}

	public static MovingObjectPosition rayTraceBlocks(final World world, final Vec3 src, final Vec3 dst, final boolean flag, final boolean flag1, final boolean flag2) {
		if(!Double.isNaN(src.xCoord) && !Double.isNaN(src.yCoord) && !Double.isNaN(src.zCoord)) {
			if(!Double.isNaN(dst.xCoord) && !Double.isNaN(dst.yCoord) && !Double.isNaN(dst.zCoord)) {
				final int x = MathHelper.floor_double(dst.xCoord);
				final int y = MathHelper.floor_double(dst.yCoord);
				final int z = MathHelper.floor_double(dst.zCoord);
				int x1 = MathHelper.floor_double(src.xCoord);
				int y1 = MathHelper.floor_double(src.yCoord);
				int z1 = MathHelper.floor_double(src.zCoord);
				final Block block = world.getBlock(x1, y1, z1);
				int metadata = world.getBlockMetadata(x1, y1, z1);

				if((!flag1 || block.getCollisionBoundingBoxFromPool(world, x1, y1, z1) != null) && block.canCollideCheck(metadata, flag) && !(!block.isOpaqueCube() && world.getTileEntity(x1, y1, z1) instanceof IEnergyReceiver)) {
					final MovingObjectPosition mop = block.collisionRayTrace(world, x1, y1, z1, src, dst);

					if(mop != null) {
						return mop;
					}
				}

				MovingObjectPosition movingobjectposition2 = null;
				metadata = 200;

				while(metadata-- >= 0) {
					if(Double.isNaN(src.xCoord) || Double.isNaN(src.yCoord) || Double.isNaN(src.zCoord)) {
						return null;
					}

					if(x1 == x && y1 == y && z1 == z) {
						return flag2 ? movingobjectposition2 : null;
					}

					boolean flag6 = true;
					boolean flag3 = true;
					boolean flag4 = true;
					double d0 = 999;
					double d1 = 999;
					double d2 = 999;

					if(x > x1) {
						d0 = x1 + 1;
					}
					else if(x < x1) {
						d0 = x1;
					}
					else {
						flag6 = false;
					}

					if(y > y1) {
						d1 = y1 + 1D;
					}
					else if(y < y1) {
						d1 = y1;
					}
					else {
						flag3 = false;
					}

					if(z > z1) {
						d2 = z1 + 1D;
					}
					else if(z < z1) {
						d2 = z1;
					}
					else {
						flag4 = false;
					}

					double d3 = 999;
					double d4 = 999;
					double d5 = 999;
					final double d6 = dst.xCoord - src.xCoord;
					final double d7 = dst.yCoord - src.yCoord;
					final double d8 = dst.zCoord - src.zCoord;

					if(flag6) {
						d3 = (d0 - src.xCoord) / d6;
					}

					if(flag3) {
						d4 = (d1 - src.yCoord) / d7;
					}

					if(flag4) {
						d5 = (d2 - src.zCoord) / d8;
					}

					final byte b0;

					if(d3 < d4 && d3 < d5) {
						if(x > x1) {
							b0 = 4;
						}
						else {
							b0 = 5;
						}

						src.xCoord = d0;
						src.yCoord += d7 * d3;
						src.zCoord += d8 * d3;
					}
					else if(d4 < d5) {
						if(y > y1) {
							b0 = 0;
						}
						else {
							b0 = 1;
						}

						src.xCoord += d6 * d4;
						src.yCoord = d1;
						src.zCoord += d8 * d4;
					}
					else {
						if(z > z1) {
							b0 = 2;
						}
						else {
							b0 = 3;
						}

						src.xCoord += d6 * d5;
						src.yCoord += d7 * d5;
						src.zCoord = d2;
					}

					final Vec3 vec32 = Vec3.createVectorHelper(src.xCoord, src.yCoord, src.zCoord);
					x1 = (int) (vec32.xCoord = MathHelper.floor_double(src.xCoord));

					if(b0 == 5) {
						--x1;
						++vec32.xCoord;
					}

					y1 = (int) (vec32.yCoord = MathHelper.floor_double(src.yCoord));

					if(b0 == 1) {
						--y1;
						++vec32.yCoord;
					}

					z1 = (int) (vec32.zCoord = MathHelper.floor_double(src.zCoord));

					if(b0 == 3) {
						--z1;
						++vec32.zCoord;
					}

					final Block block1 = world.getBlock(x1, y1, z1);
					final int l1 = world.getBlockMetadata(x1, y1, z1);

					if(!flag1 || block1.getCollisionBoundingBoxFromPool(world, x1, y1, z1) != null) {
						if(block1.canCollideCheck(l1, flag) && !(world.getTileEntity(x1, y1, z1) instanceof IEnergyReceiver && world.getTileEntity(x1, y1, z1) instanceof IEnergyTransmitter)) {
							final MovingObjectPosition movingobjectposition1 = block1.collisionRayTrace(world, x1, y1, z1, src, dst);

							if(movingobjectposition1 != null) {
								return movingobjectposition1;
							}
						}
						else {
							movingobjectposition2 = new MovingObjectPosition(x1, y1, z1, b0, src, false);
						}
					}
				}

				return flag2 ? movingobjectposition2 : null;
			}
			else {
				return null;
			}
		}
		else {
			return null;
		}
	}

	public static ArrayList<ReceiverEntry> getReceiversToPower(final IEnergyTransmitter transmitter) {
		final TransmissionHandler transmissionHandler = transmitter.getTransmissionHandler();
		final ArrayList<ReceiverEntry> tilesToPower = new ArrayList<>();

		for(final ReceiverEntry receiver : transmissionHandler.getReceivers()) {
			if(receiver.canReach() && receiver.getTile() != null) {
				tilesToPower.add(receiver);
			}
		}

		return tilesToPower;
	}

	/**
	 *
	 * @param transmitter
	 * @return A list of every descendant of this transmitter
	 */
	public static ArrayList<ReceiverEntry> getReceiverChain(final IEnergyTransmitter transmitter) {
		final ArrayList<ReceiverEntry> tilesToPower = getReceiversToPower(transmitter);
		final ArrayList<ReceiverEntry> list = new ArrayList<>(tilesToPower);

		for(final ReceiverEntry receiver : tilesToPower) {
			if(receiver.getTile() instanceof IEnergyTransmitter) {
				list.addAll(getReceiverChain(receiver.getTransmitter()));
			}
		}

		return list;
	}

	/**
	 *
	 * @param transmitter
	 * @return A list of receivers which are at the very end of the lineage of this transmitter
	 */
	public static ArrayList<ReceiverEntry> getReceiverDescendants(final IEnergyTransmitter transmitter) {
		final ArrayList<ReceiverEntry> tiles = getReceiverChain(transmitter);
		final ArrayList<ReceiverEntry> list = new ArrayList<>();

		for(final ReceiverEntry receiver : tiles) {
			if(!list.contains(receiver) && (!(receiver.getTile() instanceof IEnergyTransmitter) || getReceiversToPower(receiver.getTransmitter()).isEmpty())) {
				list.add(receiver);
			}
		}

		return list;
	}

	public static boolean isPowering(final IEnergyTransmitter transmitter, final ReceiverEntry entry) {
		return getReceiversToPower(transmitter).contains(entry);
	}

	public static boolean isPowering(final IEnergyTransmitter transmitter, final DimensionalCoords coords) {
		final ArrayList<ReceiverEntry> receivers = getReceiversToPower(transmitter);

		for(final ReceiverEntry receiver : receivers) {
			if(receiver.getCoords().equals(coords)) {
				return true;
			}
		}

		return false;
	}

	public static boolean isPowering(final IEnergyTransmitter transmitter, final TileEntity tile) {
		return isPowering(transmitter, new DimensionalCoords(tile));
	}

	public static void transferEnergy(final IEnergyContainer to, final IEnergyContainer from, final float amount, final boolean simulate) {
		to.receiveEnergy(from.extractEnergy(to.receiveEnergy(amount, true), simulate), simulate);
	}

	public static void applyEnergyUsage(final EnergyStorage storage) {
		final float usage = storage.getEnergyUsage();

		if(usage < 0) {
			storage.remove(-usage, false);
		}
		else if(usage > 0) {
			storage.add(usage, false);
		}
	}

	public static boolean canPowerChainReach(final IEnergyReceiver receiver) {
		final TileEntity tile = receiver.getReceiverHandler().getOwner().getTile();

		if(tile != null) {
			final HashSet<NetworkEntry> transmitters = receiver.getReceiverHandler().getTransmitters();

			for(final NetworkEntry ownerEntry : transmitters) {
				if(ownerEntry.getTile() instanceof IEnergyReceiver) {
					if(isPowering(ownerEntry.getTransmitter(), tile) && canPowerChainReach(ownerEntry.getReceiver())) {
						return true;
					}
				}
				else if(ownerEntry.getTransmitter().getEnergy() > 0 && isPowering(ownerEntry.getTransmitter(), tile)) {
					if(ownerEntry.getTile() instanceof TileEntityMachine) {
						final TileEntityMachine machine = (TileEntityMachine) ownerEntry.getTile();

						if(!machine.canActivate()) {
							continue;
						}
					}

					return true;
				}
			}
		}

		return false;
	}
}
