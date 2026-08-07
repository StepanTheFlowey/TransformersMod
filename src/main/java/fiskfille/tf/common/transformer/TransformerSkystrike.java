package fiskfille.tf.common.transformer;

import fiskfille.tf.common.achievement.TFAchievements;
import fiskfille.tf.common.item.TFItems;
import fiskfille.tf.common.transformer.base.TransformerJet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;

/**
 * @author gegy1000
 */
public class TransformerSkystrike extends TransformerJet {
	public TransformerSkystrike() {
		super("Skystrike");
	}

	@Override
	public Item getHelmet() {
		return TFItems.skystrikeHelmet;
	}

	@Override
	public Item getChestplate() {
		return TFItems.skystrikeChestplate;
	}

	@Override
	public Item getLeggings() {
		return TFItems.skystrikeLeggings;
	}

	@Override
	public Item getBoots() {
		return TFItems.skystrikeBoots;
	}

	@Override
	public float getHeightOffset() {
		return -0.1F;
	}

	@Override
	public boolean onJump(final EntityPlayer player) {
		player.motionY += 0.205D;
		return true;
	}

	@Override
	public void tick(final EntityPlayer player, final float timer) {
		player.addStat(TFAchievements.skystrike, 1);
	}
}
