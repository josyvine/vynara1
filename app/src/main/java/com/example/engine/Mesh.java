package com.example.engine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Mesh {
    private float[] vertices;
    private float[] normals;
    private float[] texCoords;
    private short[] indices;

    private FloatBuffer vertexBuffer;
    private FloatBuffer normalBuffer;
    private FloatBuffer texBuffer;
    private ShortBuffer indexBuffer;

    public Mesh(float[] vertices, float[] normals, float[] texCoords, short[] indices) {
        this.vertices = vertices;
        this.normals = normals;
        this.texCoords = texCoords;
        this.indices = indices;
        initBuffers();
    }

    private void initBuffers() {
        if (vertices != null && vertices.length > 0) {
            ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
            vbb.order(ByteOrder.nativeOrder());
            vertexBuffer = vbb.asFloatBuffer();
            vertexBuffer.put(vertices);
            vertexBuffer.position(0);
        }

        if (normals != null && normals.length > 0) {
            ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length * 4);
            nbb.order(ByteOrder.nativeOrder());
            normalBuffer = nbb.asFloatBuffer();
            normalBuffer.put(normals);
            normalBuffer.position(0);
        }

        if (texCoords != null && texCoords.length > 0) {
            ByteBuffer tbb = ByteBuffer.allocateDirect(texCoords.length * 4);
            tbb.order(ByteOrder.nativeOrder());
            texBuffer = tbb.asFloatBuffer();
            texBuffer.put(texCoords);
            texBuffer.position(0);
        }

        if (indices != null && indices.length > 0) {
            ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
            ibb.order(ByteOrder.nativeOrder());
            indexBuffer = ibb.asShortBuffer();
            indexBuffer.put(indices);
            indexBuffer.position(0);
        }
    }

    public float[] getVertices() { return vertices; }
    public float[] getNormals() { return normals; }
    public float[] getTexCoords() { return texCoords; }
    public short[] getIndices() { return indices; }

    public FloatBuffer getVertexBuffer() { return vertexBuffer; }
    public FloatBuffer getNormalBuffer() { return normalBuffer; }
    public FloatBuffer getTexBuffer() { return texBuffer; }
    public ShortBuffer getIndexBuffer() { return indexBuffer; }

    public int getVertexCount() {
        return vertices != null ? vertices.length / 3 : 0;
    }

    public int getTriangleCount() {
        return indices != null ? indices.length / 3 : getVertexCount() / 3;
    }
}
