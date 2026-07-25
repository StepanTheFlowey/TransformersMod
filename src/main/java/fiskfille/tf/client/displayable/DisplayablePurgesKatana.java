package fiskfille.tf.client.displayable;

import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.model.item.ModelPurgesKatana;
import fiskfille.tf.common.tick.ClientTickHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import static fiskfille.tf.TransformersMod.mc;

public class DisplayablePurgesKatana extends Displayable {
	private final ModelPurgesKatana model = new ModelPurgesKatana();
	private final ResourceLocation texture = new ResourceLocation(TransformersMod.MODID, "textures/models/purge/purge.png");

	@Override
	public void render(ItemStack itemstack) {
		bindTexture(texture);
		GL11.glRotatef((mc.thePlayer.ticksExisted + ClientTickHandler.renderTick) * 0.75F, 0F, 1F, 0F);
		final float f1 = MathHelper.sin((mc.thePlayer.ticksExisted + ClientTickHandler.renderTick) / 15F) * 0.1F;
		GL11.glTranslatef(0F, 0.7F + f1, -0.55F);
		GL11.glRotatef(-90F, 1F, 0F, 0F);

		model.render();
	}
}
