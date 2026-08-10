package fiskfille.tf.client.render.item;

import fiskfille.tf.common.block.TFBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public final class RenderItemControlPanel extends RenderItemTileEntity {
	public RenderItemControlPanel() {
		super(TFBlocks.groundBridgeControlPanel);
	}

	@Override
	public void renderItem(final ItemRenderType type, final ItemStack item, final Object... data) {
		GL11.glScalef(0.65F, 0.65F, 0.65F);

		switch(type) {
			case ENTITY:
			case INVENTORY:
				GL11.glRotatef(-90, 0, 1, 0);
				TileEntityRendererDispatcher.instance.renderTileEntityAt(tileentity, 0, -0.75F, -0.5F, 0);
				break;

			case EQUIPPED:
				GL11.glRotatef(180, 0, 1, 0);
				TileEntityRendererDispatcher.instance.renderTileEntityAt(tileentity, -0.5F, 0, -1.5F, 0);
				break;

			case EQUIPPED_FIRST_PERSON:
			case FIRST_PERSON_MAP:
				TileEntityRendererDispatcher.instance.renderTileEntityAt(tileentity, 0.5F, 0.75F, 0.5F, 0);
				break;
		}
	}
}
