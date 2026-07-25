package fiskfille.tf.common.registry;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import fiskfille.tf.TransformersMod;
import net.minecraft.item.Item;

public class TFItemRegistry {
	public static void registerItem(Item item, String name) {
		if(FMLCommonHandler.instance().getSide().isClient() && item.getCreativeTab() == null) {
			item.setCreativeTab(TransformersMod.tabTransformers);
		}

		registerItemNoTab(item, name);
	}

	public static void registerItemNoTab(Item item, String name) {
		String unlocalizedName = name.toLowerCase().replace(' ', '_').replace("'", "");

		item.setUnlocalizedName(unlocalizedName);
		item.setTextureName(getMod() + ":" + unlocalizedName);
		GameRegistry.registerItem(item, unlocalizedName);
	}

	private static String getMod() {
		return Loader.instance().activeModContainer().getModId();
	}
}
