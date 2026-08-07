package fiskfille.tf.common.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemSmeltedEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.common.achievement.TFAchievements;
import fiskfille.tf.common.data.*;
import fiskfille.tf.common.item.ItemHandler;
import fiskfille.tf.common.item.TFItems;
import fiskfille.tf.common.item.TFSubItems;
import fiskfille.tf.common.network.MessageBroadcastState;
import fiskfille.tf.common.network.MessageSendFlying;
import fiskfille.tf.common.network.base.TFNetworkManager;
import fiskfille.tf.common.recipe.TFRecipes;
import fiskfille.tf.common.transformer.base.Transformer;
import fiskfille.tf.helper.TFHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.StartTracking;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.WorldEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class CommonEventHandler {
	private final ArrayList<EntityPlayer> playersNotSunc = new ArrayList<>();
	private final HashMap<EntityPlayer, Boolean> prevFlying = new HashMap<>();

	@SubscribeEvent
	public void onHit(LivingAttackEvent event) {
		final Entity cause = event.source.getEntity();

		if(cause instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) cause;
			final Transformer transformer = TFHelper.getTransformer(player);

			if(TFHelper.isFullyTransformed(player) && !event.source.isProjectile() && (transformer == null || transformer.canInteractInVehicleMode())) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onSmelt(ItemSmeltedEvent event) {
		if(event.smelting.getItem() == TFItems.transformiumFragment) {
			event.player.addStat(TFAchievements.transformium, 1);
		}
	}

	@SubscribeEvent
	public void onCraft(ItemCraftedEvent event) {
		if(ItemHandler.matches(event.crafting, TFSubItems.tank_track)) {
			event.player.addStat(TFAchievements.tracks, 1);
		}
	}

	@SubscribeEvent
	public void onEntityLoad(EntityEvent.EntityConstructing event) {
		if(event.entity instanceof EntityPlayer) {
			event.entity.registerExtendedProperties(TFPlayerData.IDENTIFIER, new TFPlayerData());
		}

		event.entity.registerExtendedProperties(TFEntityData.IDENTIFIER, new TFEntityData());
	}

	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent event) {
		final EntityPlayer player = event.getPlayer();

		if(TFHelper.isFullyTransformed(player)) {
			final Transformer transformer = TFHelper.getTransformer(player);

			if(transformer == null || transformer.canInteractInVehicleMode()) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void startTracking(StartTracking event) {
		final EntityPlayer player = event.entityPlayer;

		if(player != null && !player.worldObj.isRemote && event.target instanceof EntityPlayer) {
			final EntityPlayer beingTracked = (EntityPlayer) event.target;

			final EntityPlayerMP playerMP = (EntityPlayerMP) player;
			final EntityPlayerMP beingTrackedMP = (EntityPlayerMP) beingTracked;

			TFNetworkManager.networkWrapper.sendTo(new MessageBroadcastState(player), beingTrackedMP);
			TFNetworkManager.networkWrapper.sendTo(new MessageBroadcastState(beingTracked), playerMP);

			TFNetworkManager.networkWrapper.sendTo(new MessageSendFlying(beingTracked, beingTracked.capabilities.isFlying), playerMP);
			TFNetworkManager.networkWrapper.sendTo(new MessageSendFlying(player, player.capabilities.isFlying), beingTrackedMP);
		}
	}

	@SubscribeEvent
	public void onClonePlayer(PlayerEvent.Clone event) {
		TFPlayerData.getData(event.entityPlayer).copy(TFPlayerData.getData(event.original));
	}

	@SubscribeEvent
	public void onEntityInteract(EntityInteractEvent event) {
		final EntityPlayer player = event.entityPlayer;

		if(TFHelper.isFullyTransformed(player)) {
			final Transformer transformer = TFHelper.getTransformer(player);

			if(transformer == null || transformer.canInteractInVehicleMode()) {
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onSpawn(EntityJoinWorldEvent event) {
		final Entity entity = event.entity;

		if(entity instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) entity;

			if(!entity.worldObj.isRemote) {
				playersNotSunc.add(player);
			}
			else {
				TFHelper.isFullyTransformed(player);

//                if (!inVehicleMode && TransformersMod.proxy.getPlayer() == player) // TODO: Should also move to ClientEventHandler
//                {
//                    ClientEventHandler.prevViewBobbing = Minecraft.getMinecraft().gameSettings.viewBobbing;
//                }
			}
		}
	}

	@SubscribeEvent
	public void onLivingJump(LivingEvent.LivingJumpEvent event) {
		if(event.entity instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) event.entity;
			final Transformer transformer = TFHelper.getTransformer(player);

			if(transformer != null && (!transformer.onJump(player) || !transformer.canJumpAsVehicle(player) && TFHelper.getTransformationTimer(player) >= 0.5F)) {
				player.motionY = 0;
			}
		}
	}

	//@SubscribeEvent
	//public void onWorldUnload(WorldEvent.Unload event) {
	//	TFChunkManager.clearCache();
	//}

	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event) {
		final World world = event.world;

		if(!world.isRemote) {
			TFWorldData.load(world);
		}

		ItemHandler.load(world);
	}

	@SubscribeEvent
	public void onItemStitchPost(ItemStitchEvent.Post event) {
		TFRecipes.register();
		TFAchievements.register();
	}

	@SubscribeEvent
	public void onItemHandlerInit(ItemHandlerEvent.Init event) {
		event.registerItemHandler(TransformersMod.MODID, TFSubItems.class);
	}

	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event) {
		TFEntityData.getData(event.entity).onUpdate();

		if(event.entity instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) event.entity;

			if(!player.worldObj.isRemote) {
				if(player.capabilities != null) {
					final Boolean isFlying = prevFlying.get(player);
					final boolean capabilitiesFlying = player.capabilities.isFlying;

					if(isFlying != null) {
						if(isFlying != capabilitiesFlying) {
							TFNetworkManager.networkWrapper.sendToDimension(new MessageSendFlying(player, capabilitiesFlying), player.dimension);

							prevFlying.put(player, capabilitiesFlying);
						}
					}
					else {
						TFNetworkManager.networkWrapper.sendToDimension(new MessageSendFlying(player, capabilitiesFlying), player.dimension);
						prevFlying.put(player, capabilitiesFlying);
					}
				}
			}

			if(!player.worldObj.isRemote) {
				if(!playersNotSunc.isEmpty() && playersNotSunc.contains(player)) {
					TFDataManager.updatePlayerWithServerInfo(player);
					playersNotSunc.remove(player);
				}
			}
		}
	}

	@SubscribeEvent
	public void onLivingFall(LivingFallEvent event) {
		if(event.entity instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) event.entity;
			final Transformer transformer = TFHelper.getTransformer(player);

			if(transformer != null) {
				final float newDist = transformer.fall(player, event.distance, TFData.ALT_MODE.get(player));

				if(newDist <= 0) {
					event.setCanceled(true);
				}
				else {
					event.distance = newDist;
				}
			}
		}
	}

	@SubscribeEvent
	public void onTick(TickEvent event) {
		switch(event.type) {
			case CLIENT:
			case SERVER:
				TransformersMod.proxy.runTasks();
				break;
		}
	}
}
