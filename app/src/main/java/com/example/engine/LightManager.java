package com.example.engine;

import java.util.ArrayList;
import java.util.List;

public class LightManager {
    private final List<Light> lights = new ArrayList<>();

    public LightManager() {
        // Default sun light & ambient light
        Light sun = new Light("light_sun", Light.Type.DIRECTIONAL);
        sun.setPosition(5f, 12f, 8f);
        sun.setIntensity(1.2f);
        lights.add(sun);

        Light ambient = new Light("light_ambient", Light.Type.AMBIENT);
        ambient.setIntensity(0.4f);
        lights.add(ambient);
    }

    public void addLight(Light light) {
        if (light != null) lights.add(light);
    }

    public List<Light> getLights() { return lights; }
}
