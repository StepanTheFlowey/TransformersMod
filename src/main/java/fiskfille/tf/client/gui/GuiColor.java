package fiskfille.tf.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.common.network.MessageColorArmor;
import fiskfille.tf.common.network.base.TFNetworkManager;
import fiskfille.tf.common.proxy.ClientProxy;
import fiskfille.tf.common.tileentity.TileEntityDisplayStation;
import fiskfille.tf.helper.TFArmorDyeHelper;
import fiskfille.tf.helper.TFRenderHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class GuiColor extends GuiScreen {
	public static final float[][] layerColors = {{1, 1, 1}, {1, 1, 1}};
	public static int ticks;
	public static boolean fromPresetMenu = false;
	public static int layerSelected;
	public static GuiColorSlider sliderRed;
	public static GuiColorSlider sliderGreen;
	public static GuiColorSlider sliderBlue;
	public static GuiTextField inputField;
	private final TileEntityDisplayStation tileentity;

	public GuiColor(TileEntityDisplayStation tile) {
		tileentity = tile;
	}

	@Override
	public void initGui() {
		super.initGui();
		buttonList.add(new GuiButton(0, width / 2 - 100, height / 6 + 168, I18n.format("gui.done")));
		buttonList.add(sliderRed = new GuiColorSlider(1, width / 2 - 22, height / 6, 0, I18n.format("gui.display_station.color.red")));
		buttonList.add(sliderGreen = new GuiColorSlider(2, width / 2 - 22, height / 6 + 21, 1, I18n.format("gui.display_station.color.green")));
		buttonList.add(sliderBlue = new GuiColorSlider(3, width / 2 - 22, height / 6 + 42, 2, I18n.format("gui.display_station.color.blue")));
		buttonList.add(new GuiButton(4, width / 2 + 29, height / 6 + 63, 78, 20, I18n.format("gui.display_station.color.presets")));
		buttonList.add(new GuiButtonSwapColors(5, width / 2 + 108, height / 6 + 63));
		buttonList.add(new GuiButtonAlt(6, width / 2 + 112, height / 6 + 84, 16, 16, "X"));

		final ItemStack head = tileentity.getStackInSlot(0).copy();
		if(TFArmorDyeHelper.isDyed(head) && !fromPresetMenu) {
			Color primary = new Color(TFArmorDyeHelper.getPrimaryColor(head));
			Color secondary = new Color(TFArmorDyeHelper.getSecondaryColor(head));

			layerColors[0][0] = (float) primary.getRed() / 255;
			layerColors[0][1] = (float) primary.getGreen() / 255;
			layerColors[0][2] = (float) primary.getBlue() / 255;
			layerColors[1][0] = (float) secondary.getRed() / 255;
			layerColors[1][1] = (float) secondary.getGreen() / 255;
			layerColors[1][2] = (float) secondary.getBlue() / 255;
		}

		fromPresetMenu = false;
		sliderRed.percentage = layerColors[layerSelected][0];
		sliderGreen.percentage = layerColors[layerSelected][1];
		sliderBlue.percentage = layerColors[layerSelected][2];

		Keyboard.enableRepeatEvents(true);
		inputField = new GuiTextField(fontRendererObj, width / 2 - 21, height / 6 + 64, 48, 17);
		inputField.setMaxStringLength(20);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		inputField.updateCursorCounter();
		++ticks;

		layerColors[layerSelected][0] = sliderRed.percentage;
		layerColors[layerSelected][1] = sliderGreen.percentage;
		layerColors[layerSelected][2] = sliderBlue.percentage;

		if(!inputField.isFocused()) {
			final Color color = new Color(sliderRed.percentage, sliderGreen.percentage, sliderBlue.percentage);
			inputField.setText(Integer.toHexString(color.getRGB()).substring(2).toUpperCase());
		}
		else {
			try {
				final Color color = new Color(Integer.parseUnsignedInt(inputField.getText(), 16));
				sliderRed.percentage = (float) color.getRed() / 255F;
				sliderGreen.percentage = (float) color.getGreen() / 255F;
				sliderBlue.percentage = (float) color.getBlue() / 255F;
			}
			catch(Exception e) {
			}
		}
	}

	@Override
	protected void keyTyped(char c, int key) {
		if(key == 1) {
			mc.displayGuiScreen(null);
		}
		else {
			inputField.textboxKeyTyped(Character.toUpperCase(c), Character.toUpperCase(key));
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) {
		final int id = button.id;

		if(id == 0) {
			final Color primary = new Color(layerColors[0][0], layerColors[0][1], layerColors[0][2]);
			final Color secondary = new Color(layerColors[1][0], layerColors[1][1], layerColors[1][2]);
			TFNetworkManager.networkWrapper.sendToServer(new MessageColorArmor(tileentity.xCoord, tileentity.yCoord, tileentity.zCoord, primary.getRGB(), secondary.getRGB()));
			TFNetworkManager.networkWrapper.sendToAll(new MessageColorArmor(tileentity.xCoord, tileentity.yCoord, tileentity.zCoord, primary.getRGB(), secondary.getRGB()));

			mc.displayGuiScreen(null);
		}
		else if(id == 4) {
			mc.displayGuiScreen(new GuiColorPresets(tileentity, this));
		}
		else if(id == 5) {
			float[] afloat = layerColors[0];
			layerColors[0] = layerColors[1];
			layerColors[1] = afloat;
			sliderRed.percentage = layerColors[layerSelected][0];
			sliderGreen.percentage = layerColors[layerSelected][1];
			sliderBlue.percentage = layerColors[layerSelected][2];
		}
		else if(id == 6) {
			int i = Integer.MIN_VALUE;
			TFNetworkManager.networkWrapper.sendToServer(new MessageColorArmor(tileentity.xCoord, tileentity.yCoord, tileentity.zCoord, i, i));

			mc.displayGuiScreen(null);
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int button) {
		super.mouseClicked(mouseX, mouseY, button);

		if(button == 0) {
			for(int i = 0; i < 2; ++i) {
				int x = width / 2 - 21 + 67 * i;
				int y = height / 6 + 85;

				if(mouseX >= x && mouseX < x + 64 && mouseY >= y && mouseY < y + 64) {
					layerSelected = i;
					sliderRed.percentage = layerColors[layerSelected][0];
					sliderGreen.percentage = layerColors[layerSelected][1];
					sliderBlue.percentage = layerColors[layerSelected][2];
				}
			}
		}

		inputField.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int button, long timeSinceMouseClick) {
		super.mouseClickMove(mouseX, mouseY, button, timeSinceMouseClick);
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		inputField.drawTextBox();
		drawCenteredString(fontRendererObj, I18n.format("gui.display_station.color"), width / 2, 15, 16777215);

		GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glColor4f(0F, 0F, 0F, 0.4F);
		drawTexturedModalRect(width / 2 - 128, height / 6, 0, 0, 100, 150);
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		EntityPlayer entity = ClientProxy.fakePlayer;

		if(entity != null) {
			Color primary = new Color(layerColors[0][0], layerColors[0][1], layerColors[0][2]);
			Color secondary = new Color(layerColors[1][0], layerColors[1][1], layerColors[1][2]);

			for(int i = 0; i < 4; ++i) {
				ItemStack armor = tileentity.getStackInSlot(i);

				if(armor != null) {
					armor = armor.copy();
					TFArmorDyeHelper.setPrimaryColor(armor, primary.getRGB());
					TFArmorDyeHelper.setSecondaryColor(armor, secondary.getRGB());
				}

				entity.setCurrentItemOrArmor(4 - i, armor);
			}

			entity.capabilities.isFlying = true;
			entity.rotationYawHead = 0;
			entity.setInvisible(true);

			int k = width / 2 - 128 + 50;
			int l = height / 6 + 132;
			GL11.glEnable(GL11.GL_COLOR_MATERIAL);
			GL11.glPushMatrix();
			GL11.glTranslatef(k, l, 50F);
			GL11.glScalef(-60, 60, 60);
			GL11.glRotatef(180F, 0F, 0F, 1F);
			GL11.glRotatef(135F, 0F, 1F, 0F);
			RenderHelper.enableStandardItemLighting();
			GL11.glRotatef(-135F, 0F, 1F, 0F);
			GL11.glTranslatef(0F, entity.yOffset, 10F);
			GL11.glRotatef((ticks + partialTicks) / 2, 0F, 1F, 0F);
			RenderManager.instance.playerViewY = 180F;
			TFRenderHelper.startGlScissor(width / 2 - 128, height / 6, 100, 150);
			RenderManager.instance.renderEntityWithPosYaw(entity, 0D, 0D, 0D, 0F, 1F);
			TFRenderHelper.endGlScissor();
			GL11.glPopMatrix();
			RenderHelper.disableStandardItemLighting();
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
			OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
		}

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glColor3f(0F, 0F, 0F);
		drawTexturedModalRect(width / 2 - 22, height / 6 + 84, 0, 0, 66, 66);
		drawTexturedModalRect(width / 2 - 22 + 67, height / 6 + 84, 0, 0, 66, 66);

		float opacityMax = 20;
		float opacity = (ticks + partialTicks) % opacityMax;
		opacity = opacity > opacityMax / 2 ? opacityMax / 2 - (opacity - opacityMax / 2) : opacity;
		GL11.glColor4f(1, 1, 0, opacity / opacityMax + 0.1F);
		drawTexturedModalRect(width / 2 - 23 + 67 * layerSelected, height / 6 + 83, 0, 0, 68, 68);

		GL11.glColor3f(layerColors[0][0], layerColors[0][1], layerColors[0][2]);
		drawTexturedModalRect(width / 2 - 21, height / 6 + 85, 0, 0, 64, 64);

		GL11.glColor3f(layerColors[1][0], layerColors[1][1], layerColors[1][2]);
		drawTexturedModalRect(width / 2 - 21 + 67, height / 6 + 85, 0, 0, 64, 64);

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		super.drawScreen(mouseX, mouseY, partialTicks);
		GL11.glPopAttrib();
	}
}
