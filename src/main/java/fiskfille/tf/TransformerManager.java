package fiskfille.tf;

import fiskfille.tf.common.transformer.*;
import fiskfille.tf.common.transformer.base.Transformer;

public final class TransformerManager {
	public static final Transformer SKYSTRIKE = new TransformerSkystrike();
	public static final Transformer PURGE = new TransformerPurge();
	public static final Transformer VURP = new TransformerVurp();
	public static final Transformer SUBWOOFER = new TransformerSubwoofer();
	public static final Transformer CLOUDTRAP = new TransformerCloudtrap();

	public static void register() {
		TransformersAPI.registerTransformer(SKYSTRIKE);
		TransformersAPI.registerTransformer(PURGE);
		TransformersAPI.registerTransformer(VURP);
		TransformersAPI.registerTransformer(SUBWOOFER);
		TransformersAPI.registerTransformer(CLOUDTRAP);
	}
}
