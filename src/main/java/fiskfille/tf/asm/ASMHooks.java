package fiskfille.tf.asm;

import net.minecraft.entity.Entity;

public class ASMHooks {
	public static float getEntityScale(Entity entity) {
		return 1F;
	}

	public static float getModifiedEntityScale(Entity entity) {
		return 1F;
	}

	public static double getScaledSneakOffset(Entity entity, double d) {
		return d * ASMHooks.getEntityScale(entity);
	}
}
