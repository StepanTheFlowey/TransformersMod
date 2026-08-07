package fiskfille.tf.common.component;

import fiskfille.tf.common.tileentity.TileEntityDisplayStation;
import net.minecraft.entity.player.EntityPlayer;

public class Component {
	public boolean canLoad(final TileEntityDisplayStation tile, final int slot) {
		return tile.getStackInSlot(4 + slot) != null;
	}

	public void load(final TileEntityDisplayStation tile, final int slot, final EntityPlayer player) {}
}
