package fiskfille.tf.common.item.armor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.model.transformer.ModelTransformerBase;
import fiskfille.tf.client.model.transformer.definition.TFModelRegistry;
import fiskfille.tf.common.transformer.base.Transformer;
import fiskfille.tf.helper.TFArmorHelper;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;

import java.util.List;

public abstract class ItemTransformerArmor extends ItemArmor implements ISpecialArmor {
	public ItemTransformerArmor(final ArmorMaterial material, final int renderIndex, final int armorPiece) {
		super(material, renderIndex, armorPiece);
		setCreativeTab(TransformersMod.tabTransformers);
	}

	@Override
	public boolean isValidArmor(final ItemStack stack, final int armorType, final Entity entity) {
		if(entity instanceof EntityLivingBase) {
			final EntityLivingBase livingBase = (EntityLivingBase) entity;

			for(int i = 0; i < 4; ++i) {
				final ItemStack armor = livingBase.getEquipmentInSlot(1 + i);

				if(armor != null && armor.getItem() instanceof ItemTransformerArmor) {
					if(getTransformer() != ((ItemTransformerArmor) armor.getItem()).getTransformer()) {
						return false;
					}
				}
			}
		}

		return super.isValidArmor(stack, armorType, entity);
	}

	@Override
	public ArmorProperties getProperties(final EntityLivingBase player, final ItemStack armor, final DamageSource source, final double damage, final int slot) {
		if(!source.isUnblockable()) {
			final ItemStack itemstack = TFArmorHelper.getArmorShell(armor);

			if(itemstack != null) {
				final ItemArmor item = (ItemArmor) itemstack.getItem();
				return new ArmorProperties(0, item.damageReduceAmount / 25D, armor.getMaxDamage() + 1 - armor.getItemDamage());
			}
			else {
				return new ArmorProperties(0, damageReduceAmount / 25D, armor.getMaxDamage() + 1 - armor.getItemDamage());
			}
		}

		return new ArmorProperties(0, 0, 0);
	}

	@Override
	public int getArmorDisplay(final EntityPlayer player, final ItemStack armor, final int slot) {
		final ItemStack itemstack = TFArmorHelper.getArmorShell(armor);

		if(itemstack != null) {
			return TFArmorHelper.getArmorValue(player, itemstack, slot);
		}

		return damageReduceAmount;
	}

	@Override
	public void damageArmor(final EntityLivingBase entity, final ItemStack stack, final DamageSource source, final int damage, final int slot) {
		stack.damageItem(damage, entity);
	}

	@Override
	public String getArmorTexture(final ItemStack stack, final Entity entity, final int slot, final String type) {
		return TFModelRegistry.getModel(getTransformer()).getTexture(entity, "").toString();
	}

	@Override
	public void addInformation(final ItemStack itemstack, final EntityPlayer player, final List info, final boolean p_77624_4_) {
		final ItemStack itemstack1 = TFArmorHelper.getArmorShell(itemstack);

		if(itemstack1 != null) {
			info.add(itemstack1.getDisplayName());
		}
	}

	public abstract Transformer getTransformer();

	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(final EntityLivingBase entity, final ItemStack itemstack, final int armorSlot) {
		final ModelTransformerBase model = getTransformer().getModel().getMainModel();

		if(itemstack != null && model != null) {
			model.layerToRender = armorSlot + 1;

			model.bipedHead.showModel = armorSlot == 0;
			model.bipedHeadwear.showModel = armorSlot == 0;
			model.bipedBody.showModel = armorSlot == 1;
			model.bipedRightArm.showModel = armorSlot == 1;
			model.bipedLeftArm.showModel = armorSlot == 1;
			model.bipedRightLeg.showModel = armorSlot == 2 || armorSlot == 3;
			model.bipedLeftLeg.showModel = armorSlot == 2 || armorSlot == 3;

			model.isSneak = entity.isSneaking();
			model.isRiding = entity.isRiding();
			model.isChild = entity.isChild();
			model.heldItemRight = entity.getEquipmentInSlot(0) != null ? 1 : 0;

			if(entity instanceof EntityPlayer) {
				final ItemStack heldItem = entity.getHeldItem();
				model.aimedBow = ((EntityPlayer) entity).getItemInUseDuration() > 0 && heldItem != null && heldItem.getItemUseAction() == EnumAction.bow;
				model.heldItemRight = ((EntityPlayer) entity).getItemInUseDuration() > 0 && heldItem != null && heldItem.getItemUseAction() == EnumAction.block ? 3 : entity.getEquipmentInSlot(0) != null ? 1 : 0;
			}

			return model;
		}

		return null;
	}

	@Override
	public void registerIcons(final IIconRegister iconRegister) {
	}
}
