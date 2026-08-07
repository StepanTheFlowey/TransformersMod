package fiskfille.tf.common.recipe;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class AssemblyTableRecipe implements IRecipe {
	public final int recipeWidth;
	public final int recipeHeight;
	public final ItemStack[] recipeItems;
	public final ItemStack[] recipeDyes;
	private final ItemStack recipeOutput;
	private boolean field_92101_f;

	public AssemblyTableRecipe(final int width, final int height, final ItemStack[] ingredients, final ItemStack[] dyes, final ItemStack result) {
		recipeWidth = width;
		recipeHeight = height;
		recipeItems = ingredients;
		recipeDyes = dyes;
		recipeOutput = result;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return recipeOutput;
	}

	@Override
	public boolean matches(final InventoryCrafting inventory, final World world) {
		for(int i = 0; i <= 5 - recipeWidth; ++i) {
			for(int j = 0; j <= 5 - recipeHeight; ++j) {
				if(checkMatch(inventory, i, j)) {
					return true;
				}
			}
		}

		return false;
	}

	private boolean checkMatch(final InventoryCrafting inventory, final int x, final int y) {
		for(int row = 0; row < 5; ++row) {
			for(int column = 0; column < 5; ++column) {
				final int x1 = row - x;
				final int y1 = column - y;
				final int id = x1 + y1 * recipeWidth;
				ItemStack recipeStack = null;

				if(x1 >= 0 && y1 >= 0 && x1 < recipeWidth && y1 < recipeHeight) {
					recipeStack = recipeItems[id];
				}

				if(id == 0) {
					recipeStack = recipeDyes[0];
				}
				else if(id == 4) {
					recipeStack = recipeDyes[1];
				}
				else if(id == 20) {
					recipeStack = recipeDyes[2];
				}

				final ItemStack stackInSlot = inventory.getStackInRowAndColumn(row, column);

				if(stackInSlot != null || recipeStack != null) {
					if(recipeStack.getItem() != stackInSlot.getItem()) {
						return false;
					}

					if(stackInSlot.stackSize < recipeStack.stackSize) {
						return false;
					}

					if(recipeStack.getItemDamage() != 32767 && recipeStack.getItemDamage() != stackInSlot.getItemDamage()) {
						return false;
					}
				}
			}
		}

		return true;
	}

	@Override
	public ItemStack getCraftingResult(final InventoryCrafting inventory) {
		final ItemStack itemstack = getRecipeOutput().copy();

		if(field_92101_f) {
			for(int i = 0; i < inventory.getSizeInventory(); ++i) {
				final ItemStack itemstack1 = inventory.getStackInSlot(i);

				if(itemstack1 != null && itemstack1.hasTagCompound()) {
					itemstack.setTagCompound((NBTTagCompound) itemstack1.getTagCompound().copy());
				}
			}
		}

		return itemstack;
	}

	@Override
	public int getRecipeSize() {
		return recipeWidth * recipeHeight;
	}

	public AssemblyTableRecipe func_92100_c() {
		field_92101_f = true;
		return this;
	}
}
