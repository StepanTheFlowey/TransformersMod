package fiskfille.tf.common.event;

import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public abstract class ItemStitchEvent extends WorldEvent {
	public ItemStitchEvent(final World world) {
		super(world);
	}

	public static class Pre extends ItemStitchEvent {
		public Pre(final World world) {
			super(world);
		}
	}

	public static class Post extends ItemStitchEvent {
		public Post(final World world) {
			super(world);
		}
	}
}
