package fiskfille.tf.common.container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.common.item.armor.ItemTransformerArmor;
import fiskfille.tf.common.tileentity.TileEntityDisplayStation;
import fiskfille.tf.helper.TFArmorHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class SlotDisplayStationArmor extends Slot {
	private final ContainerDisplayStationArmor parent;
	private final TileEntityDisplayStation tile;
	private final boolean type;

	public SlotDisplayStationArmor(final ContainerDisplayStationArmor parent, final IInventory inventory, final TileEntityDisplayStation tile, final int id, final int x, final int y) {
		super(inventory, id, x, y);
		this.parent = parent;
		this.tile = tile;
		this.type = tile == inventory;
	}

	@Override
	public int getSlotStackLimit() {
		return 1;
	}

	@Override
	public boolean isItemValid(final ItemStack itemstack) {
		final boolean flag = itemstack.getItem() instanceof ItemTransformerArmor;
		return type ? flag : !flag && tile.getStackInSlot(slotNumber % 4) != null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getBackgroundIconIndex() {
		return !type && tile.getStackInSlot(slotNumber % 4) != null ? ItemArmor.func_94602_b(slotNumber % 4) : null;
	}

	@Override
	public void onPickupFromSlot(final EntityPlayer player, final ItemStack itemstack) {
		super.onPickupFromSlot(player, itemstack);

		final int piece = slotNumber % 4;
		final ItemStack transformer = tile.getStackInSlot(piece);
		parent.craftMatrix.getStackInSlot(piece);

		if(type) {
			parent.craftMatrix.setInventorySlotContents(piece, null);
		}
		else if(transformer != null) {
			TFArmorHelper.setArmorShell(transformer, null);
		}
	}

	@Override
	public void putStack(final ItemStack itemstack) {
		super.putStack(itemstack);

		final int piece = slotNumber % 4;
		final ItemStack transformer = tile.getStackInSlot(piece);
		if(transformer != null) {
			if(type) {
				parent.craftMatrix.setInventorySlotContents(piece, TFArmorHelper.getArmorShell(transformer));
			}
			else {
				TFArmorHelper.setArmorShell(transformer, parent.craftMatrix.getStackInSlot(piece));
			}
		}
	}
}
