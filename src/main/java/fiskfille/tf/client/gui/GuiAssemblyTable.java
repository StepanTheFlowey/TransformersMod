package fiskfille.tf.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.common.container.ContainerAssemblyTable;
import fiskfille.tf.common.tileentity.TileEntityAssemblyTable;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiAssemblyTable extends GuiContainerTF {
	private final ResourceLocation texture = new ResourceLocation(TransformersMod.MODID, "textures/gui/container/assembly_table.png");

	public GuiAssemblyTable(final InventoryPlayer inventoryPlayer, final TileEntityAssemblyTable tile) {
		super(new ContainerAssemblyTable(inventoryPlayer, tile));
		ySize = 222;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
		final String s = I18n.format("gui.assembly_table");
		fontRendererObj.drawString(s, xSize / 2 - fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
		fontRendererObj.drawString(I18n.format("container.inventory"), 8, ySize - 96 + 2, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
		GL11.glColor3f(1, 1, 1);
		mc.getTextureManager().bindTexture(texture);
		drawTexturedModalRect((width - xSize) / 2, (height - ySize) / 2, 0, 0, xSize, ySize);
	}
}
