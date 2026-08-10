package fiskfille.tf.client.render.item;

import fiskfille.tf.common.block.TFBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public final class RenderItemTransmitter extends RenderItemTileEntity {
	public RenderItemTransmitter() {
		super(TFBlocks.transmitter);
	}

	@Override
	public void renderItem(final ItemRenderType type, final ItemStack item, final Object... data) {
		GL11.glScalef(0.4F, 0.4F, 0.4F);

		switch(type) {
			case ENTITY:
			case INVENTORY:
				if(type == ItemRenderType.INVENTORY) {
					GL11.glRotatef(90, 0, 1, 0);
				}
				else {
					GL11.glRotatef(180, 0, 1, 0);
				}

				GL11.glTranslatef(-0.5F, -1.35F, -0.5F);
				break;

			case EQUIPPED:
			case EQUIPPED_FIRST_PERSON:
			case FIRST_PERSON_MAP:
				GL11.glTranslatef(0.5F, 0, 0.5F);
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
