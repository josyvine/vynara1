package com.example.engine;

import android.opengl.Matrix;

public class Camera {
    private float[] eye = new float[] { 0f, 4f, 8f };
    private float[] target = new float[] { 0f, 1f, 0f };
    private float[] up = new float[] { 0f, 1f, 0f };

    private float fov = 45f;
    private float near = 0.1f;
    private float far = 100f;

    private final float[] viewMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];

    public Camera() {
        updateViewMatrix();
    }

    public void updateViewMatrix() {
        Matrix.setLookAtM(viewMatrix, 0, eye[0], eye[1], eye[2], target[0], target[1], target[2], up[0], up[1], up[2]);
    }

    public void updateProjectionMatrix(int width, int height) {
        float aspect = (float) width / (float) (height > 0 ? height : 1);
        Matrix.perspectiveM(projectionMatrix, 0, fov, aspect, near, far);
    }

    public void setEye(float x, float y, float z) {
        eye[0] = x; eye[1] = y; eye[2] = z;
        updateViewMatrix();
    }

    public void setTarget(float x, float y, float z) {
        target[0] = x; target[1] = y; target[2] = z;
        updateViewMatrix();
    }

    public float[] getEye() { return eye; }
    public float[] getTarget() { return target; }
    public float[] getViewMatrix() { return viewMatrix; }
    public float[] getProjectionMatrix() { return projectionMatrix; }
}
