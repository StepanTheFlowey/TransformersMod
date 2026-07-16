package fiskfille.tf.client.displayable;

import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.model.item.ModelVurpsSniper;
import fiskfille.tf.common.tick.ClientTickHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import static fiskfille.tf.TransformersMod.mc;

public class DisplayableVurpsSniper extends Displayable {
	private final ModelVurpsSniper model = new ModelVurpsSniper();
	private final ResourceLocation texture = new ResourceLocation(TransformersMod.modid, "textures/models/weapons/sniper.png");

	@Override
	public void render(ItemStack itemstack) {
		bindTexture(texture);
		GL11.glScalef(0.75F, 0.75F, 0.75F);
		GL11.glRotatef((mc.thePlayer.ticksExisted + ClientTickHandler.renderTick) * 0.75F, 0F, 1F, 0F);
		final float f1 = MathHelper.sin((mc.thePlayer.ticksExisted + ClientTickHandler.renderTick) / 15F) * 0.1F;
		GL11.glTranslatef(0F, 0.95F + f1, 0.1F);

		model.render();
	}
}
