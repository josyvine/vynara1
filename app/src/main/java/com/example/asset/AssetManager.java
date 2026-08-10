package com.example.asset;

import java.util.ArrayList;
import java.util.List;

public class AssetManager {
    private final List<Asset> assets = new ArrayList<>();

    public AssetManager() {
        populateDefaultAssets();
    }

    private void populateDefaultAssets() {
        assets.add(new Asset("asset_1", "Base Humanoid Mesh", "MESH", "GLTF", "2.4 MB"));
        assets.add(new Asset("asset_2", "Humanoid Biped Rig", "SKELETON", "GLTF", "420 KB"));
        assets.add(new Asset("asset_3", "Quadruped Dog Skeleton", "SKELETON", "GLTF", "380 KB"));
        assets.add(new Asset("asset_4", "Biped Walk Cycle Animation", "ANIMATION", "GLTF", "180 KB"));
        assets.add(new Asset("asset_5", "PBR Dark Walnut Material", "MATERIAL", "PBR", "1.1 MB"));
        assets.add(new Asset("asset_6", "PBR Brown Leather Material", "MATERIAL", "PBR", "1.8 MB"));
        assets.add(new Asset("asset_7", "Clear Pool Water Shading", "MATERIAL", "PBR", "350 KB"));
    }

    public List<Asset> getAssets() { return assets; }

    public void addAsset(Asset a) {
        if (a != null) assets.add(0, a);
    }
}
