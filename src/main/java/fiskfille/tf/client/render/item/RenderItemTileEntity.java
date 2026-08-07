package fiskfille.tf.client.render.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

public class RenderItemTileEntity implements IItemRenderer {
	protected final Block block;
	protected TileEntity tileentity;

	public RenderItemTileEntity(final Block b) {
		block = b;
		tileentity = block.createTileEntity(null, 0);
		tileentity.blockType = block;
		tileentity.blockMetadata = 0;
	}

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
		switch(type) {
			case INVENTORY:
				GL11.glRotatef(90, 0, 1, 0);
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				break;

			case ENTITY:
				GL11.glRotatef(180, 0, 1, 0);
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				break;
		}

		try {
			TileEntityRendererDispatcher.instance.renderTileEntityAt(tileentity, 0, 0, 0, 0);
		}
		catch(final Exception e) {
			e.printStackTrace();
		}
	}
}
