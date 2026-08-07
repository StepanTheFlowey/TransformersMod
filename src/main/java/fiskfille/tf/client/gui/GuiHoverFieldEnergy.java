package fiskfille.tf.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.common.energon.power.EnergyStorage;
import fiskfille.tf.helper.TFFormatHelper;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiHoverFieldEnergy extends GuiHoverField {
	private EnergyStorage energyStorage;

	public GuiHoverFieldEnergy(final int x, final int y, final int width, final int height, final EnergyStorage storage) {
		super(x, y, width, height, new ArrayList<>());
		energyStorage = storage;
	}

	public void update(final EnergyStorage storage) {
		energyStorage = storage;
	}

	@Override
	public List<String> getHoverText() {
		return TFFormatHelper.toString(energyStorage.format());
	}
}
