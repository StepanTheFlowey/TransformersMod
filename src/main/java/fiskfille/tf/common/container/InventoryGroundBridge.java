package fiskfille.tf.common.container;

import fiskfille.tf.common.block.BlockControlPanel;
import fiskfille.tf.common.data.tile.TileDataControlPanel;
import fiskfille.tf.common.item.ItemCSD;
import fiskfille.tf.common.item.ItemCSD.DimensionalCoords;
import fiskfille.tf.common.item.TFItems;
import fiskfille.tf.common.tileentity.TileEntityControlPanel;
import fiskfille.tf.helper.TFTileHelper;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ReportedException;
import net.minecraft.world.WorldServer;

public class InventoryGroundBridge implements IInventory {
	public final EntityPlayer player;
	public final ItemStack remoteItem;

	public final ItemStack[] inventory = new ItemStack[1];

	public InventoryGroundBridge(final EntityPlayer player, final ItemStack itemstack) {
		this.player = player;
		remoteItem = itemstack;

		if(itemstack != null) {
			if(!itemstack.hasTagCompound()) {
				itemstack.setTagCompound(new NBTTagCompound());
			}

			final NBTTagCompound nbt = itemstack.getTagCompound();

			for(int i = 0; i < getSizeInventory(); i++) {
				if(nbt.hasKey("Slot" + i)) {
					inventory[i] = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("Slot" + i));
				}
			}
		}
	}

	public ItemStack getFirstItemStack() {
		for(final ItemStack itemstack : inventory) {
			if(itemstack != null) {
				return itemstack;
			}
		}

		return null;
	}

	public int getFirstSlotID() {
		for(int i = 0; i < inventory.length; ++i) {
			final ItemStack itemstack = inventory[i];

			if(itemstack != null) {
				return i;
			}
		}

		return -1;
	}

	public boolean addItemStackToInventory(final ItemStack itemstack) {
		final boolean flag = addItemStackToInventoryTemp(itemstack);

		if(flag) {
			if(remoteItem != null) {
				final NBTTagCompound nbt = remoteItem.getTagCompound();

				for(int j = 0; j < getSizeInventory(); j++) {
					if(inventory[j] != null) {
						nbt.setTag("Slot" + j, inventory[j].writeToNBT(nbt.getCompoundTag("Slot" + j)));
					}
					else if(nbt.hasKey("Slot" + j)) {
						nbt.removeTag("Slot" + j);
					}
				}
			}
		}

		return flag;
	}

	private boolean addItemStackToInventoryTemp(final ItemStack itemstack) {
		if(itemstack != null && itemstack.stackSize != 0 && itemstack.getItem() != null) {
			try {
				int i;

				if(itemstack.isItemDamaged()) {
					i = getFirstEmptyStack();

					if(i >= 0) {
						inventory[i] = ItemStack.copyItemStack(itemstack);
						inventory[i].animationsToGo = 5;
						itemstack.stackSize = 0;
						return true;
					}
					else if(player.capabilities.isCreativeMode) {
						itemstack.stackSize = 0;
						return true;
					}
					else {
						return false;
					}
				}
				else {
					do {
						i = itemstack.stackSize;
						itemstack.stackSize = storePartialItemStack(itemstack);
					}
					while(itemstack.stackSize > 0 && itemstack.stackSize < i);

					if(itemstack.stackSize == i && player.capabilities.isCreativeMode) {
						itemstack.stackSize = 0;
						return true;
					}
					else {
						return itemstack.stackSize < i;
					}
				}
			}
			catch(final Throwable throwable) {
				final CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Adding item to inventory");
				final CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being added");
				crashreportcategory.addCrashSection("Item ID", Item.getIdFromItem(itemstack.getItem()));
				crashreportcategory.addCrashSection("Item data", itemstack.getItemDamage());
				crashreportcategory.addCrashSectionCallable("Item name", itemstack::getDisplayName);
				throw new ReportedException(crashreport);
			}
		}
		else {
			return false;
		}
	}

	public int getFirstEmptyStack() {
		for(int i = 0; i < inventory.length; ++i) {
			if(inventory[i] == null) {
				return i;
			}
		}

		return -1;
	}

	private int storePartialItemStack(final ItemStack itemstack) {
		final Item item = itemstack.getItem();
		int i = itemstack.stackSize;
		int j;

		if(itemstack.getMaxStackSize() == 1) {
			j = getFirstEmptyStack();

			if(j < 0) {
				return i;
			}
			else {
				if(inventory[j] == null) {
					inventory[j] = ItemStack.copyItemStack(itemstack);
				}

				return 0;
			}
		}
		else {
			j = storeItemStack(itemstack);

			if(j < 0) {
				j = getFirstEmptyStack();
			}

			if(j < 0) {
				return i;
			}
			else {
				if(inventory[j] == null) {
					inventory[j] = new ItemStack(item, 0, itemstack.getItemDamage());

					if(itemstack.hasTagCompound()) {
						inventory[j].setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
					}
				}

				int k = Math.min(i, inventory[j].getMaxStackSize() - inventory[j].stackSize);

				if(k > getInventoryStackLimit() - inventory[j].stackSize) {
					k = getInventoryStackLimit() - inventory[j].stackSize;
				}

				if(k != 0) {
					i -= k;
					inventory[j].stackSize += k;
					inventory[j].animationsToGo = 5;
				}

				return i;
			}
		}
	}

	private int storeItemStack(final ItemStack itemstack) {
		for(int i = 0; i < inventory.length; ++i) {
			if(inventory[i] != null && inventory[i].getItem() == itemstack.getItem() && inventory[i].isStackable() && inventory[i].stackSize < inventory[i].getMaxStackSize() && inventory[i].stackSize < getInventoryStackLimit() && (!inventory[i].getHasSubtypes() || inventory[i].getItemDamage() == itemstack.getItemDamage()) && ItemStack.areItemStackTagsEqual(inventory[i], itemstack)) {
				return i;
			}
		}

		return -1;
	}

	@Override
	public int getSizeInventory() {
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(final int slot) {
		return inventory[slot];
	}

	@Override
	public ItemStack decrStackSize(final int slot, final int amount) {
		ItemStack stack = getStackInSlot(slot);
		if(stack != null) {
			if(stack.stackSize > amount) {
				stack = stack.splitStack(amount);
				markDirty();
			}
			else {
				setInventorySlotContents(slot, null);
			}
		}
		return stack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(final int slot) {
		final ItemStack stack = getStackInSlot(slot);
		setInventorySlotContents(slot, null);
		return stack;
	}

	@Override
	public void setInventorySlotContents(final int slot, final ItemStack stack) {
		final ItemStack remote = player.getHeldItem() != null && player.getHeldItem().getItem() == TFItems.groundBridgeRemote ? player.getHeldItem() : remoteItem;
		inventory[slot] = stack;

		if(stack != null && stack.stackSize > getInventoryStackLimit()) {
			stack.stackSize = getInventoryStackLimit();
		}

		if(remote != null && stack != null && !player.worldObj.isRemote) {
			final DimensionalCoords tileCoords = ItemCSD.getCoords(remote);
			final DimensionalCoords coords = ItemCSD.getCoords(stack);
			final WorldServer targetWorld = MinecraftServer.getServer().worldServerForDimension(tileCoords.dimension);

			if(targetWorld != null) {
				final TileEntity tile = targetWorld.getTileEntity(tileCoords.posX, tileCoords.posY, tileCoords.posZ);
				final int metadata = targetWorld.getBlockMetadata(tileCoords.posX, tileCoords.posY, tileCoords.posZ);

				if(tile instanceof TileEntityControlPanel && BlockControlPanel.isBlockLeftSideOfPanel(metadata)) {
					final TileEntityControlPanel panel = (TileEntityControlPanel) tile;
					panel.setSwitchesTo(coords);
					panel.markBlockForUpdate();
				}
			}
		}

		markDirty();
	}

	@Override
	public String getInventoryName() {
		return "Ground Bridge Remote";
	}

	@Override
	public boolean hasCustomInventoryName() {
		return false;
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public void markDirty() {
		final ItemStack remote = player.getHeldItem() != null && player.getHeldItem().getItem() == TFItems.groundBridgeRemote ? player.getHeldItem() : remoteItem;

		if(remote != null) {
			final NBTTagCompound nbt = remote.getTagCompound();

			for(int i = 0; i < getSizeInventory(); i++) {
				if(inventory[i] != null) {
					nbt.setTag("Slot" + i, inventory[i].writeToNBT(nbt.getCompoundTag("Slot" + i)));
				}
				else if(nbt.hasKey("Slot" + i)) {
					nbt.removeTag("Slot" + i);
				}
			}
		}
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
	public boolean isItemValidForSlot(final int slot, final ItemStack stack) {
		final ItemStack remote = player.getHeldItem() != null && player.getHeldItem().getItem() == TFItems.groundBridgeRemote ? player.getHeldItem() : remoteItem;

		if(remote != null) {
			final DimensionalCoords tileCoords = ItemCSD.getCoords(remote);

			if(TFTileHelper.getTileData(tileCoords) instanceof TileDataControlPanel) {
				final TileDataControlPanel data = (TileDataControlPanel) TFTileHelper.getTileData(tileCoords);

				if(data.activationLeverState) {
					return false;
				}
			}
		}

		return stack.getItem() == TFItems.csd;
	}
}
