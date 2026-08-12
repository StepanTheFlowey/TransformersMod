package fiskfille.tf.client.audio;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public final class MovingSoundTransformer extends MovingSound {
	private final Entity entity;

	public MovingSoundTransformer(final Entity entity, final ResourceLocation sound) {
		super(sound);
		this.entity = entity;
		this.volume = 0.5F;
	}

	@Override
	public void update() {
		if(entity.isDead) {
			donePlaying = true;
			return;
		}

		xPosF = (float) entity.posX;
		yPosF = (float) entity.posY;
		zPosF = (float) entity.posZ;
	}
}
