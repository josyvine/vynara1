package com.example.validation;

import com.example.engine.Mesh;
import com.example.engine.Scene;
import com.example.engine.SceneObject;

import java.util.ArrayList;
import java.util.List;

public class ValidationManager {

    public List<ValidationResult> validateScene(Scene scene) {
        List<ValidationResult> results = new ArrayList<>();
        if (scene == null) {
            results.add(new ValidationResult(ValidationResult.Severity.CRITICAL, "Active scene is null.", "Initialize 3D scene."));
            return results;
        }

        if (scene.getObjects().isEmpty()) {
            results.add(new ValidationResult(ValidationResult.Severity.WARNING, "Scene is currently empty.", "Generate geometry using Create workspace."));
            return results;
        }

        for (SceneObject obj : scene.getObjects()) {
            results.addAll(validateObject(obj));
        }

        if (results.isEmpty()) {
            results.add(new ValidationResult(ValidationResult.Severity.PASS, "Scene validation passed cleanly with 0 errors.", null));
        }

        return results;
    }

    public List<ValidationResult> validateObject(SceneObject obj) {
        List<ValidationResult> results = new ArrayList<>();
        if (obj == null) return results;

        Mesh mesh = obj.getMesh();
        if (mesh == null) {
            results.add(new ValidationResult(ValidationResult.Severity.ERROR, "Object " + obj.getName() + " has missing 3D mesh.", "Call geometry generator to rebuild mesh."));
        } else if (mesh.getVertexCount() == 0) {
            results.add(new ValidationResult(ValidationResult.Severity.ERROR, "Object " + obj.getName() + " contains 0 vertices.", "Re-generate primitive or procedural geometry."));
        }

        if (obj.getMaterial() == null) {
            results.add(new ValidationResult(ValidationResult.Severity.WARNING, "Object " + obj.getName() + " has no material assigned.", "Assign default material properties."));
        }

        return results;
    }
}
