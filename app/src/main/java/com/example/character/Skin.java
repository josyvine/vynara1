package com.example.character;

import java.util.ArrayList;
import java.util.List;

public class Skin {
    private Skeleton skeleton;
    private final List<List<SkinWeight>> vertexSkinWeights = new ArrayList<>();

    public Skin(Skeleton skeleton, int vertexCount) {
        this.skeleton = skeleton;
        initDefaultSkinning(vertexCount);
    }

    private void initDefaultSkinning(int vertexCount) {
        if (skeleton == null || skeleton.getRootBone() == null) return;
        String rootBoneId = skeleton.getRootBone().getId();

        for (int i = 0; i < vertexCount; i++) {
            List<SkinWeight> weights = new ArrayList<>();
            weights.add(new SkinWeight(rootBoneId, 1.0f));
            vertexSkinWeights.add(weights);
        }
    }

    public void normalizeWeights() {
        for (List<SkinWeight> weights : vertexSkinWeights) {
            float sum = 0f;
            for (SkinWeight sw : weights) {
                sum += sw.getWeight();
            }
            if (sum > 0f) {
                for (SkinWeight sw : weights) {
                    sw.setWeight(sw.getWeight() / sum);
                }
            }
        }
    }

    public Skeleton getSkeleton() { return skeleton; }
    public List<List<SkinWeight>> getVertexSkinWeights() { return vertexSkinWeights; }
}
