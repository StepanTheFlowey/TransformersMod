package fiskfille.tf.client.render.item;

import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.model.item.ModelPurgesKatana;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public final class RenderItemPurgesKatana implements IItemRenderer {
	private final ModelPurgesKatana model = new ModelPurgesKatana();
	private final ResourceLocation texture = new ResourceLocation(TransformersMod.MODID, "textures/models/purge/purge.png");

	@Override
	public boolean handleRenderType(final ItemStack item, final ItemRenderType type) {
		return type != ItemRenderType.ENTITY && type != ItemRenderType.INVENTORY;
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
			case FIRST_PERSON_MAP:
				GL11.glRotatef(210, 0, 0, 1);
				GL11.glTranslatef(-0.7F, 0.2F, 0);

				model.render();
				break;

			case EQUIPPED:
				GL11.glRotatef(5, 1, 0, 0);
				GL11.glRotatef(-5, 0, 1, 0);
				GL11.glRotatef(215, 0, 0, 1);
				GL11.glTranslatef(-0.715F, 0.265F, -0.07F);

				model.render();
				break;
		}
	}
}
