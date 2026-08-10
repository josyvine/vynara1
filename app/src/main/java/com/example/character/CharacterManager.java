package com.example.character;

import com.example.engine.MaterialManager;
import com.example.engine.PrimitiveGenerator;
import com.example.engine.SceneObject;
import com.example.engine.ThreeDEngine;

import java.util.HashMap;
import java.util.Map;

public class CharacterManager {
    private final ThreeDEngine engine;
    private final Map<String, Character> characterMap = new HashMap<>();

    public CharacterManager(ThreeDEngine engine) {
        this.engine = engine;
    }

    public Character createHumanoid(CharacterSpecification spec) {
        String id = "char_" + System.currentTimeMillis();
        Skeleton skeleton = SkeletonBuilder.buildHumanoidSkeleton(spec.getHeight());

        SceneObject meshObj = new SceneObject(id, spec.getName(), "CHARACTER",
                PrimitiveGenerator.createCube(0.8f, spec.getHeight(), 0.5f),
                engine.getMaterialManager().getMaterial("mat_skin"));

        engine.getSceneManager().getActiveScene().addObject(meshObj);

        Character character = new Character(id, spec, meshObj, skeleton);
        characterMap.put(id, character);
        return character;
    }

    public Character createCreature(CharacterSpecification spec) {
        String id = "creature_" + System.currentTimeMillis();
        Skeleton skeleton;
        if ("bird".equalsIgnoreCase(spec.getSpecies())) {
            skeleton = SkeletonBuilder.buildBirdSkeleton();
        } else {
            skeleton = SkeletonBuilder.buildQuadrupedSkeleton();
        }

        SceneObject meshObj = new SceneObject(id, spec.getName(), "CREATURE",
                PrimitiveGenerator.createCube(1.2f, 0.8f, 1.8f),
                engine.getMaterialManager().getMaterial("mat_default"));

        engine.getSceneManager().getActiveScene().addObject(meshObj);

        Character character = new Character(id, spec, meshObj, skeleton);
        characterMap.put(id, character);
        return character;
    }

    public Character getCharacter(String id) {
        return characterMap.get(id);
    }

    public Map<String, Character> getCharacterMap() {
        return characterMap;
    }
}
