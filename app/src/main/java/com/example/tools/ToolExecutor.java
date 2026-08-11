package com.example.tools;

import com.example.character.Character;
import com.example.character.CharacterManager;
import com.example.character.CharacterSpecification;
import com.example.engine.Material;
import com.example.engine.SceneObject;
import com.example.engine.ThreeDEngine;
import com.example.export.GLTFExporter;
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

        String id = op.getToolId().toLowerCase().trim();

        switch (id) {
            case "geometry.create_primitive": {
                String type = op.getStringParam("type", "cube");
                float w = op.getFloatParam("width", 1.5f);
                float h = op.getFloatParam("height", 1.5f);
                float d = op.getFloatParam("depth", 1.5f);
                SceneObject obj = engine.createPrimitive(type, w, h, d);
                engine.getSceneManager().updateWorldTransforms();
                return obj != null;
            }

            case "geometry.create_procedural": {
                String type = op.getStringParam("type", "house");
                String name = op.getStringParam("name", type.toUpperCase());
                SceneObject obj = engine.createProceduralStructure(type, name);
                engine.getSceneManager().updateWorldTransforms();
                return obj != null;
            }

            case "geometry.transform.translate": {
                String objId = op.getStringParam("objectId", null);
                SceneObject obj = findTargetObject(objId);
                if (obj != null) {
                    float x = op.getFloatParam("x", 0f);
                    float y = op.getFloatParam("y", 0f);
                    float z = op.getFloatParam("z", 0f);
                    obj.getTransform().setPosition(x, y, z);
                    engine.getSceneManager().updateWorldTransforms();
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
                    engine.getSceneManager().updateWorldTransforms();
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
                    engine.getSceneManager().updateWorldTransforms();
                    return true;
                }
                return false;
            }

            case "geometry.delete_object": {
                String objId = op.getStringParam("objectId", null);
                boolean success;
                if (objId != null) {
                    engine.getSceneManager().getActiveScene().removeObject(objId);
                    success = true;
                } else {
                    success = engine.getSceneManager().deleteSelectedObject();
                }
                engine.getSceneManager().updateWorldTransforms();
                return success;
            }

            case "geometry.duplicate_object": {
                String objId = op.getStringParam("objectId", null);
                SceneObject target = findTargetObject(objId);
                if (target != null) {
                    SceneObject copy = engine.getSceneManager().duplicateObject(target);
                    engine.getSceneManager().updateWorldTransforms();
                    return copy != null;
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
                    float opacity = op.getFloatParam("opacity", 1.0f);

                    Material mat = new Material("mat_" + System.currentTimeMillis(), "Custom Mat", color);
                    mat.setMetallic(metallic);
                    mat.setRoughness(roughness);
                    mat.setOpacity(opacity);
                    obj.setMaterial(mat);
                    return true;
                }
                return false;
            }

            case "material.create": {
                String name = op.getStringParam("name", "New Material");
                String color = op.getStringParam("colorHex", "#FFFFFF");
                float metallic = op.getFloatParam("metallic", 0.0f);
                float roughness = op.getFloatParam("roughness", 0.5f);

                Material mat = engine.getMaterialManager().createCustomPBRMaterial(name, color, metallic, roughness);
                return mat != null;
            }

            case "character.create_humanoid": {
                String name = op.getStringParam("name", "Humanoid Character");
                float height = op.getFloatParam("height", 1.8f);
                String style = op.getStringParam("style", "REALISTIC");

                CharacterSpecification spec = new CharacterSpecification("HUMANOID", name)
                        .setHeight(height)
                        .setStyle(style);
                Character c = characterManager.createHumanoid(spec);
                engine.getSceneManager().updateWorldTransforms();
                return c != null;
            }

            case "character.create_creature": {
                String species = op.getStringParam("species", "dog");
                String name = op.getStringParam("name", species.toUpperCase());

                CharacterSpecification spec = new CharacterSpecification(species, name);
                Character c = characterManager.createCreature(spec);
                engine.getSceneManager().updateWorldTransforms();
                return c != null;
            }

            case "skeleton.bind": {
                String charId = op.getStringParam("characterId", null);
                Character c = characterManager.getCharacter(charId);
                if (c == null && !characterManager.getCharacterMap().isEmpty()) {
                    c = characterManager.getCharacterMap().values().iterator().next();
                }
                if (c != null && c.getSkin() != null) {
                    c.getSkin().normalizeWeights();
                    return true;
                }
                return c != null;
            }

            case "rig.create_ik": {
                String charId = op.getStringParam("characterId", null);
                String limb = op.getStringParam("limb", "left_arm");
                float targetX = op.getFloatParam("x", 0.5f);
                float targetY = op.getFloatParam("y", 1.2f);
                float targetZ = op.getFloatParam("z", 0.3f);

                Character c = characterManager.getCharacter(charId);
                if (c == null && !characterManager.getCharacterMap().isEmpty()) {
                    c = characterManager.getCharacterMap().values().iterator().next();
                }
                if (c != null && c.getRig() != null) {
                    c.getRig().setIKTarget(limb, targetX, targetY, targetZ);
                    return true;
                }
                return false;
            }

            case "animation.create_clip": {
                String charId = op.getStringParam("characterId", null);
                String clip = op.getStringParam("clipName", "walk");

                Character c = characterManager.getCharacter(charId);
                if (c == null && !characterManager.getCharacterMap().isEmpty()) {
                    c = characterManager.getCharacterMap().values().iterator().next();
                }
                if (c != null && c.getAnimationPlayer() != null) {
                    c.getAnimationPlayer().playClip(clip);
                    return true;
                }
                return false;
            }

            case "scene.add_light": {
                String typeStr = op.getStringParam("type", "directional");
                String color = op.getStringParam("colorHex", "#FFFFFF");
                float intensity = op.getFloatParam("intensity", 1.0f);

                com.example.engine.Light light = new com.example.engine.Light("light_" + System.currentTimeMillis(),
                        "point".equalsIgnoreCase(typeStr) ? com.example.engine.Light.Type.POINT : com.example.engine.Light.Type.DIRECTIONAL);
                light.setColorHex(color);
                light.setIntensity(intensity);
                engine.getLightManager().addLight(light);
                return true;
            }

            case "scene.set_camera": {
                float x = op.getFloatParam("posX", 0f);
                float y = op.getFloatParam("posY", 4f);
                float z = op.getFloatParam("posZ", 8f);
                float tx = op.getFloatParam("targetX", 0f);
                float ty = op.getFloatParam("targetY", 1f);
                float tz = op.getFloatParam("targetZ", 0f);

                engine.getCameraManager().getActiveCamera().setEye(x, y, z);
                engine.getCameraManager().getActiveCamera().setTarget(tx, ty, tz);
                return true;
            }

            case "validation.check_mesh": {
                if (validationManager == null || engine == null) return false;
                List<ValidationResult> results = validationManager.validateScene(engine.getSceneManager().getActiveScene());
                if (results == null) return false;
                
                // If any check fails inside validation results, fail the tool execution
                for (ValidationResult res : results) {
                    if (!res.isPassed()) {
                        return false;
                    }
                }
                return true;
            }

            case "export.gltf": {
                String gltfJson = GLTFExporter.exportSceneToGLTFJson(engine.getSceneManager().getActiveScene());
                return gltfJson != null && !gltfJson.contains("error");
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
        if (engine.getSceneManager().getSelectedObject() != null) {
            return engine.getSceneManager().getSelectedObject();
        }
        List<SceneObject> objs = engine.getSceneManager().getActiveScene().getObjects();
        return objs.isEmpty() ? null : objs.get(0);
    }

    public ThreeDEngine getEngine() { return engine; }
    public CharacterManager getCharacterManager() { return characterManager; }
    public ValidationManager getValidationManager() { return validationManager; }
}