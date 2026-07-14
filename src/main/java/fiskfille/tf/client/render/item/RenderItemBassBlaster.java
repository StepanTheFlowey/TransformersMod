package fiskfille.tf.client.render.item;

import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.model.item.ModelBassBlaster;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class RenderItemBassBlaster implements IItemRenderer {
	private final ModelBassBlaster model = new ModelBassBlaster();
	private final ResourceLocation texture = new ResourceLocation(TransformersMod.modid, "textures/models/weapons/bass_blaster.png");

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		TransformersMod.mc.renderEngine.bindTexture(texture);

		if(type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
			GL11.glTranslatef(0.8F, 0.6F, 0.2F);
			GL11.glRotatef(-5, 1, 0, 0);
			GL11.glRotatef(210, 0, 0, 1);
			GL11.glRotatef(90, 0, 1, 0);
			GL11.glScalef(1.8F, 1.8F, 1.8F);
		}
		else if(type == ItemRenderType.EQUIPPED) {
			GL11.glTranslatef(0.8F, 0.29F, -0.025F);
			GL11.glRotatef(4, 0, 0, 1);
			GL11.glRotatef(-95, 0, 1, 0);
			GL11.glRotatef(30, 1, 0, 0);
		}

		model.render();
	}
}
