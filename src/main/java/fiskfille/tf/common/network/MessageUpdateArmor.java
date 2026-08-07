package fiskfille.tf.common.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fiskfille.tf.TransformersMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class MessageUpdateArmor implements IMessage {
	public int id;
	private ItemStack armor;
	private int armorSlot;

	public MessageUpdateArmor() {}

	public MessageUpdateArmor(final EntityPlayer player, final ItemStack itemstack, final int slot) {
		id = player.getEntityId();
		armor = itemstack;
		armorSlot = slot;
	}

	@Override
	public void fromBytes(final ByteBuf buf) {
		id = buf.readInt();
		armor = ByteBufUtils.readItemStack(buf);
		armorSlot = buf.readInt();
	}

	@Override
	public void toBytes(final ByteBuf buf) {
		buf.writeInt(id);
		ByteBufUtils.writeItemStack(buf, armor);
		buf.writeInt(armorSlot);
	}

	public static class Handler implements IMessageHandler<MessageUpdateArmor, IMessage> {
		@Override
		public IMessage onMessage(final MessageUpdateArmor message, final MessageContext ctx) {
			if(ctx.side.isClient()) {
				final Entity entity = TransformersMod.proxy.getPlayer().worldObj.getEntityByID(message.id);

				if(entity instanceof EntityPlayer) {
					((EntityPlayer) entity).inventory.armorInventory[message.armorSlot] = message.armor;
				}
			}

			return null;
		}
	}
}
