package fiskfille.tf.client.audio;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class MovingSoundTransformer extends MovingSound {
	private final Entity entity;

	public MovingSoundTransformer(Entity entity, ResourceLocation sound) {
		super(sound);
		this.entity = entity;
		this.volume = 0.5F;
	}

	@Override
	public void update() {
		if(entity.isDead) {
			this.donePlaying = true;
			return;
		}

		this.xPosF = (float) entity.posX;
		this.yPosF = (float) entity.posY;
		this.zPosF = (float) entity.posZ;
	}
}
