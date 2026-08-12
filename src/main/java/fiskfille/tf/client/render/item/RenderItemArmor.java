package fiskfille.tf.client.render.item;

import fiskfille.tf.client.model.transformer.ModelTransformerBase;
import fiskfille.tf.client.model.transformer.definition.TFModelRegistry;
import fiskfille.tf.client.model.transformer.definition.TransformerModel;
import fiskfille.tf.common.transformer.base.Transformer;
import fiskfille.tf.helper.TFArmorDyeHelper;
import fiskfille.tf.helper.TFRenderHelper;
import fiskfille.tf.helper.TFTextureHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public final class RenderItemArmor implements IItemRenderer {
	private final Transformer transformer;
	private final int armorPiece;

	public RenderItemArmor(final Transformer transformer, final int armorPiece) {
		this.transformer = transformer;
		this.armorPiece = armorPiece;
	}

	@Override
	public boolean handleRenderType(final ItemStack item, final ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(final ItemRenderType type, final ItemStack item, final ItemRendererHelper helper) {
		return type == ItemRenderType.ENTITY || type == ItemRenderType.EQUIPPED_FIRST_PERSON;
	}

	@Override
	public void renderItem(final ItemRenderType type, final ItemStack item, final Object... data) {
		final TransformerModel tfModel = TFModelRegistry.getModel(transformer);
		final ModelTransformerBase model = tfModel.getItemInventoryModel();

		switch(armorPiece) {
			case 0:
				GL11.glScalef(1.5F, 1.5F, 1.5F);
				break;

			case 1:
				GL11.glScalef(0.8F, 0.8F, 0.8F);
				break;
		}

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		if(type == ItemRenderType.INVENTORY) {
			RenderHelper.enableGUIStandardItemLighting();
		}

		final TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
		if(TFArmorDyeHelper.isDyed(item)) {
			textureManager.bindTexture(tfModel.getTexture(null, "_primary"));
			TFRenderHelper.glColor(TFArmorDyeHelper.getPrimaryColor(item));
			renderArmor(type, model);

			textureManager.bindTexture(tfModel.getTexture(null, "_secondary"));
			TFRenderHelper.glColor(TFArmorDyeHelper.getSecondaryColor(item));
			renderArmor(type, model);

			textureManager.bindTexture(tfModel.getTexture(null, "_base"));
			GL11.glColor3f(1, 1, 1);
		}
		else {
			textureManager.bindTexture(tfModel.getTexture(null, ""));
		}

		renderArmor(type, model);

		if(tfModel.hasLightsLayer()) {
			textureManager.bindTexture(tfModel.getTexture(null, "_lights"));
			TFRenderHelper.setLighting(TFRenderHelper.LIGHTING_LUMINOUS);
			renderArmor(type, model);
			TFRenderHelper.resetLighting();
		}

		if(item.hasEffect(0)) {
			textureManager.bindTexture(TFTextureHelper.RES_ITEM_GLINT);
			GL11.glColor3f(0.5F, 0.5F, 0.5F);
			GL11.glDepthFunc(GL11.GL_EQUAL);
			GL11.glDepthMask(false);

			final float f2 = 0.33333334F;
			for(int i = 0; i < 2; ++i) {
				final float f1 = (Minecraft.getSystemTime() % (3000F + i * 1873) / (3000 + i * 1873) * 256) / 64;

				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glColor3f(0.5F, 0.25F, 0.8F);
				GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
				GL11.glMatrixMode(GL11.GL_TEXTURE);
				GL11.glLoadIdentity();
				GL11.glScalef(f2, f2, f2);
				GL11.glRotatef(30 - i * 60, 0, 0, 1);
				GL11.glTranslatef(0, f1, 0);
				GL11.glMatrixMode(GL11.GL_MODELVIEW);
				renderArmor(type, model);
			}

			GL11.glColor3f(1, 1, 1);
			GL11.glMatrixMode(GL11.GL_TEXTURE);
			GL11.glDepthMask(true);
			GL11.glLoadIdentity();
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glDepthFunc(GL11.GL_LEQUAL);
			GL11.glEnable(GL11.GL_ALPHA_TEST);
		}

		GL11.glDisable(GL11.GL_BLEND);
	}

	private void renderArmor(final ItemRenderType type, final ModelTransformerBase model) {
		GL11.glPushMatrix();
		switch(type) {
			case EQUIPPED_FIRST_PERSON:
				GL11.glRotatef(180, 1, 0, 0);
				GL11.glRotatef(210, 0, 1, 0);
				GL11.glRotatef(10, 0, 0, 1);
				GL11.glTranslatef(-0.9F, -1, 0.2F);

				switch(armorPiece) {
					case 0:
						GL11.glTranslatef(0.5F, 0.5F, 0.1F);
						break;

					case 1:
						GL11.glTranslatef(-0.2F, -0.4F, 0);
						break;

					case 2:
						GL11.glTranslatef(0, -0.2F, 0);
						break;
				}

				model.renderArmorPiece(armorPiece);
				break;

			case EQUIPPED:
				GL11.glRotatef(180, 1, 0, 0);
				GL11.glRotatef(-45, 0, 1, 0);
				GL11.glRotatef(-45, 0, 0, 1);
				GL11.glTranslatef(0.5F, -0.5F, 0);

				switch(armorPiece) {
					case 0:
						GL11.glTranslatef(-0.25F, 0.6F, -0.2F);
						break;

					case 1:
						GL11.glTranslatef(-0.1F, 0.1F, -0.4F);
						break;

					case 2:
						GL11.glTranslatef(0.1F, 0.3F, -0.35F);
						GL11.glRotatef(35, 0, 0, 1);
						break;

					case 3:
						GL11.glTranslatef(-0.05F, 0.3F, -0.3F);
						GL11.glRotatef(35, 0, 0, 1);
						break;
				}

				GL11.glScalef(0.7F, 0.7F, 0.7F);
				model.renderArmorPiece(armorPiece);
				break;

			case INVENTORY:
				GL11.glScalef(10, 10, 10);
				GL11.glTranslatef(0.5F, 0.5F, 1);
				GL11.glScalef(1, 1, -1);

				switch(armorPiece) {
					case 0:
						GL11.glTranslatef(0.03125F, 0, 0);
						GL11.glScalef(2, 2, 2);
						GL11.glTranslatef(0, 0.125F, 0);
						break;

					case 1:
						GL11.glTranslatef(0.5F, -0.1F, 0);
						GL11.glScalef(2, 2, 2);
						break;

					case 2:
						GL11.glTranslatef(0.325F, -0.6F, 0);
						GL11.glScalef(2, 2, 2);
						break;

					case 3:
						GL11.glTranslatef(0.3125F, 0, 0);
						GL11.glScalef(2, 2, 2);
						GL11.glTranslatef(0, -0.125F, 0);
						break;
				}

				model.renderArmorPiece(armorPiece);
				break;

			case ENTITY:
				GL11.glRotatef(180, 1, 0, 0);
				GL11.glRotatef(-90, 0, 1, 0);
				GL11.glTranslatef(0, -1, 0.1F);

				switch(armorPiece) {
					case 0:
						GL11.glTranslatef(0, 1.125F, 0);
						break;

					case 1:
						GL11.glTranslatef(0, 0.6F, -0.2F);
						break;

					case 2:
						GL11.glTranslatef(0, 0.55F, -0.1F);
						break;

					case 3:
						GL11.glTranslatef(0, 0.7F, -0.1F);
						break;
				}

				model.renderArmorPiece(armorPiece);
				break;
		}
		GL11.glPopMatrix();
	}
}
