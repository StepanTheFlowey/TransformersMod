package fiskfille.tf.common.achievement;

import net.minecraft.init.Items;
import net.minecraft.stats.Achievement;

public class TFAchievement extends Achievement {
	public TFAchievement(String name, int x, int y, Achievement parent) {
		super("achievement.tf." + name, "tf." + name, x, y, Items.fish, parent);
	}
}
