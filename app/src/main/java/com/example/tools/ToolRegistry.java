package com.example.tools;

import java.util.HashMap;
import java.util.Map;

public class ToolRegistry {
    private final Map<String, ToolDefinition> registeredTools = new HashMap<>();

    public ToolRegistry() {
        registerCoreTools();
    }

    private void registerCoreTools() {
        // Geometry Primitives
        register(new ToolDefinition("geometry.create_primitive", "Create Primitive", "GEOMETRY",
                "Creates a primitive 3D mesh (cube, sphere, cylinder, cone, plane, torus).", ToolDefinition.AvailabilityState.AVAILABLE)
                .addParam("type", "STRING", true, "cube, sphere, cylinder, cone, plane, torus")
                .addParam("width", "FLOAT", false, "Width dimension")
                .addParam("height", "FLOAT", false, "Height dimension")
                .addParam("depth", "FLOAT", false, "Depth dimension")
                .addParam("radius", "FLOAT", false, "Radius size"));

        // Geometry Procedural
        register(new ToolDefinition("geometry.create_procedural", "Create Procedural Structure", "GEOMETRY",
                "Generates procedural 3D models (house, villa, sofa, table, tree, car, pool, room).", ToolDefinition.AvailabilityState.AVAILABLE)
                .addParam("type", "STRING", true, "house, villa, sofa, table, tree, car, pool, room")
                .addParam("name", "STRING", false, "Display name for object")
                .addParam("style", "STRING", false, "realistic, stylized, modern, low_poly"));

        // Transforms
        register(new ToolDefinition("geometry.transform.translate", "Translate Object", "GEOMETRY",
                "Translates 3D position of target object.", ToolDefinition.AvailabilityState.AVAILABLE)
                .addParam("objectId", "STRING", true, "Target object ID")
                .addParam("x", "FLOAT", true, "X translation")
                .addParam("y", "FLOAT", true, "Y translation")
                .addParam("z", "FLOAT", true, "Z translation"));

        register(new ToolDefinition("geometry.transform.rotate", "Rotate Object", "GEOMETRY",
                "Rotates target object along pitch/yaw/roll axes.", ToolDefinition.AvailabilityState.AVAILABLE)
                .addParam("objectId", "STRING", true, "Target object ID")
                .addParam("x", "FLOAT", true, "Pitch angle degrees")
                .addParam("y", "FLOAT", true, "Yaw angle degrees")
                .addParam("z", "FLOAT", true, "Roll angle degrees"));

        register(new ToolDefinition("geometry.transform.scale", "Scale Object", "GEOMETRY",
                "Scales target object.", ToolDefinition.AvailabilityState.AVAILABLE)
                .addParam("objectId", "STRING", true, "Target object ID")
                .addParam("scaleX", "FLOAT", true, "X scale factor")
                .addParam("scaleY", "FLOAT", true, "Y scale factor")
                .addParam("scaleZ", "FLOAT", true, "Z scale factor"));

        // Materials
        register(new ToolDefinition("material.set_properties", "Set Material Properties", "MATERIAL",
                "Sets PBR color, metallic, roughness, and opacity of object material.", ToolDefinition.AvailabilityState.AVAILABLE)
                .addParam("objectId", "STRING", true, "Target object ID")
                .addParam("colorHex", "STRING", false, "Base color hex string like #FF0000 or #1A2B3C")
                .addParam("metallic", "FLOAT", false, "Metallic factor 0.0 to 1.0")
                .addParam("roughness", "FLOAT", false, "Roughness factor 0.0 to 1.0"));

        // Characters & Creatures
        register(new ToolDefinition("character.create_humanoid", "Create Humanoid Character", "CHARACTER",
                "Generates a 3D humanoid character with anatomy, mesh, skeleton, and rig.", ToolDefinition.AvailabilityState.AVAILABLE)
                .addParam("name", "STRING", false, "Character name")
                .addParam("height", "FLOAT", false, "Height in meters")
                .addParam("style", "STRING", false, "realistic, superhero, cartoon, low_poly"));

        register(new ToolDefinition("character.create_creature", "Create Creature / Animal", "CHARACTER",
                "Generates a 3D animal or creature (dog, bird, quadruped, fantasy).", ToolDefinition.AvailabilityState.AVAILABLE)
                .addParam("species", "STRING", true, "dog, bird, quadruped, creature")
                .addParam("name", "STRING", false, "Creature name"));

        // Skeleton & Rigging
        register(new ToolDefinition("skeleton.bind", "Bind Skeleton & Calculate Weights", "SKELETON",
                "Binds skeleton bone hierarchy to mesh and calculates normalized skin vertex weights.", ToolDefinition.AvailabilityState.AVAILABLE)
                .addParam("characterId", "STRING", true, "Target character ID"));

        register(new ToolDefinition("rig.create_ik", "Create IK Controller", "RIG",
                "Creates Inverse Kinematics solver chain for target limbs (hands, feet, head).", ToolDefinition.AvailabilityState.AVAILABLE)
                .addParam("characterId", "STRING", true, "Target character ID")
                .addParam("limb", "STRING", true, "left_arm, right_arm, left_leg, right_leg"));

        // Animation
        register(new ToolDefinition("animation.create_clip", "Create & Apply Animation Clip", "ANIMATION",
                "Applies keyframed or procedural motion clip (walk, run, idle, crouch, fly, wave).", ToolDefinition.AvailabilityState.AVAILABLE)
                .addParam("characterId", "STRING", true, "Target character ID")
                .addParam("clipName", "STRING", true, "walk, run, idle, crouch, fly, wave"));

        // Lighting & Camera
        register(new ToolDefinition("scene.add_light", "Add Light Source", "LIGHTING",
                "Adds directional, point, or spot light to active scene.", ToolDefinition.AvailabilityState.AVAILABLE)
                .addParam("type", "STRING", true, "directional, point, spot, ambient")
                .addParam("colorHex", "STRING", false, "Light color hex")
                .addParam("intensity", "FLOAT", false, "Light brightness intensity"));

        register(new ToolDefinition("scene.set_camera", "Set Camera Viewpoint", "CAMERA",
                "Positions camera target and framing.", ToolDefinition.AvailabilityState.AVAILABLE)
                .addParam("posX", "FLOAT", false, "Camera X position")
                .addParam("posY", "FLOAT", false, "Camera Y position")
                .addParam("posZ", "FLOAT", false, "Camera Z position")
                .addParam("targetX", "FLOAT", false, "Look target X")
                .addParam("targetY", "FLOAT", false, "Look target Y")
                .addParam("targetZ", "FLOAT", false, "Look target Z"));

        // Validation & Export
        register(new ToolDefinition("validation.check_mesh", "Validate Mesh & Rig", "VALIDATION",
                "Inspects topology, vertex normals, skin weights, and bounding boxes.", ToolDefinition.AvailabilityState.AVAILABLE)
                .addParam("objectId", "STRING", false, "Target object ID or null for entire scene"));

        register(new ToolDefinition("export.gltf", "Export Scene to GLTF/GLB", "EXPORT",
                "Exports active 3D scene geometry, materials, and hierarchy to GLTF.", ToolDefinition.AvailabilityState.AVAILABLE)
                .addParam("filename", "STRING", false, "Export filename"));
    }

    public void register(ToolDefinition tool) {
        registeredTools.put(tool.getId(), tool);
    }

    public ToolDefinition getTool(String id) {
        return registeredTools.get(id);
    }

    public boolean isToolAvailable(String id) {
        ToolDefinition t = registeredTools.get(id);
        return t != null && t.isAvailable();
    }

    public Map<String, ToolDefinition> getRegisteredTools() {
        return registeredTools;
    }
}
