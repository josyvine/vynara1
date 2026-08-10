package com.example.character;

public class Keyframe {
    private float timestampSeconds;
    private float[] translation = new float[3];
    private float[] rotationDegrees = new float[3];

    public Keyframe(float time, float tx, float ty, float tz, float rx, float ry, float rz) {
        this.timestampSeconds = time;
        this.translation[0] = tx; this.translation[1] = ty; this.translation[2] = tz;
        this.rotationDegrees[0] = rx; this.rotationDegrees[1] = ry; this.rotationDegrees[2] = rz;
    }

    public float getTimestampSeconds() { return timestampSeconds; }
    public float[] getTranslation() { return translation; }
    public float[] getRotationDegrees() { return rotationDegrees; }
}
