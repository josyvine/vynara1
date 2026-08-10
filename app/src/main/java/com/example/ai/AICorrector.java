package com.example.ai;

import com.example.tools.ToolExecutor;
import com.example.tools.ToolOperation;
import com.example.validation.ValidationResult;

import java.util.List;

public class AICorrector {
    private final ToolExecutor toolExecutor;

    public AICorrector(ToolExecutor toolExecutor) {
        this.toolExecutor = toolExecutor;
    }

    /**
     * Phase 11 Alignment: Evaluates validation failure results and executes targeted
     * local repair tool operations. Returns true ONLY if all critical corrections succeed.
     */
    public boolean applyCorrections(List<ValidationResult> inspectionResults) {
        if (inspectionResults == null || inspectionResults.isEmpty()) {
            return true;
        }

        boolean allCorrectionsSuccessful = true;
        
        for (ValidationResult vr : inspectionResults) {
            // Only process actionable errors or critical flaws
            if (vr.getSeverity() == ValidationResult.Severity.ERROR || 
                vr.getSeverity() == ValidationResult.Severity.CRITICAL) {
                
                boolean repairExecuted = executeTargetedRepair(vr);
                if (!repairExecuted) {
                    allCorrectionsSuccessful = false;
                }
            }
        }

        return allCorrectionsSuccessful;
    }

    private boolean executeTargetedRepair(ValidationResult vr) {
        if (vr == null || vr.getMessage() == null || toolExecutor == null) {
            return false;
        }

        String msg = vr.getMessage().toLowerCase();

        // 1. Missing or Degenerate Mesh Repair
        if (msg.contains("mesh") || msg.contains("vertex") || msg.contains("vertices")) {
            ToolOperation repairMeshOp = new ToolOperation("geometry.create_primitive")
                    .setParam("type", "cube")
                    .setParam("width", 1.5f)
                    .setParam("height", 1.5f)
                    .setParam("depth", 1.5f);
            return toolExecutor.executeOperation(repairMeshOp);
        }

        // 2. Missing Material Shading Repair
        if (msg.contains("material") || msg.contains("color") || msg.contains("shader")) {
            ToolOperation repairMatOp = new ToolOperation("material.set_properties")
                    .setParam("colorHex", "#A0A5BD")
                    .setParam("metallic", 0.1f)
                    .setParam("roughness", 0.5f);
            return toolExecutor.executeOperation(repairMatOp);
        }

        // 3. Unbound Skin or Weight Normalization Repair
        if (msg.contains("skin") || msg.contains("weight") || msg.contains("skeleton")) {
            ToolOperation bindOp = new ToolOperation("skeleton.bind");
            return toolExecutor.executeOperation(bindOp);
        }

        // 4. Default Fallback Re-validation Tool
        ToolOperation checkOp = new ToolOperation("validation.check_mesh");
        return toolExecutor.executeOperation(checkOp);
    }

    public ToolExecutor getToolExecutor() {
        return toolExecutor;
    }
}