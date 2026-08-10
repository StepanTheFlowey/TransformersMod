package fiskfille.tf.client.render.item;

import fiskfille.tf.common.block.TFBlocks;
import fiskfille.tf.common.tileentity.TileEntityEnergonTank;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;
import org.lwjgl.opengl.GL11;

public final class RenderItemEnergonTank extends RenderItemTileEntity {
	public RenderItemEnergonTank() {
		super(TFBlocks.energonFluidTank);
	}

	@Override
	public void renderItem(final ItemRenderType type, final ItemStack item, final Object... data) {
		tileentity = new TileEntityEnergonTank();

		if(item.hasTagCompound() && item.getTagCompound().hasKey("ConfigDataTF", NBT.TAG_COMPOUND)) {
			final NBTTagCompound nbt = new NBTTagCompound();
			nbt.setTag("ConfigDataTF", item.getTagCompound().getTag("ConfigDataTF"));
			tileentity.readFromNBT(nbt);
		}

		switch(type) {
			case INVENTORY:
				GL11.glEnable(GL11.GL_ALPHA_TEST);
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glRotatef(90, 0, 1, 0);
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				break;

			case ENTITY:
				GL11.glRotatef(180, 0, 1, 0);
				GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
				break;
		}

		GL11.glEnable(GL11.GL_CULL_FACE);
		RenderBlocks.getInstance().renderBlockAsItem(Block.getBlockFromItem(item.getItem()), 1, 1);
		TileEntityRendererDispatcher.instance.renderTileEntityAt(tileentity, 0, 0, 0, 0);

		if(type == ItemRenderType.INVENTORY) {
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glDisable(GL11.GL_BLEND);
		}
	}
}
