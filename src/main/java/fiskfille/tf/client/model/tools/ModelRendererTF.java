package fiskfille.tf.client.model.tools;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.HashSet;

public class ModelRendererTF extends MowzieModelRenderer {
	private HashSet<ModelRenderer> hideUntil = new HashSet<>();

	public ModelRendererTF(final ModelBase modelBase, final String name) {
		super(modelBase, name);
	}

	public ModelRendererTF(final ModelBase modelBase, final int x, final int y) {
		super(modelBase, x, y);
	}

	public ModelRendererTF(final ModelBase modelBase) {
		super(modelBase);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void render(final float f) {
		if(!isHidden && showModel) {
			if(!compiled) {
				compileDisplayList(f);
			}

			GL11.glPushMatrix();

			GL11.glTranslatef(rotationPointX * f, rotationPointY * f, rotationPointZ * f);
			GL11.glTranslatef(offsetX, offsetY, offsetZ);
			GL11.glScalef(scaleX, scaleY, scaleZ);
			GL11.glRotatef((float) Math.toDegrees(rotateAngleZ), 0, 0, 1);
			GL11.glRotatef((float) Math.toDegrees(rotateAngleY), 0, 1, 0);
			GL11.glRotatef((float) Math.toDegrees(rotateAngleX), 1, 0, 0);

			if(hideUntil.isEmpty()) {
				GL11.glCallList(displayList);
			}
			renderChildren(f);

			GL11.glPopMatrix();
		}
	}

	protected void renderChildren(final float f) {
		if(childModels != null) {
			for(final Object childModel : childModels) {
				final ModelRendererTF model = (ModelRendererTF) childModel;
				HashSet<ModelRenderer> list = new HashSet<>(hideUntil);

				if(hideUntil.contains(model)) {
					list = new HashSet<>();
				}

				model.hideUntil = list;
				model.render(f);
			}
		}
	}

	public void hideUntil(final ModelRenderer... modelRenderers) {
		if(modelRenderers.length == 0) {
			hideUntil.clear();

			if(childModels != null) {
				for(final Object childModel : childModels) {
					((ModelRendererTF) childModel).hideUntil();
				}
			}

			return;
		}

		hideUntil.addAll(Arrays.asList(modelRenderers));
	}
}
