package fiskfille.tf.common.chunk;

import com.google.common.collect.Lists;
import fiskfille.tf.common.tileentity.IChunkLoaderTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

import java.util.List;

public class TFLoadingCallback implements ForgeChunkManager.OrderedLoadingCallback {
	@Override
	public void ticketsLoaded(final List<Ticket> tickets, final World world) {
		for(final Ticket ticket : tickets) {
			for(final SubTicket subTicket : SubTicket.getChildren(ticket)) {
				final TileEntity tile = world.getTileEntity(subTicket.xCoord, subTicket.yCoord, subTicket.zCoord);

				if(tile instanceof IChunkLoaderTile) {
					((IChunkLoaderTile) tile).forceChunks(subTicket);
				}
			}
		}
	}

	@Override
	public List<Ticket> ticketsLoaded(final List<Ticket> tickets, final World world, final int maxTicketCount) {
		final List<Ticket> validTickets = Lists.newArrayList();

		for(final Ticket ticket : tickets) {
			for(final SubTicket subTicket : SubTicket.getChildren(ticket)) {
				if(world.getTileEntity(subTicket.xCoord, subTicket.yCoord, subTicket.zCoord) instanceof IChunkLoaderTile) {
					validTickets.add(ticket);
					break;
				}
				else {
					subTicket.remove();
				}
			}
		}

		return validTickets;
	}
}
