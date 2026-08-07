package fiskfille.tf.common.container;

import fiskfille.tf.common.item.TFItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerGroundBridge extends ContainerBasic {
	public final InventoryGroundBridge inventory;

	public ContainerGroundBridge(final InventoryPlayer inventoryPlayer, final InventoryGroundBridge inventoryGroundBridge) {
		super(null);
		inventory = inventoryGroundBridge;

		addSlotToContainer(new Slot(inventoryGroundBridge, 0, 13, 56) {
			@Override
			public boolean isItemValid(final ItemStack itemstack) {
				return inventory.isItemValidForSlot(slotNumber, itemstack);
			}

			@Override
			public boolean canTakeStack(final EntityPlayer player) {
				return isItemValid(getStack());
			}
		});

		addPlayerInventory(inventoryPlayer, 0);
	}

	@Override
	public boolean canInteractWith(final EntityPlayer player) {
		return player.getHeldItem() != null && player.getHeldItem().getItem() == TFItems.groundBridgeRemote;
	}

	@Override
	public ItemStack transferStackInSlot(final EntityPlayer player, final int slotId) {
		ItemStack itemstack = null;
		final Slot slot = (Slot) inventorySlots.get(slotId);
		final int CSD = 0;

		if(slot != null && slot.getHasStack()) {
			final ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if(slotId == CSD) {
				if(!mergeItemStack(itemstack1, CSD + 1, CSD + 37, true)) {
					return null;
				}

				slot.onSlotChange(itemstack1, itemstack);
			}
			else if(slotId > CSD) {
				final Slot slot1 = (Slot) inventorySlots.get(CSD);

				if(slot1 != null && slot1.isItemValid(itemstack1)) {
					if(!mergeItemStack(itemstack1, CSD, CSD + 1, false)) {
						return null;
					}
				}
				else if(slotId >= CSD + 1 && slotId < CSD + 28) {
					if(!mergeItemStack(itemstack1, CSD + 28, CSD + 37, false)) {
						return null;
					}
				}
				else if(slotId >= CSD + 28 && slotId < CSD + 37 && !mergeItemStack(itemstack1, CSD + 1, CSD + 28, false)) {
					return null;
				}
			}
			else if(!mergeItemStack(itemstack1, CSD + 1, CSD + 37, false)) {
				return null;
			}

			if(itemstack1.stackSize == 0) {
				slot.putStack(null);
			}
			else {
				slot.onSlotChanged();
			}

			if(itemstack1.stackSize == itemstack.stackSize) {
				return null;
			}

			slot.onPickupFromSlot(player, itemstack1);
		}

		return itemstack;
	}
}
