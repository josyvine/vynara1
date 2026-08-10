package com.example.engine;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    private String id;
    private String name;
    private final List<SceneObject> objects = new ArrayList<>();

    public Scene(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public String getName() { return name; }

    public void addObject(SceneObject obj) {
        if (obj != null) {
            objects.add(obj);
        }
    }

    public void removeObject(String objectId) {
        objects.removeIf(o -> o.getId().equals(objectId));
    }

    public SceneObject findObjectById(String objectId) {
        if (objectId == null) return null;
        for (SceneObject obj : objects) {
            if (obj.getId().equals(objectId)) return obj;
            SceneObject found = findChildRecursively(obj, objectId);
            if (found != null) return found;
        }
        return null;
    }

    private SceneObject findChildRecursively(SceneObject parent, String objectId) {
        for (SceneObject child : parent.getChildren()) {
            if (child.getId().equals(objectId)) return child;
            SceneObject found = findChildRecursively(child, objectId);
            if (found != null) return found;
        }
        return null;
    }

    public List<SceneObject> getObjects() { return objects; }

    public int getTotalTriangleCount() {
        int count = 0;
        for (SceneObject obj : objects) {
            if (obj.getMesh() != null) {
                count += obj.getMesh().getTriangleCount();
            }
        }
        return count;
    }
}
