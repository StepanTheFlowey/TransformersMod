package fiskfille.tf.client.render.item;

import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.model.item.ModelVurpsSniper;
import fiskfille.tf.common.data.TFDataManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public final class RenderItemVurpsSniper implements IItemRenderer {
	private final ModelVurpsSniper model = new ModelVurpsSniper();
	private final ResourceLocation texture = new ResourceLocation(TransformersMod.MODID, "textures/models/weapons/sniper.png");

	@Override
	public boolean handleRenderType(final ItemStack item, final ItemRenderType type) {
		return type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON;
	}

	@Override
	public boolean shouldUseRenderHelper(final ItemRenderType type, final ItemStack item, final ItemRendererHelper helper) {
		return false;
	}

	@Override
	public void renderItem(final ItemRenderType type, final ItemStack item, final Object... data) {
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		switch(type) {
			case EQUIPPED_FIRST_PERSON:
				if(data[1] instanceof EntityPlayer) {
					switch(TFDataManager.getZoomTimer((EntityPlayer) data[1])) {
						case 7:
							GL11.glTranslatef(0.1F, 0.45F, -1);
							GL11.glRotatef(210, 0, 0, 1);
							GL11.glRotatef(95, 0, 1, 0);
							break;

						case 6:
							GL11.glTranslatef(0.6F, 0.7F, -0.95F);
							GL11.glRotatef(210, 0, 0, 1);
							GL11.glRotatef(95, 0, 1, 0);
							break;

						case 5:
							GL11.glTranslatef(1, 0.9F, -0.9F);
							GL11.glRotatef(210, 0, 0, 1);
							GL11.glRotatef(95, 0, 1, 0);
							break;

						case 4:
							GL11.glTranslatef(1.1F, 0.9F, -0.6F);
							GL11.glRotatef(200, 0, 0, 1);
							GL11.glRotatef(105, 0, 1, 0);
							break;

						case 3:
							GL11.glTranslatef(1.2F, 0.8F, -0.45F);
							GL11.glRotatef(-5, 1, 0, 0);
							GL11.glRotatef(200, 0, 0, 1);
							GL11.glRotatef(105, 0, 1, 0);
							break;

						case 2:
							GL11.glTranslatef(1.2F, 0.7F, -0.3F);
							GL11.glRotatef(-10, 1, 0, 0);
							GL11.glRotatef(200, 0, 0, 1);
							GL11.glRotatef(100, 0, 1, 0);
							break;

						case 1:
							GL11.glTranslatef(1.2F, 0.6F, -0.15F);
							GL11.glRotatef(-10, 1, 0, 0);
							GL11.glRotatef(200, 0, 0, 1);
							GL11.glRotatef(95, 0, 1, 0);
							break;

						case 0:
							GL11.glTranslatef(1.2F, 0.6F, 0);
							GL11.glRotatef(-10, 1, 0, 0);
							GL11.glRotatef(200, 0, 0, 1);
							GL11.glRotatef(90, 0, 1, 0);
							break;
					}
					GL11.glScalef(2, 2, 2);

					model.render();
				}
				break;

			case EQUIPPED:
				GL11.glTranslatef(0.86F, 0.5F, 0.05F);
				GL11.glRotatef(-135, 0, 0, 1);
				GL11.glRotatef(95, 0, 1, 0);
				GL11.glScalef(0.9F, 0.9F, 0.9F);

				model.render();
				break;
		}
	}
}
