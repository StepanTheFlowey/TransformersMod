package fiskfille.tf.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.TransformersAPI;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.gui.GuiHandlerTF.TFGui;
import fiskfille.tf.common.container.ContainerDisplayStationArmor;
import fiskfille.tf.common.network.MessageOpenGui;
import fiskfille.tf.common.network.base.TFNetworkManager;
import fiskfille.tf.common.tileentity.TileEntityDisplayStation;
import fiskfille.tf.common.transformer.base.Transformer;
import fiskfille.tf.helper.TFArmorHelper;
import fiskfille.tf.helper.TFHelper;
import fiskfille.tf.helper.TFRenderHelper;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

@SideOnly(Side.CLIENT)
public class GuiDisplayStationArmor extends GuiContainer {
	private static final ResourceLocation guiTextures = new ResourceLocation(TransformersMod.MODID, "textures/gui/container/display_station_armor.png");
	private final TileEntityDisplayStation tileentity;

	public GuiDisplayStationArmor(InventoryPlayer inventoryPlayer, TileEntityDisplayStation tile) {
		super(new ContainerDisplayStationArmor(inventoryPlayer, tile));
		tileentity = tile;
		ySize = 186;
	}

	@Override
	protected void keyTyped(char c, int key) {
		if(key == 1 || key == mc.gameSettings.keyBindInventory.getKeyCode()) {
			TFNetworkManager.networkWrapper.sendToServer(new MessageOpenGui(mc.thePlayer, TFGui.DISPLAY_STATION.guiId, tileentity.xCoord, tileentity.yCoord, tileentity.zCoord));
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		final String s = I18n.format(tileentity.getInventoryName());
		fontRendererObj.drawString(s, xSize / 2 - fontRendererObj.getStringWidth(s) / 2, 6, 4210752);
		fontRendererObj.drawString(I18n.format("container.inventory"), 8, ySize - 94, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		mc.getTextureManager().bindTexture(guiTextures);
		GL11.glColor3f(1, 1, 1);
		drawTexturedModalRect((width - xSize) / 2, (height - ySize) / 2, 0, 0, xSize, ySize);

		final ContainerDisplayStationArmor container = (ContainerDisplayStationArmor) inventorySlots;
		final ArrayList<Transformer> list = new ArrayList<>();

		for(int i = 0; i < 4; ++i) {
			final ItemStack itemstack = tileentity.getStackInSlot(i);

			if(itemstack != null) {
				Transformer transformer = TFHelper.getTransformerFromArmor(itemstack);

				if(transformer != null && !list.contains(transformer)) {
					list.add(transformer);
				}

				final ItemStack itemstack1 = container.craftMatrix.getStackInSlot(i);
				if(itemstack1 != null) {
					int amount = TFArmorHelper.getArmorValue(mc.thePlayer, itemstack1, i) - TFArmorHelper.getArmorValue(mc.thePlayer, new ItemStack(itemstack.getItem()), i);
					String s = (amount < 0 ? EnumChatFormatting.DARK_RED : EnumChatFormatting.DARK_BLUE) + I18n.format(amount < 0 ? "gui.display_station.armor.neg" : "gui.display_station.armor", amount < 0 ? -amount : amount);

					fontRendererObj.drawString(s, (width - xSize) / 2 + 71, (height - ySize) / 2 + 22 + i * 18, -1);
				}
			}
		}

		Transformer transformer = TransformersAPI.getTransformers().get((mc.thePlayer.ticksExisted / 20) % TransformersAPI.getTransformers().size());

		if(list.size() == 1) {
			transformer = list.get(0);
		}

		if(transformer != null) {
			final boolean prevColor = itemRender.renderWithColor;

			TFRenderHelper.setupRenderItemIntoGUI();
			GL11.glColor4f(0.6F, 0.6F, 0.6F, 0.25F);
			itemRender.renderWithColor = false;

			final Item[] items = {transformer.getHelmet(), transformer.getChestplate(), transformer.getLeggings(), transformer.getBoots()};
			for(int i = 0; i < 4; ++i) {
				if(tileentity.getStackInSlot(i) == null) {
					itemRender.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), new ItemStack(items[i], 1, 0), (width - xSize) / 2 + 25, (height - ySize) / 2 + 18 + i * 18);
				}
			}

			itemRender.renderWithColor = prevColor;
			GL11.glColor3f(1F, 1F, 1F);
			TFRenderHelper.finishRenderItemIntoGUI();
		}
	}
}
