package fiskfille.tf.client.render.entity;

import fiskfille.tf.helper.TFHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.player.EntityPlayer;

public class EntityRendererTF extends EntityRenderer {
	private static final Minecraft mc = Minecraft.getMinecraft();

	public EntityRendererTF(Minecraft mc) {
		super(mc, mc.getResourceManager());
	}

	@Override
	public void updateCameraAndRender(float partialTick) {
		final EntityPlayer player = mc.thePlayer;
		if(player == null || player.isPlayerSleeping()) {
			super.updateCameraAndRender(partialTick);
			return;
		}

		final float scale = TFHelper.getHeight(player) / 1.8F;
		player.yOffset = -(scale - 1F) * 1.62F + 1.62F;
		super.updateCameraAndRender(partialTick);
		player.yOffset = 1.62F;
	}
}
