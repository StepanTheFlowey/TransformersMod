package fiskfille.tf.client.displayable;

import fiskfille.tf.TransformersMod;
import fiskfille.tf.client.model.item.ModelBassBlaster;
import fiskfille.tf.common.tick.ClientTickHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class DisplayableBassBlaster extends Displayable {
	private final ModelBassBlaster model = new ModelBassBlaster();
	private final ResourceLocation texture = new ResourceLocation(TransformersMod.MODID, "textures/models/weapons/bass_blaster.png");

	@Override
	public void render(ItemStack itemstack) {
		bindTexture(texture);
		GL11.glScalef(0.75F, 0.75F, 0.75F);
		GL11.glRotatef(Minecraft.getMinecraft().thePlayer.ticksExisted * 0.75F, 0, 1, 0);
		final float f1 = MathHelper.sin((Minecraft.getMinecraft().thePlayer.ticksExisted + ClientTickHandler.renderTick) / 15F) * 0.1F;
		GL11.glTranslatef(0, 0.95F + f1, 0.1F);

		model.render();
	}
}
