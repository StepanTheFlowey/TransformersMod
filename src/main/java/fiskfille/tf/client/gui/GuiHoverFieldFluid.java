package fiskfille.tf.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.common.fluid.FluidTankTF;
import fiskfille.tf.helper.TFFormatHelper;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiHoverFieldFluid extends GuiHoverField {
	private FluidTankTF fluidTank;

	public GuiHoverFieldFluid(final int x, final int y, final int width, final int height, final FluidTankTF tank) {
		super(x, y, width, height, new ArrayList<>());
		fluidTank = tank;
	}

	public void update(final FluidTankTF tank) {
		fluidTank = tank;
	}

	@Override
	public List<String> getHoverText() {
		return TFFormatHelper.toString(fluidTank.format());
	}
}
