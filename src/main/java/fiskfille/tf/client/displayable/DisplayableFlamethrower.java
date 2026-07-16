package fiskfille.tf.client.displayable;

import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.model.item.ModelFlamethrower;
import fiskfille.tf.common.tick.ClientTickHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import static fiskfille.tf.TransformersMod.mc;

public class DisplayableFlamethrower extends Displayable {
	private final ModelFlamethrower model = new ModelFlamethrower();
	private final ResourceLocation texture = new ResourceLocation(TransformersMod.modid, "textures/models/weapons/flame_thrower.png");

	@Override
	public void render(ItemStack itemstack) {
		bindTexture(texture);
		GL11.glScalef(0.7F, 0.7F, 0.7F);
		GL11.glRotatef((mc.thePlayer.ticksExisted + ClientTickHandler.renderTick) * 0.75F, 0F, 1F, 0F);
		final float f1 = MathHelper.sin((mc.thePlayer.ticksExisted + ClientTickHandler.renderTick) / 15F) * 0.05F;
		GL11.glTranslatef(-0.2F, 0.95F + f1, 0F);

		model.render();
	}
}
