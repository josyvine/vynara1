package com.example.engine;

import android.opengl.Matrix;

public class Transform {
    private float px = 0f, py = 0f, pz = 0f;
    private float rx = 0f, ry = 0f, rz = 0f;
    private float sx = 1f, sy = 1f, sz = 1f;

    private final float[] modelMatrix = new float[16];
    private boolean isDirty = true;

    public Transform() {
        Matrix.setIdentityM(modelMatrix, 0);
    }

    public void setPosition(float x, float y, float z) {
        this.px = x; this.py = y; this.pz = z;
        this.isDirty = true;
    }

    public void setRotation(float xDegrees, float yDegrees, float zDegrees) {
        this.rx = xDegrees; this.ry = yDegrees; this.rz = zDegrees;
        this.isDirty = true;
    }

    public void setScale(float x, float y, float z) {
        this.sx = x; this.sy = y; this.sz = z;
        this.isDirty = true;
    }

    public float getPx() { return px; }
    public float getPy() { return py; }
    public float getPz() { return pz; }
    public float getRx() { return rx; }
    public float getRy() { return ry; }
    public float getRz() { return rz; }
    public float getSx() { return sx; }
    public float getSy() { return sy; }
    public float getSz() { return sz; }

    public float[] getModelMatrix() {
        if (isDirty) {
            Matrix.setIdentityM(modelMatrix, 0);
            Matrix.translateM(modelMatrix, 0, px, py, pz);
            Matrix.rotateM(modelMatrix, 0, rx, 1f, 0f, 0f);
            Matrix.rotateM(modelMatrix, 0, ry, 0f, 1f, 0f);
            Matrix.rotateM(modelMatrix, 0, rz, 0f, 0f, 1f);
            Matrix.scaleM(modelMatrix, 0, sx, sy, sz);
            isDirty = false;
        }
        return modelMatrix;
    }
}
