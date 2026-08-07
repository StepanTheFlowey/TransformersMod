package fiskfille.tf.common.item;

import fiskfille.tf.common.entity.EntityBassCharge;
import fiskfille.tf.common.transformer.TransformerSubwoofer;
import fiskfille.tf.helper.TFHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemBassBlaster extends Item {
	public ItemBassBlaster() {
		setMaxDamage(1500);
		setMaxStackSize(1);
		setFull3D();
	}

	@Override
	public void onPlayerStoppedUsing(final ItemStack stack, final World world, final EntityPlayer player, final int time) {
		if(TFHelper.getTransformer(player) instanceof TransformerSubwoofer && !world.isRemote && (player.inventory.hasItem(TFItems.energonCrystalShard) || player.capabilities.isCreativeMode)) {
			stack.damageItem(1, player);

			if(!player.capabilities.isCreativeMode) {
				player.inventory.consumeInventoryItem(TFItems.energonCrystalShard);
			}
		}
	}

	@Override
	public void onUsingTick(final ItemStack stack, final EntityPlayer player, final int count) {
		final int duration = getMaxItemUseDuration(stack) - count;

		if(duration < 60) {
			if(player.inventory.hasItem(TFItems.energonCrystalShard) || player.capabilities.isCreativeMode) {
				final World world = player.worldObj;

				for(int i = 0; i < 2; ++i) {
					world.playSoundAtEntity(player, "note.bass", 1F, 0.8F);
				}

				if(!world.isRemote) {
					final EntityBassCharge entity = new EntityBassCharge(world, player);

					if(TFHelper.isFullyTransformed(player)) {
						entity.posY -= 1.;
					}

					world.spawnEntityInWorld(entity);
				}
			}
		}
	}

	@Override
	public ItemStack onItemRightClick(final ItemStack stack, final World world, final EntityPlayer player) {
		if(TFHelper.getTransformer(player) instanceof TransformerSubwoofer && (player.inventory.hasItem(TFItems.energonCrystalShard) || player.capabilities.isCreativeMode)) {
			player.setItemInUse(stack, getMaxItemUseDuration(stack));
		}

		return stack;
	}

	@Override
	public int getMaxItemUseDuration(final ItemStack stack) {
		return 72000;
	}
}
