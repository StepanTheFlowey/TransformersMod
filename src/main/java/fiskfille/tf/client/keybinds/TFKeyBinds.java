package fiskfille.tf.client.keybinds;

import cpw.mods.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

public class TFKeyBinds {
	public static final TFKeyBinding keyBindingTransform1 = new TFKeyBinding("key.transform1", Keyboard.KEY_C);
	public static final TFKeyBinding keyBindingStealthMode = new TFKeyBinding("key.stealth_force", Keyboard.KEY_V);
	public static final TFKeyBinding keyBindingZoom = new TFKeyBinding("key.aim", Keyboard.KEY_B);
	public static final TFKeyBinding keyBindingBrake = new TFKeyBinding("key.brake", Keyboard.KEY_Z);
	public static final TFKeyBinding keyBindingVehicleFirstPerson = new TFKeyBinding("key.first_person", Keyboard.KEY_G);
	public static final TFKeyBinding keyBindingViewFront = new TFKeyBinding("key.rear_view", Keyboard.KEY_R);

	public static void register() {
		ClientRegistry.registerKeyBinding(keyBindingTransform1);
		ClientRegistry.registerKeyBinding(keyBindingStealthMode);
		ClientRegistry.registerKeyBinding(keyBindingZoom);
		ClientRegistry.registerKeyBinding(keyBindingBrake);
		ClientRegistry.registerKeyBinding(keyBindingVehicleFirstPerson);
		ClientRegistry.registerKeyBinding(keyBindingViewFront);
	}
}
