package fiskfille.tf.common.item;

import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.gui.GuiHandlerTF.TFGui;
import fiskfille.tf.common.block.BlockControlPanel;
import fiskfille.tf.common.data.tile.TileDataControlPanel;
import fiskfille.tf.common.tileentity.TileEntityControlPanel;
import fiskfille.tf.helper.TFTileHelper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.List;

public class ItemGroundBridgeRemote extends Item {
	public IIcon[] icons;

	public ItemGroundBridgeRemote() {
		setMaxStackSize(1);
	}

	@Override
	public void addInformation(final ItemStack itemstack, final EntityPlayer player, final List list, final boolean flag) {
		final ItemCSD.DimensionalCoords coords = ItemCSD.getCoords(itemstack);
		list.add(coords.getFormatted().getFormattedText());
	}

	@Override
	public void onUpdate(final ItemStack itemstack, final World world, final Entity entity, final int usingTick, final boolean holding) {
		final ItemCSD.DimensionalCoords coords = ItemCSD.getCoords(itemstack);

		if(TFTileHelper.getTileData(coords) instanceof TileDataControlPanel) {
			itemstack.setItemDamage(1);
		}
		else {
			itemstack.setItemDamage(0);
		}
	}

	@Override
	public ItemStack onItemRightClick(final ItemStack stack, final World world, final EntityPlayer player) {
		if(!world.isRemote) {
			final ItemCSD.DimensionalCoords coords = ItemCSD.getCoords(stack);
			final WorldServer targetWorld = MinecraftServer.getServer().worldServerForDimension(coords.dimension);

			if(!player.isSneaking()) {
				if(targetWorld != null) {
					final TileEntity tile = targetWorld.getTileEntity(coords.posX, coords.posY, coords.posZ);
					final int metadata = targetWorld.getBlockMetadata(coords.posX, coords.posY, coords.posZ);

					if(tile instanceof TileEntityControlPanel && BlockControlPanel.isBlockLeftSideOfPanel(metadata)) {
						player.openGui(TransformersMod.instance, coords.dimension << 8 | TFGui.GROUND_BRIDGE_REMOTE.guiId, targetWorld, coords.posX, coords.posY, coords.posZ);
						player.addChatComponentMessage(new ChatComponentTranslation("ground_bridge_remote.connect", coords.getFormatted()));

						return stack;
					}
				}

				player.addChatComponentMessage(new ChatComponentTranslation("ground_bridge_remote.connect.fail", coords.getFormatted().getUnformattedText()).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
			}
		}

		return stack;
	}

	@Override
	public boolean onItemUse(final ItemStack itemstack, final EntityPlayer player, final World world, final int x, final int y, final int z, final int side, final float hitX, final float hitY, final float hitZ) {
		if(player.isSneaking()) {
			final TileEntity tile = TFTileHelper.getTileBase(world.getTileEntity(x, y, z));

			if(tile instanceof TileEntityControlPanel && BlockControlPanel.isBlockLeftSideOfPanel(tile.getBlockMetadata())) {
				final ItemCSD.DimensionalCoords coords = new ItemCSD.DimensionalCoords(tile.xCoord, tile.yCoord, tile.zCoord, world.provider.dimensionId);
				ItemCSD.setCoords(itemstack, coords);

				if(world.isRemote) {
					player.addChatComponentMessage(new ChatComponentTranslation("ground_bridge_remote.connect.add", coords.getFormatted()));
				}

				return true;
			}
		}

		return false;
	}

	@Override
	public IIcon getIconFromDamage(final int damage) {
		return icons[Math.min(damage, 1)];
	}

	@Override
	public void registerIcons(final IIconRegister iconRegister) {
		icons = new IIcon[]{
			iconRegister.registerIcon(getIconString() + "_off"),
			iconRegister.registerIcon(getIconString() + "_on")
		};
	}
}
