package fiskfille.tf.common.data;

import com.google.common.base.Predicates;
import net.minecraft.entity.player.EntityPlayer;

public class TFDataPrev extends TFData {
	public final TFData tracking;

	public TFDataPrev(final TFData type) {
		super(false, type.defaultValue, Predicates.alwaysFalse());
		tracking = type;
	}

	@Override
	public boolean set(final EntityPlayer player, final Object value) {
		return false;
	}

	@Override
	public boolean setWithoutNotify(final EntityPlayer player, final Object value) {
		return false;
	}

	@Override
	public void clamp(final EntityPlayer player, final Object min, final Object max) {
	}

	@Override
	public void clampWithoutNotify(final EntityPlayer player, final Object min, final Object max) {
	}

	@Override
	public void incr(final EntityPlayer player, final Object value) {
	}

	@Override
	public void incrWithoutNotify(final EntityPlayer player, final Object value) {
	}
}
