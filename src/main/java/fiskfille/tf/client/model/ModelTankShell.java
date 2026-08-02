package fiskfille.tf.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelTankShell extends ModelBase {
	private final ModelRenderer shell;

	public ModelTankShell() {
		textureWidth = 16;
		textureHeight = 8;

		shell = new ModelRenderer(this, 0, 0);
		shell.addBox(-3, -1, -1, 6, 2, 2);
		shell.rotateAngleY = (float) Math.PI / 2;
	}

	public void render() {
		shell.render(0.0625F);
	}
}
