package com.example.ai;

import com.example.engine.Scene;
import com.example.validation.ValidationManager;
import com.example.validation.ValidationResult;

import java.util.List;

public class AIInspector {
    private final ValidationManager validationManager;

    public AIInspector(ValidationManager validationManager) {
        this.validationManager = validationManager;
    }

    public List<ValidationResult> inspect(Scene scene) {
        return validationManager.validateScene(scene);
    }
}
