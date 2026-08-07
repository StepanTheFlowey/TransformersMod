package fiskfille.tf.asm;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;

@SideOnly(Side.CLIENT)
public class ASMHelper {
	public static MethodInsnNode divide(final String type) {
		return new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(ASMHelper.class), "divide", "(" + type.toUpperCase() + type.toUpperCase() + ")" + type.toUpperCase(), false);
	}

	public static int divide(final int arg1, final int arg2) {
		return arg1 / arg2;
	}

	public static float divide(final float arg1, final float arg2) {
		return arg1 / arg2;
	}

	public static double divide(final double arg1, final double arg2) {
		return arg1 / arg2;
	}

	public static MethodInsnNode multiply(final String type) {
		return new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(ASMHelper.class), "multiply", "(" + type.toUpperCase() + type.toUpperCase() + ")" + type.toUpperCase(), false);
	}

	public static int multiply(final int arg1, final int arg2) {
		return arg1 * arg2;
	}

	public static float multiply(final float arg1, final float arg2) {
		return arg1 * arg2;
	}

	public static double multiply(final double arg1, final double arg2) {
		return arg1 * arg2;
	}

	public static TypeInsnNode cast(final String to) {
		return new TypeInsnNode(Opcodes.CHECKCAST, to);
	}

	public static MethodInsnNode and() {
		return new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(ASMHelper.class), "and", "(ZZ)Z", false);
	}

	public static boolean and(final boolean arg1, final boolean arg2) {
		return arg1 && arg2;
	}

	public static MethodInsnNode or() {
		return new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(ASMHelper.class), "or", "(ZZ)Z", false);
	}

	public static boolean or(final boolean arg1, final boolean arg2) {
		return arg1 || arg2;
	}

	public static MethodInsnNode not() {
		return new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(ASMHelper.class), "not", "(Z)Z", false);
	}

	public static boolean not(final boolean arg) {
		return !arg;
	}

	public static MethodInsnNode conditional() {
		return new MethodInsnNode(Opcodes.INVOKESTATIC, Type.getInternalName(ASMHelper.class), "conditional", "(ZLjava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", false);
	}

	public static Object conditional(final boolean condition, final Object arg1, final Object arg2) {
		return condition ? arg1 : arg2;
	}
}
