package fiskfille.tf.client.displayable;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public abstract class Displayable {
	public abstract void render(ItemStack itemstack);

	protected void bindTexture(ResourceLocation resourcelocation) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(resourcelocation);
	}
}
