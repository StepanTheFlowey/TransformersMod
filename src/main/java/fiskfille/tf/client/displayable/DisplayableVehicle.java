package fiskfille.tf.client.displayable;

import fiskfille.tf.TransformersAPI;
import fiskfille.tf.client.model.transformer.definition.TFModelRegistry;
import fiskfille.tf.client.model.transformer.definition.TransformerModel;
import fiskfille.tf.client.model.transformer.vehicle.ModelVehicleBase;
import fiskfille.tf.common.tick.ClientTickHandler;
import fiskfille.tf.common.transformer.base.Transformer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class DisplayableVehicle extends Displayable {
	@Override
	public void render(ItemStack itemstack) {
		final ModelVehicleBase vehicle = getModelFromStack(itemstack).getVehicleModel();
		if(vehicle == null) {
			return;
		}

		GL11.glRotatef((mc.thePlayer.ticksExisted + ClientTickHandler.renderTick) * 0.5F, 0F, 1F, 0F);
		GL11.glTranslatef(0F, -0.2F, 0F);
		final float scale = 0.75F;
		GL11.glScalef(scale, scale, scale);

		vehicle.renderDisplayVehicle(itemstack);
	}

	private TransformerModel getModelFromStack(ItemStack displayItem) {
		final Transformer transformer = TransformersAPI.getTransformers().get(displayItem.getItemDamage());

		if(transformer != null) {
			return TFModelRegistry.getModel(transformer);
		}

		return null;
	}
}
