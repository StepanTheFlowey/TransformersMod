package fiskfille.tf.client.model.tools;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;

public class ModelRendererTF extends MowzieModelRenderer {
	private ArrayList<ModelRenderer> hideUntil = new ArrayList<>();

	public ModelRendererTF(ModelBase modelBase, String name) {
		super(modelBase, name);
	}

	public ModelRendererTF(ModelBase modelBase, int x, int y) {
		super(modelBase, x, y);
	}

	public ModelRendererTF(ModelBase modelBase) {
		super(modelBase);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void render(float f) {
		GL11.glPushMatrix();

		if(!isHidden) {
			if(showModel) {
				if(!compiled && hideUntil.isEmpty()) {
					compileDisplayList(f);
				}

				GL11.glTranslatef(rotationPointX * f, rotationPointY * f, rotationPointZ * f);
				GL11.glTranslatef(offsetX, offsetY, offsetZ);
				GL11.glScalef(scaleX, scaleY, scaleZ);
				GL11.glTranslatef(-rotationPointX * f, -rotationPointY * f, -rotationPointZ * f);

				if(rotateAngleX == 0F && rotateAngleY == 0F && rotateAngleZ == 0F) {
					if(rotationPointX == 0F && rotationPointY == 0F && rotationPointZ == 0F) {
						renderThis(f);
						renderChildren(f);
					}
					else {
						GL11.glTranslatef(rotationPointX * f, rotationPointY * f, rotationPointZ * f);
						renderThis(f);
						renderChildren(f);
						GL11.glTranslatef(-rotationPointX * f, -rotationPointY * f, -rotationPointZ * f);
					}
				}
				else {
					GL11.glPushMatrix();
					GL11.glTranslatef(rotationPointX * f, rotationPointY * f, rotationPointZ * f);

					if(rotateAngleZ != 0F) {
						GL11.glRotatef(rotateAngleZ * (180F / (float) Math.PI), 0F, 0F, 1F);
					}

					if(rotateAngleY != 0F) {
						GL11.glRotatef(rotateAngleY * (180F / (float) Math.PI), 0F, 1F, 0F);
					}

					if(rotateAngleX != 0F) {
						GL11.glRotatef(rotateAngleX * (180F / (float) Math.PI), 1F, 0F, 0F);
					}

					renderThis(f);
					renderChildren(f);
					GL11.glPopMatrix();
				}

				GL11.glTranslatef(-offsetX, -offsetY, -offsetZ);
				GL11.glScalef(1F / scaleX, 1F / scaleY, 1F / scaleZ);
			}
		}

		GL11.glPopMatrix();
	}

	protected void renderThis(float f) {
		if(hideUntil.isEmpty()) {
			GL11.glCallList(displayList);
		}
	}

	protected void renderChildren(float f) {
		if(childModels != null) {
			for(Object childModel : childModels) {
				ModelRendererTF model = (ModelRendererTF) childModel;
				ArrayList<ModelRenderer> list = new ArrayList<>(hideUntil);

				if(hideUntil.contains(model)) {
					list = new ArrayList<>();
				}

				model.hideUntil = list;
				model.render(f);
			}
		}
	}

	public void hideUntil(ModelRenderer... modelRenderers) {
		if(modelRenderers.length == 0) {
			hideUntil.clear();

			if(childModels != null) {
				for(Object childModel : childModels) {
					ModelRendererTF model = (ModelRendererTF) childModel;
					model.hideUntil();
				}
			}

			return;
		}

		hideUntil.addAll(Arrays.asList(modelRenderers));
	}
}
