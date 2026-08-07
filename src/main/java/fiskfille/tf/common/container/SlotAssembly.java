package fiskfille.tf.common.container;

import cpw.mods.fml.common.FMLCommonHandler;
import fiskfille.tf.common.recipe.AssemblyTableCraftingManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.*;
import net.minecraft.stats.AchievementList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;

import java.util.Map;

public class SlotAssembly extends Slot {
	private final InventoryAssembly craftMatrix;
	private final EntityPlayer thePlayer;
	private int amountCrafted;

	public SlotAssembly(final EntityPlayer player, final InventoryAssembly inventory1, final IInventory inventory2, final int id, final int x, final int y) {
		super(inventory2, id, x, y);
		thePlayer = player;
		craftMatrix = inventory1;
	}

	@Override
	public boolean isItemValid(final ItemStack itemstack) {
		return false;
	}

	@Override
	public ItemStack decrStackSize(final int amount) {
		if(getHasStack()) {
			amountCrafted += Math.min(amount, getStack().stackSize);
		}

		return super.decrStackSize(amount);
	}

	@Override
	protected void onCrafting(final ItemStack itemstack, final int amount) {
		amountCrafted += amount;
		onCrafting(itemstack);
	}

	@Override
	protected void onCrafting(final ItemStack itemstack) {
		itemstack.onCrafting(thePlayer.worldObj, thePlayer, amountCrafted);
		amountCrafted = 0;

		if(itemstack.getItem() == Item.getItemFromBlock(Blocks.crafting_table)) {
			thePlayer.addStat(AchievementList.buildWorkBench, 1);
		}

		if(itemstack.getItem() instanceof ItemPickaxe) {
			thePlayer.addStat(AchievementList.buildPickaxe, 1);
		}

		if(itemstack.getItem() == Item.getItemFromBlock(Blocks.furnace)) {
			thePlayer.addStat(AchievementList.buildFurnace, 1);
		}

		if(itemstack.getItem() instanceof ItemHoe) {
			thePlayer.addStat(AchievementList.buildHoe, 1);
		}

		if(itemstack.getItem() == Items.bread) {
			thePlayer.addStat(AchievementList.makeBread, 1);
		}

		if(itemstack.getItem() == Items.cake) {
			thePlayer.addStat(AchievementList.bakeCake, 1);
		}

		if(itemstack.getItem() instanceof ItemPickaxe && ((ItemPickaxe) itemstack.getItem()).func_150913_i() != Item.ToolMaterial.WOOD) {
			thePlayer.addStat(AchievementList.buildBetterPickaxe, 1);
		}

		if(itemstack.getItem() instanceof ItemSword) {
			thePlayer.addStat(AchievementList.buildSword, 1);
		}

		if(itemstack.getItem() == Item.getItemFromBlock(Blocks.enchanting_table)) {
			thePlayer.addStat(AchievementList.enchantments, 1);
		}

		if(itemstack.getItem() == Item.getItemFromBlock(Blocks.bookshelf)) {
			thePlayer.addStat(AchievementList.bookcase, 1);
		}
	}

	@Override
	public void onPickupFromSlot(final EntityPlayer player, final ItemStack itemstack) {
		FMLCommonHandler.instance().firePlayerCraftingEvent(player, itemstack, craftMatrix);
		onCrafting(itemstack);

		for(int i = 0; i < craftMatrix.getSizeInventory(); ++i) {
			final ItemStack itemstack1 = craftMatrix.getStackInSlot(i);

			if(itemstack1 != null) {
				final Map<Integer, Integer> map = AssemblyTableCraftingManager.getInstance().getDecrMap(itemstack);
				craftMatrix.decrStackSize(i, map.get(i) == null ? 0 : map.get(i));

				if(itemstack1.getItem().hasContainerItem(itemstack1)) {
					final ItemStack itemstack2 = itemstack1.getItem().getContainerItem(itemstack1);

					if(itemstack2 != null && itemstack2.isItemStackDamageable() && itemstack2.getItemDamage() > itemstack2.getMaxDamage()) {
						MinecraftForge.EVENT_BUS.post(new PlayerDestroyItemEvent(thePlayer, itemstack2));
						continue;
					}

					if(!itemstack1.getItem().doesContainerItemLeaveCraftingGrid(itemstack1) || !thePlayer.inventory.addItemStackToInventory(itemstack2)) {
						if(craftMatrix.getStackInSlot(i) == null) {
							craftMatrix.setInventorySlotContents(i, itemstack2);
						}
						else {
							thePlayer.dropPlayerItemWithRandomChoice(itemstack2, false);
						}
					}
				}
			}
		}
	}
}
