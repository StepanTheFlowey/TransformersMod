package fiskfille.tf.common.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class InventoryDisplayStationArmor implements IInventory {
	private final ItemStack[] stackList = new ItemStack[4];
	private final Container eventHandler;
	private int inventoryWidth;

	public InventoryDisplayStationArmor(final Container container) {
		this.eventHandler = container;
	}

	@Override
	public int getSizeInventory() {
		return this.stackList.length;
	}

	@Override
	public ItemStack getStackInSlot(final int slot) {
		return slot >= this.getSizeInventory() ? null : this.stackList[slot];
	}

	public ItemStack getStackInRowAndColumn(final int row, final int column) {
		if(row >= 0 && row < this.inventoryWidth) {
			final int k = row + column * this.inventoryWidth;
			return this.getStackInSlot(k);
		}
		else {
			return null;
		}
	}

	@Override
	public String getInventoryName() {
		return "container.crafting";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(final int slot) {
		if(this.stackList[slot] != null) {
			final ItemStack itemstack = this.stackList[slot];
			this.stackList[slot] = null;
			return itemstack;
		}
		else {
			return null;
		}
	}

	@Override
	public ItemStack decrStackSize(final int slot, final int amount) {
		if(this.stackList[slot] != null) {
			final ItemStack itemstack;

			if(this.stackList[slot].stackSize <= amount) {
				itemstack = this.stackList[slot];
				this.stackList[slot] = null;
			}
			else {
				itemstack = this.stackList[slot].splitStack(amount);

				if(this.stackList[slot].stackSize == 0) {
					this.stackList[slot] = null;
				}
			}

			this.eventHandler.onCraftMatrixChanged(this);
			return itemstack;
		}
		else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(final int slot, final ItemStack itemstack) {
		this.stackList[slot] = itemstack;
		this.eventHandler.onCraftMatrixChanged(this);
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public void markDirty() {
	}

	@Override
	public boolean isUseableByPlayer(final EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(final int slot, final ItemStack itemstack) {
		return true;
	}
}
