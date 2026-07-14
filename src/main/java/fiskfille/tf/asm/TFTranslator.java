package fiskfille.tf.asm;

public class TFTranslator {
	public static boolean obfuscatedEnv;

	public static String getMappedName(String name, String devName) {
		return obfuscatedEnv ? name : devName;
	}
}
