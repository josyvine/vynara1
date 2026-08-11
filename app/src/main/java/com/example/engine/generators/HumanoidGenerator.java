package com.example.engine.generators;

import com.example.character.CharacterSpecification;
import com.example.engine.Material;
import com.example.engine.MaterialManager;
import com.example.engine.PrimitiveGenerator;
import com.example.engine.SceneObject;

public class HumanoidGenerator {

    /**
     * Phase 6 & 23 Alignment: Constructs a procedural anatomical humanoid character mesh hierarchy.
     * Generates distinct sub-nodes for head, neck, torso, pelvis, upper arms, forearms, hands,
     * thighs, calves, and feet, using proportional specifications and custom PBR materials.
     */
    public static SceneObject generateHumanoidMesh(String rootId, CharacterSpecification spec, MaterialManager matMgr) {
        float h = spec != null ? spec.getHeight() : 1.8f;
        float sw = spec != null ? spec.getShoulderWidth() : 0.45f;
        float limbRatio = spec != null ? spec.getLimbLengthRatio() : 1.0f;
        float headRatio = spec != null ? spec.getHeadSizeRatio() : 1.0f;
        float build = spec != null ? spec.getBodyBuildFactor() : 1.0f;

        Material skinMat = matMgr.getMaterial("mat_skin");
        Material clothesMat = matMgr.getMaterial("mat_fabric_grey");

        // 1. Root Pelvis Node (Anchors the hierarchy tree and sits at half-height)
        SceneObject pelvis = new SceneObject(rootId, spec != null ? spec.getName() : "Humanoid", "CHARACTER",
                PrimitiveGenerator.createCube(0.35f * build, 0.15f, 0.25f * build), clothesMat);
        pelvis.getTransform().setPosition(0f, h * 0.52f, 0f);

        // 2. Torso Chest Node (Connected to pelvis)
        SceneObject torso = new SceneObject(rootId + "_torso", "Torso", "CHARACTER",
                PrimitiveGenerator.createCube(0.4f * build, h * 0.28f, 0.25f * build), clothesMat);
        torso.getTransform().setPosition(0f, h * 0.22f, 0f);
        pelvis.addChild(torso);

        // 3. Neck & Head Node Chain
        SceneObject neck = new SceneObject(rootId + "_neck", "Neck", "CHARACTER",
                PrimitiveGenerator.createCylinder(0.08f * build, h * 0.08f, 8), skinMat);
        neck.getTransform().setPosition(0f, h * 0.18f, 0f);
        torso.addChild(neck);

        SceneObject head = new SceneObject(rootId + "_head", "Head", "CHARACTER",
                PrimitiveGenerator.createSphere(0.12f * headRatio, 12, 12), skinMat);
        head.getTransform().setPosition(0f, h * 0.12f, 0f);
        neck.addChild(head);

        // 4. Arms (Shoulder -> Upper arm -> Forearm -> Hand)
        float armLen = h * 0.2f * limbRatio;
        float shoulderOffset = sw / 2f;

        // Left Arm Chain
        SceneObject lUpperArm = new SceneObject(rootId + "_l_up_arm", "Left Upper Arm", "CHARACTER",
                PrimitiveGenerator.createCylinder(0.06f * build, armLen, 8), skinMat);
        lUpperArm.getTransform().setPosition(-shoulderOffset, h * 0.12f, 0f);
        torso.addChild(lUpperArm);

        SceneObject lForearm = new SceneObject(rootId + "_l_fore", "Left Forearm", "CHARACTER",
                PrimitiveGenerator.createCylinder(0.05f * build, armLen, 8), skinMat);
        lForearm.getTransform().setPosition(0f, -armLen, 0f);
        lUpperArm.addChild(lForearm);

        SceneObject lHand = new SceneObject(rootId + "_l_hand", "Left Hand", "CHARACTER",
                PrimitiveGenerator.createSphere(0.045f, 8, 8), skinMat);
        lHand.getTransform().setPosition(0f, -armLen * 0.8f, 0f);
        lForearm.addChild(lHand);

        // Right Arm Chain
        SceneObject rUpperArm = new SceneObject(rootId + "_r_up_arm", "Right Upper Arm", "CHARACTER",
                PrimitiveGenerator.createCylinder(0.06f * build, armLen, 8), skinMat);
        rUpperArm.getTransform().setPosition(shoulderOffset, h * 0.12f, 0f);
        torso.addChild(rUpperArm);

        SceneObject rForearm = new SceneObject(rootId + "_r_fore", "Right Forearm", "CHARACTER",
                PrimitiveGenerator.createCylinder(0.05f * build, armLen, 8), skinMat);
        rForearm.getTransform().setPosition(0f, -armLen, 0f);
        rUpperArm.addChild(rForearm);

        SceneObject rHand = new SceneObject(rootId + "_r_hand", "Right Hand", "CHARACTER",
                PrimitiveGenerator.createSphere(0.045f, 8, 8), skinMat);
        rHand.getTransform().setPosition(0f, -armLen * 0.8f, 0f);
        rForearm.addChild(rHand);

        // 5. Legs (Hip -> Thigh -> Calf -> Foot)
        float legLen = h * 0.26f * limbRatio;
        float hipOffset = 0.12f * build;

        // Left Leg Chain
        SceneObject lThigh = new SceneObject(rootId + "_l_thigh", "Left Thigh", "CHARACTER",
                PrimitiveGenerator.createCylinder(0.08f * build, legLen, 8), clothesMat);
        lThigh.getTransform().setPosition(-hipOffset, -h * 0.1f, 0f);
        pelvis.addChild(lThigh);

        SceneObject lCalf = new SceneObject(rootId + "_l_calf", "Left Calf", "CHARACTER",
                PrimitiveGenerator.createCylinder(0.06f * build, legLen, 8), skinMat);
        lCalf.getTransform().setPosition(0f, -legLen, 0f);
        lThigh.addChild(lCalf);

        SceneObject lFoot = new SceneObject(rootId + "_l_foot", "Left Foot", "CHARACTER",
                PrimitiveGenerator.createCube(0.08f * build, 0.05f, 0.16f), clothesMat);
        lFoot.getTransform().setPosition(0f, -legLen * 0.95f, 0.04f);
        lCalf.addChild(lFoot);

        // Right Leg Chain
        SceneObject rThigh = new SceneObject(rootId + "_r_thigh", "Right Thigh", "CHARACTER",
                PrimitiveGenerator.createCylinder(0.08f * build, legLen, 8), clothesMat);
        rThigh.getTransform().setPosition(hipOffset, -h * 0.1f, 0f);
        pelvis.addChild(rThigh);

        SceneObject rCalf = new SceneObject(rootId + "_r_calf", "Right Calf", "CHARACTER",
                PrimitiveGenerator.createCylinder(0.06f * build, legLen, 8), skinMat);
        rCalf.getTransform().setPosition(0f, -legLen, 0f);
        rThigh.addChild(rCalf);

        SceneObject rFoot = new SceneObject(rootId + "_r_foot", "Right Foot", "CHARACTER",
                PrimitiveGenerator.createCube(0.08f * build, 0.05f, 0.16f), clothesMat);
                rFoot.getTransform().setPosition(0f, -legLen * 0.95f, 0.04f);
        rCalf.addChild(rFoot);

        return pelvis;
    }
}