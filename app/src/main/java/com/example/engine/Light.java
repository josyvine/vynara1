package com.example.engine;

public class Light {
    public enum Type { DIRECTIONAL, POINT, SPOT, AMBIENT }

    private String id;
    private Type type;
    private float[] position = new float[] { 5f, 10f, 5f };
    private float[] colorRGB = new float[] { 1f, 1f, 1f };
    private float intensity = 1.0f;

    public Light(String id, Type type) {
        this.id = id;
        this.type = type;
    }

    public String getId() { return id; }
    public Type getType() { return type; }
    public float[] getPosition() { return position; }
    public float[] getColorRGB() { return colorRGB; }
    public float getIntensity() { return intensity; }

    public void setPosition(float x, float y, float z) {
        position[0] = x; position[1] = y; position[2] = z;
    }

    public void setColor(float r, float g, float b) {
        colorRGB[0] = r; colorRGB[1] = g; colorRGB[2] = b;
    }

    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }
}
