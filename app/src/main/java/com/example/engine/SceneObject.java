package com.example.engine;

import java.util.ArrayList;
import java.util.List;

public class SceneObject {
    private String id;
    private String name;
    private String semanticType; // PRIMITIVE, STRUCTURE, HOUSE, SOFA, CHARACTER, CREATURE
    private Transform transform;
    private Mesh mesh;
    private Material material;
    private boolean isVisible = true;
    private boolean isSelected = false;

    private SceneObject parent;
    private final List<SceneObject> children = new ArrayList<>();

    public SceneObject(String id, String name, String semanticType, Mesh mesh, Material material) {
        this.id = id;
        this.name = name;
        this.semanticType = semanticType;
        this.mesh = mesh;
        this.material = material;
        this.transform = new Transform();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getSemanticType() { return semanticType; }
    public Transform getTransform() { return transform; }
    public Mesh getMesh() { return mesh; }
    public Material getMaterial() { return material; }
    public boolean isVisible() { return isVisible; }
    public boolean isSelected() { return isSelected; }

    public void setMaterial(Material material) { this.material = material; }
    public void setVisible(boolean visible) { isVisible = visible; }
    public void setSelected(boolean selected) { isSelected = selected; }

    public void addChild(SceneObject child) {
        if (child != null) {
            child.parent = this;
            children.add(child);
        }
    }

    public List<SceneObject> getChildren() { return children; }
    public SceneObject getParent() { return parent; }
}
