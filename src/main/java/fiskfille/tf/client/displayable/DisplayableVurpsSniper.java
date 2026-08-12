package fiskfille.tf.client.displayable;

import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.model.item.ModelVurpsSniper;
import fiskfille.tf.common.tick.ClientTickHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

final class DisplayableVurpsSniper extends Displayable {
	private final ModelVurpsSniper model = new ModelVurpsSniper();
	private final ResourceLocation texture = new ResourceLocation(TransformersMod.MODID, "textures/models/weapons/sniper.png");

	@Override
	public void render(final ItemStack itemstack) {
		bindTexture(texture);
		GL11.glScalef(0.75F, 0.75F, 0.75F);
		final float ticks = Minecraft.getMinecraft().thePlayer.ticksExisted + ClientTickHandler.renderTick;
		GL11.glRotatef(ticks * 0.75F, 0, 1, 0);
		final float f1 = MathHelper.sin(ticks / 15F) * 0.1F;
		GL11.glTranslatef(0, 0.95F + f1, 0.1F);

		model.render();
	}
}
