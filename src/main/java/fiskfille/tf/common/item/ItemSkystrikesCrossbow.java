package fiskfille.tf.common.item;

import com.google.common.collect.Multimap;
import fiskfille.tf.common.entity.EntityLaserBeam;
import fiskfille.tf.common.transformer.TransformerSkystrike;
import fiskfille.tf.helper.TFHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemSkystrikesCrossbow extends Item {
	public ItemSkystrikesCrossbow() {
		setMaxDamage(1500);
		setMaxStackSize(1);
		setFull3D();
	}

	@Override
	public void onUpdate(ItemStack itemstack, World world, Entity entity, int i, boolean b) {
		if(!itemstack.hasTagCompound()) {
			itemstack.setTagCompound(new NBTTagCompound());
			itemstack.getTagCompound().setBoolean("blueMode", false);
		}
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int time) {
		if(TFHelper.getTransformer(player) instanceof TransformerSkystrike && !world.isRemote && (player.inventory.hasItem(TFItems.energonCrystalShard) || player.capabilities.isCreativeMode)) {
			final boolean blue = isBlue(stack);
			stack.getTagCompound().setBoolean("blueMode", !blue);

			stack.damageItem(1, player);

			if(!player.capabilities.isCreativeMode) {
				player.inventory.consumeInventoryItem(TFItems.energonCrystalShard);
			}
		}
	}

	private boolean isBlue(ItemStack stack) {
		return stack.hasTagCompound() && stack.getTagCompound().getBoolean("blueMode");
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
		final int duration = getMaxItemUseDuration(stack) - count;

		if(stack.hasTagCompound() && (player.inventory.hasItem(TFItems.energonCrystalShard) || player.capabilities.isCreativeMode)) {
			final World world = player.worldObj;

			if(!world.isRemote) {
				if(duration > 1 && duration < 80) {
					final EntityLaserBeam entity = new EntityLaserBeam(world, player, isBlue(stack));
					world.spawnEntityInWorld(entity);
				}
			}
		}
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.bow;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if(TFHelper.getTransformer(player) instanceof TransformerSkystrike && (player.inventory.hasItem(TFItems.energonCrystalShard) || player.capabilities.isCreativeMode)) {
			player.setItemInUse(stack, getMaxItemUseDuration(stack));
		}

		return stack;
	}

	@Override
	public Multimap getItemAttributeModifiers() {
		Multimap multimap = super.getItemAttributeModifiers();
		multimap.removeAll(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName());
		multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", 4D, 0));
		return multimap;
	}
}
