package fiskfille.tf.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fiskfille.tf.common.energon.power.IEnergyReceiver;
import fiskfille.tf.common.energon.power.IEnergyTransmitter;
import fiskfille.tf.common.energon.power.ReceiverEntry;
import fiskfille.tf.common.energon.power.TransmissionHandler;
import fiskfille.tf.common.item.ItemCSD.DimensionalCoords;
import io.netty.buffer.ByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class MessageConnectReceiver implements IMessage {
	private DimensionalCoords transmitterCoords;
	private DimensionalCoords receiverCoords;

	public MessageConnectReceiver() {}

	public MessageConnectReceiver(final DimensionalCoords transmitterCoords, final DimensionalCoords receiverCoords) {
		this.transmitterCoords = transmitterCoords;
		this.receiverCoords = receiverCoords;
	}

	@Override
	public void fromBytes(final ByteBuf buf) {
		transmitterCoords = new DimensionalCoords().fromBytes(buf);
		receiverCoords = new DimensionalCoords().fromBytes(buf);
	}

	@Override
	public void toBytes(final ByteBuf buf) {
		transmitterCoords.toBytes(buf);
		receiverCoords.toBytes(buf);
	}

	public static class Handler implements IMessageHandler<MessageConnectReceiver, IMessage> {
		@Override
		public IMessage onMessage(final MessageConnectReceiver message, final MessageContext ctx) {
			if(ctx.side.isServer()) {
				final World world = MinecraftServer.getServer().worldServerForDimension(message.transmitterCoords.dimension);

				if(world != null) {
					final DimensionalCoords transmitterCoords = message.transmitterCoords;
					final TileEntity transmitterTile = world.getTileEntity(transmitterCoords.posX, transmitterCoords.posY, transmitterCoords.posZ);

					final DimensionalCoords receiverCoords = message.receiverCoords;
					final TileEntity receiverTile = world.getTileEntity(receiverCoords.posX, receiverCoords.posY, receiverCoords.posZ);

					if(transmitterTile instanceof IEnergyTransmitter && receiverTile instanceof IEnergyReceiver) {
						final TransmissionHandler handler = ((IEnergyTransmitter) transmitterTile).getTransmissionHandler();

						if(handler.getReceiver(receiverCoords) != null) {
							handler.remove(new ReceiverEntry(receiverTile));
						}
						else {
							handler.add(new ReceiverEntry(receiverTile));
						}
					}
				}
			}

			return null;
		}
	}
}
