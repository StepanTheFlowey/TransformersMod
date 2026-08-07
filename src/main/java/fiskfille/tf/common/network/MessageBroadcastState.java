package fiskfille.tf.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.common.data.TFData;
import fiskfille.tf.common.network.base.TFNetworkManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Map.Entry;

public class MessageBroadcastState extends MessageSyncBase {
	private int id;

	public MessageBroadcastState() {}

	public MessageBroadcastState(final EntityPlayer player) {
		super(player);
		id = player.getEntityId();
	}

	@Override
	public void fromBytes(final ByteBuf buf) {
		super.fromBytes(buf);
		id = buf.readInt();
	}

	@Override
	public void toBytes(final ByteBuf buf) {
		super.toBytes(buf);
		buf.writeInt(id);
	}

	public static class Handler implements IMessageHandler<MessageBroadcastState, IMessage> {
		@Override
		public IMessage onMessage(final MessageBroadcastState message, final MessageContext ctx) {
			if(ctx.side.isClient()) {
				final EntityPlayer player = TransformersMod.proxy.getPlayer();
				final Entity lookupEntity = player.worldObj.getEntityByID(message.id);

				if(lookupEntity instanceof EntityPlayer && player != lookupEntity) {
					final EntityPlayer lookupPlayer = (EntityPlayer) lookupEntity;

					for(final Entry<TFData, Object> e : message.playerData.entrySet()) {
						e.getKey().setWithoutNotify(lookupPlayer, e.getValue());
					}
				}
			}
			else {
				final EntityPlayer player = ctx.getServerHandler().playerEntity;

				for(final Entry<TFData, Object> e : message.playerData.entrySet()) {
					e.getKey().setWithoutNotify(player, e.getValue());
				}

				TFNetworkManager.networkWrapper.sendToDimension(new MessageBroadcastState(player), player.dimension);
			}

			return null;
		}
	}
}
