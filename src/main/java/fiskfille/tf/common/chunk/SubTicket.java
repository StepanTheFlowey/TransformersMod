package fiskfille.tf.common.chunk;

import com.google.common.collect.Lists;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.util.Constants.NBT;

import java.util.List;

public class SubTicket {
	public final int xCoord;
	public final int yCoord;
	public final int zCoord;
	public Ticket owner;
	private NBTTagCompound nbtTag;

	public SubTicket(final int x, final int y, final int z) {
		xCoord = x;
		yCoord = y;
		zCoord = z;
	}

	public static SubTicket readFromNBT(final NBTTagCompound nbttagcompound) {
		final SubTicket subTicket = new SubTicket(nbttagcompound.getInteger("xCoord"), nbttagcompound.getInteger("yCoord"), nbttagcompound.getInteger("zCoord"));
		subTicket.nbtTag = nbttagcompound.getCompoundTag("Tag");
		return subTicket;
	}

	public static List<SubTicket> getChildren(final Ticket ticket) {
		final List<SubTicket> list = Lists.newArrayList();
		final NBTTagList nbttaglist = ticket.getModData().getTagList("SubTickets", NBT.TAG_COMPOUND);

		for(int i = 0; i < nbttaglist.tagCount(); ++i) {
			final SubTicket subTicket = readFromNBT(nbttaglist.getCompoundTagAt(i));
			subTicket.owner = ticket;
			list.add(subTicket);
		}

		return list;
	}

	public static SubTicket get(final Ticket ticket, final TileEntity tile) {
		final List<SubTicket> list = getChildren(ticket);

		for(final SubTicket subTicket : list) {
			if(subTicket.xCoord == tile.xCoord && subTicket.yCoord == tile.yCoord && subTicket.zCoord == tile.zCoord) {
				return subTicket;
			}
		}

		return null;
	}

	public static SubTicket fromTile(final TileEntity tile) {
		return fromTile(null, tile);
	}

	public static SubTicket fromTile(final Ticket ticket, final TileEntity tile) {
		final SubTicket subTicket = new SubTicket(tile.xCoord, tile.yCoord, tile.zCoord);
		subTicket.owner = ticket;
		return subTicket;
	}

	public boolean matches(final Object obj) {
		if(this == obj) {
			return true;
		}
		else if(!(obj instanceof SubTicket)) {
			return false;
		}
		else {
			final SubTicket subTicket = (SubTicket) obj;
			return xCoord == subTicket.xCoord && yCoord == subTicket.yCoord && zCoord == subTicket.zCoord;
		}
	}

	public NBTTagCompound getTag() {
		if(nbtTag == null) {
			nbtTag = new NBTTagCompound();
		}

		return nbtTag;
	}

	public SubTicket assign(final Ticket ticket) {
		final NBTTagList nbttaglist = ticket.getModData().getTagList("SubTickets", NBT.TAG_COMPOUND);
		nbttaglist.appendTag(writeToNBT());

		ticket.getModData().setTag("SubTickets", nbttaglist);
		owner = ticket;

		return this;
	}

	public void remove() {
		final List<SubTicket> list = SubTicket.getChildren(owner);
		final List<SubTicket> list1 = Lists.newArrayList();

		for(final SubTicket subTicket : list) {
			if(!matches(subTicket)) {
				list1.add(subTicket);
			}
		}

		owner.getModData().setTag("SubTickets", new NBTTagList());

		for(final SubTicket ignored : list1) {
			assign(owner);
		}
	}

	public NBTTagCompound writeToNBT() {
		final NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setInteger("xCoord", xCoord);
		nbttagcompound.setInteger("yCoord", yCoord);
		nbttagcompound.setInteger("zCoord", zCoord);
		nbttagcompound.setTag("Tag", getTag());
		return nbttagcompound;
	}
}
