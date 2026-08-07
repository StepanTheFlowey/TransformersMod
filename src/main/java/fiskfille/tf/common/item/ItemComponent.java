package fiskfille.tf.common.item;

import fiskfille.tf.TransformersMod;
import fiskfille.tf.common.component.Component;
import fiskfille.tf.common.component.IComponent;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

public class ItemComponent extends Item implements IComponent {
	public static IIcon outline;
	private final Component component;

	public ItemComponent(final Component c) {
		component = c;
	}

	@Override
	public Component getComponent() {
		return component;
	}

	@Override
	public void registerIcons(final IIconRegister iconRegister) {
		super.registerIcons(iconRegister);
		outline = iconRegister.registerIcon(TransformersMod.MODID + ":component_outline");
	}
}
