package com.example.engine;

import android.graphics.Color;

public class Material {
    private String id;
    private String name;
    private float[] baseColorRGBA = new float[] { 0.8f, 0.8f, 0.8f, 1.0f };
    private float metallic = 0.1f;
    private float roughness = 0.5f;

    public Material(String id, String name, String hexColor) {
        this.id = id;
        this.name = name;
        setColorHex(hexColor);
    }

    public Material(String id, String name, float r, float g, float b, float a) {
        this.id = id;
        this.name = name;
        this.baseColorRGBA = new float[] { r, g, b, a };
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public float[] getBaseColorRGBA() { return baseColorRGBA; }
    public float getMetallic() { return metallic; }
    public float getRoughness() { return roughness; }

    public void setMetallic(float metallic) { this.metallic = metallic; }
    public void setRoughness(float roughness) { this.roughness = roughness; }

    public void setColorHex(String hexColor) {
        if (hexColor != null && !hexColor.isEmpty()) {
            try {
                int c = Color.parseColor(hexColor.startsWith("#") ? hexColor : "#" + hexColor);
                baseColorRGBA[0] = Color.red(c) / 255f;
                baseColorRGBA[1] = Color.green(c) / 255f;
                baseColorRGBA[2] = Color.blue(c) / 255f;
                baseColorRGBA[3] = Color.alpha(c) / 255f;
            } catch (Exception ignored) {}
        }
    }
}
