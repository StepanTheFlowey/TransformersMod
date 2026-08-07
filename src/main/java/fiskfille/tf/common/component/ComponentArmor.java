package fiskfille.tf.common.component;

import fiskfille.tf.client.gui.GuiHandlerTF.TFGui;
import fiskfille.tf.common.network.MessageOpenGui;
import fiskfille.tf.common.network.base.TFNetworkManager;
import fiskfille.tf.common.tileentity.TileEntityDisplayStation;
import net.minecraft.entity.player.EntityPlayer;

public class ComponentArmor extends Component {
	@Override
	public void load(final TileEntityDisplayStation tile, final int slot, final EntityPlayer player) {
		TFNetworkManager.networkWrapper.sendToServer(new MessageOpenGui(player, TFGui.DISPLAY_STATION_ARMOR.guiId, tile.xCoord, tile.yCoord, tile.zCoord));
	}
}
