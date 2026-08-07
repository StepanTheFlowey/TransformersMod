package fiskfille.tf.client.render.item;

import fiskfille.tf.common.block.TFBlocks;
import fiskfille.tf.helper.TFRenderHelper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class RenderItemDataCore implements IItemRenderer {
	public final RenderBlocks renderBlocks = RenderBlocks.getInstance();

	@Override
	public boolean handleRenderType(final ItemStack item, final ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(final ItemRenderType type, final ItemStack item, final ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(final ItemRenderType type, final ItemStack item, final Object... data) {
		GL11.glColor3f(1F, 1F, 1F);

		float scale = type != ItemRenderType.INVENTORY ? 0.5F : 1F;
		if(type == ItemRenderType.EQUIPPED_FIRST_PERSON || type == ItemRenderType.EQUIPPED) {
			GL11.glTranslatef(0.5F, 0.5F, 0.5F);
		}
		else if(type == ItemRenderType.ENTITY) {
			scale *= 0.5F;
		}

		GL11.glScalef(scale, scale, scale);
		render(item);
	}

	public void render(final ItemStack itemstack) {
		final Block block = TFBlocks.groundBridgeControlPanel;
		final Tessellator tessellator = Tessellator.instance;

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_ALPHA_TEST);

		for(int i = 0; i < 2; ++i) {
			final IIcon icon = itemstack.getItem().getIcon(itemstack, i);
			final int color = itemstack.getItem().getColorFromItemStack(itemstack, i);
			final float f1 = (color >> 16 & 255) / 255F;
			final float f2 = (color >> 8 & 255) / 255F;
			final float f3 = (color & 255) / 255F;

			if(i == 1) {
				GL11.glDisable(GL11.GL_LIGHTING);
				TFRenderHelper.setLighting(TFRenderHelper.LIGHTING_LUMINOUS);
			}

			if(renderBlocks.useInventoryTint) {
				GL11.glColor3f(f1, f2, f3);
			}

			renderBlocks.setRenderBounds(0, 0, 0, 1, 1, 1);
			GL11.glPushMatrix();
			GL11.glRotatef(90, 0, 1, 0);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
			tessellator.startDrawingQuads();
			tessellator.setNormal(0, -1, 0);
			renderBlocks.renderFaceYNeg(block, 0D, 0D, 0D, icon);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0, 1, 0);
			renderBlocks.renderFaceYPos(block, 0D, 0D, 0D, icon);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0, 0, -1);
			renderBlocks.renderFaceZNeg(block, 0D, 0D, 0D, icon);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(0, 0, 1);
			renderBlocks.renderFaceZPos(block, 0D, 0D, 0D, icon);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(-1, 0, 0);
			renderBlocks.renderFaceXNeg(block, 0D, 0D, 0D, icon);
			tessellator.draw();
			tessellator.startDrawingQuads();
			tessellator.setNormal(1, 0, 0);
			renderBlocks.renderFaceXPos(block, 0D, 0D, 0D, icon);
			tessellator.draw();
			GL11.glPopMatrix();

			if(i == 1) {
				TFRenderHelper.resetLighting();
				GL11.glEnable(GL11.GL_LIGHTING);
			}
		}

		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_ALPHA_TEST);
	}
}
