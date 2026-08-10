package com.example.character;

import com.example.engine.Mesh;

import java.util.ArrayList;
import java.util.List;

public class Skin {
    private Skeleton skeleton;
    private final List<List<SkinWeight>> vertexSkinWeights = new ArrayList<>();
    private static final int MAX_BONE_INFLUENCES_PER_VERTEX = 4;

    public Skin(Skeleton skeleton, int vertexCount) {
        this.skeleton = skeleton;
        initMultiBoneSkinning(vertexCount);
    }

    public Skin(Skeleton skeleton, Mesh mesh) {
        this.skeleton = skeleton;
        if (mesh != null && mesh.getVertices() != null && mesh.getVertices().length >= 3) {
            bindMeshVerticesToSkeleton(mesh.getVertices());
        } else {
            initMultiBoneSkinning(mesh != null ? mesh.getVertexCount() : 100);
        }
    }

    /**
     * Phase 8 Alignment: Binds 3D mesh vertex positions to the closest skeleton bones
     * using proximity and distance-decay functions across up to 4 bone influences.
     */
    public void bindMeshVerticesToSkeleton(float[] vertexPositions) {
        vertexSkinWeights.clear();
        if (skeleton == null || skeleton.getAllBones().isEmpty() || vertexPositions == null) return;

        List<Bone> allBones = skeleton.getAllBones();
        int vertexCount = vertexPositions.length / 3;

        for (int i = 0; i < vertexCount; i++) {
            float vx = vertexPositions[i * 3];
            float vy = vertexPositions[i * 3 + 1];
            float vz = vertexPositions[i * 3 + 2];

            List<SkinWeight> vertexWeights = new ArrayList<>();
            float totalCalculatedWeight = 0f;

            // Calculate distance to each bone's local transform position
            for (Bone bone : allBones) {
                if (bone == null || bone.getLocalTransform() == null) continue;

                float bx = bone.getLocalTransform().getPx();
                float by = bone.getLocalTransform().getPy();
                float bz = bone.getLocalTransform().getPz();

                float distSq = (vx - bx) * (vx - bx) + (vy - by) * (vy - by) + (vz - bz) * (vz - bz);
                float distance = (float) Math.sqrt(distSq);

                // Inverse distance weight decay
                float rawWeight = 1.0f / (distance + 0.1f);
                vertexWeights.add(new SkinWeight(bone.getId(), rawWeight));
                totalCalculatedWeight += rawWeight;
            }

            // Keep top MAX_BONE_INFLUENCES_PER_VERTEX
            vertexWeights.sort((w1, w2) -> Float.compare(w2.getWeight(), w1.getWeight()));
            if (vertexWeights.size() > MAX_BONE_INFLUENCES_PER_VERTEX) {
                vertexWeights = new ArrayList<>(vertexWeights.subList(0, MAX_BONE_INFLUENCES_PER_VERTEX));
            }

            vertexSkinWeights.add(vertexWeights);
        }

        normalizeWeights();
    }

    /**
     * Phase 8 Alignment: Purged 100% root-only weighting. Distributes initial weights
     * across bone chains (Spine, Pelvis, Limbs) sequentially.
     */
    private void initMultiBoneSkinning(int vertexCount) {
        vertexSkinWeights.clear();
        if (skeleton == null || skeleton.getAllBones().isEmpty()) return;

        List<Bone> bones = skeleton.getAllBones();
        int boneCount = bones.size();

        for (int i = 0; i < vertexCount; i++) {
            List<SkinWeight> weights = new ArrayList<>();
            
            // Assign primary and secondary adjacent bone influences
            int primaryIdx = i % boneCount;
            int secondaryIdx = (i + 1) % boneCount;

            String primaryBoneId = bones.get(primaryIdx).getId();
            String secondaryBoneId = bones.get(secondaryIdx).getId();

            weights.add(new SkinWeight(primaryBoneId, 0.7f));
            if (!primaryBoneId.equals(secondaryBoneId)) {
                weights.add(new SkinWeight(secondaryBoneId, 0.3f));
            } else {
                weights.add(new SkinWeight(skeleton.getRootBone().getId(), 0.3f));
            }

            vertexSkinWeights.add(weights);
        }

        normalizeWeights();
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
            } else if (!weights.isEmpty()) {
                weights.get(0).setWeight(1.0f);
            }
        }
    }

    /**
     * Phase 8 & 11 Alignment: Validates that every vertex has normalized weights
     * and valid bone ID references.
     */
    public boolean validateSkinning() {
        if (skeleton == null || vertexSkinWeights.isEmpty()) return false;

        for (List<SkinWeight> weights : vertexSkinWeights) {
            if (weights == null || weights.isEmpty()) return false;

            float sum = 0f;
            for (SkinWeight sw : weights) {
                if (sw.getBoneId() == null || skeleton.getBoneById(sw.getBoneId()) == null) {
                    return false; // Invalid bone reference
                }
                sum += sw.getWeight();
            }

            if (Math.abs(sum - 1.0f) > 0.01f) {
                return false; // Non-normalized weights
            }
        }

        return true;
    }

    public Skeleton getSkeleton() { return skeleton; }
    public List<List<SkinWeight>> getVertexSkinWeights() { return vertexSkinWeights; }
}