package fiskfille.tf.client.render.item;

import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.model.item.ModelSkystrikesCrossbow;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class RenderItemSkystrikesCrossbow implements IItemRenderer {
	private final ModelSkystrikesCrossbow model = new ModelSkystrikesCrossbow();
	private final ResourceLocation texture = new ResourceLocation(TransformersMod.MODID, "textures/models/weapons/skystrikes_crossbow.png");

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
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);

		if(type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
			if(data[1] instanceof EntityPlayer) {
				if(((EntityPlayer) data[1]).getItemInUseDuration() == 0) {
					GL11.glRotatef(7F, 1F, 0F, 0F);
					GL11.glRotatef(-15F, 0F, 1F, 0F);
					GL11.glRotatef(20F, 0F, 0F, 1F);
					GL11.glTranslatef(0.4F, 0.3F, -0.4F);
				}
				else {
					GL11.glRotatef(-10F, 0F, 1F, 0F);
					GL11.glRotatef(40F, 0F, 0F, 1F);
					GL11.glTranslatef(0.8F, -0.2F, -0.1F);
				}
			}
			else {
				GL11.glRotatef(7F, 1F, 0F, 0F);
				GL11.glRotatef(-15F, 0F, 1F, 0F);
				GL11.glRotatef(20F, 0F, 0F, 1F);
				GL11.glTranslatef(0.4F, 0.3F, -0.4F);
			}

			GL11.glRotatef(110F, 0.2F, 6F, 4F);
			GL11.glRotatef(165F, -2.35F, 0.8F, 0.2F);
		}
		else if(type == ItemRenderType.EQUIPPED) {
			GL11.glRotatef(-90F, 0F, 1F, 0F);
			GL11.glRotatef(180F, 0F, 0F, 1F);
			GL11.glRotatef(-45F, 1F, 0F, 0F);
			GL11.glRotatef(5F, 0F, 1F, 0F);
			GL11.glRotatef(-10F, 1F, 0F, 0F);
			GL11.glTranslatef(0.1F, 0.5F, -0.6F);
		}

		model.render();
	}
}
