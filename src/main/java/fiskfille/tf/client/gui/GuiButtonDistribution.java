package fiskfille.tf.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.common.tileentity.TileEntityMachine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@SideOnly(Side.CLIENT)
public class GuiButtonDistribution extends GuiButtonFlat {
	public final TileEntityMachine machine;

	public GuiButtonDistribution(final int id, final int x, final int y, final TileEntityMachine tile) {
		super(id, x, y, 13, "");
		machine = tile;
	}

	@Override
	public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
		switch(machine.distribution) {
			case QUEUED:
				displayString = "=";
				break;

			case SPREAD:
				displayString = "/";
				break;
		}

		super.drawButton(mc, mouseX, mouseY);
	}

	@Override
	public List<String> getHoverText() {
		return Arrays.asList(I18n.format("gui.tf.distribution"), EnumChatFormatting.GRAY + I18n.format("gui.tf.distribution." + machine.distribution.name().toLowerCase(Locale.ENGLISH)));
	}
}
