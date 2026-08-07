package fiskfille.tf.helper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

/**
 * @author FiskFille
 */
public final class TFVectorHelper {
	public static Vec3 getBackSideCoords(final EntityPlayer player, final double amount, final boolean side, final double backAmount, final boolean pitch) {
		final Vec3 front = getFrontCoords(player, backAmount, pitch).addVector(-player.posX, -player.boundingBox.minY, -player.posZ);
		return getSideCoords(player, amount, side).addVector(front.xCoord, front.yCoord, front.zCoord);
	}

	public static Vec3 add(final Vec3 vec31, final Vec3 vec32) {
		return vec31.addVector(vec32.xCoord, vec32.yCoord, vec32.zCoord);
	}

	public static Vec3 getSideCoords(final EntityPlayer player, final double amount, final boolean side) {
		final float rotationYaw = player.rotationYaw + (side ? -90 : 90);

		final float f3 = MathHelper.cos(-rotationYaw * 0.017453292F - (float) Math.PI);
		final float f4 = MathHelper.sin(-rotationYaw * 0.017453292F - (float) Math.PI);

		return Vec3.createVectorHelper(player.posX, player.boundingBox.minY, player.posZ).addVector(-f4 * amount, 0, -f3 * amount);
	}

	public static Vec3 getSideCoords(final EntityPlayer player, final double amount, final boolean side, final boolean pitch) {
		final float rotationPitch = pitch ? player.rotationPitch : 0;
		final float rotationYaw = player.rotationYaw + (side ? -90 : 90);

		final float f3 = MathHelper.cos(-rotationYaw * 0.017453292F - (float) Math.PI);
		final float f4 = MathHelper.sin(-rotationYaw * 0.017453292F - (float) Math.PI);
		final float f5 = -MathHelper.cos(rotationPitch * 0.017453292F);
		final float f6 = -MathHelper.sin(rotationPitch * 0.017453292F);
		final float f7 = f4 * f5;
		final float f8 = f3 * f5;

		return Vec3.createVectorHelper(player.posX, player.boundingBox.minY, player.posZ).addVector(f7 * amount, f6 * amount, f8 * amount);
	}

	public static Vec3 getFrontCoords(final EntityPlayer player, final double amount, final boolean pitch) {
		final float rotationPitch = pitch ? player.rotationPitch : 0;

		final float f3 = MathHelper.cos(-player.rotationYaw * 0.017453292F - (float) Math.PI);
		final float f4 = MathHelper.sin(-player.rotationYaw * 0.017453292F - (float) Math.PI);
		final float f5 = -MathHelper.cos(rotationPitch * 0.017453292F);
		final float f6 = -MathHelper.sin(rotationPitch * 0.017453292F);
		final float f7 = f4 * f5;
		final float f8 = f3 * f5;

		return Vec3.createVectorHelper(player.posX, player.boundingBox.minY, player.posZ).addVector(f7 * amount, f6 * amount, f8 * amount);
	}

	public static Vec3 getFrontCoords(final EntityPlayer player, final float angle, final double amount) {
		final float f3 = MathHelper.cos(-player.rotationYaw * 0.017453292F - (float) Math.PI);
		final float f4 = MathHelper.sin(-player.rotationYaw * 0.017453292F - (float) Math.PI);
		final float f5 = -MathHelper.cos(angle * 0.017453292F);
		final float f6 = -MathHelper.sin(angle * 0.017453292F);
		final float f7 = f4 * f5;
		final float f8 = f3 * f5;

		return Vec3.createVectorHelper(player.posX, player.boundingBox.minY, player.posZ).addVector(f7 * amount, f6 * amount, f8 * amount);
	}
}
