package com.example.knowledge;

import java.util.ArrayList;
import java.util.List;

public class KnowledgeEntry {
    private String id;
    private String name;
    private String category; // PHYSICAL_OBJECT, CHARACTER, ANIMAL, ARCHITECTURE, ENVIRONMENT, VEHICLE, FURNITURE
    private String proceduralGeneratorType;
    private final List<String> components;
    private final List<String> requiredCapabilities;
    private final List<String> defaultMaterials;

    public KnowledgeEntry(String id, String name, String category) {
        this.id = id != null ? id : "entry";
        this.name = name != null ? name : "Domain Concept";
        this.category = category != null ? category : "PHYSICAL_OBJECT";
        this.components = new ArrayList<>();
        this.requiredCapabilities = new ArrayList<>();
        this.defaultMaterials = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getProceduralGeneratorType() { return proceduralGeneratorType; }
    public List<String> getComponents() { return components; }
    public List<String> getRequiredCapabilities() { return requiredCapabilities; }
    public List<String> getDefaultMaterials() { return defaultMaterials; }

    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    
    public KnowledgeEntry setProceduralGeneratorType(String generatorType) {
        this.proceduralGeneratorType = generatorType;
        return this;
    }

    public KnowledgeEntry addComponent(String comp) {
        if (comp != null && !comp.trim().isEmpty() && !components.contains(comp)) {
            components.add(comp);
        }
        return this;
    }

    public KnowledgeEntry addCapability(String cap) {
        if (cap != null && !cap.trim().isEmpty() && !requiredCapabilities.contains(cap)) {
            requiredCapabilities.add(cap);
        }
        return this;
    }

    public KnowledgeEntry addMaterial(String mat) {
        if (mat != null && !mat.trim().isEmpty() && !defaultMaterials.contains(mat)) {
            defaultMaterials.add(mat);
        }
        return this;
    }

    public KnowledgeEntry cloneEntry() {
        KnowledgeEntry copy = new KnowledgeEntry(this.id, this.name, this.category);
        copy.setProceduralGeneratorType(this.proceduralGeneratorType);
        copy.components.addAll(this.components);
        copy.requiredCapabilities.addAll(this.requiredCapabilities);
        copy.defaultMaterials.addAll(this.defaultMaterials);
        return copy;
    }
}