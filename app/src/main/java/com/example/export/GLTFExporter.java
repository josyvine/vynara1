package com.example.export;

import com.example.engine.Mesh;
import com.example.engine.Scene;
import com.example.engine.SceneObject;

import org.json.JSONArray;
import org.json.JSONObject;

public class GLTFExporter {

    public static String exportSceneToGLTFJson(Scene scene) {
        if (scene == null) return "{}";

        try {
            JSONObject root = new JSONObject();

            JSONObject asset = new JSONObject();
            asset.put("version", "2.0");
            asset.put("generator", "Vynara AI 3D Creation Engine");
            root.put("asset", asset);

            JSONArray sceneArray = new JSONArray();
            JSONObject mainScene = new JSONObject();
            mainScene.put("name", scene.getName());

            JSONArray nodeIndices = new JSONArray();
            for (int i = 0; i < scene.getObjects().size(); i++) {
                nodeIndices.put(i);
            }
            mainScene.put("nodes", nodeIndices);
            sceneArray.put(mainScene);
            root.put("scenes", sceneArray);
            root.put("scene", 0);

            JSONArray nodes = new JSONArray();
            JSONArray meshes = new JSONArray();

            for (int i = 0; i < scene.getObjects().size(); i++) {
                SceneObject obj = scene.getObjects().get(i);
                JSONObject node = new JSONObject();
                node.put("name", obj.getName());
                node.put("mesh", i);

                JSONArray translation = new JSONArray();
                translation.put(obj.getTransform().getPx());
                translation.put(obj.getTransform().getPy());
                translation.put(obj.getTransform().getPz());
                node.put("translation", translation);

                nodes.put(node);

                // Mesh
                JSONObject meshObj = new JSONObject();
                meshObj.put("name", obj.getName() + "_Mesh");
                JSONArray primitives = new JSONArray();
                JSONObject prim = new JSONObject();
                prim.put("mode", 4); // TRIANGLES
                primitives.put(prim);
                meshObj.put("primitives", primitives);
                meshes.put(meshObj);
            }

            root.put("nodes", nodes);
            root.put("meshes", meshes);

            return root.toString(2);
        } catch (Exception e) {
            return "{\"error\":\"Export failed: " + e.getMessage() + "\"}";
        }
    }
}
