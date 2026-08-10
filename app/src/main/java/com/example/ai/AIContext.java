package com.example.ai;

import com.example.engine.Scene;
import com.example.engine.SceneObject;

import org.json.JSONArray;
import org.json.JSONObject;

public class AIContext {

    public static String buildSceneContextJson(Scene scene) {
        if (scene == null) return "{}";

        try {
            JSONObject root = new JSONObject();
            root.put("sceneId", scene.getId());
            root.put("sceneName", scene.getName());
            root.put("totalTriangles", scene.getTotalTriangleCount());

            JSONArray objectsArr = new JSONArray();
            for (SceneObject obj : scene.getObjects()) {
                JSONObject objJson = new JSONObject();
                objJson.put("id", obj.getId());
                objJson.put("name", obj.getName());
                objJson.put("type", obj.getSemanticType());
                objJson.put("visible", obj.isVisible());
                objJson.put("selected", obj.isSelected());

                if (obj.getTransform() != null) {
                    JSONObject tJson = new JSONObject();
                    tJson.put("px", obj.getTransform().getPx());
                    tJson.put("py", obj.getTransform().getPy());
                    tJson.put("pz", obj.getTransform().getPz());
                    objJson.put("position", tJson);
                }

                objectsArr.put(objJson);
            }
            root.put("objects", objectsArr);
            return root.toString();
        } catch (Exception e) {
            return "{}";
        }
    }
}
