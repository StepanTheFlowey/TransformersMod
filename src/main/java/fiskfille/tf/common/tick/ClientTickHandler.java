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
	public void onPlayerTick(final PlayerTickEvent event) {
		final EntityPlayer player = event.player;

		if(event.phase == TickEvent.Phase.END) {
			final Transformer transformer = TFHelper.getTransformer(player);

			if(transformer != null) {
				final float transformationTimer = TFHelper.getTransformationTimer(player);

				if(transformationTimer >= 0.5F) {
					transformer.updateMovement(player);

					if(TFData.BOOSTING.get(player) && TFData.NITRO.get(player) > 0 && TFHelper.isFullyTransformed(player)) {
						transformer.doNitroParticles(player);
					}
				}

				if(player == Minecraft.getMinecraft().thePlayer) {
					if(transformer.overrideFirstPerson()) {
						final GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;

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

						if(TFData.TRANSFORM_PROGRESS.get(player) == 0 && TFData.PREV_TRANSFORM_PROGRESS.get(player) > 0 && TFConfig.firstPersonAfterTransformation) {
							gameSettings.thirdPersonView = 0;
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onClientTick(final ClientTickEvent event) {
		if(event.phase == TickEvent.Phase.START) {
			final Minecraft minecraft = Minecraft.getMinecraft();

			if(minecraft.theWorld != null) {
				for(final EntityPlayer player : (List<EntityPlayer>) minecraft.theWorld.playerEntities) {
					TFRenderHelper.updateMotionY(player);
				}

				if(minecraft.theWorld.isRemote && !minecraft.isGamePaused()) {
					for(final Map.Entry<DimensionalCoords, TileData> e : TFTileHelper.getTileData().entrySet()) {
						e.getValue().clientTick();
					}
				}

				if(ClientProxy.fakePlayer == null || ClientProxy.fakePlayer.worldObj != minecraft.theWorld) {
					if(minecraft.playerController != null) {
						ClientProxy.fakePlayer = minecraft.playerController.func_147493_a(minecraft.theWorld, new StatFileWriter());
						ClientProxy.fakePlayer.movementInput = new MovementInputFromOptions(minecraft.gameSettings);
					}
				}
				else {
					ClientProxy.fakePlayer.ticksExisted += 1;
				}
			}
		}
	}

	@SubscribeEvent
	public void onRenderTick(final RenderTickEvent event) {
		renderTick = event.renderTickTime;

		final Minecraft minecraft = Minecraft.getMinecraft();
		final World world = minecraft.theWorld;

		if(world != null) {
			if(event.phase == TickEvent.Phase.START) {
				final EntityClientPlayerMP player = minecraft.thePlayer;

				if(TFRenderHelper.shouldOverrideView(player)) {
					if(renderer == null) {
						renderer = new EntityRendererTF(minecraft);
					}

					if(minecraft.entityRenderer != renderer) {
						prevRenderer = minecraft.entityRenderer;
						minecraft.entityRenderer = renderer;
					}
				}
				else if(prevRenderer != null && minecraft.entityRenderer == renderer) {
					minecraft.entityRenderer = prevRenderer;
				}
			}

			if(minecraft.thePlayer != null) {
				final EntityPlayer player = minecraft.thePlayer;

				if(TFRenderHelper.shouldOverrideThirdPersonDistance(player)) {
					final Transformer transformer = TFHelper.getTransformer(player);

					if(transformer != null) {
						final float thirdPersonDistance;

						if(transformer.canZoom() && TFHelper.isFullyTransformed(player) && TFKeyBinds.keyBindingZoom.getIsKeyPressed() && !TFKeyBinds.keyBindingViewFront.getIsKeyPressed()) {
							thirdPersonDistance = transformer.getZoomAmount();
						}
						else {
							thirdPersonDistance = transformer.getThirdPersonDistance(player);
						}

						TFReflection.setField(TFReflection.thirdPersonDistanceField, minecraft.entityRenderer, thirdPersonDistance);
					}
				}
			}
		}
	}
}
