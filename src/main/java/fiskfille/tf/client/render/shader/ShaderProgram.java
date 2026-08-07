package fiskfille.tf.client.render.shader;

import net.minecraft.client.renderer.OpenGlHelper;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ShaderProgram {
	private static final FloatBuffer MATRIX_BUFFER = BufferUtils.createFloatBuffer(16);

	private static final List<ShaderProgram> PROGRAMS = new ArrayList<>();

	private final Map<String, Integer> uniforms = new HashMap<>();

	private final int programID;
	private final int vertexShaderID;
	private final int fragmentShaderID;
	private final boolean hasGeometryShader;
	private int geometryShaderID;

	public ShaderProgram(final String vertex, final String fragment, final String geometry) throws Exception {
		this.hasGeometryShader = geometry != null;
		this.vertexShaderID = ShaderProgram.loadShader(vertex, ARBVertexShader.GL_VERTEX_SHADER_ARB);
		this.fragmentShaderID = ShaderProgram.loadShader(fragment, ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);

		if(this.hasGeometryShader) {
			this.geometryShaderID = ShaderProgram.loadShader(geometry, ARBGeometryShader4.GL_GEOMETRY_SHADER_ARB);
		}

		this.programID = OpenGlHelper.func_153183_d();

		OpenGlHelper.func_153178_b(this.programID, this.vertexShaderID);
		OpenGlHelper.func_153178_b(this.programID, this.fragmentShaderID);

		if(this.hasGeometryShader) {
			OpenGlHelper.func_153178_b(this.programID, this.geometryShaderID);
		}

		this.bindAttributes();

		OpenGlHelper.func_153179_f(this.programID);

		if(OpenGlHelper.func_153175_a(this.programID, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
			throw new RuntimeException("Error creating shader: " + ShaderProgram.getLogInfoProgram(this.programID));
		}

		GL20.glValidateProgram(this.programID);

		if(OpenGlHelper.func_153175_a(this.programID, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
			throw new RuntimeException("Error creating shader: " + getLogInfoProgram(this.programID));
		}

		PROGRAMS.add(this);

		for(final String uniform : this.getUniforms()) {
			final int location = this.getUniformLocation(uniform);

			if(location == -1) {
				System.err.println("Could not find uniform location for " + uniform + " in " + this.getClass().getSimpleName() + "!");
			}
			else if(this.uniforms.containsValue(location)) {
				System.err.println("Duplicate uniform location for " + uniform + " in " + this.getClass().getSimpleName() + "!");
			}

			this.uniforms.put(uniform, location);
		}

		this.stop();
	}

	public ShaderProgram(final String vertex, final String fragment) throws Exception {
		this(vertex, fragment, null);
	}

	public static int loadShader(final String resource, final int type) throws Exception {
		final BufferedReader in = new BufferedReader(new InputStreamReader(ShaderProgram.class.getResourceAsStream("/assets/transformers/shaders/" + resource)));

		String line;
		final StringBuilder source = new StringBuilder();
		while((line = in.readLine()) != null) {
			source.append(line).append("\n");
		}

		final int shaderID = OpenGlHelper.func_153195_b(type);
		final byte[] bytes = source.toString().getBytes();
		final ByteBuffer buffer = BufferUtils.createByteBuffer(bytes.length).put(bytes);
		buffer.flip();
		OpenGlHelper.func_153169_a(shaderID, buffer);
		OpenGlHelper.func_153170_c(shaderID);

		if(OpenGlHelper.func_153157_c(shaderID, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE) {
			System.err.println("Failed to compile shader: " + resource);
			System.err.println(getLogInfoShader(shaderID));
		}

		return shaderID;
	}

	public static void deletePrograms() {
		for(final ShaderProgram program : PROGRAMS) {
			program.delete();
		}

		PROGRAMS.clear();
	}

	private static String getLogInfoShader(final int shader) {
		return OpenGlHelper.func_153158_d(shader, OpenGlHelper.func_153157_c(shader, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
	}

	private static String getLogInfoProgram(final int program) {
		return OpenGlHelper.func_153166_e(program, OpenGlHelper.func_153175_a(program, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
	}

	protected abstract void bindAttributes();

	protected abstract String[] getUniforms();

	protected void bindAttribute(final int index, final String name) {
		GL20.glBindAttribLocation(this.programID, index, name);
	}

	protected int getUniformLocation(final String name) {
		return OpenGlHelper.func_153194_a(this.programID, name);
	}

	public void setUniform(final String name, final float value) {
		GL20.glUniform1f(this.uniforms.get(name), value);
	}

	public void setUniform(final String name, final int value) {
		GL20.glUniform1i(this.uniforms.get(name), value);
	}

	public void setUniform(final String name, final Vector3f value) {
		GL20.glUniform3f(this.uniforms.get(name), value.x, value.y, value.z);
	}

	public void setUniform(final String name, final boolean value) {
		GL20.glUniform1f(this.uniforms.get(name), value ? 1F : 0F);
	}

	public void setUniform(final String name, final Matrix4f value) {
		value.store(MATRIX_BUFFER);
		MATRIX_BUFFER.flip();
		GL20.glUniformMatrix4(this.uniforms.get(name), false, MATRIX_BUFFER);
	}

	public void setUniform(final String name, final Vector4f value) {
		GL20.glUniform4f(this.uniforms.get(name), value.x, value.y, value.z, value.w);
	}

	public void start() {
		OpenGlHelper.func_153161_d(this.programID);
	}

	public void stop() {
		OpenGlHelper.func_153161_d(0);
	}

	public void delete() {
		this.stop();

		GL20.glDetachShader(this.programID, this.vertexShaderID);
		GL20.glDetachShader(this.programID, this.fragmentShaderID);

		if(this.hasGeometryShader) {
			GL20.glDetachShader(this.programID, this.geometryShaderID);
		}

		OpenGlHelper.func_153180_a(this.vertexShaderID);
		OpenGlHelper.func_153180_a(this.fragmentShaderID);

		if(this.hasGeometryShader) {
			OpenGlHelper.func_153180_a(this.geometryShaderID);
		}

		OpenGlHelper.func_153187_e(this.programID);
		PROGRAMS.remove(this);
	}
}
