package com.example.ai;

import com.example.tools.ToolExecutor;
import com.example.validation.ValidationResult;

import java.util.List;

public class AICorrector {
    private final ToolExecutor toolExecutor;

    public AICorrector(ToolExecutor toolExecutor) {
        this.toolExecutor = toolExecutor;
    }

    public boolean applyCorrections(List<ValidationResult> inspectionResults) {
        if (inspectionResults == null) return true;

        boolean allFixed = true;
        for (ValidationResult vr : inspectionResults) {
            if (vr.getSeverity() == ValidationResult.Severity.ERROR || vr.getSeverity() == ValidationResult.Severity.CRITICAL) {
                // Execute repair tool operation
                if (vr.getMessage().contains("mesh")) {
                    toolExecutor.executeOperation(new com.example.tools.ToolOperation("geometry.create_primitive"));
                } else if (vr.getMessage().contains("material")) {
                    toolExecutor.executeOperation(new com.example.tools.ToolOperation("material.set_properties"));
                }
            }
        }
        return allFixed;
    }
}
