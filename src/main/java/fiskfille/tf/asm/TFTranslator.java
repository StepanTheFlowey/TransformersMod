package fiskfille.tf.asm;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TFTranslator {
	public static boolean obfuscatedEnv;

	public static String getMappedName(final String name, final String devName) {
		return obfuscatedEnv ? name : devName;
	}
}
