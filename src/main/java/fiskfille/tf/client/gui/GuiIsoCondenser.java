package fiskfille.tf.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.common.container.ContainerEmpty;
import fiskfille.tf.common.energon.IEnergon;
import fiskfille.tf.common.item.ItemCSD.DimensionalCoords;
import fiskfille.tf.common.network.MessageTileTrigger;
import fiskfille.tf.common.network.base.TFNetworkManager;
import fiskfille.tf.common.tileentity.TileEntityIsoCondenser;
import fiskfille.tf.helper.TFFormatHelper;
import fiskfille.tf.helper.TFRenderHelper;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SideOnly(Side.CLIENT)
public class GuiIsoCondenser extends GuiContainerTF {
	private final ResourceLocation texture = new ResourceLocation(TransformersMod.MODID, "textures/gui/container/isotopic_condenser.png");
	private final TileEntityIsoCondenser tileentity;

	private GuiHoverFieldEnergy fieldEnergy;

	public GuiIsoCondenser(final InventoryPlayer inventoryPlayer, final TileEntityIsoCondenser tile) {
		super(new ContainerEmpty(inventoryPlayer));
		tileentity = tile;
	}

	@Override
	public void initGui() {
		super.initGui();

		final int x = (width - xSize) / 2, y = (height - ySize) / 2;
		buttonList.add(fieldEnergy = new GuiHoverFieldEnergy(x + 17, y + 17, 16, 52, tileentity.data.storage));
		buttonList.add(new GuiButtonConfigSides(0, x + xSize - 18, y + 5));
		buttonList.add(new GuiButtonConfigRedstone(1, x + xSize - 18, y + 20, tileentity));
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		fieldEnergy.update(tileentity.data.storage);
	}

	@Override
	protected void actionPerformed(final GuiButton button) {
		switch(button.id) {
			case 0:
				mc.displayGuiScreen(new GuiConfigSides(mc.thePlayer.inventory, this, tileentity));
				TFNetworkManager.networkWrapper.sendToServer(new MessageTileTrigger(new DimensionalCoords(tileentity), mc.thePlayer, -tileentity.io.length - 1));
				break;

			case 1:
				TFNetworkManager.networkWrapper.sendToServer(new MessageTileTrigger(new DimensionalCoords(tileentity), mc.thePlayer, -tileentity.io.length - 2));
				break;
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
		final String s = I18n.format("gui.isotopic_condenser");
		fontRendererObj.drawString(s, xSize / 2 - fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
		fontRendererObj.drawString(I18n.format("container.inventory"), 8, ySize - 94, 4210752);

		final HashMap<Block, Integer> map = new HashMap<>();
		final ArrayList<Block> blocks = new ArrayList<>();

		for(final Map.Entry<ForgeDirection, Block> e : tileentity.providers.entrySet()) {
			if(!map.containsKey(e.getValue())) {
				map.put(e.getValue(), 1);
				blocks.add(e.getValue());
			}
			else {
				map.put(e.getValue(), map.get(e.getValue()) + 1);
			}
		}

		int totalMass = 0;
		TFRenderHelper.setupRenderItemIntoGUI();

		final int[][] aint = {{37, 35}, {37, 53}, {88, 35}, {88, 53}};
		for(int i = 0; i < Math.min(aint.length, blocks.size()); ++i) {
			final Block block = blocks.get(i);
			final int[] pos = aint[i];
			final int amount = map.get(block);

			TFRenderHelper.renderItemIntoGUI(pos[0], pos[1], new ItemStack(block, amount));
			totalMass += ((IEnergon) block).getMass() * amount;
		}

		TFRenderHelper.finishRenderItemIntoGUI();

		for(int i = 0; i < Math.min(aint.length, blocks.size()); ++i) {
			final Block block = blocks.get(i);
			final int[] pos = aint[i];
			final int percent = Math.round((float) ((IEnergon) block).getMass() * map.get(block) / totalMass * 100);

			drawString(fontRendererObj, I18n.format("gui.tf.percent", percent), pos[0] + 21, pos[1] + 4, -1);
		}

		fontRendererObj.drawString(I18n.format("gui.isotopic_condenser.generating", TFFormatHelper.formatNumberPrecise(tileentity.getGenerationRate(totalMass))), 37, 23, 0x707070);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(final float partialTicks, final int mouseX, final int mouseY) {
		mc.getTextureManager().bindTexture(texture);
		GL11.glColor3f(1, 1, 1);
		final int x = (width - xSize) / 2, y = (height - ySize) / 2;
		drawTexturedModalRect(x, y, 0, 0, xSize, ySize);

		if(tileentity.getEnergy() > 0) {
			final float f = tileentity.getEnergy() / tileentity.getMaxEnergy();
			drawTexturedModalRect(x + 17, y + 17 + Math.round(52 * (1 - f)), 176, Math.round(52 * (1 - f)), 16, Math.round(52 * f));
		}
	}
}
