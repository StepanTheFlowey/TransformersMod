package fiskfille.tf.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;

import java.lang.reflect.Constructor;

public class TFParticles {
	public static void spawnParticle(TFParticleType particleType, double x, double y, double z, float motionX, float motionY, float motionZ) {
		if(Minecraft.getMinecraft() != null && Minecraft.getMinecraft().renderViewEntity != null && Minecraft.getMinecraft().effectRenderer != null) {
			if(Minecraft.getMinecraft().theWorld.isRemote) {
				int particleSetting = Minecraft.getMinecraft().gameSettings.particleSetting;
				if(particleSetting == 1 && Minecraft.getMinecraft().theWorld.rand.nextInt(3) == 0) {
					particleSetting = 2;
				}

				final double diffX = Minecraft.getMinecraft().renderViewEntity.posX - x;
				final double diffY = Minecraft.getMinecraft().renderViewEntity.posY - y;
				final double diffZ = Minecraft.getMinecraft().renderViewEntity.posZ - z;
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
						final EntityFX particle = c.newInstance(Minecraft.getMinecraft().theWorld, x, y, z, motionX, motionY, motionZ);

						Minecraft.getMinecraft().effectRenderer.addEffect(particle);
					}
					catch(Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
