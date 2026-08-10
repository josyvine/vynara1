package com.example.engine;

public class SceneManager {
    private Scene activeScene;
    private SceneObject selectedObject;

    public SceneManager() {
        this.activeScene = new Scene("scene_default", "Default 3D Studio Scene");
    }

    public void setActiveScene(Scene scene) {
        if (scene != null) {
            this.activeScene = scene;
            this.selectedObject = null;
        }
    }

    public Scene getActiveScene() { return activeScene; }

    public void selectObject(SceneObject obj) {
        if (selectedObject != null) {
            selectedObject.setSelected(false);
        }
        selectedObject = obj;
        if (selectedObject != null) {
            selectedObject.setSelected(true);
        }
    }

    public SceneObject getSelectedObject() { return selectedObject; }

    public void clearScene() {
        activeScene.getObjects().clear();
        selectedObject = null;
    }
}
