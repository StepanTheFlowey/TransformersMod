package fiskfille.tf.common.chunk;

import fiskfille.tf.TransformersMod;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TFChunkManager {
	private static final HashMap<World, LinkedList<Ticket>> ticketsForWorld = new HashMap<>();
	private static final HashMap<ForcedChunk, Integer> chunkForcers = new HashMap<>();

	public static void forceChunk(final Ticket ticket, final ForcedChunk chunk) {
		int i = chunkForcers.get(chunk) == null ? 0 : chunkForcers.get(chunk);

		if(i == 0) {
			ForgeChunkManager.forceChunk(ticket, chunk);
		}

		chunkForcers.put(chunk, ++i);
	}

	public static void releaseChunk(final SubTicket subTicket, final ForcedChunk chunk) {
		int i = chunkForcers.get(chunk) == null ? 0 : chunkForcers.get(chunk);
		subTicket.remove();

		if(i == 1) {
			ForgeChunkManager.unforceChunk(subTicket.owner, chunk);
		}

		chunkForcers.put(chunk, Math.max(--i, 0));

		for(final Map.Entry<World, LinkedList<Ticket>> e : ticketsForWorld.entrySet()) {
			for(int j = 0; j < ticketsForWorld.get(e.getKey()).size(); ++j) {
				final Ticket ticket = ticketsForWorld.get(e.getKey()).get(j);

				if(ticket.getChunkList().isEmpty()) {
					try {
						ForgeChunkManager.releaseTicket(ticket);
					}
					catch(final Exception ignored) {
					}

					ticketsForWorld.get(e.getKey()).remove(j);
				}
			}
		}
	}

	public static void clearCache() {
		ticketsForWorld.clear();
		chunkForcers.clear();
	}

	private static void debug() {
		int entries = 0;
		int chunks = 0;
		int tickets = 0;

		for(final Map.Entry<World, LinkedList<Ticket>> e : ticketsForWorld.entrySet()) {
			if(e.getValue() != null) {
				for(int i = 0; i < e.getValue().size(); ++i) {
					final Ticket ticket = e.getValue().get(i);
					chunks += ticket.getChunkList().size();

					for(int j = 0; j < ticket.getChunkList().size(); ++j) {
						final ChunkCoordIntPair coords = ticket.getChunkList().asList().get(j);
						final Integer k = chunkForcers.get(new ForcedChunk(ticket.world, coords.chunkXPos, coords.chunkZPos));

						entries += k != null ? k : 0;
					}
				}

				tickets += e.getValue().size();
			}
		}

		TransformersMod.log.info("{} entries / {} chunks / {} tickets", entries, chunks, tickets);
	}

	public static Ticket getTicketForChunk(final ForcedChunk chunk) {
		final World world = chunk.worldObj;

		ticketsForWorld.computeIfAbsent(world, k -> new LinkedList<>());

		for(int i = 0; i < ticketsForWorld.get(world).size(); ++i) {
			final Ticket ticket = ticketsForWorld.get(world).get(i);
			final List<SubTicket> list = SubTicket.getChildren(ticket);

			for(final SubTicket subTicket : list) {
				if(subTicket.xCoord << 4 == chunk.chunkXPos && subTicket.zCoord << 4 == chunk.chunkZPos) {
					return ticket;
				}
			}
		}

		return requestTicket(world);
	}

	public static Ticket requestTicket(final World world) {
		ticketsForWorld.computeIfAbsent(world, k -> new LinkedList<>());
		return getNextAvailableTicket(world);
	}

	private static void newTicket(final World world) {
		final LinkedList<Ticket> list = ticketsForWorld.get(world);
		final Ticket ticket = ForgeChunkManager.requestTicket(TransformersMod.instance, world, Type.NORMAL);

		if(ticket != null) {
			list.add(ticket);
			ticketsForWorld.put(world, list);
		}
	}

	private static LinkedList<Ticket> getAvailableTickets(final World world) {
		final LinkedList<Ticket> list = new LinkedList<>();

		for(int i = 0; i < ticketsForWorld.get(world).size(); ++i) {
			final Ticket ticket = ticketsForWorld.get(world).get(i);

			if(ticket.getChunkList().size() < ticket.getMaxChunkListDepth()) {
				list.add(ticket);
			}
		}

		if(list.isEmpty()) {
			newTicket(world);
		}

		return list;
	}

	private static Ticket getNextAvailableTicket(final World world) {
		return getAvailableTickets(world).isEmpty() ? null : getAvailableTickets(world).getLast();
	}
}
