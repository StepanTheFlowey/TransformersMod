package fiskfille.tf.common.item;

import com.google.common.collect.Multimap;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.common.transformer.TransformerPurge;
import fiskfille.tf.helper.TFHelper;
import fiskfille.tf.helper.TFVectorHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemPurgesKatana extends ItemSword {
	public ItemPurgesKatana() {
		super(ToolMaterial.EMERALD);
		setMaxDamage(1500);
		setCreativeTab(TransformersMod.tabTransformers);
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int time) {
		if(!TFHelper.isFullyTransformed(player) && TFHelper.getTransformer(player) instanceof TransformerPurge) {
			int timeLeft = getMaxItemUseDuration(stack) - time;
			double force = (double) timeLeft / 10;

			if(force > 2D) {
				force = 2D;
			}

			stack.damageItem(1, player);
			Vec3 vec3 = TFVectorHelper.getFrontCoords(player, player.onGround ? force : force * 0.75D, true);
			player.motionX += vec3.xCoord - player.posX;
			player.motionY += vec3.yCoord - player.boundingBox.minY;
			player.motionZ += vec3.zCoord - player.posZ;
			player.fallDistance = 0;
			player.swingItem();
		}
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.drink;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if(TFHelper.getTransformer(player) instanceof TransformerPurge) {
			if(!TFHelper.isFullyTransformed(player)) {
				player.setItemInUse(stack, getMaxItemUseDuration(stack));
			}
		}

		return stack;
	}

	@Override
	public Multimap getItemAttributeModifiers() {
		Multimap multimap = super.getItemAttributeModifiers();
		multimap.removeAll(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName());
		multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", 8D, 0));
		return multimap;
	}
}
