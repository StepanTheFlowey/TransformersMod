package fiskfille.tf.common.item;

import fiskfille.tf.common.energon.Energon;
import fiskfille.tf.common.energon.IEnergon;
import net.minecraft.item.Item;

public class ItemEnergon extends Item implements IEnergon {
	private final Energon energonType;

	public ItemEnergon(Energon type) {
		energonType = type;
	}

	@Override
	public Energon getEnergonType() {
		return energonType;
	}

	@Override
	public int getMass() {
		return Energon.CRYSTAL_SHARD;
	}
}
