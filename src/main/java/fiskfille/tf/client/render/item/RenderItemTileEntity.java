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

	public RenderItemTileEntity(Block b) {
		block = b;
		tileentity = block.createTileEntity(null, 0);
		tileentity.blockType = block;
		tileentity.blockMetadata = 0;
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		if(type == ItemRenderType.INVENTORY) {
			GL11.glRotatef(90F, 0F, 1F, 0F);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		}
		else if(type == ItemRenderType.ENTITY) {
			GL11.glRotatef(180F, 0F, 1F, 0F);
			GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		}

		try {
			TileEntityRendererDispatcher.instance.renderTileEntityAt(tileentity, 0, 0, 0, 0);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
