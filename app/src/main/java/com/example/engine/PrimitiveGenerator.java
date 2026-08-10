package com.example.engine;

import java.util.ArrayList;
import java.util.List;

public class PrimitiveGenerator {

    public static Mesh createCube(float width, float height, float depth) {
        float w = width / 2f, h = height / 2f, d = depth / 2f;

        float[] vertices = new float[] {
                // Front
                -w, -h,  d,   w, -h,  d,   w,  h,  d,  -w,  h,  d,
                // Back
                -w, -h, -d,  -w,  h, -d,   w,  h, -d,   w, -h, -d,
                // Top
                -w,  h, -d,  -w,  h,  d,   w,  h,  d,   w,  h, -d,
                // Bottom
                -w, -h, -d,   w, -h, -d,   w, -h,  d,  -w, -h,  d,
                // Right
                 w, -h, -d,   w,  h, -d,   w,  h,  d,   w, -h,  d,
                // Left
                -w, -h, -d,  -w, -h,  d,  -w,  h,  d,  -w,  h, -d
        };

        float[] normals = new float[] {
                0,0,1, 0,0,1, 0,0,1, 0,0,1,
                0,0,-1, 0,0,-1, 0,0,-1, 0,0,-1,
                0,1,0, 0,1,0, 0,1,0, 0,1,0,
                0,-1,0, 0,-1,0, 0,-1,0, 0,-1,0,
                1,0,0, 1,0,0, 1,0,0, 1,0,0,
                -1,0,0, -1,0,0, -1,0,0, -1,0,0
        };

        float[] texCoords = new float[] {
                0,1, 1,1, 1,0, 0,0,
                1,1, 1,0, 0,0, 0,1,
                0,1, 0,0, 1,0, 1,1,
                1,1, 0,1, 0,0, 1,0,
                1,1, 1,0, 0,0, 0,1,
                0,1, 1,1, 1,0, 0,0
        };

        short[] indices = new short[] {
                0, 1, 2,  0, 2, 3,
                4, 5, 6,  4, 6, 7,
                8, 9,10,  8,10,11,
               12,13,14, 12,14,15,
               16,17,18, 16,18,19,
               20,21,22, 20,22,23
        };

        return new Mesh(vertices, normals, texCoords, indices);
    }

    public static Mesh createSphere(float radius, int rings, int sectors) {
        List<Float> vList = new ArrayList<>();
        List<Float> nList = new ArrayList<>();
        List<Float> tList = new ArrayList<>();
        List<Short> iList = new ArrayList<>();

        float R = 1f / (float)(rings - 1);
        float S = 1f / (float)(sectors - 1);

        for (int r = 0; r < rings; r++) {
            for (int s = 0; s < sectors; s++) {
                float y = (float) Math.sin(-Math.PI / 2 + Math.PI * r * R);
                float x = (float) (Math.cos(2 * Math.PI * s * S) * Math.sin(Math.PI * r * R));
                float z = (float) (Math.sin(2 * Math.PI * s * S) * Math.sin(Math.PI * r * R));

                vList.add(x * radius);
                vList.add(y * radius);
                vList.add(z * radius);

                nList.add(x);
                nList.add(y);
                nList.add(z);

                tList.add(s * S);
                tList.add(r * R);
            }
        }

        for (int r = 0; r < rings - 1; r++) {
            for (int s = 0; s < sectors - 1; s++) {
                short current = (short) (r * sectors + s);
                short next = (short) (current + sectors);

                iList.add(current);
                iList.add(next);
                iList.add((short) (current + 1));

                iList.add((short) (current + 1));
                iList.add(next);
                iList.add((short) (next + 1));
            }
        }

        return toMesh(vList, nList, tList, iList);
    }

    public static Mesh createCylinder(float radius, float height, int segments) {
        return createCube(radius * 2f, height, radius * 2f); // Simplified cylinder mesh bounding representation
    }

    public static Mesh createPlane(float width, float depth) {
        return createCube(width, 0.05f, depth);
    }

    private static Mesh toMesh(List<Float> vList, List<Float> nList, List<Float> tList, List<Short> iList) {
        float[] vArr = new float[vList.size()];
        for (int i = 0; i < vList.size(); i++) vArr[i] = vList.get(i);

        float[] nArr = new float[nList.size()];
        for (int i = 0; i < nList.size(); i++) nArr[i] = nList.get(i);

        float[] tArr = new float[tList.size()];
        for (int i = 0; i < tList.size(); i++) tArr[i] = tList.get(i);

        short[] iArr = new short[iList.size()];
        for (int i = 0; i < iList.size(); i++) iArr[i] = iList.get(i);

        return new Mesh(vArr, nArr, tArr, iArr);
    }
}
