package fiskfille.tf.client.render.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import fiskfille.tf.common.block.BlockEnergonOre;
import fiskfille.tf.helper.TFRenderHelper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public final class RenderBlockEnergonOre implements ISimpleBlockRenderingHandler {
	public static final RenderBlockEnergonOre instance = new RenderBlockEnergonOre();
	public static final int renderId = RenderingRegistry.getNextAvailableRenderId();

	@Override
	public boolean renderWorldBlock(final IBlockAccess world, final int x, final int y, final int z, final Block block, final int modelId, final RenderBlocks renderer) {
		boolean flag = false;

		BlockEnergonOre.renderPass = 1;
		flag |= renderer.renderStandardBlock(block, x, y, z);
		BlockEnergonOre.renderPass = 2;
		flag |= renderer.renderStandardBlock(block, x, y, z);
		BlockEnergonOre.renderPass = 0;

		return flag;
	}

	@Override
	public void renderInventoryBlock(final Block block, final int metadata, final int modelID, final RenderBlocks renderer) {
		GL11.glRotatef(90, 0, 1, 0);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

		BlockEnergonOre.renderPass = 0;
		block.setBlockBoundsForItemRender();
		renderer.setRenderBoundsFromBlock(block);
		TFRenderHelper.renderBlock(block, metadata, renderer);
	}

	@Override
	public boolean shouldRender3DInInventory(final int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return renderId;
	}
}
