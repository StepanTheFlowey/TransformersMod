package fiskfille.tf.client.render.item;

import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.model.item.ModelFlamethrower;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

public class RenderItemFlamethrower implements IItemRenderer {
	private final ModelFlamethrower model = new ModelFlamethrower();
	private final ResourceLocation texture = new ResourceLocation(TransformersMod.MODID, "textures/models/weapons/flame_thrower.png");

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
			GL11.glTranslatef(1.1F, 0.5F, 0);
			GL11.glRotatef(-5, 1, 0, 0);
			GL11.glRotatef(210, 0, 0, 1);
			GL11.glRotatef(190, 0, 1, 0);

			if(data[1] instanceof EntityPlayer) {
				final EntityPlayer player = (EntityPlayer) data[1];

				if(player.getItemInUseDuration() != 0) {
					GL11.glRotatef(-10, 0, 0, 1);
					GL11.glTranslatef(-0.4F, 0F, 0);
				}
			}

			GL11.glScalef(2F, 2F, 2F);
		}
		else if(type == ItemRenderType.EQUIPPED) {
			GL11.glTranslatef(0.8F, 0.3F, -0.025F);
			GL11.glRotatef(-135, 0, 0, 1);
			GL11.glRotatef(185, 0, 1, 0);
			GL11.glScalef(0.9F, 0.9F, 0.9F);
		}

		model.render();
	}
}
