package com.example.character;

public class SkinWeight {
    private String boneId;
    private float weight;

    public SkinWeight(String boneId, float weight) {
        this.boneId = boneId;
        this.weight = weight;
    }

    public String getBoneId() { return boneId; }
    public float getWeight() { return weight; }
    public void setWeight(float weight) { this.weight = weight; }
}
