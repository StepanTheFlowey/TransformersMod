package fiskfille.tf.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fiskfille.tf.TransformersMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class MessageOpenGui implements IMessage {
	public int id;
	private int modGuiId;
	private int x;
	private int y;
	private int z;

	public MessageOpenGui() {}

	public MessageOpenGui(final EntityPlayer player, final int modGuiId, final int x, final int y, final int z) {
		this.id = player.getEntityId();
		this.modGuiId = modGuiId;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void fromBytes(final ByteBuf buf) {
		id = buf.readInt();
		modGuiId = buf.readInt();
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
	}

	@Override
	public void toBytes(final ByteBuf buf) {
		buf.writeInt(id);
		buf.writeInt(modGuiId);
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
	}

	public static class Handler implements IMessageHandler<MessageOpenGui, IMessage> {
		@Override
		public IMessage onMessage(final MessageOpenGui message, final MessageContext ctx) {
			if(ctx.side.isClient()) {
				final EntityPlayer player = TransformersMod.proxy.getPlayer();
				final Entity entity = player.worldObj.getEntityByID(message.id);

				if(entity instanceof EntityPlayer) {
					((EntityPlayer) entity).openGui(TransformersMod.instance, message.modGuiId, entity.worldObj, message.x, message.y, message.z);
				}
			}
			else {
				final EntityPlayer player = ctx.getServerHandler().playerEntity;

				if(player != null) {
					final Entity entity = player.worldObj.getEntityByID(message.id);

					if(entity instanceof EntityPlayer) {
						((EntityPlayer) entity).openGui(TransformersMod.instance, message.modGuiId, entity.worldObj, message.x, message.y, message.z);
					}
				}
			}

			return null;
		}
	}
}
