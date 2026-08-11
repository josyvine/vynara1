package com.example.engine.generators;

import com.example.engine.Material;
import com.example.engine.MaterialManager;
import com.example.engine.PrimitiveGenerator;
import com.example.engine.SceneObject;

public class ChairGenerator {

    /**
     * Phase 4 & 23 Alignment: Generates a procedural multi-component chair asset.
     * Assembles a comfortable seat cushion, a high backrest, support struts, and four legs
     * under a root node hierarchy with distinct PBR materials instead of returning a single cube.
     */
    public static SceneObject generateChair(String rootId, String rootName, MaterialManager matMgr) {
        Material seatMat = matMgr.getMaterial("mat_fabric_grey");
        Material frameMat = matMgr.getMaterial("mat_wood_walnut");

        // 1. Root Chair Container Node
        SceneObject chairRoot = new SceneObject(rootId, rootName, "CHAIR", null, null);

        // 2. Seat Cushion (Flat horizontal pad)
        SceneObject seat = new SceneObject(rootId + "_seat", "Chair Seat Cushion", "STRUCTURE",
                PrimitiveGenerator.createCube(0.5f, 0.08f, 0.5f), seatMat);
        seat.getTransform().setPosition(0f, 0.48f, 0f);
        chairRoot.addChild(seat);

        // 3. Backrest Cushion (Thin vertical support)
        SceneObject backrest = new SceneObject(rootId + "_back", "Chair Backrest", "STRUCTURE",
                PrimitiveGenerator.createCube(0.5f, 0.5f, 0.06f), seatMat);
        backrest.getTransform().setPosition(0f, 0.75f, -0.22f);
        chairRoot.addChild(backrest);

        // 4. Backrest Support Struts (2 wooden struts linking the base and back)
        SceneObject lStrut = new SceneObject(rootId + "_strut_l", "Backrest Strut Left", "STRUCTURE",
                PrimitiveGenerator.createCube(0.04f, 0.4f, 0.04f), frameMat);
        lStrut.getTransform().setPosition(-0.18f, 0.65f, -0.22f);
        chairRoot.addChild(lStrut);

        SceneObject rStrut = new SceneObject(rootId + "_strut_r", "Backrest Strut Right", "STRUCTURE",
                PrimitiveGenerator.createCube(0.04f, 0.4f, 0.04f), frameMat);
        rStrut.getTransform().setPosition(0.18f, 0.65f, -0.22f);
        chairRoot.addChild(rStrut);

        // 5. Supporting Legs (4 legs reaching down to the ground)
        float lx = 0.21f, lz = 0.21f;
        float[][] legPositions = new float[][] {
                { -lx, 0.22f,  lz },
                {  lx, 0.22f,  lz },
                { -lx, 0.22f, -lz },
                {  lx, 0.22f, -lz }
        };

        for (int i = 0; i < 4; i++) {
            SceneObject leg = new SceneObject(rootId + "_leg_" + i, "Chair Leg " + (i + 1), "STRUCTURE",
                    PrimitiveGenerator.createCylinder(0.03f, 0.44f, 8), frameMat);
            leg.getTransform().setPosition(legPositions[i][0], legPositions[i][1], legPositions[i][2]);
            chairRoot.addChild(leg);
        }

        return chairRoot;
    }
}