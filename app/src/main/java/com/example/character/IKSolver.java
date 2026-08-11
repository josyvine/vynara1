package com.example.character;

public class IKSolver {

    /**
     * Phase 9 Alignment: Analytical 2-Bone Inverse Kinematics (IK) solver
     * using the Law of Cosines to solve upper and lower joint angles towards 3D target coordinates.
     */
    public static boolean solveLimbIK(Bone upperBone, Bone lowerBone, Bone endBone, 
                                       float targetX, float targetY, float targetZ) {
        return solveTwoBoneIKWithPole(upperBone, lowerBone, endBone, 
                targetX, targetY, targetZ, 0f, 0f, 1f);
    }

    public static boolean solveTwoBoneIK(Bone upperBone, Bone lowerBone, Bone endBone, 
                                         float targetX, float targetY, float targetZ) {
        return solveTwoBoneIKWithPole(upperBone, lowerBone, endBone, 
                targetX, targetY, targetZ, 0f, 0f, 1f);
    }

    /**
     * Phase 9 Alignment: Solves 2-Bone IK chain (e.g. Shoulder-Elbow-Hand or Thigh-Knee-Foot)
     * using Law of Cosines with target position and pole vector orientation.
     */
    public static boolean solveTwoBoneIKWithPole(Bone upperBone, Bone lowerBone, Bone endBone, 
                                                 float targetX, float targetY, float targetZ, 
                                                 float poleX, float poleY, float poleZ) {
        if (upperBone == null || lowerBone == null || endBone == null) return false;

        float ux = upperBone.getLocalTransform().getPx();
        float uy = upperBone.getLocalTransform().getPy();
        float uz = upperBone.getLocalTransform().getPz();

        float lx = lowerBone.getLocalTransform().getPx();
        float ly = lowerBone.getLocalTransform().getPy();
        float lz = lowerBone.getLocalTransform().getPz();

        float ex = endBone.getLocalTransform().getPx();
        float ey = endBone.getLocalTransform().getPy();
        float ez = endBone.getLocalTransform().getPz();

        // Calculate bone length segments
        float len1 = (float) Math.sqrt((lx - ux) * (lx - ux) + (ly - uy) * (ly - uy) + (lz - uz) * (lz - uz));
        float len2 = (float) Math.sqrt((ex - lx) * (ex - lx) + (ey - ly) * (ey - ly) + (ez - lz) * (ez - lz));

        if (len1 <= 0.001f) len1 = 0.4f;
        if (len2 <= 0.001f) len2 = 0.4f;

        // Vector from upper bone origin to target
        float dx = targetX - ux;
        float dy = targetY - uy;
        float dz = targetZ - uz;

        float distSq = dx * dx + dy * dy + dz * dz;
        float dist = (float) Math.sqrt(distSq);

        // Clamp distance within bone chain reach limits
        float maxDist = (len1 + len2) * 0.999f;
        float minDist = Math.abs(len1 - len2) + 0.001f;
        dist = Math.max(minDist, Math.min(maxDist, dist));

        // Law of Cosines: Angle alpha at upper joint
        float cosAlpha = (len1 * len1 + dist * dist - len2 * len2) / (2f * len1 * dist);
        cosAlpha = Math.max(-1f, Math.min(1f, cosAlpha));
        float alphaRad = (float) Math.acos(cosAlpha);

        // Law of Cosines: Angle beta at knee/elbow joint
        float cosBeta = (len1 * len1 + len2 * len2 - dist * dist) / (2f * len1 * len2);
        cosBeta = Math.max(-1f, Math.min(1f, cosBeta));
        float betaRad = (float) Math.acos(cosBeta);

        // Yaw and Pitch angles towards target position
        float yawDeg = (float) Math.toDegrees(Math.atan2(dx, dz));
        float pitchDeg = (float) Math.toDegrees(Math.atan2(-dy, Math.sqrt(dx * dx + dz * dz)));

        float alphaDeg = (float) Math.toDegrees(alphaRad);
        float betaDeg = (float) Math.toDegrees(Math.PI - betaRad);

        // Apply joint limit constraints (prevent elbows and knees from bending backward)
        if (betaDeg < 0.0f) {
            betaDeg = 0.0f;
        } else if (betaDeg > 155.0f) {
            betaDeg = 155.0f; // Soft limit
        }

        // Incorporate pole vector coordinate offsets to align joint bend plane orientation
        float poleAngleDeg = 0.0f;
        if (Math.abs(poleX) > 0.001f || Math.abs(poleY) > 0.001f) {
            poleAngleDeg = (float) Math.toDegrees(Math.atan2(poleY - uy, poleX - ux));
        }

        // Apply calculated IK angles to bone transforms
        upperBone.getLocalTransform().setRotation(pitchDeg - alphaDeg, yawDeg + poleAngleDeg, 0f);
        lowerBone.getLocalTransform().setRotation(betaDeg, 0f, 0f);

        return true;
    }
}