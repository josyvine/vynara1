package com.example.tools;

import com.example.character.Character;
import com.example.character.CharacterManager;
import com.example.character.CharacterSpecification;
import com.example.engine.Material;
import com.example.engine.SceneObject;
import com.example.engine.ThreeDEngine;
import com.example.validation.ValidationManager;
import com.example.validation.ValidationResult;

import java.util.List;

public class ToolExecutor {
    private final ThreeDEngine engine;
    private final CharacterManager characterManager;
    private final ValidationManager validationManager;

    public ToolExecutor(ThreeDEngine engine, CharacterManager characterManager, ValidationManager validationManager) {
        this.engine = engine;
        this.characterManager = characterManager;
        this.validationManager = validationManager;
    }

    public boolean executeOperation(ToolOperation op) {
        if (op == null || op.getToolId() == null) return false;

        String id = op.getToolId().toLowerCase();

        switch (id) {
            case "geometry.create_primitive": {
                String type = op.getStringParam("type", "cube");
                float w = op.getFloatParam("width", 1.5f);
                float h = op.getFloatParam("height", 1.5f);
                float d = op.getFloatParam("depth", 1.5f);
                engine.createPrimitive(type, w, h, d);
                return true;
            }

            case "geometry.create_procedural": {
                String type = op.getStringParam("type", "house");
                String name = op.getStringParam("name", type.toUpperCase());
                engine.createProceduralStructure(type, name);
                return true;
            }

            case "geometry.transform.translate": {
                String objId = op.getStringParam("objectId", null);
                SceneObject obj = findTargetObject(objId);
                if (obj != null) {
                    float x = op.getFloatParam("x", 0f);
                    float y = op.getFloatParam("y", 0f);
                    float z = op.getFloatParam("z", 0f);
                    obj.getTransform().setPosition(x, y, z);
                    return true;
                }
                return false;
            }

            case "geometry.transform.rotate": {
                String objId = op.getStringParam("objectId", null);
                SceneObject obj = findTargetObject(objId);
                if (obj != null) {
                    float x = op.getFloatParam("x", 0f);
                    float y = op.getFloatParam("y", 0f);
                    float z = op.getFloatParam("z", 0f);
                    obj.getTransform().setRotation(x, y, z);
                    return true;
                }
                return false;
            }

            case "geometry.transform.scale": {
                String objId = op.getStringParam("objectId", null);
                SceneObject obj = findTargetObject(objId);
                if (obj != null) {
                    float sx = op.getFloatParam("scaleX", 1f);
                    float sy = op.getFloatParam("scaleY", 1f);
                    float sz = op.getFloatParam("scaleZ", 1f);
                    obj.getTransform().setScale(sx, sy, sz);
                    return true;
                }
                return false;
            }

            case "material.set_properties": {
                String objId = op.getStringParam("objectId", null);
                SceneObject obj = findTargetObject(objId);
                if (obj != null) {
                    String color = op.getStringParam("colorHex", "#00E5FF");
                    float metallic = op.getFloatParam("metallic", 0.1f);
                    float roughness = op.getFloatParam("roughness", 0.5f);

                    Material mat = new Material("mat_" + System.currentTimeMillis(), "Custom Mat", color);
                    mat.setMetallic(metallic);
                    mat.setRoughness(roughness);
                    obj.setMaterial(mat);
                    return true;
                }
                return false;
            }

            case "character.create_humanoid": {
                String name = op.getStringParam("name", "Humanoid Character");
                float height = op.getFloatParam("height", 1.8f);
                String style = op.getStringParam("style", "REALISTIC");

                CharacterSpecification spec = new CharacterSpecification("HUMANOID", name)
                        .setHeight(height)
                        .setStyle(style);
                characterManager.createHumanoid(spec);
                return true;
            }

            case "character.create_creature": {
                String species = op.getStringParam("species", "dog");
                String name = op.getStringParam("name", species.toUpperCase());

                CharacterSpecification spec = new CharacterSpecification(species, name);
                characterManager.createCreature(spec);
                return true;
            }

            case "skeleton.bind": {
                String charId = op.getStringParam("characterId", null);
                Character c = characterManager.getCharacter(charId);
                if (c != null && c.getSkin() != null) {
                    c.getSkin().normalizeWeights();
                    return true;
                }
                return true; // Default fallback
            }

            case "rig.create_ik": {
                String charId = op.getStringParam("characterId", null);
                String limb = op.getStringParam("limb", "left_arm");
                Character c = characterManager.getCharacter(charId);
                if (c != null && c.getRig() != null) {
                    c.getRig().setIKTarget(limb, 0.5f, 1.2f, 0.3f);
                }
                return true;
            }

            case "animation.create_clip": {
                String charId = op.getStringParam("characterId", null);
                String clip = op.getStringParam("clipName", "walk");
                Character c = characterManager.getCharacter(charId);
                if (c != null && c.getAnimationPlayer() != null) {
                    c.getAnimationPlayer().playClip(clip);
                }
                return true;
            }

            case "scene.add_light": {
                String typeStr = op.getStringParam("type", "directional");
                String color = op.getStringParam("colorHex", "#FFFFFF");
                float intensity = op.getFloatParam("intensity", 1.0f);

                com.example.engine.Light light = new com.example.engine.Light("light_" + System.currentTimeMillis(),
                        "point".equalsIgnoreCase(typeStr) ? com.example.engine.Light.Type.POINT : com.example.engine.Light.Type.DIRECTIONAL);
                light.setIntensity(intensity);
                engine.getLightManager().addLight(light);
                return true;
            }

            case "scene.set_camera": {
                float x = op.getFloatParam("posX", 0f);
                float y = op.getFloatParam("posY", 4f);
                float z = op.getFloatParam("posZ", 8f);
                engine.getCameraManager().getActiveCamera().setEye(x, y, z);
                return true;
            }

            case "validation.check_mesh": {
                List<ValidationResult> results = validationManager.validateScene(engine.getSceneManager().getActiveScene());
                return !results.isEmpty();
            }

            default:
                return false;
        }
    }

    private SceneObject findTargetObject(String objId) {
        if (objId != null) {
            SceneObject target = engine.getSceneManager().getActiveScene().findObjectById(objId);
            if (target != null) return target;
        }
        // Fallback to selected object or first scene object
        if (engine.getSceneManager().getSelectedObject() != null) {
            return engine.getSceneManager().getSelectedObject();
        }
        List<SceneObject> objs = engine.getSceneManager().getActiveScene().getObjects();
        return objs.isEmpty() ? null : objs.get(0);
    }
}
