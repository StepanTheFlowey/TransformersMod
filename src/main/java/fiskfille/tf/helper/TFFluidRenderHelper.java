package fiskfille.tf.helper;

import fiskfille.tf.common.fluid.FluidTankTF;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import org.lwjgl.opengl.GL11;

import javax.vecmath.Vector4d;
import java.util.HashMap;
import java.util.Map;

public final class TFFluidRenderHelper {
	public static final int DISPLAY_STAGES = 100;
	private static final RenderInfo liquidBlock = new RenderInfo();
	private static final Map<Fluid, int[]> flowingRenderCache = new HashMap<>();
	private static final Map<Fluid, int[]> stillRenderCache = new HashMap<>();

	public static void onTextureStitch(final TextureMap map) {
		for(final int[] aint : flowingRenderCache.values()) {
			for(final int i : aint) {
				GL11.glDeleteLists(i, 1);
			}
		}

		flowingRenderCache.clear();

		for(final int[] aint : stillRenderCache.values()) {
			for(final int i : aint) {
				GL11.glDeleteLists(i, 1);
			}
		}

		stillRenderCache.clear();
	}

	public static IIcon getFluidTexture(final FluidStack fluidStack, final boolean flowing) {
		if(fluidStack == null) {
			return null;
		}

		return getFluidTexture(fluidStack.getFluid(), flowing);
	}

	public static IIcon getFluidTexture(final Fluid fluid, final boolean flowing) {
		if(fluid == null) {
			return null;
		}

		IIcon icon = flowing ? fluid.getFlowingIcon() : fluid.getStillIcon();

		if(icon == null) {
			icon = ((TextureMap) Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.locationBlocksTexture)).getAtlasSprite("missingno");
		}

		return icon;
	}

	public static void setColorForFluidStack(final FluidStack fluidstack) {
		if(fluidstack == null) {
			return;
		}

		TFRenderHelper.glColorRGB(fluidstack.getFluid().getColor(fluidstack));
	}

	public static int[] getFluidDisplayLists(final RenderBlocks renderBlocks, final FluidStack fluidStack, final World world, final boolean flowing) {
		if(fluidStack == null) {
			return null;
		}

		final Fluid fluid = fluidStack.getFluid();
		if(fluid == null) {
			return null;
		}

		final Map<Fluid, int[]> cache = flowing ? flowingRenderCache : stillRenderCache;
		int[] diplayLists = cache.get(fluid);

		if(diplayLists != null) {
			return diplayLists;
		}

		diplayLists = new int[DISPLAY_STAGES];

		if(fluid.getBlock() != null) {
			liquidBlock.baseBlock = fluid.getBlock();
		}
		else {
			liquidBlock.baseBlock = Blocks.water;
		}
		liquidBlock.texture = getFluidTexture(fluidStack, flowing);

		cache.put(fluid, diplayLists);

		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_CULL_FACE);

		for(int stage = 0; stage < DISPLAY_STAGES; ++stage) {
			diplayLists[stage] = GLAllocation.generateDisplayLists(1);
			GL11.glNewList(diplayLists[stage], 4864 /* GL_COMPILE */);

			liquidBlock.minX = 0;
			liquidBlock.minY = 0;
			liquidBlock.minZ = 0;
			liquidBlock.maxX = 1;
			liquidBlock.maxY = (float) stage / (DISPLAY_STAGES - 1);
			liquidBlock.maxZ = 1;

			liquidBlock.renderBlock(renderBlocks);
			GL11.glEndList();
		}

		GL11.glColor3f(1, 1, 1);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_LIGHTING);

		return diplayLists;
	}

	public static void renderIntoGUI(final FluidTankTF tank, final int x, final int y, final int width, final int height, final float zLevel) {
		final FluidStack stack = tank.getFluid();

		if(stack != null && stack.amount > 0) {
			final IIcon icon = stack.getFluid().getStillIcon();
			final float f = (float) stack.amount / tank.getCapacity();
			final Vector4d tex = new Vector4d(icon.getMinU(), icon.getInterpolatedV(16 * (1 - f)), icon.getInterpolatedU(16 * (float) width / height), icon.getMaxV());
			final Vector4d pos = new Vector4d(x, y, width, height);
			pos.y += pos.w * (1 - f);
			pos.w *= f;
			pos.z += pos.x;
			pos.w += pos.y;

			Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
			TFFluidRenderHelper.setColorForFluidStack(stack);

			final Tessellator tessellator = Tessellator.instance;
			tessellator.startDrawingQuads();
			tessellator.setTranslation(0, 0, zLevel);
			tessellator.addVertexWithUV(pos.x, pos.w, 0, tex.x, tex.w);
			tessellator.addVertexWithUV(pos.z, pos.w, 0, tex.z, tex.w);
			tessellator.addVertexWithUV(pos.z, pos.y, 0, tex.z, tex.y);
			tessellator.addVertexWithUV(pos.x, pos.y, 0, tex.x, tex.y);
			tessellator.setTranslation(0, 0, 0);
			GL11.glEnable(GL11.GL_BLEND);

			tessellator.draw();

			GL11.glDisable(GL11.GL_BLEND);
			GL11.glColor3f(1, 1, 1);
		}
	}
}
