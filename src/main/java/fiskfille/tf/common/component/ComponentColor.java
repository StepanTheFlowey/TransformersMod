package fiskfille.tf.common.component;

import fiskfille.tf.client.gui.GuiHandlerTF.TFGui;
import fiskfille.tf.common.tileentity.TileEntityDisplayStation;
import fiskfille.tf.common.transformer.base.Transformer;
import fiskfille.tf.helper.TFHelper;
import net.minecraft.entity.player.EntityPlayer;

public class ComponentColor extends Component {
	@Override
	public boolean canLoad(TileEntityDisplayStation tile, int slot) {
		final Transformer helmetTransformer = TFHelper.getTransformerFromArmor(tile.getStackInSlot(0));
		final Transformer chestTransformer = TFHelper.getTransformerFromArmor(tile.getStackInSlot(1));
		final Transformer legsTransformer = TFHelper.getTransformerFromArmor(tile.getStackInSlot(2));
		final Transformer feetTransformer = TFHelper.getTransformerFromArmor(tile.getStackInSlot(3));

		return helmetTransformer != null && helmetTransformer == chestTransformer && chestTransformer == legsTransformer && legsTransformer == feetTransformer && super.canLoad(tile, slot);
	}

	@Override
	public void load(TileEntityDisplayStation tile, int slot, EntityPlayer player) {
		TFGui.DISPLAY_STATION_COLOR.open(player, tile);
	}
}
