package fiskfille.tf.common.tick;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import fiskfille.tf.TFReflection;
import fiskfille.tf.client.keybinds.TFKeyBinds;
import fiskfille.tf.client.render.entity.EntityRendererTF;
import fiskfille.tf.common.data.TFData;
import fiskfille.tf.common.data.tile.TileData;
import fiskfille.tf.common.item.ItemCSD.DimensionalCoords;
import fiskfille.tf.common.proxy.ClientProxy;
import fiskfille.tf.common.transformer.base.Transformer;
import fiskfille.tf.config.TFConfig;
import fiskfille.tf.helper.TFHelper;
import fiskfille.tf.helper.TFRenderHelper;
import fiskfille.tf.helper.TFTileHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.StatFileWriter;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;

public class ClientTickHandler {
	public static float renderTick;
	private EntityRenderer renderer, prevRenderer;

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		EntityPlayer player = event.player;
		Transformer transformer = TFHelper.getTransformer(player);

		int altMode = TFData.ALT_MODE.get(player);
		float transformationTimer = TFHelper.getTransformationTimer(player);
		TFHelper.getStealthModeTimer(player);

		if(event.phase == TickEvent.Phase.END) {
			if(transformer != null) {
				if(transformationTimer >= 0.5F) {
					transformer.updateMovement(player);

					if(TFData.BOOSTING.get(player) && TFData.NITRO.get(player) > 0) {
						if(TFHelper.isFullyTransformed(player)) {
							transformer.doNitroParticles(player);
						}
					}
				}

				if(player == Minecraft.getMinecraft().thePlayer) {
					if(transformer.overrideFirstPerson()) {
						GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;

						if(transformationTimer >= 0.5F) {
							if(TFKeyBinds.keyBindingViewFront.getIsKeyPressed() && Minecraft.getMinecraft().currentScreen == null) {
								gameSettings.thirdPersonView = 2;
							}
							else if(TFKeyBinds.keyBindingVehicleFirstPerson.getIsKeyPressed()) {
								gameSettings.thirdPersonView = 0;
							}
							else {
								gameSettings.thirdPersonView = 1;
							}
						}

						boolean useNitro = false;

						if(transformationTimer >= 0.5F && transformer.canUseNitro(player)) {
							useNitro = gameSettings.keyBindForward.getIsKeyPressed() && (gameSettings.keyBindSprint.getIsKeyPressed());
						}

						TFData.BOOSTING.set(player, useNitro);

						if(TFData.TRANSFORM_PROGRESS.get(player) == 0 && TFData.PREV_TRANSFORM_PROGRESS.get(player) > 0) {
							if(TFConfig.firstPersonAfterTransformation) {
								gameSettings.thirdPersonView = 0;
							}
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
		if(event.phase == TickEvent.Phase.START) {
			if(Minecraft.getMinecraft().theWorld != null) {
				for(EntityPlayer player : (List<EntityPlayer>) Minecraft.getMinecraft().theWorld.playerEntities) {
					TFRenderHelper.updateMotionY(player);
				}

				if(Minecraft.getMinecraft().theWorld.isRemote && !Minecraft.getMinecraft().isGamePaused()) {
					for(Map.Entry<DimensionalCoords, TileData> e : TFTileHelper.getTileData().entrySet()) {
						e.getValue().clientTick();
					}
				}

				if(ClientProxy.fakePlayer == null || ClientProxy.fakePlayer.worldObj != Minecraft.getMinecraft().theWorld) {
					if(Minecraft.getMinecraft().playerController != null) {
						ClientProxy.fakePlayer = Minecraft.getMinecraft().playerController.func_147493_a(Minecraft.getMinecraft().theWorld, new StatFileWriter());
						ClientProxy.fakePlayer.movementInput = new MovementInputFromOptions(Minecraft.getMinecraft().gameSettings);
					}
				}
				else {
					ClientProxy.fakePlayer.ticksExisted += 1;
				}
			}
		}
	}

	@SubscribeEvent
	public void onRenderTick(RenderTickEvent event) {
		World world = Minecraft.getMinecraft().theWorld;
		renderTick = event.renderTickTime;

		if(world != null) {
			if(event.phase == TickEvent.Phase.START) {
				EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;

				if(TFRenderHelper.shouldOverrideView(player)) {
					if(renderer == null) {
						renderer = new EntityRendererTF(Minecraft.getMinecraft());
					}

					if(Minecraft.getMinecraft().entityRenderer != renderer) {
						prevRenderer = Minecraft.getMinecraft().entityRenderer;
						Minecraft.getMinecraft().entityRenderer = renderer;
					}
				}
				else if(prevRenderer != null && Minecraft.getMinecraft().entityRenderer == renderer) {
					Minecraft.getMinecraft().entityRenderer = prevRenderer;
				}
			}

			if(Minecraft.getMinecraft().thePlayer != null) {
				EntityPlayer player = Minecraft.getMinecraft().thePlayer;
				Transformer transformer = TFHelper.getTransformer(player);

				if(TFRenderHelper.shouldOverrideThirdPersonDistance(player)) {
					if(transformer != null) {
						final int altMode = TFData.ALT_MODE.get(player);
						float thirdPersonDistance;

						if(transformer.canZoom() && TFHelper.isFullyTransformed(player) && TFKeyBinds.keyBindingZoom.getIsKeyPressed() && !TFKeyBinds.keyBindingViewFront.getIsKeyPressed()) {
							thirdPersonDistance = transformer.getZoomAmount();
						}
						else {
							thirdPersonDistance = transformer.getThirdPersonDistance(player);
						}

						TFReflection.setField(TFReflection.thirdPersonDistanceField, Minecraft.getMinecraft().entityRenderer, thirdPersonDistance);
					}
				}
			}
		}
	}
}
