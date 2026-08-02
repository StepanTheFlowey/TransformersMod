package fiskfille.tf.common.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fiskfille.tf.common.block.TFBlocks;
import fiskfille.tf.common.entity.EntityLaser;
import fiskfille.tf.common.item.ItemVurpsSniper;
import fiskfille.tf.common.transformer.TransformerVurp;
import fiskfille.tf.common.transformer.base.Transformer;
import fiskfille.tf.helper.TFHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class MessageLaserShoot implements IMessage {
	public int id;
	public boolean consume;

	public MessageLaserShoot() {}

	public MessageLaserShoot(EntityPlayer player, boolean consumeItems) {
		id = player.getEntityId();
		consume = consumeItems;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		id = buf.readInt();
		consume = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(id);
		buf.writeBoolean(consume);
	}

	public static class Handler implements IMessageHandler<MessageLaserShoot, IMessage> {
		@Override
		public IMessage onMessage(MessageLaserShoot message, MessageContext ctx) {
			if(!ctx.side.isClient()) {
				EntityPlayer from = null;

				for(World world : MinecraftServer.getServer().worldServers) {
					final Entity entity = world.getEntityByID(message.id);

					if(entity instanceof EntityPlayer) {
						from = (EntityPlayer) entity;
						break;
					}
				}

				if(from != null) {
					final Transformer transformer = TFHelper.getTransformer(from);
					final ItemStack heldItem = from.getHeldItem();
					final boolean hasSniper = heldItem != null && heldItem.getItem() instanceof ItemVurpsSniper && TFHelper.getTransformationTimer(from) == 0;

					if(transformer instanceof TransformerVurp && (hasSniper || transformer.canShoot(from))) {
						final Item shootItem = Item.getItemFromBlock(TFBlocks.energonCube);
						final boolean isCreative = from.capabilities.isCreativeMode;
						final boolean consumeItems = !isCreative || from.inventory.hasItem(shootItem) && message.consume;

						if(!message.consume) {
							final World world = from.worldObj;
							world.spawnEntityInWorld(new EntityLaser(world, from));
						}
						else if(consumeItems && !isCreative) {
							from.inventory.consumeInventoryItem(shootItem);
						}
					}
				}
			}

			return null;
		}
	}
}
