package fiskfille.tf.asm;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ASMHooks {
	public static double getScaledSneakOffset(Entity entity, double d) {
		return d;
	}
}
