package fiskfille.tf.client.render.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.model.tileentity.ModelEnergyColumn;
import fiskfille.tf.client.render.item.RenderItemPowerCanister;
import fiskfille.tf.common.item.TFItems;
import fiskfille.tf.common.tileentity.TileEntityColumn;
import fiskfille.tf.helper.TFRenderHelper;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public final class RenderColumn extends TileEntitySpecialRenderer {
	private final ModelEnergyColumn model = new ModelEnergyColumn();
	private final ResourceLocation texture = new ResourceLocation(TransformersMod.MODID, "textures/models/tiles/energy_column.png");
	private final ResourceLocation textureLights = new ResourceLocation(TransformersMod.MODID, "textures/models/tiles/energy_column_lights.png");

	private void render(final TileEntityColumn tile, final double x, final double y, final double z) {
		final World world = tile.getWorldObj();
		int metadata = 0;

		if(world != null) {
			metadata = tile.getBlockMetadata();
		}

		if(metadata < 4) {
			bindTexture(texture);
			GL11.glPushMatrix();
			GL11.glTranslated(x + 0.5D, y + 1.5D, z + 0.5D);
			GL11.glScalef(1, -1, -1);
			GL11.glRotatef(metadata * 90, 0, 1, 0);

			model.render(tile);

			bindTexture(textureLights);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			TFRenderHelper.setLighting(TFRenderHelper.LIGHTING_LUMINOUS);

			model.render(tile);

			final float f = 0.001F;
			float texX = 30F;
			final float texY = 31F;
			final float texWidth = 7F;
			final float width = texWidth * 0.0625F;
			final Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV(width / 2, -(0.5F + f), -width / 2, (texX + texWidth) * (1F / 128), (texY + texWidth) * (1F / 64));
			tessellator.addVertexWithUV(width / 2, -(0.5F + f), width / 2, (texX + texWidth) * (1F / 128), texY * (1F / 64));
			tessellator.addVertexWithUV(-width / 2, -(0.5F + f), width / 2, texX * (1F / 128), texY * (1F / 64));
			tessellator.addVertexWithUV(-width / 2, -(0.5F + f), -width / 2, texX * (1F / 128), (texY + texWidth) * (1F / 64));
			texX += texWidth;
			tessellator.addVertexWithUV(width / 2, 1.5F + f, -width / 2, (texX + texWidth) * (1F / 128), (texY + texWidth) * (1F / 64));
			tessellator.addVertexWithUV(-width / 2, 1.5F + f, -width / 2, texX * (1F / 128), (texY + texWidth) * (1F / 64));
			tessellator.addVertexWithUV(-width / 2, 1.5F + f, width / 2, texX * (1F / 128), texY * (1F / 64));
			tessellator.addVertexWithUV(width / 2, 1.5F + f, width / 2, (texX + texWidth) * (1F / 128), texY * (1F / 64));
			tessellator.draw();

			TFRenderHelper.resetLighting();
			GL11.glEnable(GL11.GL_LIGHTING);

			if(world != null) {
				final int progress = TFRenderHelper.getBlockDestroyProgress(world, tile.xCoord, tile.yCoord, tile.zCoord);

				if(progress >= 0) {
					bindTexture(new ResourceLocation(String.format("textures/blocks/destroy_stage_%s.png", progress)));
					GL11.glColor4f(1, 1, 1, 0.5F);

					GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT);
					GL11.glEnable(GL11.GL_POLYGON_OFFSET_FILL);
					GL11.glEnable(GL11.GL_ALPHA_TEST);
					GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
					OpenGlHelper.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR, GL11.GL_ONE, GL11.GL_ZERO);
					model.setBreaking(true);

					model.render(tile);

					model.setBreaking(false);
					GL11.glDisable(GL11.GL_POLYGON_OFFSET_FILL);
					GL11.glPopAttrib();
				}
			}

			GL11.glDisable(GL11.GL_BLEND);

			for(int i = 0; i < tile.getSizeInventory(); ++i) {
				final ItemStack itemstack = tile.getStackInSlot(i);

				if(itemstack != null && itemstack.getItem() == TFItems.powerCanister) {
					GL11.glPushMatrix();
					GL11.glRotatef((i + 2) * 360F / tile.getSizeInventory(), 0, 1, 0);
					GL11.glTranslatef(0.0625F * 7, 0.0625F * 12, 0);

					RenderItemPowerCanister.renderCanister(itemstack);

					GL11.glPopMatrix();
				}
			}

			GL11.glPopMatrix();
		}
	}

	@Override
	public void renderTileEntityAt(final TileEntity tileentity, final double x, final double y, final double z, final float partialTicks) {
		render((TileEntityColumn) tileentity, x, y, z);
	}
}
