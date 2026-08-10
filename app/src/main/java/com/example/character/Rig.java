package com.example.character;

import java.util.HashMap;
import java.util.Map;

public class Rig {
    private Skeleton skeleton;
    private final Map<String, float[]> ikTargets = new HashMap<>();

    public Rig(Skeleton skeleton) {
        this.skeleton = skeleton;
    }

    public void setIKTarget(String limb, float x, float y, float z) {
        ikTargets.put(limb.toLowerCase(), new float[] { x, y, z });

        if ("left_arm".equalsIgnoreCase(limb)) {
            IKSolver.solveLimbIK(
                    skeleton.getBoneBySemanticName("LEFT_SHOULDER"),
                    skeleton.getBoneBySemanticName("LEFT_ARM"),
                    skeleton.getBoneBySemanticName("LEFT_HAND"),
                    x, y, z);
        } else if ("right_arm".equalsIgnoreCase(limb)) {
            IKSolver.solveLimbIK(
                    skeleton.getBoneBySemanticName("RIGHT_SHOULDER"),
                    skeleton.getBoneBySemanticName("RIGHT_ARM"),
                    skeleton.getBoneBySemanticName("RIGHT_HAND"),
                    x, y, z);
        } else if ("left_leg".equalsIgnoreCase(limb)) {
            IKSolver.solveLimbIK(
                    skeleton.getBoneBySemanticName("LEFT_LEG"),
                    skeleton.getBoneBySemanticName("LEFT_LEG"),
                    skeleton.getBoneBySemanticName("LEFT_FOOT"),
                    x, y, z);
        } else if ("right_leg".equalsIgnoreCase(limb)) {
            IKSolver.solveLimbIK(
                    skeleton.getBoneBySemanticName("RIGHT_LEG"),
                    skeleton.getBoneBySemanticName("RIGHT_LEG"),
                    skeleton.getBoneBySemanticName("RIGHT_FOOT"),
                    x, y, z);
        }
    }

    public Skeleton getSkeleton() { return skeleton; }
    public Map<String, float[]> getIkTargets() { return ikTargets; }
}
