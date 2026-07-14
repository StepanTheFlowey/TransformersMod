package fiskfille.tf.client.render.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import fiskfille.tf.common.block.BlockAlloyCrucible;
import fiskfille.tf.helper.TFRenderHelper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public class RenderBlockAlloyCrucible implements ISimpleBlockRenderingHandler {
	public static final RenderBlockAlloyCrucible instance = new RenderBlockAlloyCrucible();
	public static final int renderId = RenderingRegistry.getNextAvailableRenderId();

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		final int metadata = world.getBlockMetadata(x, y, z);
		boolean flag = renderer.renderStandardBlock(block, x, y, z);

		if(BlockAlloyCrucible.getFlag(metadata, BlockAlloyCrucible.FLAG_TOP) || BlockAlloyCrucible.getFlag(metadata, BlockAlloyCrucible.FLAG_FRONT)) {
			BlockAlloyCrucible.renderPass = 1;
			flag |= renderer.renderStandardBlock(block, x, y, z);
			BlockAlloyCrucible.renderPass = 0;
		}

		return flag;
	}

	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
		GL11.glRotatef(90F, 0F, 1F, 0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

		block.setBlockBoundsForItemRender();
		renderer.setRenderBoundsFromBlock(block);
		TFRenderHelper.renderBlock(block, metadata, renderer);
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}

	@Override
	public int getRenderId() {
		return renderId;
	}
}
