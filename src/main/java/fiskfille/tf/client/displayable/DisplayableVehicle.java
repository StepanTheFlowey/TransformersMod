package fiskfille.tf.client.displayable;

import fiskfille.tf.TransformersAPI;
import fiskfille.tf.client.model.transformer.definition.TFModelRegistry;
import fiskfille.tf.common.tick.ClientTickHandler;
import fiskfille.tf.common.transformer.base.Transformer;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class DisplayableVehicle extends Displayable {
	@Override
	public void render(ItemStack itemstack) {
		final Transformer transformer = TransformersAPI.getTransformers().get(itemstack.getItemDamage());
		if(transformer == null) {
			return;
		}

		GL11.glRotatef((Minecraft.getMinecraft().thePlayer.ticksExisted + ClientTickHandler.renderTick) * 0.5F, 0F, 1F, 0F);
		GL11.glTranslatef(0F, -0.2F, 0F);
		GL11.glScalef(0.75F, 0.75F, 0.75F);

		TFModelRegistry.getModel(transformer).getVehicleModel().renderDisplayVehicle(itemstack);
	}
}
