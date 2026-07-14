package fiskfille.tf.client.gui;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.common.data.TFData;
import fiskfille.tf.common.data.TFDataManager;
import fiskfille.tf.common.item.ItemVurpsSniper;
import fiskfille.tf.common.item.TFItems;
import fiskfille.tf.common.transformer.TransformerPurge;
import fiskfille.tf.common.transformer.TransformerVurp;
import fiskfille.tf.common.transformer.base.Transformer;
import fiskfille.tf.config.TFConfig;
import fiskfille.tf.helper.TFHelper;
import fiskfille.tf.helper.TFShootManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public class GuiOverlay extends Gui {
	public static final ResourceLocation texture = new ResourceLocation(TransformersMod.modid, "textures/gui/mod_icons.png");
	private static final Minecraft mc = Minecraft.getMinecraft();
	public static double prevSpeed;
	public static double speed;
	private final RenderItem itemRenderer = new RenderItem();

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onRender(RenderGameOverlayEvent.Pre event) {
		if(event.isCanceled()) {
			return;
		}

		final EntityPlayer player = mc.thePlayer;
		final int width = event.resolution.getScaledWidth();
		final int height = event.resolution.getScaledHeight();

		if(event.type == ElementType.HOTBAR) {
			final Transformer transformer = TFHelper.getTransformer(player);
			final boolean flag = transformer == null || transformer.renderSpeedAndNitro(player, TFData.ALT_MODE.get(player));

			if(flag) {
				renderNitroAndSpeed(event, width, height, player);
			}

			renderKatanaDash(event, width, height, player);
			renderShotsLeft(event, width, height, player);
			renderLaserCharge(event, width, height, player);
		}
	}

	public void renderLaserCharge(RenderGameOverlayEvent.Pre event, int width, int height, EntityPlayer player) {
		int altMode = TFData.ALT_MODE.get(player);

		ItemStack heldItem = player.getHeldItem();
		Transformer transformer = TFHelper.getTransformer(player);
		boolean hasSniper = heldItem != null && heldItem.getItem() instanceof ItemVurpsSniper && TFHelper.isInRobotMode(player);

		if(transformer instanceof TransformerVurp && (hasSniper || transformer.canShoot(player, altMode))) {
			float stealthModeTimer = TFHelper.getStealthModeTimer(player);

			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(0F, 0F, 0F, 0.3F);

			int offset = Math.round((1 - Math.min(TFHelper.getTransformationTimer(player), stealthModeTimer)) * 210);
			int x = 6 - offset;

			if(hasSniper) {
				x = 6;
			}

			int y = 3;

			if(!hasSniper) {
				y = 30;
			}

			// Charge Outline
			drawTexturedModalRect(x + 17, y, 0, 0, 102, 12);

			if(hasSniper) {
				GL11.glColor4f(0F, 1F, 1F, 0.5F);
			}
			else {
				GL11.glColor4f(1F, 0F, 0F, 0.5F);
			}

			// Charge Bar
			drawTexturedModalRect(x + 18, y + 1, 0, 0, TFShootManager.laserCharge * 2, 10);

			GL11.glColor4f(0F, 0F, 0F, 0.2F);
			drawTexturedModalRect(x - 1, y, 0, 0, 16, 16);
			drawTexturedModalRect(x, y + 1, 0, 0, 14, 14);
			GL11.glEnable(GL11.GL_TEXTURE_2D);

			drawCenteredString(mc.fontRenderer, I18n.format("stats.laser_charge.name"), x + 50 + 18, y + 2, 0xffffff);

			RenderHelper.enableGUIStandardItemLighting();
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glEnable(GL11.GL_COLOR_MATERIAL);
			GL11.glEnable(GL11.GL_LIGHTING);
			itemRenderer.renderItemIntoGUI(mc.fontRenderer, mc.getTextureManager(), new ItemStack(transformer.getShootItem(altMode)), x - 1, y);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDepthMask(true);
			GL11.glEnable(GL11.GL_DEPTH_TEST);

			float scale = 0.5F;
			GL11.glPushMatrix();
			GL11.glScalef(scale, scale, scale);
			drawString(mc.fontRenderer, I18n.format("stats.ammo.name", I18n.format(transformer.getShootItem(altMode).getUnlocalizedName() + ".name")), (int) ((x - 1) / scale), (int) ((y + 17) / scale), 0xffffff);
			GL11.glPopMatrix();
		}
	}

	public void renderNitroAndSpeed(RenderGameOverlayEvent.Pre event, int width, int height, EntityPlayer player) {
		float transformationTimer = TFHelper.getTransformationTimer(player);

		if(transformationTimer > 0) {
			float nitro = TFHelper.median(TFData.NITRO.get(player), TFData.PREV_NITRO.get(player), event.partialTicks);
			double speed = TFHelper.median(GuiOverlay.speed, GuiOverlay.prevSpeed, event.partialTicks);
			int offset = Math.round((1 - transformationTimer) * 210);

			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(0F, 0F, 0F, 0.3F);

			// Speed Outline
			drawTexturedModalRect(5 - offset, 3, 0, 0, 202, 12);

			// Nitro Outline
			drawTexturedModalRect(5 - offset, 16, 0, 0, 202, 12);
			GL11.glColor4f(0F, 1F, 1F, 0.5F);

			// Nitro Bar
			drawTexturedModalRect(6 - offset, 4, 0, 0, Math.round(nitro * 200), 10);
			GL11.glColor4f(1F, 0F, 0F, 0.5F);

			// Speed Bar
			drawTexturedModalRect(6 - offset, 17, 0, 0, speed > 200 ? 200 : (int) speed, 10);
			GL11.glEnable(GL11.GL_TEXTURE_2D);

			drawCenteredString(mc.fontRenderer, I18n.format("stats.nitro.name"), 106 - offset, 5, 0xffffff);
			drawCenteredString(mc.fontRenderer, (int) (TFConfig.useMiles ? speed * 0.621371192 : speed) + (TFConfig.useMiles ? " mph" : " km/h"), 106 - offset, 18, 0xffffff);
		}
	}

	public void renderShotsLeft(RenderGameOverlayEvent.Pre event, int width, int height, EntityPlayer player) {
		Transformer transformer = TFHelper.getTransformer(player);
		int altMode = TFData.ALT_MODE.get(player);

		if(transformer != null && !(transformer instanceof TransformerVurp)) {
			float transformationTimer = TFHelper.getTransformationTimer(player);
			float stealthModeTimer = TFHelper.getStealthModeTimer(player);

			if(transformationTimer > 0 && transformer.canShoot(player, altMode)) {
				float f = transformationTimer;

				if(transformer.hasStealthForce(player, altMode)) {
					f = stealthModeTimer;
				}

				int offset = Math.round((1 - Math.min(transformationTimer, f)) * 210);
				int y = 30;
				int x = 6;
				int j = 20 - TFShootManager.shootCooldown;
				double d = j * 2.5;
				String shotsLeft = "" + TFShootManager.shotsLeft;

				if(TFShootManager.shotsLeft <= 0) {
					shotsLeft = EnumChatFormatting.RED + shotsLeft;
				}

				drawString(mc.fontRenderer, I18n.format("stats.shots_left.name") + ": " + shotsLeft, x + 19 - offset, 32, 0xffffff);

				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glColor4f(0F, 0F, 0F, 0.15F);
				drawTexturedModalRect(x + 94 - offset, y, 0, 0, 52, 12);
				GL11.glColor4f(0F, 0F, 0F, 0.2F);
				drawTexturedModalRect(x - 1 - offset, y - 1, 0, 0, 16, 16);
				drawTexturedModalRect(x - offset, y, 0, 0, 14, 14);
				GL11.glColor4f(1F, 0F, 0F, 0.25F);
				drawTexturedModalRect(x + 95 - offset, y + 1, 0, 0, (int) d, 10);
				GL11.glEnable(GL11.GL_TEXTURE_2D);

				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glEnable(GL12.GL_RESCALE_NORMAL);
				GL11.glEnable(GL11.GL_COLOR_MATERIAL);
				GL11.glEnable(GL11.GL_LIGHTING);
				itemRenderer.renderItemIntoGUI(mc.fontRenderer, mc.getTextureManager(), new ItemStack(transformer.getShootItem(altMode)), x - 1 - offset, y - 1);
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDepthMask(true);
				GL11.glEnable(GL11.GL_DEPTH_TEST);

				float scale = 0.5F;
				GL11.glPushMatrix();
				GL11.glTranslatef(x - 1 - offset, y + 16, 0);
				GL11.glScalef(scale, scale, scale);
				drawString(mc.fontRenderer, I18n.format("stats.ammo.name", I18n.format(transformer.getShootItem(altMode).getUnlocalizedName() + ".name")), 0, 0, 0xffffff);
				GL11.glPopMatrix();
			}
		}
		else if(transformer instanceof TransformerVurp) {
			ItemStack heldItem = player.getHeldItem();

			if(heldItem != null) {
				float transformationTimer = TFHelper.getTransformationTimer(player);

				if(transformationTimer == 0 && heldItem.getItem() == TFItems.vurpsSniper) {
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
					GL11.glColor4f(0F, 0F, 0F, 0.15F);

					if(mc.gameSettings.thirdPersonView == 0 && heldItem.getItem() == TFItems.vurpsSniper && TFDataManager.getZoomTimer(player) > 7) {
						GL11.glDisable(GL11.GL_DEPTH_TEST);
						GL11.glDepthMask(false);
						OpenGlHelper.glBlendFunc(770, 771, 1, 0);
						GL11.glColor3f(1F, 1F, 1F);
						GL11.glDisable(GL11.GL_ALPHA_TEST);
						mc.getTextureManager().bindTexture(new ResourceLocation(TransformersMod.modid, "textures/misc/sniper_scope.png"));
						Tessellator tessellator = Tessellator.instance;
						tessellator.startDrawingQuads();
						tessellator.addVertexWithUV(0D, height, -90D, 0D, 1D);
						tessellator.addVertexWithUV(width, height, -90D, 1D, 1D);
						tessellator.addVertexWithUV(width, 0D, -90D, 1D, 0D);
						tessellator.addVertexWithUV(0D, 0D, -90D, 0D, 0D);
						tessellator.draw();
						GL11.glDepthMask(true);
						GL11.glEnable(GL11.GL_DEPTH_TEST);
						GL11.glEnable(GL11.GL_ALPHA_TEST);
						GL11.glColor3f(1F, 1F, 1F);
					}
				}
			}
		}
	}

	public void renderKatanaDash(RenderGameOverlayEvent.Pre event, int width, int height, EntityPlayer player) {
		if(player.getHeldItem() != null && player.getHeldItem().getItem() == TFItems.purgesKatana && !TFHelper.isFullyTransformed(player) && TFHelper.getTransformer(player) instanceof TransformerPurge) {
			if(player.isUsingItem()) {
				int x = width / 2 - 26;
				int j = TFItems.purgesKatana.getMaxItemUseDuration(player.getHeldItem()) - player.getItemInUseCount();
				double d = (double) j / 10;

				if(d > 2D) {
					d = 2D;
				}

				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glColor4f(0F, 0F, 0F, 0.15F);

				int y = 0;

				if(TFConfig.purgeDashTop) {
					y = 5;
				}
				else {
					y = height / 2 + 9;
				}

				drawTexturedModalRect(x, y, 0, 0, 52, 12);
				GL11.glColor4f(1F, 0F, 0F, 0.25F);
				drawTexturedModalRect(x + 1, y + 1, 0, 0, (int) (d * 25), 10);

				GL11.glEnable(GL11.GL_TEXTURE_2D);
			}
		}
	}

	public void renderCrossbowAmmo(RenderGameOverlayEvent.Pre event, int width, int height, EntityPlayer player) {

	}
}
