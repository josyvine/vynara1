package com.example.character;

public class IKSolver {

    public static boolean solveLimbIK(Bone upperBone, Bone lowerBone, Bone endBone, float targetX, float targetY, float targetZ) {
        if (upperBone == null || lowerBone == null || endBone == null) return false;

        // Position target on end bone
        float dx = targetX - upperBone.getLocalTransform().getPx();
        float dy = targetY - upperBone.getLocalTransform().getPy();
        float dz = targetZ - upperBone.getLocalTransform().getPz();

        float angleY = (float) Math.toDegrees(Math.atan2(dx, dz));
        float angleX = (float) Math.toDegrees(Math.atan2(-dy, Math.sqrt(dx * dx + dz * dz)));

        upperBone.getLocalTransform().setRotation(angleX, angleY, 0f);
        lowerBone.getLocalTransform().setRotation(angleX * 0.5f, 0f, 0f);
        return true;
    }
}
