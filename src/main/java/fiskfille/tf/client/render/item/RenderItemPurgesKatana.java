package fiskfille.tf.client.render.item;

import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.model.item.ModelPurgesKatana;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class RenderItemPurgesKatana implements IItemRenderer {
	private final ModelPurgesKatana model = new ModelPurgesKatana();
	private final ResourceLocation texture = new ResourceLocation(TransformersMod.MODID, "textures/models/purge/purge.png");

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return type != ItemRenderType.ENTITY && type != ItemRenderType.INVENTORY;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		if(type == ItemRenderType.EQUIPPED_FIRST_PERSON || type == ItemRenderType.FIRST_PERSON_MAP) {
			GL11.glRotatef(210, 0F, 0F, 1F);
			GL11.glTranslatef(-0.7F, 0.2F, -0F);

			model.render();
		}
		else if(type == ItemRenderType.EQUIPPED) {
			GL11.glRotatef(5, 1F, 0F, 0F);
			GL11.glRotatef(-5, 0F, 1F, 0F);
			GL11.glRotatef(215, 0F, 0F, 1F);
			GL11.glTranslatef(-0.715F, 0.265F, -0.07F);

			model.render();
		}
	}
}
