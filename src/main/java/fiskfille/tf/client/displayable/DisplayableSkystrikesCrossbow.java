package fiskfille.tf.client.displayable;

import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.model.item.ModelSkystrikesCrossbow;
import fiskfille.tf.common.tick.ClientTickHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class DisplayableSkystrikesCrossbow extends Displayable {
	private final ModelSkystrikesCrossbow model = new ModelSkystrikesCrossbow();
	private final ResourceLocation texture = new ResourceLocation(TransformersMod.MODID, "textures/models/weapons/skystrikes_crossbow.png");

	@Override
	public void render(ItemStack itemstack) {
		bindTexture(texture);
		GL11.glScalef(0.5F, 0.5F, 0.5F);
		GL11.glRotatef((Minecraft.getMinecraft().thePlayer.ticksExisted + ClientTickHandler.renderTick) * 0.75F, 0F, 1F, 0F);
		final float f1 = MathHelper.sin((Minecraft.getMinecraft().thePlayer.ticksExisted + ClientTickHandler.renderTick) / 15F) * 0.1F;
		GL11.glTranslatef(0F, 1F + f1, -0.0625F);

		model.render();
	}
}
