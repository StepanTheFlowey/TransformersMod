package fiskfille.tf.common.component;

import fiskfille.tf.common.tileentity.TileEntityDisplayStation;
import net.minecraft.entity.player.EntityPlayer;

public class Component {
	public boolean canLoad(TileEntityDisplayStation tile, int slot) {
		return tile.getStackInSlot(4 + slot) != null;
	}

	public void load(TileEntityDisplayStation tile, int slot, EntityPlayer player) {

	}
}
