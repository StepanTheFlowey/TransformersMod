package fiskfille.tf;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.Entity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TFReflection {
	public static Method renderHandMethod;
	public static Field thirdPersonDistanceField;
	public static Method setSizeMethod;

	@SideOnly(Side.CLIENT)
	public static void client() {
		renderHandMethod = getMethod(EntityRenderer.class, "renderHand", "func_78476_b");
		thirdPersonDistanceField = getField(EntityRenderer.class, "thirdPersonDistance", "field_78490_B");
	}

	public static void common() {
		setSizeMethod = getMethod(Entity.class, "setSize", "func_70105_a");
	}

	public static Method getMethod(final Class clazz, final String... names) {
		for(final String name : names) {
			for(final Method method : clazz.getDeclaredMethods()) {
				if(method.getName().equals(name)) {
					method.setAccessible(true);
					return method;
				}
			}
		}

		return null;
	}

	public static Field getField(final Class clazz, final String... names) {
		for(final String name : names) {
			for(final Field field : clazz.getDeclaredFields()) {
				if(field.getName().equals(name)) {
					field.setAccessible(true);
					return field;
				}
			}
		}

		return null;
	}

	public static Object getField(final Object obj, final Field field) {
		try {
			return field.get(obj);
		}
		catch(final Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void renderHand(final EntityRenderer obj, final float f, final int i) {
		try {
			renderHandMethod.invoke(obj, f, i);
		}
		catch(final Exception e) {
			e.printStackTrace();
		}
	}

	public static void setSize(final Entity obj, final float f, final float f1) {
		try {
			setSizeMethod.invoke(obj, f, f1);
		}
		catch(final Exception e) {
			e.printStackTrace();
		}
	}

	public static Object getField(final Field field, final Object owner) {
		try {
			return field.get(owner);
		}
		catch(final Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void setField(final Field field, final Object owner, final Object arg) {
		try {
			field.set(owner, arg);
		}
		catch(final Exception e) {
			e.printStackTrace();
		}
	}

	public static Object getMethod(final Method method, final Object owner, final Object... args) {
		try {
			return method.invoke(owner, args);
		}
		catch(final Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void invokeMethod(final Method method, final Object owner, final Object... args) {
		try {
			method.invoke(owner, args);
		}
		catch(final Exception e) {
			e.printStackTrace();
		}
	}
}
