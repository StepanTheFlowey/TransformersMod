package fiskfille.tf.client.model.tools;

import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Vec3;

public class TexturedQuadPartial {
	public final int nVertices;
	public PositionTextureVertex[] vertexPositions;
	private boolean invertNormal;

	public TexturedQuadPartial(PositionTextureVertex[] vertices) {
		vertexPositions = vertices;
		nVertices = vertices.length;
	}

	public TexturedQuadPartial(PositionTextureVertex[] vertices, double f0, double f1, double f2, double f3, double width, double height) {
		this(vertices);
		final double x = 1 / width, y = 1 / height;
		vertices[0] = vertices[0].setTexturePosition((float) (f2 / width - x), (float) (f1 / height + y));
		vertices[1] = vertices[1].setTexturePosition((float) (f0 / width + x), (float) (f1 / height + y));
		vertices[2] = vertices[2].setTexturePosition((float) (f0 / width + x), (float) (f3 / height - y));
		vertices[3] = vertices[3].setTexturePosition((float) (f2 / width - x), (float) (f3 / height - y));
	}

	public void flipFace() {
		final PositionTextureVertex[] vertex = new PositionTextureVertex[vertexPositions.length];

		for(int i = 0; i < vertexPositions.length; ++i) {
			vertex[i] = vertexPositions[vertexPositions.length - i - 1];
		}

		vertexPositions = vertex;
	}

	public void draw(Tessellator tessellator, double f) {
		final Vec3 vec3 = vertexPositions[1].vector3D.subtract(vertexPositions[0].vector3D);
		final Vec3 vec31 = vertexPositions[1].vector3D.subtract(vertexPositions[2].vector3D);
		final Vec3 vec32 = vec31.crossProduct(vec3).normalize();
		tessellator.startDrawingQuads();

		if(invertNormal) {
			tessellator.setNormal(-(float) vec32.xCoord, -(float) vec32.yCoord, -(float) vec32.zCoord);
		}
		else {
			tessellator.setNormal((float) vec32.xCoord, (float) vec32.yCoord, (float) vec32.zCoord);
		}

		for(int i = 0; i < 4; ++i) {
			final PositionTextureVertex vertex = vertexPositions[i];
			tessellator.addVertexWithUV(vertex.vector3D.xCoord * f, vertex.vector3D.yCoord * f, vertex.vector3D.zCoord * f, vertex.texturePositionX, vertex.texturePositionY);
		}

		tessellator.draw();
	}
}
