package fiskfille.tf.common.entity;

import cpw.mods.fml.common.registry.EntityRegistry;
import fiskfille.tf.TransformersMod;
import net.minecraft.entity.Entity;

public class TFEntities {
	public static void register() {
		registerEntity(EntityTankShell.class, "tank_shell");
		registerEntity(EntityMissile.class, "missile");
		registerEntity(EntityLaser.class, "laser");
		registerEntity(EntityTransformiumSeed.class, "transformium_seed");
		registerEntity(EntityFlamethrowerFire.class, "flamethrower_fire");
		registerEntity(EntityBassCharge.class, "bass_charge");
		registerEntity(EntityLaserBeam.class, "laser_beam");
	}

	private static void registerEntity(Class<? extends Entity> entityClass, String name) {
		name = "tf_" + name;

		final int id = EntityRegistry.findGlobalUniqueEntityId();
		EntityRegistry.registerGlobalEntityID(entityClass, name, id);
		EntityRegistry.registerModEntity(entityClass, name, id, TransformersMod.instance, 20, 10, true);
	}
}
