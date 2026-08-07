package fiskfille.tf.common.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.audio.MovingSoundTransformer;
import fiskfille.tf.common.data.TFData;
import fiskfille.tf.common.transformer.base.Transformer;
import fiskfille.tf.helper.TFHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class MessagePlayerData implements IMessage {
	public int id;
	private TFData<?> type;
	private Object value;

	public MessagePlayerData() {}

	public MessagePlayerData(EntityPlayer player, TFData<?> data, Object obj) {
		id = player.getEntityId();
		type = data;
		value = obj;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		id = buf.readInt();
		String id = ByteBufUtils.readUTF8String(buf);

		for(TFData<?> data : TFData.VALUES) {
			if(data.id.equals(id)) {
				type = data;
				break;
			}
		}

		value = type.readDataFromNBT(ByteBufUtils.readTag(buf));
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(id);
		ByteBufUtils.writeUTF8String(buf, type.id);
		ByteBufUtils.writeTag(buf, type.writeDataToNBT(new NBTTagCompound(), value));
	}

	public static class Handler implements IMessageHandler<MessagePlayerData, IMessage> {
		@Override
		public IMessage onMessage(MessagePlayerData message, MessageContext ctx) {
			if(ctx.side.isClient()) {
				return client(message, ctx);
			}

			return server(message, ctx);
		}

		@SideOnly(Side.CLIENT)
		private IMessage client(MessagePlayerData message, MessageContext ctx) {
			final Entity entity = TransformersMod.proxy.getPlayer().worldObj.getEntityByID(message.id);

			if(entity instanceof EntityPlayer) {
				final TFData type = message.type;
				final Object value = message.value;

				type.setWithoutNotify((EntityPlayer) entity, value);

				if(type == TFData.ALT_MODE) {
					final Transformer transformer = TFHelper.getTransformer((EntityPlayer) entity);

					if(transformer != null) {
						Minecraft.getMinecraft().getSoundHandler().playSound(new MovingSoundTransformer(entity, transformer.getTransformationSound((Integer) value)));
					}
				}
				else if(type == TFData.STEALTH_FORCE) {
					Minecraft.getMinecraft().getSoundHandler().playSound(new MovingSoundTransformer(entity, new ResourceLocation(TransformersMod.MODID, "transform_stealth" + ((Boolean) value ? "" : "_in"))));
				}
			}

			return null;
		}

		private IMessage server(MessagePlayerData message, MessageContext ctx) {
			final EntityPlayer player = ctx.getServerHandler().playerEntity;

			if(player != null) {
				final Entity entity = player.worldObj.getEntityByID(message.id);

				if(entity instanceof EntityPlayer) {
					((TFData) message.type).set((EntityPlayer) entity, message.value);
				}
			}

			return null;
		}
	}
}
