package com.example.knowledge;

import java.util.ArrayList;
import java.util.List;

public class KnowledgeEntry {
    private String id;
    private String name;
    private String category; // PHYSICAL_OBJECT, CHARACTER, ANIMAL, ARCHITECTURE, ENVIRONMENT
    private List<String> components;
    private List<String> requiredCapabilities;
    private List<String> defaultMaterials;

    public KnowledgeEntry(String id, String name, String category) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.components = new ArrayList<>();
        this.requiredCapabilities = new ArrayList<>();
        this.defaultMaterials = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public List<String> getComponents() { return components; }
    public List<String> getRequiredCapabilities() { return requiredCapabilities; }
    public List<String> getDefaultMaterials() { return defaultMaterials; }

    public KnowledgeEntry addComponent(String comp) {
        components.add(comp);
        return this;
    }

    public KnowledgeEntry addCapability(String cap) {
        requiredCapabilities.add(cap);
        return this;
    }

    public KnowledgeEntry addMaterial(String mat) {
        defaultMaterials.add(mat);
        return this;
    }
}
