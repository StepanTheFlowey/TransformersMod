package fiskfille.tf.waila;

import com.google.common.collect.Maps;
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

public class DataProviderControlPanel extends DataProviderMachine {
	public DataProviderControlPanel(String s) {
		super(s, TileEntityControlPanel.class);
	}

	@Override
	public List<String> getWailaBody(ItemStack itemstack, List<String> list, IWailaDataAccessor accessor, IWailaConfigHandler config) {
		list = super.getWailaBody(itemstack, list, accessor, config);
		TileEntity tileentity = TFTileHelper.getTileBase(accessor.getTileEntity());

		if(tileentity instanceof TileEntityControlPanel && config.getConfig(key, true)) {
			TileEntityControlPanel tile = (TileEntityControlPanel) tileentity;
			List<DataCore> upgrades = tile.getUpgrades();
			LinkedHashMap<DataCore, Integer> map = Maps.newLinkedHashMap();

			for(DataCore dataCore : upgrades) {
				map.put(dataCore, map.containsKey(dataCore) ? map.get(dataCore) + 1 : 1);
			}

			for(Map.Entry<DataCore, Integer> e : map.entrySet()) {
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
