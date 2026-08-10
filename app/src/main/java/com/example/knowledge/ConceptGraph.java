package com.example.knowledge;

import java.util.HashMap;
import java.util.Map;

public class ConceptGraph {
    private final Map<String, KnowledgeEntry> concepts = new HashMap<>();

    public ConceptGraph() {
        populateDefaultConcepts();
    }

    private void populateDefaultConcepts() {
        // Humanoid Character
        concepts.put("humanoid", new KnowledgeEntry("humanoid", "Humanoid Character", "CHARACTER")
                .addComponent("torso").addComponent("head").addComponent("left_arm").addComponent("right_arm")
                .addComponent("left_leg").addComponent("right_leg").addComponent("hands").addComponent("feet")
                .addCapability("geometry").addCapability("skeleton").addCapability("rigging").addCapability("animation")
                .addMaterial("skin").addMaterial("clothing"));

        // Quadruped / Dog
        concepts.put("dog", new KnowledgeEntry("dog", "Dog / Quadruped", "ANIMAL")
                .addComponent("body_torso").addComponent("head_snout").addComponent("front_left_leg")
                .addComponent("front_right_leg").addComponent("rear_left_leg").addComponent("rear_right_leg")
                .addComponent("tail")
                .addCapability("geometry").addCapability("quadruped_skeleton").addCapability("rigging").addCapability("locomotion")
                .addMaterial("fur"));

        // Bird / Eagle
        concepts.put("bird", new KnowledgeEntry("bird", "Bird / Flying Creature", "ANIMAL")
                .addComponent("body").addComponent("head_beak").addComponent("left_wing").addComponent("right_wing")
                .addComponent("tail_feathers").addComponent("claws")
                .addCapability("geometry").addCapability("bird_skeleton").addCapability("wing_rig").addCapability("flight_animation")
                .addMaterial("feathers"));

        // House / Villa
        concepts.put("house", new KnowledgeEntry("house", "Modern House / Villa", "ARCHITECTURE")
                .addComponent("foundation").addComponent("walls").addComponent("roof").addComponent("doors")
                .addComponent("windows").addComponent("swimming_pool").addComponent("pool_deck").addComponent("furniture")
                .addCapability("geometry").addCapability("materials").addCapability("architectural_builder").addCapability("lighting")
                .addMaterial("concrete").addMaterial("glass").addMaterial("wood").addMaterial("water"));

        // Sofa
        concepts.put("sofa", new KnowledgeEntry("sofa", "Leather Sofa", "FURNITURE")
                .addComponent("frame").addComponent("cushions").addComponent("armrests").addComponent("legs")
                .addCapability("geometry").addCapability("materials").addCapability("uv_mapping")
                .addMaterial("leather").addMaterial("wood"));

        // Table
        concepts.put("table", new KnowledgeEntry("table", "Wooden Table", "FURNITURE")
                .addComponent("top_surface").addComponent("four_legs").addComponent("support_beams")
                .addCapability("geometry").addCapability("materials")
                .addMaterial("wood"));

        // Tree
        concepts.put("tree", new KnowledgeEntry("tree", "Procedural Tree", "ENVIRONMENT")
                .addComponent("trunk").addComponent("branches").addComponent("foliage")
                .addCapability("geometry").addCapability("vegetation_scatter")
                .addMaterial("bark").addMaterial("leaves"));
    }

    public KnowledgeEntry getConcept(String key) {
        if (key == null) return null;
        String lowerKey = key.toLowerCase();
        for (Map.Entry<String, KnowledgeEntry> entry : concepts.entrySet()) {
            if (lowerKey.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return concepts.get("humanoid"); // Default fallback
    }

    public Map<String, KnowledgeEntry> getAllConcepts() {
        return concepts;
    }
}
