package fiskfille.tf.common.network;

import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.common.achievement.TFAchievements;
import fiskfille.tf.common.network.base.TFNetworkManager;
import fiskfille.tf.common.transformer.base.Transformer;
import fiskfille.tf.helper.TFHelper;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class MessageVehicleShoot implements IMessage {
	public int id;

	public MessageVehicleShoot() {}

	public MessageVehicleShoot(final EntityPlayer player) {
		id = player.getEntityId();
	}

	@Override
	public void fromBytes(final ByteBuf buf) {
		id = buf.readInt();
	}

	@Override
	public void toBytes(final ByteBuf buf) {
		buf.writeInt(id);
	}

	public static class Handler implements IMessageHandler<MessageVehicleShoot, IMessage> {
		@Override
		public IMessage onMessage(final MessageVehicleShoot message, final MessageContext ctx) {
			if(ctx.side.isClient()) {
				final Entity fromEntity = TransformersMod.proxy.getPlayer().worldObj.getEntityByID(message.id);

				if(fromEntity instanceof EntityPlayer) {
					final EntityPlayer from = (EntityPlayer) fromEntity;
					final Transformer transformer = TFHelper.getTransformer(from);

					if(transformer != null) {
						final String shootSound = transformer.getShootSound();

						if(shootSound != null) {
							from.worldObj.playSound(from.posX, from.posY - from.yOffset, from.posZ, shootSound, transformer.getShootVolume(), 1, false);
						}
					}
				}
			}
			else {
				EntityPlayer from = null;

				for(final World world : MinecraftServer.getServer().worldServers) {
					final Entity entity = world.getEntityByID(message.id);
					if(entity instanceof EntityPlayer) {
						from = (EntityPlayer) entity;
						break;
					}
				}

				if(from != null) {
					final Transformer transformer = TFHelper.getTransformer(from);

					if(transformer != null) {
						if(transformer.canShoot(from) && TFHelper.isFullyTransformed(from)) {
							final Item shootItem = transformer.getShootItem();
							final boolean isCreative = from.capabilities.isCreativeMode;
							final boolean hasAmmo = isCreative || from.inventory.hasItem(shootItem);

							if(hasAmmo) {
								if(transformer.getShootSound() != null) {
									TFNetworkManager.networkWrapper.sendToAllAround(new MessageVehicleShoot(from), new TargetPoint(from.dimension, from.posX, from.posY, from.posZ, 32));
								}

								from.worldObj.spawnEntityInWorld(transformer.getShootEntity(from));

								if(!isCreative) {
									from.inventory.consumeInventoryItem(shootItem);
								}

								from.addStat(TFAchievements.firstMissile, 1);
							}
						}
					}
				}
			}

			return null;
		}
	}
}
