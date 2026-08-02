package fiskfille.tf.common.energon;

import fiskfille.tf.TransformersAPI;

public class TFEnergonManager {
	public static final Energon energon = new DefaultEnergon();
	public static final Energon redEnergon = new RedEnergon();

	public static void registerEnergonTypes() {
		TransformersAPI.registerEnergonType(energon);
		TransformersAPI.registerEnergonType(redEnergon);
	}
}
