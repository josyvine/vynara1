package com.example.engine;

public class ThreeDEngine {
    private final SceneManager sceneManager;
    private final MaterialManager materialManager;
    private final LightManager lightManager;
    private final CameraManager cameraManager;

    public ThreeDEngine() {
        this.sceneManager = new SceneManager();
        this.materialManager = new MaterialManager();
        this.lightManager = new LightManager();
        this.cameraManager = new CameraManager();
    }

    public SceneManager getSceneManager() { return sceneManager; }
    public MaterialManager getMaterialManager() { return materialManager; }
    public LightManager getLightManager() { return lightManager; }
    public CameraManager getCameraManager() { return cameraManager; }

    public SceneObject createPrimitive(String type, float width, float height, float depth) {
        Mesh mesh;
        if ("sphere".equalsIgnoreCase(type)) {
            mesh = PrimitiveGenerator.createSphere(width > 0 ? width : 1.0f, 16, 16);
        } else if ("cylinder".equalsIgnoreCase(type)) {
            mesh = PrimitiveGenerator.createCylinder(width > 0 ? width : 1.0f, height > 0 ? height : 2.0f, 16);
        } else if ("plane".equalsIgnoreCase(type)) {
            mesh = PrimitiveGenerator.createPlane(width > 0 ? width : 4.0f, depth > 0 ? depth : 4.0f);
        } else {
            mesh = PrimitiveGenerator.createCube(width > 0 ? width : 1.5f, height > 0 ? height : 1.5f, depth > 0 ? depth : 1.5f);
        }

        Material mat = materialManager.getMaterial("mat_default");
        String id = "obj_" + type + "_" + System.currentTimeMillis();
        SceneObject obj = new SceneObject(id, type.toUpperCase(), "PRIMITIVE", mesh, mat);
        sceneManager.getActiveScene().addObject(obj);
        return obj;
    }

    public SceneObject createProceduralStructure(String structureType, String name) {
        String objName = name != null && !name.isEmpty() ? name : structureType.toUpperCase();
        String id = "struct_" + structureType + "_" + System.currentTimeMillis();

        Mesh mesh;
        Material mat;

        if ("sofa".equalsIgnoreCase(structureType)) {
            mesh = PrimitiveGenerator.createCube(2.4f, 0.9f, 1.0f);
            mat = materialManager.getMaterial("mat_leather_brown");
        } else if ("house".equalsIgnoreCase(structureType) || "villa".equalsIgnoreCase(structureType)) {
            mesh = PrimitiveGenerator.createCube(6.0f, 4.0f, 5.0f);
            mat = materialManager.getMaterial("mat_concrete");
        } else if ("pool".equalsIgnoreCase(structureType)) {
            mesh = PrimitiveGenerator.createCube(4.0f, 0.2f, 8.0f);
            mat = materialManager.getMaterial("mat_pool_water");
        } else if ("table".equalsIgnoreCase(structureType)) {
            mesh = PrimitiveGenerator.createCube(1.8f, 0.8f, 1.0f);
            mat = materialManager.getMaterial("mat_wood_walnut");
        } else if ("tree".equalsIgnoreCase(structureType)) {
            mesh = PrimitiveGenerator.createCube(1.2f, 3.5f, 1.2f);
            mat = materialManager.getMaterial("mat_foliage");
        } else {
            mesh = PrimitiveGenerator.createCube(2.0f, 2.0f, 2.0f);
            mat = materialManager.getMaterial("mat_default");
        }

        SceneObject obj = new SceneObject(id, objName, "STRUCTURE", mesh, mat);
        sceneManager.getActiveScene().addObject(obj);
        return obj;
    }
}
