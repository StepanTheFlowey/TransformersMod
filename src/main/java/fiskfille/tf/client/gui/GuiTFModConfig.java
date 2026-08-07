package fiskfille.tf.client.gui;

import cpw.mods.fml.client.config.DummyConfigElement;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import fiskfille.tf.TransformersMod;
import fiskfille.tf.config.TFConfig;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;

import java.util.ArrayList;
import java.util.List;

public class GuiTFModConfig extends GuiConfig {
	public GuiTFModConfig(final GuiScreen parent) {
		super(parent, getConfigElements(), TransformersMod.MODID, false, false, "Transformers Mod Configuration");
	}

	/**
	 * Compiles a list of config elements
	 */
	private static List<IConfigElement> getConfigElements() {
		final List<IConfigElement> elements = new ArrayList<>();

		elements.add(categoryElement("Options", "General", "General Options"));
		elements.add(categoryElement("Aesthetic", "Aesthetic", "Aesthetic Options"));
		elements.add(categoryElement("Projectiles", "Projectiles", "Projectile Options"));
		elements.add(categoryElement("Transformation", "Transformation", "Transformation Options"));

		return elements;
	}

	/**
	 * Creates a button linking to another screen where all options of the category are available
	 */
	private static IConfigElement categoryElement(final String category, final String name, final String tooltip_key) {
		return new DummyConfigElement.DummyCategoryElement(name, tooltip_key, new ConfigElement(TFConfig.configFile.getCategory(category.toLowerCase())).getChildElements());
	}
}
