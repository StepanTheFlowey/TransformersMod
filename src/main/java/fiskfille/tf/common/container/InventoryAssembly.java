package fiskfille.tf.common.container;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;

public class InventoryAssembly extends InventoryCrafting {
	private final ItemStack[] stackList;
	private final int inventoryWidth;
	private final Container eventHandler;

	public InventoryAssembly(Container container, int width, int height) {
		super(container, width, height);
		int k = width * height;
		stackList = new ItemStack[k];
		eventHandler = container;
		inventoryWidth = width;
	}

	@Override
	public int getSizeInventory() {
		return stackList.length;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return slot >= getSizeInventory() ? null : stackList[slot];
	}

	@Override
	public ItemStack getStackInRowAndColumn(int row, int column) {
		if(row >= 0 && row < inventoryWidth) {
			int k = row + column * inventoryWidth;
			return getStackInSlot(k);
		}
		else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slot) {
		if(stackList[slot] != null) {
			ItemStack itemstack = stackList[slot];
			stackList[slot] = null;
			return itemstack;
		}
		else {
			return null;
		}
	}

	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		if(stackList[slot] != null) {
			ItemStack itemstack;

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
	public void setInventorySlotContents(int slot, ItemStack itemstack) {
		stackList[slot] = itemstack;
		eventHandler.onCraftMatrixChanged(this);
	}
}
