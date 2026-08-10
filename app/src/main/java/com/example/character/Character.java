package com.example.character;

import com.example.engine.SceneObject;

public class Character {
    private String id;
    private CharacterSpecification specification;
    private SceneObject sceneObject;
    private Skeleton skeleton;
    private Skin skin;
    private Rig rig;
    private AnimationPlayer animationPlayer;

    public Character(String id, CharacterSpecification spec, SceneObject sceneObject, Skeleton skeleton) {
        this.id = id;
        this.specification = spec;
        this.sceneObject = sceneObject;
        this.skeleton = skeleton;
        this.skin = new Skin(skeleton, sceneObject != null && sceneObject.getMesh() != null ? sceneObject.getMesh().getVertexCount() : 100);
        this.rig = new Rig(skeleton);
        this.animationPlayer = new AnimationPlayer(skeleton);
    }

    public String getId() { return id; }
    public CharacterSpecification getSpecification() { return specification; }
    public SceneObject getSceneObject() { return sceneObject; }
    public Skeleton getSkeleton() { return skeleton; }
    public Skin getSkin() { return skin; }
    public Rig getRig() { return rig; }
    public AnimationPlayer getAnimationPlayer() { return animationPlayer; }
}
