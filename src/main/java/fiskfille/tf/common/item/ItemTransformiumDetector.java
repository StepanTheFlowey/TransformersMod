package fiskfille.tf.common.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.common.block.TFBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemTransformiumDetector extends Item {
	// TODO-TF improve it for 0.6.0

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean hand) {
		int time = entity.ticksExisted;

		if(time > 0) {
			NBTTagCompound tagCompound = stack.getTagCompound();

			if(tagCompound == null) {
				tagCompound = new NBTTagCompound();
				stack.setTagCompound(tagCompound);
			}

			int energonFuel = tagCompound.getInteger("fuel");

			if(entity instanceof EntityPlayer) {
				final EntityPlayer player = (EntityPlayer) entity;

				if(energonFuel > 0) {
					if(time % 500 == 0) {
						--energonFuel;
						tagCompound.setInteger("fuel", energonFuel);
						if(energonFuel <= 0) {
							player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("transformium_detector.no_fuel.message")));
						}
					}

					if(time % 5 == 0) {
						final int posX = (int) entity.posX;
						final int posY = (int) entity.posY;
						final int posZ = (int) entity.posZ;
						final int startX = posX - 10;
						final int startY = posY - 10;
						final int startZ = posZ - 10;

						final int endX = posX + 10;
						final int endY = posY + 10;
						final int endZ = posZ + 10;

						int smallestDist = 1000;

						for(int x = startX; x < endX; x++) {
							for(int y = startY; y < endY; y++) {
								for(int z = startZ; z < endZ; z++) {
									if(world.getBlock(x, y, z) == TFBlocks.transformiumOre) {
										final int xDiff = x - posX;
										final int yDiff = y - posY;
										final int zDiff = z - posZ;

										final int distance = (int) Math.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);

										if(distance < smallestDist) {
											smallestDist = distance;
										}
									}
								}
							}
						}

						if(smallestDist != 1000) {
							tagCompound.setInteger("d", smallestDist);
						}
						else {
							tagCompound.setInteger("d", -1);
						}
					}

					if(player.worldObj.isRemote) {
						if(Minecraft.getMinecraft().thePlayer == player) {
							int d = tagCompound.getInteger("d");

							if(d > 0) {
								if(time % (d * 3) == 0) {
									for(int i = 0; i < 3; ++i) {
										entity.playSound("note.harp", 1, 1.2F);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if(stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
		}

		final int fuel = stack.getTagCompound().getInteger("fuel");

		if(player.isSneaking()) {
			if(player.inventory.getFirstEmptyStack() != -1) {
				player.inventory.addItemStackToInventory(new ItemStack(TFItems.energonCrystalShard, fuel));
				stack.getTagCompound().setInteger("fuel", 0);
			}
			else {
				if(world.isRemote) {
					player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("transformium_detector.no_space.message")));
				}
			}
		}
		else {
			if(player.capabilities.isCreativeMode || player.inventory.consumeInventoryItem(TFItems.energonCrystalShard)) {
				stack.getTagCompound().setInteger("fuel", fuel + 1);
				if(world.isRemote) {
					player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("transformium_detector.insert_fuel.message")));
				}
			}
			else {
				if(world.isRemote) {
					player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal("transformium_detector.no_energon.message")));
				}
			}
		}

		return stack;
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer player, List informationList, boolean p_77624_4_) {
		final NBTTagCompound tagCompound = stack.getTagCompound();

		if(tagCompound != null) {
			informationList.add(StatCollector.translateToLocal("stats.fuel.name") + ": " + tagCompound.getInteger("fuel"));
		}
	}
}
