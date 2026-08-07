package fiskfille.tf.client.model.tools;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;

/**
 * @author BobMowzie, gegy1000, FiskFille
 */
@SideOnly(Side.CLIENT)
public class MowzieModelRenderer extends ModelRenderer {
	public ModelRenderer parent;

	public float initRotateAngleX;
	public float initRotateAngleY;
	public float initRotateAngleZ;

	public float initOffsetX;
	public float initOffsetY;
	public float initOffsetZ;

	public float initRotationPointX;
	public float initRotationPointY;
	public float initRotationPointZ;

	public float scaleX = 1F;
	public float scaleY = 1F;
	public float scaleZ = 1F;
	public boolean hasInitPose;

	protected int displayList;
	protected boolean compiled;

	public MowzieModelRenderer(final ModelBase modelBase, final String name) {
		super(modelBase, name);
	}

	public MowzieModelRenderer(final ModelBase modelBase, final int x, final int y) {
		super(modelBase, x, y);

		if(modelBase instanceof MowzieModelBase) {
			((MowzieModelBase) modelBase).addPart(this);
		}
	}

	public MowzieModelRenderer(final ModelBase modelBase) {
		super(modelBase);
	}

	@Override
	public void addChild(final ModelRenderer renderer) {
		super.addChild(renderer);

		if(renderer instanceof MowzieModelRenderer) {
			((MowzieModelRenderer) renderer).setParent(this);
		}
	}

	public void postRenderParentChain(final float f) {
		if(parent instanceof MowzieModelRenderer) {
			((MowzieModelRenderer) parent).postRenderParentChain(f);
		}
		else if(parent != null) {
			parent.postRender(f);
		}

		postRender(f);
	}

	/**
	 * Returns the parent of this ModelRenderer
	 */
	public ModelRenderer getParent() {
		return parent;
	}

	/**
	 * Sets the parent of this ModelRenderer
	 */
	private void setParent(final ModelRenderer modelRenderer) {
		parent = modelRenderer;
	}

	/**
	 * Set the initialization pose to the current pose
	 */
	public void setInitValuesToCurrentPose() {
		initRotateAngleX = rotateAngleX;
		initRotateAngleY = rotateAngleY;
		initRotateAngleZ = rotateAngleZ;

		initRotationPointX = rotationPointX;
		initRotationPointY = rotationPointY;
		initRotationPointZ = rotationPointZ;

		initOffsetX = offsetX;
		initOffsetY = offsetY;
		initOffsetZ = offsetZ;

		hasInitPose = true;
	}

	/**
	 * Resets the pose to init pose
	 */
	public void setCurrentPoseToInitValues() {
		if(hasInitPose) {
			rotateAngleX = initRotateAngleX;
			rotateAngleY = initRotateAngleY;
			rotateAngleZ = initRotateAngleZ;

			rotationPointX = initRotationPointX;
			rotationPointY = initRotationPointY;
			rotationPointZ = initRotationPointZ;

			offsetX = initOffsetX;
			offsetY = initOffsetY;
			offsetZ = initOffsetZ;
		}
	}

	public void setRotationAngles(final float x, final float y, final float z) {
		rotateAngleX = x;
		rotateAngleY = y;
		rotateAngleZ = z;
	}

	/**
	 * Resets all rotation points.
	 */
	public void resetAllRotationPoints() {
		rotationPointX = initRotationPointX;
		rotationPointY = initRotationPointY;
		rotationPointZ = initRotationPointZ;
	}

	/**
	 * Resets X rotation point.
	 */
	public void resetXRotationPoints() {
		rotationPointX = initRotationPointX;
	}

	/**
	 * Resets Y rotation point.
	 */
	public void resetYRotationPoints() {
		rotationPointY = initRotationPointY;
	}

	/**
	 * Resets Z rotation point.
	 */
	public void resetZRotationPoints() {
		rotationPointZ = initRotationPointZ;
	}

	/**
	 * Resets all rotations.
	 */
	public void resetAllRotations() {
		rotateAngleX = initRotateAngleX;
		rotateAngleY = initRotateAngleY;
		rotateAngleZ = initRotateAngleZ;
	}

	/**
	 * Resets X rotation.
	 */
	public void resetXRotations() {
		rotateAngleX = initRotateAngleX;
	}

	/**
	 * Resets Y rotation.
	 */
	public void resetYRotations() {
		rotateAngleY = initRotateAngleY;
	}

	/**
	 * Resets Z rotation.
	 */
	public void resetZRotations() {
		rotateAngleZ = initRotateAngleZ;
	}

	/**
	 * Copies the rotation point coordinates.
	 */
	public void copyAllRotationPoints(final MowzieModelRenderer target) {
		rotationPointX = target.rotationPointX;
		rotationPointY = target.rotationPointY;
		rotationPointZ = target.rotationPointZ;
	}

	/**
	 * Copies X rotation point.
	 */
	public void copyXRotationPoint(final MowzieModelRenderer target) {
		rotationPointX = target.rotationPointX;
	}

	/**
	 * Copies Y rotation point.
	 */
	public void copyYRotationPoint(final MowzieModelRenderer target) {
		rotationPointY = target.rotationPointY;
	}

	/**
	 * Copies Z rotation point.
	 */
	public void copyZRotationPoint(final MowzieModelRenderer target) {
		rotationPointZ = target.rotationPointZ;
	}

	public void renderWithParents(final float partialTicks) {
		if(parent instanceof MowzieModelRenderer) {
			((MowzieModelRenderer) parent).renderWithParents(partialTicks);
		}
		else if(parent != null) {
			parent.render(partialTicks);
		}

		render(partialTicks);
	}

	public void setScale(final float x, final float y, final float z) {
		scaleX = x;
		scaleY = y;
		scaleZ = z;
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
			GL11.glRotatef((float) Math.toDegrees( rotateAngleZ), 0, 0, 1);
			GL11.glRotatef((float) Math.toDegrees( rotateAngleY), 0, 1, 0);
			GL11.glRotatef((float) Math.toDegrees( rotateAngleX), 1, 0, 0);

			GL11.glCallList(displayList);
			if(childModels != null) {
				for(Object childModel : childModels) {
					((MowzieModelRenderer) childModel).render(f);
				}
			}

			GL11.glPopMatrix();
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void postRender(float f) {
		if(!isHidden && showModel) {
			if(!compiled) {
				compileDisplayList(f);
			}

			GL11.glTranslatef(rotationPointX * f, rotationPointY * f, rotationPointZ * f);
			GL11.glTranslatef(offsetX, offsetY, offsetZ);
			GL11.glRotatef((float) Math.toDegrees(rotateAngleZ), 0, 0, 1);
			GL11.glRotatef((float) Math.toDegrees(rotateAngleY), 0, 1, 0);
			GL11.glRotatef((float) Math.toDegrees(rotateAngleX), 1, 0, 0);
		}
	}

	@SideOnly(Side.CLIENT)
	protected void compileDisplayList(final float f) {
		displayList = GLAllocation.generateDisplayLists(1);
		GL11.glNewList(displayList, GL11.GL_COMPILE);

		final Tessellator tessellator = Tessellator.instance;
		for(final Object cube : cubeList) {
			((ModelBox) cube).render(tessellator, f);
		}

		GL11.glEndList();
		compiled = true;
	}

	public void renderWithParentRotations(final float partialTicks) {
		final float x = getParentRotX();
		final float y = getParentRotY();
		final float z = getParentRotZ();

		rotateAngleX -= x;
		rotateAngleY -= y;
		rotateAngleZ -= z;

		render(partialTicks);

		rotateAngleX += x;
		rotateAngleY += y;
		rotateAngleZ += z;
	}

	public float getParentRotX() {
		if(getParent() instanceof MowzieModelRenderer) {
			return ((MowzieModelRenderer) getParent()).getParentRotX() + rotateAngleX;
		}

		return rotateAngleX;
	}

	public float getParentRotY() {
		if(getParent() instanceof MowzieModelRenderer) {
			return ((MowzieModelRenderer) getParent()).getParentRotY() + rotateAngleY;
		}

		return rotateAngleY;
	}

	public float getParentRotZ() {
		if(getParent() instanceof MowzieModelRenderer) {
			return ((MowzieModelRenderer) getParent()).getParentRotZ() + rotateAngleZ;
		}

		return rotateAngleZ;
	}
}
