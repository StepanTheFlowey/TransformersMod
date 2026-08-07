package fiskfille.tf.common.container;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public class InventoryAssembly extends InventoryCrafting {
	private final ItemStack[] stackList;
	private final int inventoryWidth;
	private final Container eventHandler;

	public InventoryAssembly(final Container container, final int width, final int height) {
		super(container, width, height);
		final int k = width * height;
		stackList = new ItemStack[k];
		eventHandler = container;
		inventoryWidth = width;
	}

	@Override
	public int getSizeInventory() {
		return stackList.length;
	}

	@Override
	public ItemStack getStackInSlot(final int slot) {
		return slot >= getSizeInventory() ? null : stackList[slot];
	}

	@Override
	public ItemStack getStackInRowAndColumn(final int row, final int column) {
		if(row >= 0 && row < inventoryWidth) {
			final int k = row + column * inventoryWidth;
			return getStackInSlot(k);
		}
		else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(final int slot) {
		if(stackList[slot] != null) {
			final ItemStack itemstack = stackList[slot];
			stackList[slot] = null;
			return itemstack;
		}
		else {
			return null;
		}
	}

	@Override
	public ItemStack decrStackSize(final int slot, final int amount) {
		if(stackList[slot] != null) {
			final ItemStack itemstack;

			if(stackList[slot].stackSize <= amount) {
				itemstack = stackList[slot];
				stackList[slot] = null;
			}
			else {
				itemstack = stackList[slot].splitStack(amount);

				if(stackList[slot].stackSize == 0) {
					stackList[slot] = null;
				}
			}

			eventHandler.onCraftMatrixChanged(this);
			return itemstack;
		}
		else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(final int slot, final ItemStack itemstack) {
		stackList[slot] = itemstack;
		eventHandler.onCraftMatrixChanged(this);
	}
}
