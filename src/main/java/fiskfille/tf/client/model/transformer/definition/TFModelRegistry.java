package fiskfille.tf.client.model.transformer.definition;

import fiskfille.tf.TransformerManager;
import fiskfille.tf.common.transformer.base.Transformer;

import java.util.HashMap;

public class TFModelRegistry {
	private static final HashMap<Transformer, TransformerModel> models = new HashMap<>();

	public static void registerModel(final Transformer transformer, final TransformerModel model) {
		models.put(transformer, model);
	}

	public static TransformerModel getModel(final Transformer transformer) {
		return models.get(transformer);
	}

	public static void registerModels() {
		TFModelRegistry.registerModel(TransformerManager.CLOUDTRAP, new TFModelCloudtrap());
		TFModelRegistry.registerModel(TransformerManager.PURGE, new TFModelPurge());
		TFModelRegistry.registerModel(TransformerManager.SKYSTRIKE, new TFModelSkystrike());
		TFModelRegistry.registerModel(TransformerManager.SUBWOOFER, new TFModelSubwoofer());
		TFModelRegistry.registerModel(TransformerManager.VURP, new TFModelVurp());
	}
}
