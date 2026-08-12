package fiskfille.tf.client.displayable;

import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.model.item.ModelFlamethrower;
import fiskfille.tf.common.tick.ClientTickHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

final class DisplayableFlamethrower extends Displayable {
	private final ModelFlamethrower model = new ModelFlamethrower();
	private final ResourceLocation texture = new ResourceLocation(TransformersMod.MODID, "textures/models/weapons/flame_thrower.png");

	@Override
	public void render(final ItemStack itemstack) {
		bindTexture(texture);
		GL11.glScalef(0.7F, 0.7F, 0.7F);
		final float ticks = Minecraft.getMinecraft().thePlayer.ticksExisted + ClientTickHandler.renderTick;
		GL11.glRotatef(ticks * 0.75F, 0, 1, 0);
		final float f1 = MathHelper.sin(ticks / 15F) * 0.05F;
		GL11.glTranslatef(-0.2F, 0.95F + f1, 0);

		model.render();
	}
}
