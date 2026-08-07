package fiskfille.tf.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.common.tileentity.TileEntityGroundBridgeTeleporter;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;

public class MessageClosePortal implements IMessage {
	private int x;
	private int y;
	private int z;

	public MessageClosePortal() {}

	public MessageClosePortal(final int x, final int y, final int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void fromBytes(final ByteBuf buf) {
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
	}

	@Override
	public void toBytes(final ByteBuf buf) {
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
	}

	public static class Handler implements IMessageHandler<MessageClosePortal, IMessage> {
		@Override
		public IMessage onMessage(final MessageClosePortal message, final MessageContext ctx) {
			if(ctx.side.isClient()) {
				final TileEntity tile = TransformersMod.proxy.getPlayer().worldObj.getTileEntity(message.x, message.y, message.z);

				if(tile instanceof TileEntityGroundBridgeTeleporter) {
					((TileEntityGroundBridgeTeleporter) tile).clientClosing = true;
				}
			}

			return null;
		}
	}
}
