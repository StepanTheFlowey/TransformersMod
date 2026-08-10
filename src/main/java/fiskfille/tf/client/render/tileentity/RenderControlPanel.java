package fiskfille.tf.client.render.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.model.tileentity.ModelControlPanel;
import fiskfille.tf.common.block.BlockControlPanel;
import fiskfille.tf.common.data.tile.TileDataControlPanel;
import fiskfille.tf.common.groundbridge.DataCore;
import fiskfille.tf.common.tileentity.TileEntityControlPanel;
import fiskfille.tf.helper.TFDimensionHelper;
import fiskfille.tf.helper.TFRenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public final class RenderControlPanel extends TileEntitySpecialRenderer {
	private final ModelControlPanel model = new ModelControlPanel();
	private final ResourceLocation texturePanel = new ResourceLocation(TransformersMod.MODID, "textures/models/tiles/ground_bridge_control_panel.png");
	private final ResourceLocation texturePanelLights = new ResourceLocation(TransformersMod.MODID, "textures/models/tiles/ground_bridge_control_panel_lights.png");
	private final ResourceLocation textureEnergy = new ResourceLocation(TransformersMod.MODID, "textures/models/tiles/energy_meter.png");
	private final ItemRenderer itemRenderer = new ItemRenderer(Minecraft.getMinecraft());

	private void render(final TileEntityControlPanel tile, final double x, final double y, final double z, final float partialTicks) {
		int metadata = 0;
		if(tile.getWorldObj() != null) {
			metadata = tile.getBlockMetadata();
		}

		if(BlockControlPanel.isBlockLeftSideOfPanel(metadata)) {
			GL11.glPushMatrix();
			GL11.glTranslated(x + 0.5D, y + 1.5D, z + 0.5D);
			GL11.glScalef(1, -1, -1);
			GL11.glRotatef(BlockControlPanel.getDirection(metadata) * 90 + 180, 0, 1, 0);
			GL11.glTranslatef(0.5F, 0, 0);

			GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			for(int i = 0; i < tile.getSizeInventory(); ++i) {
				final ItemStack itemstack = tile.getStackInSlot(i);
				if(itemstack == null) {
					continue;
				}

				GL11.glPushMatrix();
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glRotatef(-90, 0, 1, 0);
				GL11.glScalef(2, 2, 2);
				GL11.glTranslatef(-0.497F / 2, 0.8765F / 2, (-0.2825F - i * 0.2175F) / 2);
				GL11.glScalef(-0.155F, -0.155F, 0.155F);
				GL11.glColor3f(1, 1, 1);

				itemRenderer.renderItem(Minecraft.getMinecraft().thePlayer, itemstack, 0);

				GL11.glColor3f(1, 1, 1);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glPopMatrix();
			}

			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

			bindTexture(texturePanel);
			model.render(tile, partialTicks);

			TFRenderHelper.setLighting(TFRenderHelper.LIGHTING_LUMINOUS);
			bindTexture(texturePanelLights);
			model.render(tile, partialTicks);
			model.table1.postRender(0.0625F);
			model.table2.postRender(0.0625F);

			if(tile.getWorldObj() != null) {
				final float energy = tile.getEnergy();

				if(energy > 0) {
					final float f = 1F / 32;
					final float f1 = energy / tile.getMaxEnergy();
					final float length = f * 18;
					final float width = f * 4;
					final Tessellator tessellator = Tessellator.instance;
					tessellator.startDrawingQuads();
					tessellator.addVertexWithUV(width, 0, 0, width, length);
					tessellator.addVertexWithUV(width, 0, length * f1, width, length * (1 - f1));
					tessellator.addVertexWithUV(0, 0, length * f1, 0, length * (1 - f1));
					tessellator.addVertexWithUV(0, 0, 0, 0, length);

					bindTexture(textureEnergy);
					GL11.glPushMatrix();
					GL11.glDisable(GL11.GL_LIGHTING);
					GL11.glTranslatef(f * 40, -0.1251F, f * 7);

					tessellator.draw();

					GL11.glEnable(GL11.GL_LIGHTING);
					GL11.glPopMatrix();
				}
			}

			final TileDataControlPanel data = tile.data;
			String dimensionName = "";

			if(tile.getWorldObj() != null) {
				dimensionName = TFDimensionHelper.getDimensionName(data.destination.dimension);
			}

			GL11.glPushMatrix();
			model.screen1.postRender(0.0625F);
			model.screen2.postRender(0.0625F);
			GL11.glTranslatef(0, 0, -0.001F);

			renderText(StatCollector.translateToLocal("ground_bridge.destination"), 0, 0, -1);
			renderText(StatCollector.translateToLocalFormatted("ground_bridge.destination.format", data.destination.posX, tile.hasUpgrade(DataCore.leveler) ? String.format("%s -> %s", data.destination.posY, data.modifiedDestY) : data.destination.posY, data.destination.posZ, dimensionName), 1, 0, -1);

			if(!data.errors.isEmpty()) {
				renderText(StatCollector.translateToLocal("ground_bridge.error"), 2, 0.025F, 0xC10000);
				renderText(data.errors.get(0).translate(), 3, 0.025F, 0xC10000);

				if(data.errors.size() == 1) {
					renderText(StatCollector.translateToLocal("ground_bridge.error.no_other_errors"), 7, 0, -1);
				}
				else {
					renderText(StatCollector.translateToLocalFormatted("ground_bridge.error.other_error" + (tile.data.errors.size() == 2 ? "" : "s"), tile.data.errors.size() - 1), 7, 0, 0xC10000);
				}
			}

			if(tile.hasUpgrade(DataCore.spaceBridge)) {
				GL11.glPushMatrix();
				model.dimPanel1.postRender(0.0625F);
				model.dimPanel2.postRender(0.0625F);

				GL11.glPushMatrix();
				model.dimPanel3.postRender(0.0625F);
				GL11.glTranslatef(0.3125F, -0.025F, -0.0625F * 5.21F);
				renderCenteredText("<", 0, 0);
				GL11.glPopMatrix();

				GL11.glPushMatrix();
				model.dimPanel4.postRender(0.0625F);
				GL11.glTranslatef(0.3125F, -0.025F, -0.0625F * 5.21F);
				renderCenteredText(">", 0, 0);
				GL11.glPopMatrix();

				GL11.glPushMatrix();
				model.dimPanel6.postRender(0.0625F);
				model.dimPanel7.postRender(0.0625F);
				GL11.glTranslatef(0.345F, -0.025F, -0.0625F * 6.3F);
				GL11.glRotatef(5, 1, 0, 0);
				renderCenteredText(Integer.toString(data.destination.dimension), 0, 0.02F);
				GL11.glPopMatrix();

				GL11.glPopMatrix();
			}

			GL11.glPopMatrix();

			GL11.glPushMatrix();
			model.table6.postRender(0.0625F);

			GL11.glPushMatrix();
			model.compass1.postRender(0.0625F);
			GL11.glTranslatef(0, -0.032F, 0);
			GL11.glRotatef(-90, 1, 0, 0);
			renderCenteredText(StatCollector.translateToLocal("direction.south.short"), -0.0465F, 0.13F);
			GL11.glPopMatrix();

			GL11.glPushMatrix();
			model.compass2.postRender(0.0625F);
			GL11.glTranslatef(0, -0.032F, 0);
			GL11.glRotatef(-90, 0, 1, 0);
			GL11.glRotatef(-90, 1, 0, 0);
			renderCenteredText(StatCollector.translateToLocal("direction.west.short"), -0.2395F, -0.0625F);
			GL11.glPopMatrix();

			GL11.glPushMatrix();
			model.compass3.postRender(0.0625F);
			GL11.glTranslatef(0, -0.032F, 0);
			GL11.glRotatef(180, 0, 1, 0);
			GL11.glRotatef(-90, 1, 0, 0);
			renderCenteredText(StatCollector.translateToLocal("direction.north.short"), -0.0465F, -0.2575F);
			GL11.glPopMatrix();

			GL11.glPushMatrix();
			model.compass4.postRender(0.0625F);
			GL11.glTranslatef(0, -0.032F, 0);
			GL11.glRotatef(90, 0, 1, 0);
			GL11.glRotatef(-90, 1, 0, 0);
			renderCenteredText(StatCollector.translateToLocal("direction.east.short"), 0.1475F, -0.0625F);
			GL11.glPopMatrix();

			GL11.glPopMatrix();
			GL11.glPopMatrix();
			TFRenderHelper.resetLighting();

			if(tile.getWorldObj() != null) {
				final int progress = TFRenderHelper.getBlockDestroyProgress(tile.getWorldObj(), tile.xCoord, tile.yCoord, tile.zCoord);

				if(progress >= 0) {
					bindTexture(new ResourceLocation(String.format("textures/blocks/destroy_stage_%s.png", progress)));
					GL11.glColor4f(1, 1, 1, 0.5F);

					GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT);
					GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
					GL11.glEnable(GL11.GL_ALPHA_TEST);
					GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
					OpenGlHelper.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR, GL11.GL_ONE, GL11.GL_ZERO);
					model.setBreaking(true);

					model.render(tile, partialTicks);

					model.setBreaking(false);
					GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
					GL11.glPopAttrib();
				}
			}

			GL11.glDisable(GL11.GL_BLEND);
			GL11.glPopMatrix();
		}
	}

	private void renderText(final String s, final int line, final float y, final int color) {
		GL11.glPushMatrix();
		GL11.glTranslatef(0.05F, y + 0.0375F + 0.05F * line, -0.001F);
		GL11.glScalef(0.004F, 0.004F, -0.004F);
		GL11.glColor3f(1, 1, 1);
		GL11.glDisable(GL11.GL_LIGHTING);

		Minecraft.getMinecraft().fontRenderer.drawSplitString(s, 0, 0, 200, color);

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}

	private void renderCenteredText(final String s, final float x, final float y) {
		GL11.glPushMatrix();
		GL11.glTranslatef(x + 0.05F, y + 0.0375F, -0.001F);
		GL11.glScalef(0.00725F, 0.00725F, -0.00725F);
		GL11.glColor3f(1, 1, 1);
		GL11.glDisable(GL11.GL_LIGHTING);

		Minecraft.getMinecraft().fontRenderer.drawString(s, -Minecraft.getMinecraft().fontRenderer.getStringWidth(s) / 2, 0, -1);

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(final TileEntity tileentity, final double x, final double y, final double z, final float partialTicks) {
		render((TileEntityControlPanel) tileentity, x, y, z, partialTicks);
	}
}
