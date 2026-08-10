package fiskfille.tf.client.render.item;

import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.model.item.ModelSkystrikesCrossbow;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public final class RenderItemSkystrikesCrossbow implements IItemRenderer {
	private final ModelSkystrikesCrossbow model = new ModelSkystrikesCrossbow();
	private final ResourceLocation texture = new ResourceLocation(TransformersMod.MODID, "textures/models/weapons/skystrikes_crossbow.png");

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
					if(((EntityPlayer) data[1]).getItemInUseDuration() == 0) {
						GL11.glRotatef(7, 1, 0, 0);
						GL11.glRotatef(-15, 0, 1, 0);
						GL11.glRotatef(20, 0, 0, 1);
						GL11.glTranslatef(0.4F, 0.3F, -0.4F);
					}
					else {
						GL11.glRotatef(-10, 0, 1, 0);
						GL11.glRotatef(40, 0, 0, 1);
						GL11.glTranslatef(0.8F, -0.2F, -0.1F);
					}
				}
				else {
					GL11.glRotatef(7, 1, 0, 0);
					GL11.glRotatef(-15, 0, 1, 0);
					GL11.glRotatef(20, 0, 0, 1);
					GL11.glTranslatef(0.4F, 0.3F, -0.4F);
				}

				GL11.glRotatef(110, 0.2F, 6, 4);
				GL11.glRotatef(165, -2.35F, 0.8F, 0.2F);
				break;

			case EQUIPPED:
				GL11.glRotatef(-90, 0, 1, 0);
				GL11.glRotatef(180, 0, 0, 1);
				GL11.glRotatef(-45, 1, 0, 0);
				GL11.glRotatef(5, 0, 1, 0);
				GL11.glRotatef(-10, 1, 0, 0);
				GL11.glTranslatef(0.1F, 0.5F, -0.6F);
				break;
		}

		model.render();
	}
}
