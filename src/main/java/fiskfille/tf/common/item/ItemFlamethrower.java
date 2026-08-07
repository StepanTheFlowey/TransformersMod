package fiskfille.tf.common.item;

import fiskfille.tf.client.particle.TFParticleType;
import fiskfille.tf.client.particle.TFParticles;
import fiskfille.tf.common.entity.EntityFlamethrowerFire;
import fiskfille.tf.common.transformer.TransformerCloudtrap;
import fiskfille.tf.helper.TFHelper;
import fiskfille.tf.helper.TFVectorHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.concurrent.ThreadLocalRandom;

public class ItemFlamethrower extends Item {
	public ItemFlamethrower() {
		setMaxDamage(1500);
		setMaxStackSize(1);
		setFull3D();
	}

	@Override
	public void onPlayerStoppedUsing(final ItemStack stack, final World world, final EntityPlayer player, final int time) {
		if(TFHelper.getTransformer(player) instanceof TransformerCloudtrap && !world.isRemote && (player.inventory.hasItem(TFItems.energonCrystalShard) || player.capabilities.isCreativeMode)) {
			stack.damageItem(1, player);

			if(!player.capabilities.isCreativeMode) {
				player.inventory.consumeInventoryItem(TFItems.energonCrystalShard);
			}
		}
	}

	@Override
	public void onUsingTick(final ItemStack stack, final EntityPlayer player, final int count) {
		final int duration = getMaxItemUseDuration(stack) - count;

		if(duration < 40) {
			if(player.inventory.hasItem(TFItems.energonCrystalShard) || player.capabilities.isCreativeMode) {
				final World world = player.worldObj;

				if(duration % 4 == 0) {
					final Vec3 backCoords = TFVectorHelper.getFrontCoords(player, -0.075D, true);
					player.motionX = backCoords.xCoord - player.posX;
					player.motionZ = backCoords.zCoord - player.posZ;
					world.playAuxSFX(1009, (int) player.posX, (int) player.posY, (int) player.posZ, 0);
				}

				final ThreadLocalRandom random = ThreadLocalRandom.current();
				final Vec3 sideCoords = TFVectorHelper.getBackSideCoords(player, 0.3D, false, 0.6D, true);
				final Vec3 backCoords = TFVectorHelper.getFrontCoords(player, 0.5D, true);
				final int divider = 3;

				if(world.isRemote) {
					for(int i = 0; i < 50; ++i) {
						TFParticles.spawnParticle(
										TFParticleType.FLAMETHROWER_FLAME,
										sideCoords.xCoord,
										sideCoords.yCoord + player.yOffset,
										sideCoords.zCoord,
										(float) (backCoords.xCoord - player.posX) + (random.nextFloat() - 0.5F) / divider,
										(float) (backCoords.yCoord - player.boundingBox.minY) + (random.nextFloat() - 0.5F) / divider,
										(float) (backCoords.zCoord - player.posZ) + (random.nextFloat() - 0.5F) / divider
						);
					}
				}
				else {
					for(int i = 0; i < 5; ++i) {
						final EntityFlamethrowerFire entity = new EntityFlamethrowerFire(world, player);
						entity.motionX = (backCoords.xCoord - player.posX) + (random.nextDouble() - 0.5D) / divider;
						entity.motionY = (backCoords.yCoord - player.boundingBox.minY) + (random.nextDouble() - 0.5D) / divider;
						entity.motionZ = (backCoords.zCoord - player.posZ) + (random.nextDouble() - 0.5D) / divider;
						entity.setPosition(sideCoords.xCoord, sideCoords.yCoord + player.getEyeHeight(), sideCoords.zCoord);
						world.spawnEntityInWorld(entity);
					}
				}
			}
		}
	}

	@Override
	public int getMaxItemUseDuration(final ItemStack stack) {
		return 72000;
	}

	@Override
	public ItemStack onItemRightClick(final ItemStack stack, final World world, final EntityPlayer player) {
		if(TFHelper.getTransformer(player) instanceof TransformerCloudtrap && (player.inventory.hasItem(TFItems.energonCrystalShard) || player.capabilities.isCreativeMode)) {
			player.setItemInUse(stack, getMaxItemUseDuration(stack));
		}

		return stack;
	}
}
