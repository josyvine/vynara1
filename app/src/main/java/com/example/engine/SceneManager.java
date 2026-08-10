package com.example.engine;

import java.util.ArrayList;
import java.util.List;

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

    public Scene getActiveScene() { 
        return activeScene; 
    }

    public void selectObject(SceneObject obj) {
        if (selectedObject != null) {
            selectedObject.setSelected(false);
        }
        selectedObject = obj;
        if (selectedObject != null) {
            selectedObject.setSelected(true);
        }
    }

    public SceneObject getSelectedObject() { 
        return selectedObject; 
    }

    public void clearSelection() {
        if (selectedObject != null) {
            selectedObject.setSelected(false);
            selectedObject = null;
        }
    }

    /**
     * Phase 15 & 16 Alignment: Deletes the currently selected scene object
     * and removes it from the active scene hierarchy.
     */
    public boolean deleteSelectedObject() {
        if (selectedObject == null || activeScene == null) return false;

        String idToRemove = selectedObject.getId();
        activeScene.removeObject(idToRemove);
        selectedObject = null;
        return true;
    }

    /**
     * Phase 15 & 16 Alignment: Duplicates the target scene object,
     * assigning new unique IDs and adding the copy to the active scene graph.
     */
    public SceneObject duplicateObject(SceneObject target) {
        if (target == null || activeScene == null) return null;

        String newId = target.getId() + "_copy_" + System.currentTimeMillis();
        String newName = target.getName() + " (Copy)";

        SceneObject copy = new SceneObject(newId, newName, target.getSemanticType(), target.getMesh(), target.getMaterial());
        
        // Copy transform values
        if (target.getTransform() != null) {
            copy.getTransform().setPosition(
                    target.getTransform().getPx() + 0.5f, // Slightly offset duplicate
                    target.getTransform().getPy(),
                    target.getTransform().getPz() + 0.5f
            );
            copy.getTransform().setRotation(
                    target.getTransform().getRx(),
                    target.getTransform().getRy(),
                    target.getTransform().getRz()
            );
            copy.getTransform().setScale(
                    target.getTransform().getSx(),
                    target.getTransform().getSy(),
                    target.getTransform().getSz()
            );
        }

        activeScene.addObject(copy);
        selectObject(copy);
        return copy;
    }

    /**
     * Binds a child SceneObject to a new parent SceneObject in the scene graph.
     */
    public boolean parentObject(SceneObject child, SceneObject parent) {
        if (child == null || parent == null || child.equals(parent)) return false;

        // Unparent from previous parent if exists
        if (child.getParent() != null) {
            child.getParent().getChildren().remove(child);
        } else {
            activeScene.getObjects().remove(child);
        }

        parent.addChild(child);
        return true;
    }

    /**
     * Unparents a child object and moves it back to the root scene graph array.
     */
    public boolean unparentObject(SceneObject child) {
        if (child == null || child.getParent() == null) return false;

        SceneObject parent = child.getParent();
        parent.getChildren().remove(child);
        
        activeScene.addObject(child);
        return true;
    }

    public SceneObject findObjectById(String id) {
        if (activeScene == null || id == null) return null;
        return activeScene.findObjectById(id);
    }

    public List<SceneObject> getAllObjects() {
        if (activeScene == null) return new ArrayList<>();
        return activeScene.getObjects();
    }

    public void clearScene() {
        if (activeScene != null) {
            activeScene.getObjects().clear();
        }
        selectedObject = null;
    }
}