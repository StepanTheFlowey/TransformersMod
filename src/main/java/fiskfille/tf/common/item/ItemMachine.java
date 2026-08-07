package fiskfille.tf.common.item;

import fiskfille.tf.common.energon.power.IEnergyTransmitter;
import fiskfille.tf.helper.TFFormatHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

import java.util.List;

public class ItemMachine extends ItemBlock {
	public ItemMachine(final Block block) {
		super(block);
	}

	@Override
	public String getItemStackDisplayName(final ItemStack itemstack) {
		String s = super.getItemStackDisplayName(itemstack);

		if(itemstack.hasTagCompound() && itemstack.getTagCompound().hasKey("ConfigDataTF", NBT.TAG_COMPOUND)) {
			s = StatCollector.translateToLocalFormatted("gui.emb.item.configured", s + EnumChatFormatting.ITALIC);
		}

		return s;
	}

	@Override
	public void addInformation(final ItemStack itemstack, final EntityPlayer player, final List list, final boolean flag) {
		final TileEntity tile = getTileEntity(player.worldObj);

		if(tile instanceof IEnergyTransmitter) {
			final IEnergyTransmitter transmitter = (IEnergyTransmitter) tile;
			list.add(StatCollector.translateToLocalFormatted("gui.emb.rate", TFFormatHelper.formatNumber(transmitter.getTransmissionRate())));
		}
	}

	public TileEntity getTileEntity(final World world) {
		if(field_150939_a.hasTileEntity(0)) {
			return field_150939_a.createTileEntity(world, 0);
		}

		return null;
	}
}
