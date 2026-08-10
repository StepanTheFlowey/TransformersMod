package fiskfille.tf.client.render.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fiskfille.tf.helper.TFHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.player.EntityPlayer;

@SideOnly(Side.CLIENT)
public final class EntityRendererTF extends EntityRenderer {
	public EntityRendererTF(final Minecraft mc) {
		super(mc, mc.getResourceManager());
	}

	@Override
	public void updateCameraAndRender(final float partialTick) {
		final EntityPlayer player = Minecraft.getMinecraft().thePlayer;
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
