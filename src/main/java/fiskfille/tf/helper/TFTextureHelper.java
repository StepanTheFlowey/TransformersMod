package fiskfille.tf.helper;

import fiskfille.tf.TransformersMod;
import fiskfille.tf.common.tileentity.TileEntityMachine.EnumIO;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.Locale;

public final class TFTextureHelper {
	public static final ResourceLocation RES_ITEM_GLINT = new ResourceLocation("textures/misc/enchanted_item_glint.png");
	public static IIcon energonFlowingIcon;
	public static IIcon energonStillIcon;
	public static IIcon[] ioIcons;

	public static void onTextureStitch(final TextureMap map) {
		energonFlowingIcon = map.registerIcon(TransformersMod.MODID + ":energon_flow");
		energonStillIcon = map.registerIcon(TransformersMod.MODID + ":energon_still");

		ioIcons = new IIcon[EnumIO.values().length];

		for(final EnumIO io : EnumIO.values()) {
			if(io.ordinal() > 0) {
				ioIcons[io.ordinal()] = map.registerIcon(TransformersMod.MODID + ":io_" + io.name().toLowerCase(Locale.ENGLISH));
			}
		}
	}

	public static ResourceLocation getSkin(final String username) {
		ResourceLocation resourcelocation = AbstractClientPlayer.locationStevePng;

		if(username != null && username.length() > 0) {
			resourcelocation = AbstractClientPlayer.getLocationSkin(username);
			AbstractClientPlayer.getDownloadImageSkin(resourcelocation, username);
		}

		return resourcelocation;
	}

	public static boolean isBoundTexture(final ResourceLocation resourceLocation) {
		final ITextureObject texture = Minecraft.getMinecraft().getTextureManager().getTexture(resourceLocation);

		return texture != null && texture.getGlTextureId() == GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D);
	}
}
