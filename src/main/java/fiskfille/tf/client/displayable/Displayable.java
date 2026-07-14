package fiskfille.tf.client.displayable;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import static fiskfille.tf.TransformersMod.mc;

public abstract class Displayable {
	public abstract void render(ItemStack itemstack);

	protected void bindTexture(ResourceLocation resourcelocation) {
		mc.getTextureManager().bindTexture(resourcelocation);
	}
}
