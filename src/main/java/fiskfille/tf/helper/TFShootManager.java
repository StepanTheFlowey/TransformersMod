package fiskfille.tf.helper;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fiskfille.tf.common.data.TFData;
import fiskfille.tf.common.network.MessageLaserShoot;
import fiskfille.tf.common.network.MessageVehicleShoot;
import fiskfille.tf.common.network.base.TFNetworkManager;
import fiskfille.tf.common.transformer.TransformerVurp;
import fiskfille.tf.common.transformer.base.Transformer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import org.lwjgl.input.Mouse;

public class TFShootManager {
	public static int shootCooldown = 0;
	public static int shotsLeft = 4;
	public static int laserCharge;
	public static boolean laserFilling;

	private static boolean reloading;

	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event) {
		if(event.entity instanceof EntityPlayer) {
			final EntityPlayer player = (EntityPlayer) event.entity;

			if(event.entity.worldObj.isRemote) {
				final boolean isTransformed = TFData.ALT_MODE.get(player) != -1;

				if(player == Minecraft.getMinecraft().thePlayer) {
					if(laserFilling) {
						final int max = 50;

						if(laserCharge < max) {
							laserCharge += 1;
						}
						else if(laserCharge >= max) {
							laserFilling = false;
							laserCharge = max;
						}
					}

					final Transformer transformer = TFHelper.getTransformer(player);
					if(transformer != null) {
						if(shootCooldown > 0) {
							shootCooldown--;
						}

						final Item ammo = transformer.getShootItem();
						if(ammo != null) {
							final int ammoCount = getShotsLeft(player, transformer, ammo);

							if(isTransformed) {
								if(reloading && shootCooldown <= 0) {
									shotsLeft = ammoCount;
									reloading = false;
								}
							}
							else if(shotsLeft > ammoCount) {
								shotsLeft = ammoCount;
							}
						}
					}
				}

				if(Mouse.isButtonDown(1)) {
					final Transformer transformer = TFHelper.getTransformer(player);

					if(transformer != null && isTransformed) {
						if(transformer.canShoot(player) && transformer.hasRapidFire() && player.ticksExisted % 2 == 0) {
							stealthForceShoot(transformer, player);
						}
					}
				}
			}
		}
	}

	private static int getShotsLeft(EntityPlayer player, Transformer transformer, Item shootItem) {
		final int maxAmmo = transformer.getShots();
		int ammoCount;

		if(player.capabilities.isCreativeMode) {
			ammoCount = maxAmmo;
		}
		else {
			ammoCount = getAmountOf(shootItem, player);
		}

		if(ammoCount > maxAmmo) {
			ammoCount = maxAmmo;
		}

		if(shotsLeft > ammoCount) {
			shotsLeft = ammoCount;
		}

		return ammoCount;
	}

	private static int getAmountOf(Item item, EntityPlayer player) {
		int amount = 0;

		for(final ItemStack stack : player.inventory.mainInventory) {
			if(stack != null) {
				if(stack.getItem() == item) {
					amount += stack.stackSize;
				}
			}
		}

		return amount;
	}

	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent event) {
		switch(event.action) {
			case RIGHT_CLICK_AIR:
			case RIGHT_CLICK_BLOCK:
				final EntityPlayer player = event.entityPlayer;
				final Transformer transformer = TFHelper.getTransformer(player);
				final boolean isTransformed = TFData.ALT_MODE.get(player) != -1;

				if(transformer != null && isTransformed) {
					if(transformer.canShoot(player) && !transformer.hasRapidFire() && player.worldObj.isRemote) {
						stealthForceShoot(transformer, player);
						event.setCanceled(true);
					}
				}
				break;
		}
	}

	private static void stealthForceShoot(Transformer transformer, EntityPlayer player) {
		if(player == Minecraft.getMinecraft().thePlayer) {
			if(transformer instanceof TransformerVurp) {
				if(transformer.canShoot(player)) {
					if(!laserFilling && laserCharge > 0) {
						laserCharge -= 5;
						player.playSound("random.fizz", 1, 2F);
						TFNetworkManager.networkWrapper.sendToServer(new MessageLaserShoot(player, false));
					}
					else if(!laserFilling && (player.inventory.hasItem(transformer.getShootItem()) || player.capabilities.isCreativeMode)) {
						TFNetworkManager.networkWrapper.sendToServer(new MessageLaserShoot(player, true));
						laserFilling = true;
					}
				}
			}
			else {
				final boolean isCreative = player.capabilities.isCreativeMode;

				if(shotsLeft > 0) {
					if(shootCooldown <= 0) {
						if(transformer.canShoot(player)) {
							final Item shootItem = transformer.getShootItem();

							final boolean hasAmmo = isCreative || player.inventory.hasItem(shootItem);

							if(hasAmmo) {
								TFNetworkManager.networkWrapper.sendToServer(new MessageVehicleShoot(player));

								if(!isCreative) {
									player.inventory.consumeInventoryItem(shootItem);
								}
							}
						}

						if(shotsLeft > transformer.getShots()) {
							shotsLeft = transformer.getShots();
						}

						shotsLeft--;

						if(shotsLeft <= 0) {
							shootCooldown = isCreative ? 0 : 20;
							reloading = true;
						}
					}
				}
				else if(!reloading) {
					shootCooldown = isCreative ? 0 : 20;
					reloading = true;
				}
			}
		}
	}
}
