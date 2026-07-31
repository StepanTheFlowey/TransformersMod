package fiskfille.tf.common.transformer;

import fiskfille.tf.client.keybinds.TFKeyBinds;
import fiskfille.tf.common.achievement.TFAchievements;
import fiskfille.tf.common.block.TFBlocks;
import fiskfille.tf.common.data.TFDataManager;
import fiskfille.tf.common.entity.EntityLaser;
import fiskfille.tf.common.item.ItemVurpsSniper;
import fiskfille.tf.common.item.TFItems;
import fiskfille.tf.common.transformer.base.TransformerCar;
import fiskfille.tf.helper.TFHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

/**
 * @author gegy1000, FiskFille
 */
public class TransformerVurp extends TransformerCar {
	public TransformerVurp() {
		super("Vurp");
	}

	@Override
	public Item getHelmet() {
		return TFItems.vurpHelmet;
	}

	@Override
	public Item getChestplate() {
		return TFItems.vurpChestplate;
	}

	@Override
	public Item getLeggings() {
		return TFItems.vurpLeggings;
	}

	@Override
	public Item getBoots() {
		return TFItems.vurpBoots;
	}

	@Override
	public float getHeightOffset() {
		return -0.3F;
	}

	@Override
	public float getVehicleHeightOffset() {
		return -1.4F;
	}

	@Override
	public Item getShootItem() {
		return Item.getItemFromBlock(TFBlocks.energonCube);
	}

	@Override
	public Entity getShootEntity(EntityPlayer player) {
		return new EntityLaser(player.worldObj, player);
	}

	@Override
	public String getShootSound() {
		return "random.fizz";
	}

	@Override
	public float getShootVolume() {
		return 0.3F;
	}

	@Override
	public int getShots() {
		return 64;
	}

	@Override
	public boolean hasRapidFire() {
		return true;
	}

	@Override
	public void tick(EntityPlayer player, float timer) {
		super.tick(player, timer);

		final ItemStack heldItem = player.getHeldItem();
		final int zoomTimer = TFDataManager.getZoomTimer(player);
		final boolean holdingSniper = heldItem != null && heldItem.getItem() instanceof ItemVurpsSniper;

		final PotionEffect activePotionEffect = player.getActivePotionEffect(Potion.nightVision);
		if(activePotionEffect == null || activePotionEffect.getDuration() == 0) {
			if(holdingSniper && zoomTimer > 7) {
				player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 1, 0));
			}
			else {
				player.removePotionEffect(Potion.nightVision.id);
			}
		}

		if(player.worldObj.isRemote) {
			if(holdingSniper && TFKeyBinds.keyBindingZoom.getIsKeyPressed() && !TFHelper.isFullyTransformed(player)) {
				if(zoomTimer < 10) {
					TFDataManager.setZoomTimer(player, zoomTimer + 1);
				}
			}
			else {
				if(zoomTimer > 0) {
					TFDataManager.setZoomTimer(player, zoomTimer - 1);
				}
			}
		}

		player.addStat(TFAchievements.vurp, 1);
	}
}
