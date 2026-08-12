package fiskfille.tf.waila;

import fiskfille.tf.common.groundbridge.DataCore;
import fiskfille.tf.common.tileentity.TileEntityControlPanel;
import fiskfille.tf.helper.TFTileHelper;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class DataProviderControlPanel extends DataProviderMachine {
	public DataProviderControlPanel(final String s) {
		super(s, TileEntityControlPanel.class);
	}

	@Override
	public List<String> getWailaBody(final ItemStack itemstack, List<String> list, final IWailaDataAccessor accessor, final IWailaConfigHandler config) {
		list = super.getWailaBody(itemstack, list, accessor, config);
		final TileEntity tileentity = TFTileHelper.getTileBase(accessor.getTileEntity());

		if(tileentity instanceof TileEntityControlPanel && config.getConfig(key, true)) {
			final LinkedHashMap<DataCore, Integer> map = new LinkedHashMap<>();

			for(final DataCore dataCore : ((TileEntityControlPanel) tileentity).getUpgrades()) {
				map.put(dataCore, map.containsKey(dataCore) ? map.get(dataCore) + 1 : 1);
			}

			for(final Map.Entry<DataCore, Integer> e : map.entrySet()) {
				String s = e.getKey().getTranslatedName();

				if(e.getValue() > 1) {
					s = String.format("%s %s", s, I18n.format("tile.display_pillar.amount", e.getValue()));
				}

				list.add(s);
			}
		}

		return list;
	}
}
