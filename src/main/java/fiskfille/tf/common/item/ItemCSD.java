package fiskfille.tf.common.item;

import fiskfille.tf.common.tileentity.TileEntityControlPanel;
import fiskfille.tf.helper.TFDimensionHelper;
import fiskfille.tf.helper.TFTileHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

import java.util.List;

public class ItemCSD extends Item {
	public ItemCSD() {
		setMaxStackSize(1);
	}

	public static DimensionalCoords getCoords(final ItemStack itemstack) {
		final DimensionalCoords coords = new DimensionalCoords();

		if(itemstack.hasTagCompound() && itemstack.getTagCompound().hasKey("Coordinates", NBT.TAG_COMPOUND)) {
			final NBTTagCompound nbttagcompound = itemstack.getTagCompound().getCompoundTag("Coordinates");
			coords.set(nbttagcompound.getInteger("x"), nbttagcompound.getInteger("y"), nbttagcompound.getInteger("z"), nbttagcompound.getInteger("dim"));
		}

		return coords;
	}

	public static void setCoords(final ItemStack itemstack, final DimensionalCoords coords) {
		if(!itemstack.hasTagCompound()) {
			itemstack.setTagCompound(new NBTTagCompound());
		}

		final NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setInteger("x", coords.posX);
		nbttagcompound.setInteger("y", coords.posY);
		nbttagcompound.setInteger("z", coords.posZ);
		nbttagcompound.setInteger("dim", coords.dimension);

		itemstack.getTagCompound().setTag("Coordinates", nbttagcompound);
	}

	@Override
	public void addInformation(final ItemStack itemstack, final EntityPlayer player, final List list, final boolean flag) {
		final DimensionalCoords coords = getCoords(itemstack);
		list.add(coords.getFormatted().getFormattedText());
	}

	@Override
	public boolean onItemUse(final ItemStack itemstack, final EntityPlayer player, final World world, final int x, final int y, final int z, final int side, final float hitX, final float hitY, final float hitZ) {
		world.getBlockMetadata(x, y, z);

		if(player.isSneaking()) {
			final DimensionalCoords coords = new DimensionalCoords(x, y + 1, z, world.provider.dimensionId);
			final TileEntity tile = TFTileHelper.getTileBase(world.getTileEntity(x, y, z));

			if(tile instanceof TileEntityControlPanel) {
				coords.set(((TileEntityControlPanel) tile).data.destination);
			}

			setCoords(itemstack, coords);

			if(world.isRemote) {
				player.addChatComponentMessage(new ChatComponentTranslation("csd.save.success", coords.getFormatted()));
			}

			return true;
		}
		else {
			if(world.getTileEntity(x, y, z) instanceof TileEntityControlPanel) {
				final TileEntityControlPanel tile = TFTileHelper.getTileBase((TileEntityControlPanel) world.getTileEntity(x, y, z));

				if(!tile.data.activationLeverState) {
					final DimensionalCoords coords = getCoords(itemstack);
					tile.setSwitchesTo(coords);
					tile.markBlockForUpdate();

					if(world.isRemote) {
						player.addChatComponentMessage(new ChatComponentTranslation("csd.load.success", coords.getFormatted()));
					}

					return true;
				}
				else {
					if(world.isRemote) {
						player.addChatComponentMessage(new ChatComponentTranslation("csd.load.fail").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
					}
				}
			}
		}

		return false;
	}

	public static class DimensionalCoords extends ChunkCoordinates {
		public int dimension = 0;

		public DimensionalCoords() {}

		public DimensionalCoords(final int x, final int y, final int z, final int dim) {
			super(x, y, z);
			dimension = dim;
		}

		public DimensionalCoords(final ChunkCoordinates coords, final int dim) {
			super(coords);
			dimension = dim;
		}

		public DimensionalCoords(final DimensionalCoords coords) {
			set(coords);
		}

		public DimensionalCoords(final TileEntity tile) {
			set(tile);
		}

		public static DimensionalCoords fromArray(final int[] aint) {
			final int[] aint1 = new int[4];

			System.arraycopy(aint, 0, aint1, 0, Math.min(aint.length, aint1.length));

			return new DimensionalCoords(aint1[0], aint1[1], aint1[2], aint1[3]);
		}

		public void set(final int x, final int y, final int z, final int dim) {
			posX = x;
			posY = y;
			posZ = z;
			dimension = dim;
		}

		public void set(final DimensionalCoords coords) {
			posX = coords.posX;
			posY = coords.posY;
			posZ = coords.posZ;
			dimension = coords.dimension;
		}

		public void set(final TileEntity tile) {
			posX = tile.xCoord;
			posY = tile.yCoord;
			posZ = tile.zCoord;
			if(tile.hasWorldObj()) {
				dimension = tile.getWorldObj().provider.dimensionId;
			}
		}

		public void set(final int... args) {
			final int[] aint = toArray();

			System.arraycopy(args, 0, aint, 0, Math.min(args.length, aint.length));

			set(aint[0], aint[1], aint[2], aint[3]);
		}

		public IChatComponent getFormatted() {
			final ChatStyle green = new ChatStyle().setColor(EnumChatFormatting.GREEN);
			return new ChatComponentTranslation("csd.format", new ChatComponentText(TFDimensionHelper.getDimensionName(dimension)).setChatStyle(green), new ChatComponentText(posX + "").setChatStyle(green), new ChatComponentText(posY + "").setChatStyle(green), new ChatComponentText(posZ + "").setChatStyle(green));
		}

		public void toBytes(final ByteBuf buf) {
			buf.writeInt(posX);
			buf.writeInt(posY);
			buf.writeInt(posZ);
			buf.writeInt(dimension);
		}

		public DimensionalCoords fromBytes(final ByteBuf buf) {
			set(buf.readInt(), buf.readInt(), buf.readInt(), buf.readInt());

			return this;
		}

		public int[] toArray() {
			return new int[]{posX, posY, posZ, dimension};
		}

		@Override
		public boolean equals(final Object object) {
			if(object instanceof DimensionalCoords) {
				final DimensionalCoords dimensionalCoords = (DimensionalCoords) object;
				return posX == dimensionalCoords.posX && posY == dimensionalCoords.posY && posZ == dimensionalCoords.posZ && dimension == dimensionalCoords.dimension;
			}

			return false;
		}

		@Override
		public int hashCode() {
			return posX + posZ << 8 + posY << 16 + dimension << 32;
		}

		@Override
		public String toString() {
			return "Pos{x=" + posX + ", y=" + posY + ", z=" + posZ + ", dim=" + dimension + '}';
		}

		@Override
		public int compareTo(final Object obj) {
			return compareTo((DimensionalCoords) obj);
		}

		public int compareTo(final DimensionalCoords coords) {
			return dimension == coords.dimension ? posY == coords.posY ? posZ == coords.posZ ? posX - coords.posX : posZ - coords.posZ : posY - coords.posY : dimension - coords.dimension;
		}
	}
}
