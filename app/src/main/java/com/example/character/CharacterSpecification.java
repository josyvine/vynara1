package com.example.character;

public class CharacterSpecification {
    private String species; // HUMANOID, DOG, BIRD, QUADRUPED, CREATURE
    private String name;
    private float height = 1.8f;
    private String style = "REALISTIC"; // REALISTIC, SUPERHERO, CARTOON, LOW_POLY
    private boolean isRigRequired = true;
    private boolean isAnimationRequired = true;

    public CharacterSpecification(String species, String name) {
        this.species = species != null ? species.toUpperCase() : "HUMANOID";
        this.name = name != null ? name : "Character";
    }

    public String getSpecies() { return species; }
    public String getName() { return name; }
    public float getHeight() { return height; }
    public String getStyle() { return style; }
    public boolean isRigRequired() { return isRigRequired; }
    public boolean isAnimationRequired() { return isAnimationRequired; }

    public CharacterSpecification setHeight(float height) { this.height = height; return this; }
    public CharacterSpecification setStyle(String style) { this.style = style; return this; }
}
