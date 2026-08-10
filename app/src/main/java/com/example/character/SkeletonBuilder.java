package com.example.character;

public class SkeletonBuilder {

    public static Skeleton buildHumanoidSkeleton(float height) {
        Bone root = new Bone("bone_root", "ROOT");
        Bone pelvis = new Bone("bone_pelvis", "PELVIS");
        pelvis.getLocalTransform().setPosition(0f, height * 0.5f, 0f);
        root.addChild(pelvis);

        // Spine chain
        Bone spine = new Bone("bone_spine", "SPINE");
        spine.getLocalTransform().setPosition(0f, 0.2f, 0f);
        pelvis.addChild(spine);

        Bone chest = new Bone("bone_chest", "CHEST");
        chest.getLocalTransform().setPosition(0f, 0.25f, 0f);
        spine.addChild(chest);

        Bone neck = new Bone("bone_neck", "NECK");
        neck.getLocalTransform().setPosition(0f, 0.2f, 0f);
        chest.addChild(neck);

        Bone head = new Bone("bone_head", "HEAD");
        head.getLocalTransform().setPosition(0f, 0.15f, 0f);
        neck.addChild(head);

        // Arms
        Bone lShoulder = new Bone("bone_l_shoulder", "LEFT_SHOULDER");
        lShoulder.getLocalTransform().setPosition(-0.15f, 0.15f, 0f);
        chest.addChild(lShoulder);

        Bone lArm = new Bone("bone_l_arm", "LEFT_ARM");
        lArm.getLocalTransform().setPosition(-0.25f, 0f, 0f);
        lShoulder.addChild(lArm);

        Bone lHand = new Bone("bone_l_hand", "LEFT_HAND");
        lHand.getLocalTransform().setPosition(-0.25f, 0f, 0f);
        lArm.addChild(lHand);

        Bone rShoulder = new Bone("bone_r_shoulder", "RIGHT_SHOULDER");
        rShoulder.getLocalTransform().setPosition(0.15f, 0.15f, 0f);
        chest.addChild(rShoulder);

        Bone rArm = new Bone("bone_r_arm", "RIGHT_ARM");
        rArm.getLocalTransform().setPosition(0.25f, 0f, 0f);
        rShoulder.addChild(rArm);

        Bone rHand = new Bone("bone_r_hand", "RIGHT_HAND");
        rHand.getLocalTransform().setPosition(0.25f, 0f, 0f);
        rArm.addChild(rHand);

        // Legs
        Bone lLeg = new Bone("bone_l_leg", "LEFT_LEG");
        lLeg.getLocalTransform().setPosition(-0.15f, -0.4f, 0f);
        pelvis.addChild(lLeg);

        Bone lFoot = new Bone("bone_l_foot", "LEFT_FOOT");
        lFoot.getLocalTransform().setPosition(0f, -0.4f, 0.1f);
        lLeg.addChild(lFoot);

        Bone rLeg = new Bone("bone_r_leg", "RIGHT_LEG");
        rLeg.getLocalTransform().setPosition(0.15f, -0.4f, 0f);
        pelvis.addChild(rLeg);

        Bone rFoot = new Bone("bone_r_foot", "RIGHT_FOOT");
        rFoot.getLocalTransform().setPosition(0f, -0.4f, 0.1f);
        rLeg.addChild(rFoot);

        return new Skeleton(root);
    }

    public static Skeleton buildQuadrupedSkeleton() {
        Bone root = new Bone("bone_root", "ROOT");
        Bone spine = new Bone("bone_spine", "SPINE");
        root.addChild(spine);

        Bone neck = new Bone("bone_neck", "NECK");
        spine.addChild(neck);

        Bone head = new Bone("bone_head", "HEAD");
        neck.addChild(head);

        Bone flLeg = new Bone("bone_fl_leg", "FRONT_LEFT_LEG");
        spine.addChild(flLeg);

        Bone frLeg = new Bone("bone_fr_leg", "FRONT_RIGHT_LEG");
        spine.addChild(frLeg);

        Bone rlLeg = new Bone("bone_rl_leg", "REAR_LEFT_LEG");
        spine.addChild(rlLeg);

        Bone rrLeg = new Bone("bone_rr_leg", "REAR_RIGHT_LEG");
        spine.addChild(rrLeg);

        Bone tail = new Bone("bone_tail", "TAIL");
        spine.addChild(tail);

        return new Skeleton(root);
    }

    public static Skeleton buildBirdSkeleton() {
        Bone root = new Bone("bone_root", "ROOT");
        Bone spine = new Bone("bone_spine", "SPINE");
        root.addChild(spine);

        Bone neck = new Bone("bone_neck", "NECK");
        spine.addChild(neck);

        Bone head = new Bone("bone_head", "HEAD");
        neck.addChild(head);

        Bone lWing = new Bone("bone_l_wing", "LEFT_WING");
        spine.addChild(lWing);

        Bone rWing = new Bone("bone_r_wing", "RIGHT_WING");
        spine.addChild(rWing);

        Bone tail = new Bone("bone_tail", "TAIL");
        spine.addChild(tail);

        return new Skeleton(root);
    }
}
