package com.example.engine;

public class CameraManager {
    private final Camera activeCamera;
    private float orbitAngleYaw = 0f;
    private float orbitAnglePitch = 20f;
    private float orbitDistance = 10f;

    public CameraManager() {
        this.activeCamera = new Camera();
        updateOrbitCamera();
    }

    public void orbit(float deltaYaw, float deltaPitch) {
        orbitAngleYaw += deltaYaw;
        orbitAnglePitch += deltaPitch;

        if (orbitAnglePitch > 89f) orbitAnglePitch = 89f;
        if (orbitAnglePitch < -89f) orbitAnglePitch = -89f;

        updateOrbitCamera();
    }

    public void zoom(float factor) {
        orbitDistance *= factor;
        if (orbitDistance < 1f) orbitDistance = 1f;
        if (orbitDistance > 50f) orbitDistance = 50f;

        updateOrbitCamera();
    }

    public void updateOrbitCamera() {
        double yawRad = Math.toRadians(orbitAngleYaw);
        double pitchRad = Math.toRadians(orbitAnglePitch);

        float x = (float) (orbitDistance * Math.cos(pitchRad) * Math.sin(yawRad));
        float y = (float) (orbitDistance * Math.sin(pitchRad));
        float z = (float) (orbitDistance * Math.cos(pitchRad) * Math.cos(yawRad));

        activeCamera.setEye(x, y + 1f, z);
    }

    public Camera getActiveCamera() { return activeCamera; }
}
