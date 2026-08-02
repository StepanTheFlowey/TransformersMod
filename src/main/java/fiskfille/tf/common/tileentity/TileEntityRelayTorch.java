package fiskfille.tf.common.tileentity;

import fiskfille.tf.common.energon.power.ReceiverEntry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Set;

public class TileEntityRelayTorch extends TileEntityRelayTower {
	@Override
	public AxisAlignedBB getRenderBoundingBox() {
		AxisAlignedBB bounds = AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 1, zCoord + 1);

		if(isValid(getBlockMetadata())) {
			final Set<ReceiverEntry> receivers = data.transmissionHandler.getReceivers();

			for(ReceiverEntry entry : receivers) {
				final TileEntity tile = entry.getTile();

				if(tile != null) {
					bounds = bounds.func_111270_a(tile.getRenderBoundingBox());
				}
			}
		}

		return bounds;
	}

	@Override
	public int getTransmissionRate() {
		return 40;
	}

	@Override
	public int getRange() {
		return 10;
	}

	@Override
	public Vec3 getEnergyInputOffset() {
		final ForgeDirection dir = ForgeDirection.getOrientation(getBlockMetadata());
		final float f = 0.0625F * 3.5F;

		switch(dir) {
			case UP:
				return Vec3.createVectorHelper(0, -f, 0);

			case DOWN:
				return Vec3.createVectorHelper(0, f, 0);
		}

		final int[] rotations = {2, 0, 1, 3};
		final float yaw = rotations[dir.ordinal() - 2] * 90;

		final Vec3 vec3 = Vec3.createVectorHelper(0, 0, -f);
		vec3.rotateAroundY(-yaw * (float) Math.PI / 180F);
		return vec3;
	}

	@Override
	public boolean isValid(int metadata) {
		return true;
	}

	@Override
	public int[] getBaseOffsets(int metadata) {
		return new int[]{0, 0, 0};
	}
}
