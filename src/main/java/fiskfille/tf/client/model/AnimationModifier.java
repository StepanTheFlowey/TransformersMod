package fiskfille.tf.client.model;

import com.google.common.base.Predicate;

public class AnimationModifier {
	public final Type type;
	public final Predicate predicate;
	public final float factor;

	public AnimationModifier(final Type t, final Predicate p, final float f) {
		type = t;
		predicate = p;
		factor = f;
	}

	public enum Type {
		SPEED, DEGREE
	}
}
