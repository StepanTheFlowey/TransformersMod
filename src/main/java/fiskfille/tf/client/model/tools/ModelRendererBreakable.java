package fiskfille.tf.client.model.tools;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.TextureOffset;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

public class ModelRendererBreakable extends ModelRendererTF {
	public boolean breaking = false;
	public boolean renderBreaking = true;

	protected final int[] displayLists = new int[2];

	private final ModelBase baseModel;
	private int textureOffsetX;
	private int textureOffsetY;

	public ModelRendererBreakable(final ModelBase modelBase, final String name) {
		super(modelBase, name);
		baseModel = modelBase;
	}

	public ModelRendererBreakable(final ModelBase modelBase, final boolean render, final int x, final int y) {
		super(modelBase, x, y);
		baseModel = modelBase;
		renderBreaking = render;
	}

	public ModelRendererBreakable(final ModelBase modelBase, final int x, final int y) {
		this(modelBase, true, x, y);
	}

	public ModelRendererBreakable(final ModelBase modelBase) {
		super(modelBase);
		baseModel = modelBase;
	}

	@Override
	public ModelRenderer setTextureOffset(final int x, final int y) {
		textureOffsetX = x;
		textureOffsetY = y;
		return super.setTextureOffset(x, y);
	}

	@Override
	public ModelRenderer addBox(String name, final float p_78786_2_, final float p_78786_3_, final float p_78786_4_, final int p_78786_5_, final int p_78786_6_, final int p_78786_7_) {
		name = boxName + "." + name;

		final TextureOffset offset = baseModel.getTextureOffset(name);
		setTextureOffset(offset.textureOffsetX, offset.textureOffsetY);
		cubeList.add(new ModelBoxBreakable(this, textureOffsetX, textureOffsetY, p_78786_2_, p_78786_3_, p_78786_4_, p_78786_5_, p_78786_6_, p_78786_7_, 0F).func_78244_a(name));
		return this;
	}

	@Override
	public ModelRenderer addBox(final float p_78789_1_, final float p_78789_2_, final float p_78789_3_, final int p_78789_4_, final int p_78789_5_, final int p_78789_6_) {
		cubeList.add(new ModelBoxBreakable(this, textureOffsetX, textureOffsetY, p_78789_1_, p_78789_2_, p_78789_3_, p_78789_4_, p_78789_5_, p_78789_6_, 0F));
		return this;
	}

	@Override
	public void addBox(final float p_78790_1_, final float p_78790_2_, final float p_78790_3_, final int p_78790_4_, final int p_78790_5_, final int p_78790_6_, final float p_78790_7_) {
		cubeList.add(new ModelBoxBreakable(this, textureOffsetX, textureOffsetY, p_78790_1_, p_78790_2_, p_78790_3_, p_78790_4_, p_78790_5_, p_78790_6_, p_78790_7_));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void render(final float f) {
		displayList = displayLists[breaking ? 1 : 0];
		super.render(f);
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected void compileDisplayList(final float f) {
		super.compileDisplayList(f);
		displayLists[0] = displayList;
		displayLists[1] = GLAllocation.generateDisplayLists(1);

		final boolean prevBreaking = breaking;
		breaking = true;
		GL11.glNewList(displayLists[1], GL11.GL_COMPILE);

		for(final Object cube : cubeList) {
			if(renderBreaking) {
				((ModelBox) cube).render(Tessellator.instance, f);
			}
		}

		breaking = prevBreaking;
		GL11.glEndList();

		displayList = displayLists[breaking ? 1 : 0];
	}
}
