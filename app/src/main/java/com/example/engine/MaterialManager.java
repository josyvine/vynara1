package com.example.engine;

import java.util.HashMap;
import java.util.Map;

public class MaterialManager {
    private final Map<String, Material> materials = new HashMap<>();

    public MaterialManager() {
        populateDefaultMaterials();
    }

    private void populateDefaultMaterials() {
        materials.put("mat_default", new Material("mat_default", "Default Grey", "#A0A5BD"));
        materials.put("mat_skin", new Material("mat_skin", "Human Skin", "#E0AC69"));
        materials.put("mat_leather_brown", new Material("mat_leather_brown", "Brown Leather", "#6E3B1F"));
        materials.put("mat_wood_walnut", new Material("mat_wood_walnut", "Dark Walnut Wood", "#4A2E1B"));
        materials.put("mat_concrete", new Material("mat_concrete", "White Concrete", "#E2E4EB"));
        materials.put("mat_glass", new Material("mat_glass", "Glass Windows", "#3300E5FF"));
        materials.put("mat_pool_water", new Material("mat_pool_water", "Pool Water", "#6600B2FF"));
        materials.put("mat_foliage", new Material("mat_foliage", "Green Leaves", "#2E7D32"));
        materials.put("mat_metallic_gold", new Material("mat_metallic_gold", "Metallic Gold", "#FFD700"));
        materials.get("mat_metallic_gold").setMetallic(0.9f);
        materials.get("mat_metallic_gold").setRoughness(0.2f);
    }

    public Material getMaterial(String id) {
        return materials.get(id);
    }

    public void addMaterial(Material mat) {
        if (mat != null) materials.put(mat.getId(), mat);
    }

    public Map<String, Material> getAllMaterials() {
        return materials;
    }
}
