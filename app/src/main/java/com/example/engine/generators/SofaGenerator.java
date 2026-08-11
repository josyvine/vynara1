package com.example.engine.generators;

import com.example.engine.Material;
import com.example.engine.MaterialManager;
import com.example.engine.PrimitiveGenerator;
import com.example.engine.SceneObject;

public class SofaGenerator {

    /**
     * Phase 4 & 23 Alignment: Generates a procedural multi-component sofa asset.
     * Assembles frame base, backrest, left/right armrests, seat cushions, and corner legs 
     * under a root node hierarchy with distinct PBR materials instead of returning a single cube.
     */
    public static SceneObject generateSofa(String rootId, String rootName, MaterialManager matMgr) {
        Material frameMat = matMgr.getMaterial("mat_wood_walnut");
        Material cushionMat = matMgr.getMaterial("mat_leather_brown");
        Material legMat = matMgr.getMaterial("mat_metallic_steel");

        // 1. Root Sofa Container Node
        SceneObject sofaRoot = new SceneObject(rootId, rootName, "SOFA", null, null);

        // 2. Base Wooden Frame (Long, wide, low height)
        SceneObject baseFrame = new SceneObject(rootId + "_base", "Sofa Frame Base", "STRUCTURE",
                PrimitiveGenerator.createCube(2.4f, 0.2f, 0.9f), frameMat);
        baseFrame.getTransform().setPosition(0f, 0.2f, 0f);
        sofaRoot.addChild(baseFrame);

        // 3. Backrest Cushion (Long, thin, high height)
        SceneObject backrest = new SceneObject(rootId + "_backrest", "Sofa Backrest", "STRUCTURE",
                PrimitiveGenerator.createCube(2.4f, 0.6f, 0.15f), cushionMat);
        backrest.getTransform().setPosition(0f, 0.6f, -0.375f);
        sofaRoot.addChild(backrest);

        // 4. Left Armrest
        SceneObject leftArm = new SceneObject(rootId + "_l_arm", "Sofa Left Armrest", "STRUCTURE",
                PrimitiveGenerator.createCube(0.15f, 0.5f, 0.9f), cushionMat);
        leftArm.getTransform().setPosition(-1.125f, 0.45f, 0f);
        sofaRoot.addChild(leftArm);

        // 5. Right Armrest
        SceneObject rightArm = new SceneObject(rootId + "_r_arm", "Sofa Right Armrest", "STRUCTURE",
                PrimitiveGenerator.createCube(0.15f, 0.5f, 0.9f), cushionMat);
        rightArm.getTransform().setPosition(1.125f, 0.45f, 0f);
        sofaRoot.addChild(rightArm);

        // 6. Cushions (3 separate seat cushions)
        float cushionWidth = 0.7f;
        for (int i = 0; i < 3; i++) {
            SceneObject cushion = new SceneObject(rootId + "_cushion_" + i, "Seat Cushion " + (i + 1), "STRUCTURE",
                    PrimitiveGenerator.createCube(cushionWidth, 0.15f, 0.75f), cushionMat);
            float xOffset = (i - 1) * (cushionWidth + 0.02f);
            cushion.getTransform().setPosition(xOffset, 0.3f, 0.05f);
            sofaRoot.addChild(cushion);
        }

        // 7. Leg Support Pegs (4 metallic cylinders at corners)
        float lx = 1.1f, lz = 0.35f;
        float[][] legPositions = new float[][] {
                { -lx, 0.1f,  lz },
                {  lx, 0.1f,  lz },
                { -lx, 0.1f, -lz },
                {  lx, 0.1f, -lz }
        };

        for (int i = 0; i < 4; i++) {
            SceneObject leg = new SceneObject(rootId + "_leg_" + i, "Sofa Leg " + (i + 1), "STRUCTURE",
                    PrimitiveGenerator.createCylinder(0.05f, 0.2f, 8), legMat);
            leg.getTransform().setPosition(legPositions[i][0], legPositions[i][1], legPositions[i][2]);
            sofaRoot.addChild(leg);
        }

        return sofaRoot;
    }
}