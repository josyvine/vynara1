package com.example.knowledge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConceptGraph {
    private final Map<String, KnowledgeEntry> concepts = new HashMap<>();

    public ConceptGraph() {
        populateDefaultConcepts();
    }

    private void populateDefaultConcepts() {
        // 1. Humanoid Character Anatomy
        concepts.put("humanoid", new KnowledgeEntry("humanoid", "Humanoid Character", "CHARACTER")
                .addComponent("head").addComponent("neck").addComponent("torso").addComponent("pelvis")
                .addComponent("clavicles").addComponent("upper_arms").addComponent("forearms")
                .addComponent("hands").addComponent("fingers").addComponent("thighs")
                .addComponent("calves").addComponent("feet").addComponent("toes")
                .addCapability("geometry").addCapability("skeleton").addCapability("rigging").addCapability("animation")
                .addMaterial("skin").addMaterial("clothing"));

        // 2. Quadruped / Dog Anatomy
        concepts.put("dog", new KnowledgeEntry("dog", "Dog / Quadruped", "ANIMAL")
                .addComponent("body_torso").addComponent("head_snout").addComponent("neck")
                .addComponent("front_left_leg").addComponent("front_right_leg")
                .addComponent("rear_left_leg").addComponent("rear_right_leg").addComponent("tail")
                .addCapability("geometry").addCapability("quadruped_skeleton").addCapability("rigging").addCapability("locomotion")
                .addMaterial("fur"));

        // 3. Bird / Flying Creature Anatomy
        concepts.put("bird", new KnowledgeEntry("bird", "Bird / Flying Creature", "ANIMAL")
                .addComponent("body").addComponent("head_beak").addComponent("neck")
                .addComponent("left_wing").addComponent("right_wing").addComponent("tail_feathers").addComponent("claws")
                .addCapability("geometry").addCapability("bird_skeleton").addCapability("wing_rig").addCapability("flight_animation")
                .addMaterial("feathers"));

        // 4. Architecture: Modern Villa / House
        concepts.put("house", new KnowledgeEntry("house", "Modern House / Villa", "ARCHITECTURE")
                .addComponent("foundation").addComponent("exterior_walls").addComponent("interior_rooms")
                .addComponent("door_openings").addComponent("window_frames").addComponent("glass_panes")
                .addComponent("roof_structure").addComponent("floor_deck")
                .addCapability("geometry").addCapability("materials").addCapability("architectural_builder").addCapability("lighting")
                .addMaterial("concrete").addMaterial("glass").addMaterial("wood"));

        // 5. Architecture: Swimming Pool
        concepts.put("pool", new KnowledgeEntry("pool", "Swimming Pool & Deck", "ARCHITECTURE")
                .addComponent("basin_walls").addComponent("tiled_floor").addComponent("water_surface")
                .addComponent("edge_coping").addComponent("deck_structure")
                .addCapability("geometry").addCapability("materials").addCapability("water_shader")
                .addMaterial("water").addMaterial("tiles").addMaterial("wood"));

        // 6. Furniture: Leather Sofa
        concepts.put("sofa", new KnowledgeEntry("sofa", "Leather Sofa", "FURNITURE")
                .addComponent("frame").addComponent("back_cushions").addComponent("seat_cushions")
                .addComponent("armrests").addComponent("legs").addComponent("stitching_seams")
                .addCapability("geometry").addCapability("materials").addCapability("uv_mapping")
                .addMaterial("leather").addMaterial("wood").addMaterial("metal"));

        // 7. Furniture: Wooden Table
        concepts.put("table", new KnowledgeEntry("table", "Wooden Table", "FURNITURE")
                .addComponent("top_surface").addComponent("four_legs").addComponent("support_beams")
                .addCapability("geometry").addCapability("materials")
                .addMaterial("wood").addMaterial("metal"));

        // 8. Furniture: Chair
        concepts.put("chair", new KnowledgeEntry("chair", "Armchair", "FURNITURE")
                .addComponent("seat").addComponent("backrest").addComponent("four_legs").addComponent("armrests")
                .addCapability("geometry").addCapability("materials")
                .addMaterial("fabric").addMaterial("wood"));

        // 9. Environment: Procedural Tree / Vegetation
        concepts.put("tree", new KnowledgeEntry("tree", "Procedural Tree", "ENVIRONMENT")
                .addComponent("trunk").addComponent("branches").addComponent("foliage_canopy")
                .addCapability("geometry").addCapability("vegetation_scatter")
                .addMaterial("bark").addMaterial("leaves"));

        // 10. Environment: Terrain & Landscape
        concepts.put("terrain", new KnowledgeEntry("terrain", "3D Terrain", "ENVIRONMENT")
                .addComponent("ground_plane").addComponent("elevation_grid")
                .addCapability("geometry").addCapability("heightmap")
                .addMaterial("sand").addMaterial("grass").addMaterial("rock"));

        // 11. Vehicle: Sci-Fi / Modern Rover
        concepts.put("vehicle", new KnowledgeEntry("vehicle", "Vehicle / Car", "VEHICLE")
                .addComponent("chassis_body").addComponent("four_wheels").addComponent("cockpit_windows").addComponent("headlights")
                .addCapability("geometry").addCapability("materials")
                .addMaterial("steel").addMaterial("glass").addMaterial("rubber"));
    }

    public void addConcept(KnowledgeEntry entry) {
        if (entry != null && entry.getId() != null) {
            concepts.put(entry.getId().toLowerCase(), entry);
        }
    }

    public KnowledgeEntry getConcept(String key) {
        if (key == null) return concepts.get("humanoid");
        String lowerKey = key.toLowerCase().trim();

        if (concepts.containsKey(lowerKey)) {
            return concepts.get(lowerKey);
        }

        for (Map.Entry<String, KnowledgeEntry> entry : concepts.entrySet()) {
            if (lowerKey.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        return concepts.get("house"); // Default fallback
    }

    public List<KnowledgeEntry> getConceptsByCategory(String category) {
        List<KnowledgeEntry> results = new ArrayList<>();
        if (category == null) return results;

        for (KnowledgeEntry entry : concepts.values()) {
            if (category.equalsIgnoreCase(entry.getCategory())) {
                results.add(entry);
            }
        }
        return results;
    }

    public Map<String, KnowledgeEntry> getAllConcepts() {
        return concepts;
    }
}