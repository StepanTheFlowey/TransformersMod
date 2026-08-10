package fiskfille.tf.client.particle;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

public final class EntityTFFlameFX extends EntityFX {
	private final float flameScale;

	public EntityTFFlameFX(final World world, final double x, final double y, final double z, final double motionX, final double motionY, final double motionZ) {
		super(world, x, y, z, motionX, motionY, motionZ);
		this.motionX = this.motionX * 0.009999999776482582D + motionX;
		this.motionY = this.motionY * 0.009999999776482582D + motionY;
		this.motionZ = this.motionZ * 0.009999999776482582D + motionZ;
		flameScale = particleScale;
		particleRed = particleGreen = particleBlue = 1F;
		particleMaxAge = (int) (8D / (Math.random() * 0.8D + 0.2D)) + 4;
		noClip = false;
		setParticleTextureIndex(48);
	}

	@Override
	public void renderParticle(final Tessellator tesselator, final float x, final float y, final float z, final float r, final float g, final float b) {
		final float f6 = (particleAge + x) / particleMaxAge;
		particleScale = flameScale * (1F - f6 * f6 * 0.5F);
		super.renderParticle(tesselator, x, y, z, r, g, b);
	}

	@Override
	public int getBrightnessForRender(final float partialTicks) {
		return 0xF000F0;
	}

	@Override
	public float getBrightness(final float partialTicks) {
		return 0xF000F0;
	}

	@Override
	public void onUpdate() {
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;

		if(particleAge++ >= particleMaxAge) {
			setDead();
		}

		moveEntity(motionX, motionY, motionZ);
		motionX *= 0.9599999785423279D;
		motionY *= 0.9599999785423279D;
		motionZ *= 0.9599999785423279D;
	}
}
