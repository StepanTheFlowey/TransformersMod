package fiskfille.tf.client.particle;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;

import java.lang.reflect.Constructor;

import static fiskfille.tf.TransformersMod.mc;

public class TFParticles {
	public static void spawnParticle(TFParticleType particleType, double x, double y, double z, float motionX, float motionY, float motionZ) {
		if(mc != null && mc.renderViewEntity != null && mc.effectRenderer != null) {
			if(mc.theWorld.isRemote) {
				int particleSetting = mc.gameSettings.particleSetting;
				if(particleSetting == 1 && mc.theWorld.rand.nextInt(3) == 0) {
					particleSetting = 2;
				}

				final double diffX = mc.renderViewEntity.posX - x;
				final double diffY = mc.renderViewEntity.posY - y;
				final double diffZ = mc.renderViewEntity.posZ - z;
				final double maxRenderDistance = 16D;

				if(diffX * diffX + diffY * diffY + diffZ * diffZ > maxRenderDistance * maxRenderDistance) {
					return;
				}
				else if(particleSetting > 1) {
					return;
				}
				else {
					try {
						final Constructor<? extends EntityFX> c = particleType.particleClass.getConstructor(World.class, double.class, double.class, double.class, double.class, double.class, double.class);
						final EntityFX particle = c.newInstance(mc.theWorld, x, y, z, motionX, motionY, motionZ);

						mc.effectRenderer.addEffect(particle);
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
