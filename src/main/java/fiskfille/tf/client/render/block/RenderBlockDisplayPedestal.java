package fiskfille.tf.client.render.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import fiskfille.tf.common.block.BlockDisplayPedestal;
import fiskfille.tf.common.block.BlockDisplayPedestal.BlockIcon;
import fiskfille.tf.helper.TFRenderHelper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;

public final class RenderBlockDisplayPedestal implements ISimpleBlockRenderingHandler {
	public static final RenderBlockDisplayPedestal instance = new RenderBlockDisplayPedestal();
	public static final int renderId = RenderingRegistry.getNextAvailableRenderId();

	@Override
	public boolean renderWorldBlock(final IBlockAccess world, final int x, final int y, final int z, final Block block, final int modelId, final RenderBlocks renderer) {
		final boolean flag = !renderer.hasOverrideBlockTexture();
		boolean flag1 = false;

		final BlockIcon[] icons = BlockDisplayPedestal.getTexture(world.getBlockMetadata(x, y, z));
		final AxisAlignedBB[] bounds = BlockDisplayPedestal.getBounds();
		renderer.setRenderAllFaces(true);

		for(int i = 0; i < Math.min(icons.length, bounds.length); ++i) {
			final AxisAlignedBB aabb = bounds[i];

			if(flag) {
				renderer.setOverrideBlockTexture(icons[i].block.getIcon(icons[i].side, icons[i].metadata));
			}

			renderer.setRenderBounds(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
			flag1 |= renderer.renderStandardBlock(block, x, y, z);
		}

		if(flag) {
			renderer.clearOverrideBlockTexture();
		}

		return flag1;
	}

	@Override
	public void renderInventoryBlock(final Block block, final int metadata, final int modelID, final RenderBlocks renderer) {
		final BlockIcon[] icons = BlockDisplayPedestal.getTexture(metadata);
		final AxisAlignedBB[] bounds = BlockDisplayPedestal.getBounds();

		GL11.glRotatef(90, 0, 1, 0);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

		for(int i = 0; i < Math.min(icons.length, bounds.length); ++i) {
			final AxisAlignedBB aabb = bounds[i];
			renderer.setOverrideBlockTexture(icons[i].block.getIcon(icons[i].side, icons[i].metadata));
			renderer.setRenderBounds(aabb.minX, aabb.minY, aabb.minZ, aabb.maxX, aabb.maxY, aabb.maxZ);
			TFRenderHelper.renderBlock(block, metadata, renderer);
		}

		renderer.clearOverrideBlockTexture();
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
