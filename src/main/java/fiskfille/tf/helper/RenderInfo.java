package fiskfille.tf.helper;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;

public class RenderInfo {
	public final boolean[] renderSide = new boolean[]{true, true, true, true, true, true};
	public final int brightness = -1;

	public IIcon[] textureArray;
	public IIcon texture;
	public Block baseBlock = Blocks.sand;
	public double minX = 0;
	public double minY = 0;
	public double minZ = 0;
	public double maxX = 1;
	public double maxY = 1;
	public double maxZ = 1;

	public RenderInfo() {}

	public IIcon getBlockTextureFromSide(final int i) {
		if(texture != null) {
			return texture;
		}

		int index = i;

		if(textureArray == null || textureArray.length == 0) {
			return baseBlock.getBlockTextureFromSide(index);
		}
		else {
			if(index >= textureArray.length) {
				index = 0;
			}

			return textureArray[index];
		}
	}

	public void renderBlock(final RenderBlocks renderBlocks) {
		final Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();

		renderBlocks.setRenderBounds(minX, minY, minZ, maxX, maxY, maxZ);

		if(brightness != -1) {
			tessellator.setBrightness(brightness << 4);
		}

		if(renderSide[0]) {
			tessellator.setNormal(0, -1, 0);
			renderBlocks.renderFaceYNeg(baseBlock, 0, 0, 0, getBlockTextureFromSide(0));
		}
		if(renderSide[1]) {
			tessellator.setNormal(0, 1, 0);
			renderBlocks.renderFaceYPos(baseBlock, 0, 0, 0, getBlockTextureFromSide(1));
		}
		if(renderSide[2]) {
			tessellator.setNormal(0, 0, -1);
			renderBlocks.renderFaceZNeg(baseBlock, 0, 0, 0, getBlockTextureFromSide(2));
		}
		if(renderSide[3]) {
			tessellator.setNormal(0, 0, 1);
			renderBlocks.renderFaceZPos(baseBlock, 0, 0, 0, getBlockTextureFromSide(3));
		}
		if(renderSide[4]) {
			tessellator.setNormal(-1, 0, 0);
			renderBlocks.renderFaceXNeg(baseBlock, 0, 0, 0, getBlockTextureFromSide(4));
		}
		if(renderSide[5]) {
			tessellator.setNormal(1, 0, 0);
			renderBlocks.renderFaceXPos(baseBlock, 0, 0, 0, getBlockTextureFromSide(5));
		}

		tessellator.draw();
	}
}
