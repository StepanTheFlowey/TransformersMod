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
	public void ticketsLoaded(List<Ticket> tickets, World world) {
		for(Ticket ticket : tickets) {
			List<SubTicket> subTickets = SubTicket.getChildren(ticket);

			for(SubTicket subTicket : subTickets) {
				TileEntity tile = world.getTileEntity(subTicket.xCoord, subTicket.yCoord, subTicket.zCoord);

				if(tile instanceof IChunkLoaderTile) {
					((IChunkLoaderTile) tile).forceChunks(subTicket);
				}
			}
		}
	}

	@Override
	public List<Ticket> ticketsLoaded(List<Ticket> tickets, World world, int maxTicketCount) {
		List<Ticket> validTickets = Lists.newArrayList();

		for(Ticket ticket : tickets) {
			List<SubTicket> subTickets = SubTicket.getChildren(ticket);

			for(SubTicket subTicket : subTickets) {
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
