package com.example.engine.generators;

import com.example.engine.Material;
import com.example.engine.MaterialManager;
import com.example.engine.PrimitiveGenerator;
import com.example.engine.SceneObject;

public class VillaGenerator {

    /**
     * Phase 4 & 23 Alignment: Generates a procedural multi-component Modern Seaside Villa.
     * Integrates a large wood deck base, a concrete villa house residence, a swimming pool with 
     * transparent PBR water shading, outdoor lounge furniture, and surrounding scattered 
     * palm trees under a unified scene graph parent-child hierarchy.
     */
    public static SceneObject generateVilla(String rootId, String rootName, MaterialManager matMgr) {
        Material deckMat = matMgr.getMaterial("mat_wood_walnut");

        // 1. Root Villa Container Node
        SceneObject villaRoot = new SceneObject(rootId, rootName, "VILLA", null, null);

        // 2. Large Wooden Deck Base (Long, wide, low height)
        SceneObject deckBase = new SceneObject(rootId + "_deck", "Villa Wooden Deck", "STRUCTURE",
                PrimitiveGenerator.createCube(16.0f, 0.1f, 16.0f), deckMat);
        deckBase.getTransform().setPosition(0f, 0.05f, 0f);
        villaRoot.addChild(deckBase);

        // 3. Main Villa House Structure (Procedural Concrete House on Left)
        SceneObject villaHouse = HouseGenerator.generateHouse(rootId + "_house", "Villa Residence Structure", matMgr);
        villaHouse.getTransform().setPosition(-4.0f, 0.1f, -3.0f);
        villaRoot.addChild(villaHouse);

        // 4. Swimming Pool Structure (Procedural Swimming Pool on Right)
        SceneObject pool = PoolGenerator.generatePool(rootId + "_pool", "Seaside Pool & Deck", matMgr);
        pool.getTransform().setPosition(4.0f, 0.1f, 2.0f);
        villaRoot.addChild(pool);

        // 5. Exterior Lounge Furniture (Sofa & Coffee Table placed on the deck)
        SceneObject loungeSofa = SofaGenerator.generateSofa(rootId + "_sofa", "Deck Lounge Sofa", matMgr);
        loungeSofa.getTransform().setPosition(-1.0f, 0.1f, 4.0f);
        loungeSofa.getTransform().setRotation(0f, 45f, 0f); // Angled placement for realistic design
        villaRoot.addChild(loungeSofa);

        SceneObject coffeeTable = TableGenerator.generateTable(rootId + "_table", "Lounge Coffee Table", matMgr);
        coffeeTable.getTransform().setPosition(0.8f, 0.1f, 5.0f);
        villaRoot.addChild(coffeeTable);

        // 6. Surrounding Palm Trees (4 procedural trees scattered around deck corners)
        float[][] treePositions = new float[][] {
                { -7.0f, 0.1f,  7.0f },
                { -7.0f, 0.1f, -7.0f },
                {  7.0f, 0.1f, -7.0f },
                {  7.0f, 0.1f, -3.0f }
        };

        for (int i = 0; i < 4; i++) {
            SceneObject palmTree = TreeGenerator.generateTree(rootId + "_palm_" + i, "Palm Tree " + (i + 1), matMgr);
            palmTree.getTransform().setPosition(treePositions[i][0], treePositions[i][1], treePositions[i][2]);
            
            // Random subtle scale variations (0.9 to 1.14) to avoid artificial uniformity
            float scaleVar = 0.9f + (i * 0.08f);
            palmTree.getTransform().setScale(scaleVar, scaleVar, scaleVar);
            
            villaRoot.addChild(palmTree);
        }

        return villaRoot;
    }
}