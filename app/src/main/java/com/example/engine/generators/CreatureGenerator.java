package com.example.engine.generators;

import com.example.character.CharacterSpecification;
import com.example.engine.Material;
import com.example.engine.MaterialManager;
import com.example.engine.PrimitiveGenerator;
import com.example.engine.SceneObject;

public class CreatureGenerator {

    /**
     * Phase 7 & 23 Alignment: Generates a procedural multi-component quadruped or bird creature.
     * Assembles body segments, leg chains, wings, tails, neck, and head under a parent-child scene graph 
     * hierarchy instead of returning a single primitive cube.
     */
    public static SceneObject generateCreatureMesh(String rootId, CharacterSpecification spec, MaterialManager matMgr) {
        String species = spec != null ? spec.getSpecies().toLowerCase() : "quadruped";
        String name = spec != null ? spec.getName() : "Creature";

        if ("bird".equalsIgnoreCase(species)) {
            return generateBird(rootId, name, matMgr);
        } else {
            return generateQuadruped(rootId, name, species, matMgr);
        }
    }

    private static SceneObject generateQuadruped(String rootId, String name, String species, MaterialManager matMgr) {
        Material skinMat = matMgr.getMaterial("mat_leather_brown");
        Material defaultMat = matMgr.getMaterial("mat_default");

        // 1. Root Pelvis Node (Anchors the quadruped and sits on the ground)
        SceneObject pelvis = new SceneObject(rootId, name, "CREATURE",
                PrimitiveGenerator.createCube(0.35f, 0.35f, 0.4f), skinMat);
        pelvis.getTransform().setPosition(0f, 0.5f, -0.2f);

        // 2. Spine Chest Node (Connected forward relative to pelvis)
        SceneObject chest = new SceneObject(rootId + "_chest", "Chest", "CREATURE",
                PrimitiveGenerator.createCube(0.4f, 0.4f, 0.5f), skinMat);
        chest.getTransform().setPosition(0f, 0.05f, 0.45f);
        pelvis.addChild(chest);

        // 3. Neck & Head Node Assembly
        SceneObject neck = new SceneObject(rootId + "_neck", "Neck", "CREATURE",
                PrimitiveGenerator.createCylinder(0.08f, 0.3f, 8), skinMat);
        neck.getTransform().setPosition(0f, 0.22f, 0.22f);
        neck.getTransform().setRotation(35.0f, 0f, 0f); // Natural dynamic neck slant
        chest.addChild(neck);

        SceneObject head = new SceneObject(rootId + "_head", "Head", "CREATURE",
                PrimitiveGenerator.createSphere(0.12f, 10, 10), skinMat);
        head.getTransform().setPosition(0f, 0.18f, 0.12f);
        neck.addChild(head);

        // Snout detail for dog/cat muzzle shape (Mounted relative to head skull)
        SceneObject snout = new SceneObject(rootId + "_snout", "Snout", "CREATURE",
                PrimitiveGenerator.createCube(0.08f, 0.08f, 0.12f), defaultMat);
        snout.getTransform().setPosition(0f, -0.02f, 0.12f);
        head.addChild(snout);

        // 4. Front Legs (Hip/Shoulder -> Upper leg -> Lower leg -> Foot)
        float flLegLen = 0.22f;
        float flX = 0.18f;

        // Front Left Leg
        SceneObject flUpper = new SceneObject(rootId + "_fl_up", "Front Left Upper Leg", "CREATURE",
                PrimitiveGenerator.createCylinder(0.07f, flLegLen, 8), skinMat);
        flUpper.getTransform().setPosition(-flX, -0.16f, 0.16f);
        chest.addChild(flUpper);

        SceneObject flLower = new SceneObject(rootId + "_fl_low", "Front Left Lower Leg", "CREATURE",
                PrimitiveGenerator.createCylinder(0.05f, flLegLen, 8), skinMat);
        flLower.getTransform().setPosition(0f, -flLegLen, 0f);
        flUpper.addChild(flLower);

        // Front Right Leg
        SceneObject frUpper = new SceneObject(rootId + "_fr_up", "Front Right Upper Leg", "CREATURE",
                PrimitiveGenerator.createCylinder(0.07f, flLegLen, 8), skinMat);
        frUpper.getTransform().setPosition(flX, -0.16f, 0.16f);
        chest.addChild(frUpper);

        SceneObject frLower = new SceneObject(rootId + "_fr_low", "Front Right Lower Leg", "CREATURE",
                PrimitiveGenerator.createCylinder(0.05f, flLegLen, 8), skinMat);
        frLower.getTransform().setPosition(0f, -flLegLen, 0f);
        frUpper.addChild(frLower);

        // 5. Rear Legs
        float rlLegLen = 0.24f;
        float rlX = 0.16f;

        // Rear Left Leg
        SceneObject rlUpper = new SceneObject(rootId + "_rl_up", "Rear Left Upper Leg", "CREATURE",
                PrimitiveGenerator.createCylinder(0.08f, rlLegLen, 8), skinMat);
        rlUpper.getTransform().setPosition(-rlX, -0.16f, -0.12f);
        pelvis.addChild(rlUpper);

        SceneObject rlLower = new SceneObject(rootId + "_rl_low", "Rear Left Lower Leg", "CREATURE",
                PrimitiveGenerator.createCylinder(0.06f, rlLegLen, 8), skinMat);
        rlLower.getTransform().setPosition(0f, -rlLegLen, 0f);
        rlUpper.addChild(rlLower);

        // Rear Right Leg
        SceneObject rrUpper = new SceneObject(rootId + "_rr_up", "Rear Right Upper Leg", "CREATURE",
                PrimitiveGenerator.createCylinder(0.08f, rlLegLen, 8), skinMat);
        rrUpper.getTransform().setPosition(rlX, -0.16f, -0.12f);
        pelvis.addChild(rrUpper);

        SceneObject rrLower = new SceneObject(rootId + "_rr_low", "Rear Right Lower Leg", "CREATURE",
                PrimitiveGenerator.createCylinder(0.06f, rlLegLen, 8), skinMat);
        rrLower.getTransform().setPosition(0f, -rlLegLen, 0f);
        rrUpper.addChild(rrLower);

        // 6. Tail (Mounted relative to pelvis anchor)
        SceneObject tail = new SceneObject(rootId + "_tail", "Tail", "CREATURE",
                PrimitiveGenerator.createCylinder(0.04f, 0.35f, 8), skinMat);
        tail.getTransform().setPosition(0f, 0.12f, -0.22f);
        tail.getTransform().setRotation(-35.0f, 0f, 0f); // Slanted downward tail
        pelvis.addChild(tail);

        return pelvis;
    }

    private static SceneObject generateBird(String rootId, String name, MaterialManager matMgr) {
        Material skinMat = matMgr.getMaterial("mat_foliage"); // Green feather PBR placeholder
        Material beakMat = matMgr.getMaterial("mat_metallic_gold");

        // 1. Root Spine Body Node (Acts as primary transform anchor)
        SceneObject spine = new SceneObject(rootId, name, "CREATURE",
                PrimitiveGenerator.createCube(0.35f, 0.35f, 0.5f), skinMat);
        spine.getTransform().setPosition(0f, 0.5f, 0f);

        // 2. Neck & Head
        SceneObject neck = new SceneObject(rootId + "_neck", "Neck", "CREATURE",
                PrimitiveGenerator.createCylinder(0.06f, 0.18f, 8), skinMat);
        neck.getTransform().setPosition(0f, 0.16f, 0.16f);
        neck.getTransform().setRotation(20.0f, 0f, 0f); // Curved neck slant
        spine.addChild(neck);

        SceneObject head = new SceneObject(rootId + "_head", "Head", "CREATURE",
                PrimitiveGenerator.createSphere(0.1f, 10, 10), skinMat);
        head.getTransform().setPosition(0f, 0.16f, 0.08f);
        neck.addChild(head);

        // Beak (Procedural cone mounted relative to head skull)
        SceneObject beak = new SceneObject(rootId + "_beak", "Beak", "CREATURE",
                PrimitiveGenerator.createCylinder(0.03f, 0.12f, 4), beakMat);
        beak.getTransform().setPosition(0f, 0f, 0.1f);
        beak.getTransform().setRotation(90.0f, 0f, 0f); // Forward facing beak
        head.addChild(beak);

        // 3. Wings (Left & Right Wing Arm -> Wing Tip)
        float wingLen = 0.45f;

        // Left Wing Assembly
        SceneObject lWingArm = new SceneObject(rootId + "_l_wing_arm", "Left Wing Arm", "CREATURE",
                PrimitiveGenerator.createCube(wingLen, 0.04f, 0.25f), skinMat);
        lWingArm.getTransform().setPosition(-0.25f, 0.08f, 0f);
        lWingArm.getTransform().setRotation(0f, 0f, 20.0f); // Angled upward
        spine.addChild(lWingArm);

        SceneObject lWingTip = new SceneObject(rootId + "_l_wing_tip", "Left Wing Tip", "CREATURE",
                PrimitiveGenerator.createCube(wingLen, 0.02f, 0.18f), skinMat);
        lWingTip.getTransform().setPosition(-wingLen, 0f, 0f);
        lWingArm.addChild(lWingTip);

        // Right Wing Assembly
        SceneObject rWingArm = new SceneObject(rootId + "_r_wing_arm", "Right Wing Arm", "CREATURE",
                PrimitiveGenerator.createCube(wingLen, 0.04f, 0.25f), skinMat);
        rWingArm.getTransform().setPosition(0.25f, 0.08f, 0f);
        rWingArm.getTransform().setRotation(0f, 0f, -20.0f);
        spine.addChild(rWingArm);

        SceneObject rWingTip = new SceneObject(rootId + "_r_wing_tip", "Right Wing Tip", "CREATURE",
                PrimitiveGenerator.createCube(wingLen, 0.02f, 0.18f), skinMat);
        rWingTip.getTransform().setPosition(wingLen, 0f, 0f);
        rWingArm.addChild(rWingTip);

        // 4. Supporting Legs
        SceneObject lLeg = new SceneObject(rootId + "_l_leg", "Left Leg", "CREATURE",
                PrimitiveGenerator.createCylinder(0.03f, 0.22f, 8), beakMat);
        lLeg.getTransform().setPosition(-0.1f, -0.22f, 0f);
        spine.addChild(lLeg);

        SceneObject rLeg = new SceneObject(rootId + "_r_leg", "Right Leg", "CREATURE",
                PrimitiveGenerator.createCylinder(0.03f, 0.22f, 8), beakMat);
        rLeg.getTransform().setPosition(0.1f, -0.22f, 0f);
        spine.addChild(rLeg);

        // Feather Tail
        SceneObject tail = new SceneObject(rootId + "_tail", "Feather Tail", "CREATURE",
                PrimitiveGenerator.createCube(0.2f, 0.02f, 0.3f), skinMat);
        tail.getTransform().setPosition(0f, -0.05f, -0.28f);
        tail.getTransform().setRotation(-10.0f, 0f, 0f);
        spine.addChild(tail);

        return spine;
    }
}