package fiskfille.tf.client.render.shader;

public final class PortalShader extends ShaderProgram {
	public PortalShader() throws Exception {
		super("portal.vsh", "portal.fsh");
	}

	@Override
	protected void bindAttributes() {}

	public void setTime(final float time) {
		this.setUniform("time", time);
	}

	@Override
	protected String[] getUniforms() {
		return new String[]{"time"};
	}
}
