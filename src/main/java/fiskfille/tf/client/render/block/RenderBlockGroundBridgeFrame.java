package fiskfille.tf.client.render.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import fiskfille.tf.common.block.BlockGroundBridgeFrame;
import fiskfille.tf.helper.TFRenderHelper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

public class RenderBlockGroundBridgeFrame implements ISimpleBlockRenderingHandler {
	public static final RenderBlockGroundBridgeFrame instance = new RenderBlockGroundBridgeFrame();
	public static final int renderId = RenderingRegistry.getNextAvailableRenderId();

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		final ForgeDirection dir = BlockGroundBridgeFrame.getFrameDirection(world, x, y, z);

		if(dir != null) {
			final int metadata = world.getBlockMetadata(x, y, z);

			if(dir == ForgeDirection.EAST) {
				renderer.uvRotateTop = metadata == 0 ? 1 : 2;
			}
			else {
				renderer.uvRotateTop = metadata == 0 ? 0 : 3;
			}
		}

		final boolean flag = renderer.renderStandardBlock(block, x, y, z);
		renderer.uvRotateTop = 0;
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
