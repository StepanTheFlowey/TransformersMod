package fiskfille.tf.common.recipe;

import fiskfille.tf.TransformersAPI;
import fiskfille.tf.common.item.TFItems;
import fiskfille.tf.common.item.armor.ItemTransformerArmor;
import fiskfille.tf.common.transformer.base.Transformer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;

public class RecipeDisplayItems implements IRecipe {
	@Override
	public boolean matches(final InventoryCrafting inventoryCrafting, final World world) {
		final ItemStack[] stacks = new ItemStack[9];

		for(int i = 0; i < stacks.length; i++) {
			stacks[i] = inventoryCrafting.getStackInSlot(i);
		}

		ItemStack head = null;
		ItemStack chest = null;
		ItemStack legs = null;
		ItemStack boots = null;

		int emptySlots = 0;

		for(final ItemStack itemStack : stacks) {
			if(itemStack != null) {
				final Item item = itemStack.getItem();

				if(item instanceof ItemTransformerArmor) {
					final ItemArmor armorItem = (ItemArmor) item;

					switch(armorItem.armorType) {
						case 0:
							head = itemStack;
							break;

						case 1:
							chest = itemStack;
							break;

						case 2:
							legs = itemStack;
							break;

						case 3:
							boots = itemStack;
							break;
					}
				}
			}
			else {
				emptySlots++;
			}
		}

		if(emptySlots != 5) {
			return false;
		}

		return head != null && chest != null && legs != null && boots != null;
	}

	@Override
	public ItemStack getCraftingResult(final InventoryCrafting inventoryCrafting) {
		ItemStack itemstack = new ItemStack(TFItems.displayVehicle, 1);
		itemstack.setTagCompound(new NBTTagCompound());

		final ItemStack[] stacks = new ItemStack[9];

		for(int i = 0; i < stacks.length; i++) {
			stacks[i] = inventoryCrafting.getStackInSlot(i);
		}

		ItemStack head = null;
		ItemStack chest = null;
		ItemStack legs = null;
		ItemStack feet = null;

		for(final ItemStack itemStack : stacks) {
			if(itemStack != null) {
				final Item item = itemStack.getItem();

				if(item instanceof ItemTransformerArmor) {
					final ItemArmor armorItem = (ItemArmor) item;

					switch(armorItem.armorType) {
						case 0:
							head = itemStack;
							break;

						case 1:
							chest = itemStack;
							break;

						case 2:
							legs = itemStack;
							break;

						case 3:
							feet = itemStack;
							break;
					}
				}
			}
		}

		if(head != null && chest != null && legs != null && feet != null) {
			final Item headItem = head.getItem();
			final Item chestItem = chest.getItem();
			final Item legsItem = legs.getItem();
			final Item feetItem = feet.getItem();

			int i = 0;

			boolean found = false;

			for(final Transformer transformer : TransformersAPI.getTransformers()) {
				final Item helmet = transformer.getHelmet();
				final Item chestplate = transformer.getChestplate();
				final Item leggings = transformer.getLeggings();
				final Item boots = transformer.getBoots();

				if(headItem == helmet && chestItem == chestplate && legsItem == leggings && feetItem == boots) {
					itemstack.setItemDamage(i);
					setNBTData(head, chest, legs, feet, itemstack);
					found = true;
				}

				i++;
			}

			if(!found) {
				itemstack = null;
			}
		}

		return itemstack;
	}

	public void setNBTData(final ItemStack head, final ItemStack chest, final ItemStack legs, final ItemStack feet, final ItemStack itemstack) {
		final ItemStack[] itemstacks = {head, chest, legs, feet};
		final NBTTagList itemsList = new NBTTagList();

		for(int i = 0; i < itemstacks.length; ++i) {
			if(itemstacks[i] != null) {
				final NBTTagCompound itemTag = new NBTTagCompound();
				itemTag.setByte("Slot", (byte) i);
				itemstacks[i].writeToNBT(itemTag);
				itemsList.appendTag(itemTag);
			}
		}

		itemstack.getTagCompound().setTag("Items", itemsList);
	}

	@Override
	public int getRecipeSize() {
		return 10;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return null;
	}
}
