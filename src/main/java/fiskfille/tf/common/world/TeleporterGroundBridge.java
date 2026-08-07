package fiskfille.tf.common.world;

import net.minecraft.entity.Entity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

public class TeleporterGroundBridge extends Teleporter {
	public TeleporterGroundBridge(final WorldServer world) {
		super(world);
	}

	@Override
	public void placeInPortal(final Entity entity, final double x, final double y, final double z, final float f) {

	}

	@Override
	public boolean placeInExistingPortal(final Entity entity, final double x, final double y, final double z, final float f) {
		return false;
	}

	@Override
	public boolean makePortal(final Entity entity) {
		return false;
	}

	@Override
	public void removeStalePortalLocations(final long l) {

	}
}
