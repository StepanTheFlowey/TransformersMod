package fiskfille.tf.helper;

/**
 * @author gegy1000
 */
public class ModelOffset {
	private final boolean initialized;
	public float headOffsetY = 0;
	public float headOffsetX = 0;
	public float headOffsetZ = 0;

	public ModelOffset(final boolean init) {
		initialized = init;
	}

	public boolean isInitialized() {
		return initialized;
	}
}
