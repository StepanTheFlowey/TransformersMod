package fiskfille.tf.common.tileentity;

import fiskfille.tf.helper.TFTileHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public abstract class TileEntityContainer extends TileEntityTF implements IInventory {
	protected ItemStack[] inventory = new ItemStack[getSizeInventory()];

	public ItemStack[] getItemStacks() {
		if(hasWorldObj()) {
			final TileEntityContainer base = TFTileHelper.getTileBase(this);

			if(base != this) {
				if(base != null) {
					return base.getItemStacks();
				}

				return new ItemStack[getSizeInventory()];
			}
		}

		return inventory;
	}

	public void setItemStacks(final ItemStack[] itemstacks) {
		if(hasWorldObj()) {
			final TileEntityContainer base = TFTileHelper.getTileBase(this);

			if(base != this) {
				if(base != null) {
					base.setItemStacks(itemstacks);
				}

				return;
			}
		}

		inventory = itemstacks;
	}

	@Override
	public ItemStack getStackInSlot(final int slot) {
		return getItemStacks()[slot];
	}

	@Override
	public ItemStack decrStackSize(final int slot, final int amount) {
		if(getItemStacks()[slot] != null) {
			final ItemStack itemstack;

			if(getItemStacks()[slot].stackSize <= amount) {
				itemstack = getItemStacks()[slot];
				getItemStacks()[slot] = null;
				return itemstack;
			}
			else {
				itemstack = getItemStacks()[slot].splitStack(amount);

				if(getItemStacks()[slot].stackSize == 0) {
					getItemStacks()[slot] = null;
				}

				return itemstack;
			}
		}
		else {
			return null;
		}
	}

	@Override
	public ItemStack getStackInSlotOnClosing(final int slot) {
		if(getItemStacks()[slot] != null) {
			final ItemStack itemstack = getItemStacks()[slot];
			getItemStacks()[slot] = null;
			return itemstack;
		}
		else {
			return null;
		}
	}

	@Override
	public void setInventorySlotContents(final int slot, final ItemStack itemstack) {
		getItemStacks()[slot] = itemstack;

		if(itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public void readCustomNBT(final NBTTagCompound nbt) {
		if(nbt.hasKey("LoadInventory")) {
			final NBTTagList nbttaglist = nbt.getTagList("Items", 10);
			setItemStacks(new ItemStack[getSizeInventory()]);

			for(int i = 0; i < nbttaglist.tagCount(); ++i) {
				final NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
				final byte slot = nbttagcompound1.getByte("Slot");

				if(slot >= 0 && slot < getItemStacks().length) {
					getItemStacks()[slot] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
				}
			}
		}
	}

	@Override
	public void writeCustomNBT(final NBTTagCompound nbt) {
		if(TFTileHelper.getTileBase(this) != this) {
			return;
		}

		nbt.setBoolean("LoadInventory", true);
		final NBTTagList nbttaglist = new NBTTagList();

		for(int i = 0; i < getItemStacks().length; ++i) {
			if(getItemStacks()[i] != null) {
				final NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				nbttagcompound1.setByte("Slot", (byte) i);
				getItemStacks()[i].writeToNBT(nbttagcompound1);
				nbttaglist.appendTag(nbttagcompound1);
			}
		}

		nbt.setTag("Items", nbttaglist);
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(final EntityPlayer player) {
		return player.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64D;
	}

	@Override
	public void openInventory() {
	}

	@Override
	public void closeInventory() {
	}

	@Override
	public boolean isItemValidForSlot(final int slot, final ItemStack stack) {
		return true;
	}
}
