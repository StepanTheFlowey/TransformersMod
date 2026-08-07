package fiskfille.tf.client.model.item;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelVurpsSniper extends ModelBase {
	private final ModelRenderer ammo;
	private final ModelRenderer backPiece1;
	private final ModelRenderer backPiece2;
	private final ModelRenderer barrel;
	private final ModelRenderer barrelLower;
	private final ModelRenderer bodypart;
	private final ModelRenderer bottomPiece1;
	private final ModelRenderer bottomPiece2;
	private final ModelRenderer handle;
	private final ModelRenderer middlePiece;
	private final ModelRenderer monitorA;
	private final ModelRenderer monitorB;
	private final ModelRenderer muzzle;
	private final ModelRenderer scope;
	private final ModelRenderer scopeholdA;
	private final ModelRenderer scopeholdB;
	private final ModelRenderer upperPiece;

	public ModelVurpsSniper() {
		textureWidth = 64;
		textureHeight = 32;

		backPiece2 = new ModelRenderer(this, 11, 0);
		backPiece2.setRotationPoint(0F, -1.4F, 8F);
		backPiece2.addBox(-0.5F, -1F, -0.5F, 1, 3, 2);

		bodypart = new ModelRenderer(this, 11, 17);
		bodypart.setRotationPoint(0F, -0.6F, -4.8F);
		bodypart.addBox(-1F, -1F, 0F, 2, 2, 4);
		setRotateAngle(bodypart, 0F, 0.7853981633974483F);

		monitorA = new ModelRenderer(this, 12, 11);
		monitorA.setRotationPoint(0F, -4.05F, -1.4F);
		monitorA.addBox(-1F, 0F, 0F, 2, 2, 3);

		muzzle = new ModelRenderer(this, 26, 0);
		muzzle.setRotationPoint(0F, -1.5F, -2F);
		muzzle.addBox(-1F, -1F, -11F, 2, 3, 2);

		scopeholdB = new ModelRenderer(this, 24, 5);
		scopeholdB.setRotationPoint(1.5F, -2F, 3F);
		scopeholdB.addBox(-0.5F, -1.5F, -6F, 1, 1, 1);
		setRotateAngle(scopeholdB, 0F, -0.6981317007977318F);

		bottomPiece1 = new ModelRenderer(this, 0, 17);
		bottomPiece1.setRotationPoint(-1F, -0.3F, 0F);
		bottomPiece1.addBox(-0.5F, -0.5F, -9F, 1, 1, 9);
		setRotateAngle(bottomPiece1, 0F, 1.0471975511965976F);

		scope = new ModelRenderer(this, 24, 8);
		scope.setRotationPoint(1.5F, -2F, 3F);
		scope.addBox(-0.5F, -0.5F, -7F, 1, 1, 7);
		setRotateAngle(scope, 0F, -0.6981317007977318F);

		barrelLower = new ModelRenderer(this, 11, 5);
		barrelLower.setRotationPoint(0F, -1.5F, -2F);
		barrelLower.addBox(-0.5F, 0.8F, -10F, 1, 1, 11);

		upperPiece = new ModelRenderer(this, 42, 17);
		upperPiece.setRotationPoint(0F, -2F, 0F);
		upperPiece.addBox(-0.5F, -0.5F, -9F, 1, 1, 9);

		scopeholdA = new ModelRenderer(this, 24, 5);
		scopeholdA.setRotationPoint(1.5F, -2F, 3F);
		scopeholdA.addBox(-0.5F, -1.5F, -3F, 1, 1, 1);
		setRotateAngle(scopeholdA, 0F, -0.6981317007977318F);

		ammo = new ModelRenderer(this, 0, 16);
		ammo.setRotationPoint(0F, 0.3F, -3F);
		ammo.addBox(-0.5F, -0.7F, 0F, 1, 3, 2);
		setRotateAngle(ammo, -0.2617993877991494F, 0F);

		barrel = new ModelRenderer(this, 6, 0);
		barrel.setRotationPoint(0F, -1.5F, -3F);
		barrel.addBox(-0.5F, -0.5F, -11F, 1, 1, 16);

		monitorB = new ModelRenderer(this, 0, 11);
		monitorB.setRotationPoint(0F, -4.3F, -4.2F);
		monitorB.addBox(-1F, 0F, 0F, 2, 2, 3);
		setRotateAngle(monitorB, -0.08936085770210968F, 0F);

		middlePiece = new ModelRenderer(this, 0, 0);
		middlePiece.setRotationPoint(0F, -1F, 5F);
		middlePiece.addBox(-1F, -1F, -7F, 2, 2, 7);

		bottomPiece2 = new ModelRenderer(this, 0, 17);
		bottomPiece2.setRotationPoint(1F, -0.3F, 0F);
		bottomPiece2.addBox(-0.5F, -0.5F, -9F, 1, 1, 9);
		setRotateAngle(bottomPiece2, 0F, -1.0471975511965976F);

		handle = new ModelRenderer(this, 0, 0);
		handle.setRotationPoint(0F, -0.45F, 3F);
		handle.addBox(-0.5F, 0F, -1F, 1, 3, 2);
		setRotateAngle(handle, 0.4553564018453205F, 0F);

		backPiece1 = new ModelRenderer(this, 14, 2);
		backPiece1.setRotationPoint(0F, -1.4F, 5F);
		backPiece1.addBox(-0.5F, -0.5F, -0.5F, 1, 1, 3);
	}

	@Override
	public void render(final Entity entity, final float f, final float f1, final float f2, final float f3, final float f4, final float f5) {
		backPiece2.render(f5);
		bodypart.render(f5);
		monitorA.render(f5);
		muzzle.render(f5);
		scopeholdB.render(f5);
		bottomPiece1.render(f5);
		scope.render(f5);
		barrelLower.render(f5);
		upperPiece.render(f5);
		scopeholdA.render(f5);
		ammo.render(f5);
		barrel.render(f5);
		monitorB.render(f5);
		middlePiece.render(f5);
		bottomPiece2.render(f5);
		handle.render(f5);
		backPiece1.render(f5);
	}

	public void render() {
		final float f5 = 0.0625F;

		backPiece2.render(f5);
		bodypart.render(f5);
		monitorA.render(f5);
		muzzle.render(f5);
		scopeholdB.render(f5);
		bottomPiece1.render(f5);
		scope.render(f5);
		barrelLower.render(f5);
		upperPiece.render(f5);
		scopeholdA.render(f5);
		ammo.render(f5);
		barrel.render(f5);
		monitorB.render(f5);
		middlePiece.render(f5);
		bottomPiece2.render(f5);
		handle.render(f5);
		backPiece1.render(f5);
	}

	/**
	 * This is a helper function from Tabula to set the rotation of model parts
	 */
	private void setRotateAngle(final ModelRenderer modelRenderer, final float x, final float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = 0;
		modelRenderer.rotateAngleZ = z;
	}
}
