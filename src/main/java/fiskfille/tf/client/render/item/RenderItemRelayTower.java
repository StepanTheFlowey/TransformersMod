package fiskfille.tf.client.render.item;

import fiskfille.tf.common.block.TFBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class RenderItemRelayTower extends RenderItemTileEntity {
	public RenderItemRelayTower() {
		super(TFBlocks.relayTower);
	}

	@Override
	public void renderItem(final ItemRenderType type, final ItemStack item, final Object... data) {
		GL11.glScalef(0.65F, 0.65F, 0.65F);

		if(type == ItemRenderType.ENTITY || type == ItemRenderType.INVENTORY) {
			if(type == ItemRenderType.INVENTORY) {
				GL11.glRotatef(90F, 0F, 1F, 0F);
			}
			else {
				GL11.glRotatef(180F, 0F, 1F, 0F);
			}

			GL11.glTranslatef(-0.5F, -0.9F, -0.5F);
		}
		else if(type == ItemRenderType.EQUIPPED) {
			GL11.glTranslatef(0.5F, 0F, 0.5F);
		}
		else if(type == ItemRenderType.EQUIPPED_FIRST_PERSON || type == ItemRenderType.FIRST_PERSON_MAP) {
			GL11.glTranslatef(0.5F, 0F, 0.5F);
		}

		try {
			TileEntityRendererDispatcher.instance.renderTileEntityAt(tileentity, 0, 0, 0, 0);
		}
		catch(final Exception e) {
			e.printStackTrace();
		}
	}
}
