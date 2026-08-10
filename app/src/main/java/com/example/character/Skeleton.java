package com.example.character;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Skeleton {
    private Bone rootBone;
    private final Map<String, Bone> boneMap = new HashMap<>();

    public Skeleton(Bone rootBone) {
        this.rootBone = rootBone;
        registerBoneRecursively(rootBone);
    }

    private void registerBoneRecursively(Bone bone) {
        if (bone == null) return;
        boneMap.put(bone.getSemanticName().toUpperCase(), bone);
        for (Bone child : bone.getChildren()) {
            registerBoneRecursively(child);
        }
    }

    public Bone getRootBone() { return rootBone; }

    public Bone getBoneBySemanticName(String semanticName) {
        if (semanticName == null) return null;
        return boneMap.get(semanticName.toUpperCase());
    }

    public List<Bone> getAllBones() {
        return new ArrayList<>(boneMap.values());
    }

    public int getBoneCount() { return boneMap.size(); }
}
