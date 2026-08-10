package com.example.ai;

import com.example.ai.protocol.AIToolCall;
import com.example.ai.protocol.AIProductionPlan;
import com.example.ai.protocol.AIProductionRequest;
import com.example.knowledge.KnowledgeEntry;
import com.example.knowledge.KnowledgeManager;
import com.example.tasks.ProductionPlan;
import com.example.tasks.TaskGraph;
import com.example.tasks.TaskNode;
import com.example.tools.ToolOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PromptInterpreter {
    private final KnowledgeManager knowledgeManager;

    public PromptInterpreter(KnowledgeManager knowledgeManager) {
        this.knowledgeManager = knowledgeManager;
    }

    public ProductionPlan createProductionPlan(String userPrompt, String style, String targetEngine) {
        return createProductionPlan(userPrompt, style, targetEngine, new ArrayList<>());
    }

    public ProductionPlan createProductionPlan(String userPrompt, String style, String targetEngine, List<String> referenceImageUris) {
        KnowledgeEntry knowledge = knowledgeManager.retrieveKnowledgeForPrompt(userPrompt);
        String category = knowledge != null ? knowledge.getCategory() : "PHYSICAL_OBJECT";

        String projectName = extractProjectName(userPrompt, category);
        ProductionPlan plan = new ProductionPlan(projectName, userPrompt, category, knowledge, referenceImageUris);
        TaskGraph graph = plan.getTaskGraph();

        // Prepend reference image processing step if reference images are present
        TaskNode t0 = null;
        if (plan.hasReferenceImages()) {
            t0 = new TaskNode("t0", "Processing Reference Images", "Analyzing " + plan.getReferenceImageUris().size() + " visual reference image(s)", null);
            t0.setStatus(TaskNode.Status.COMPLETED);
            graph.addTask(t0);
        }

        if ("CHARACTER".equalsIgnoreCase(category)) {
            // Humanoid DAG
            TaskNode t1 = new TaskNode("t1", "Understanding Humanoid Request", "Analyzing anatomical requirements", null);
            t1.setStatus(TaskNode.Status.COMPLETED);
            if (t0 != null) t1.addDependency("t0");

            TaskNode t2 = new TaskNode("t2", "Generating Humanoid Body Mesh", "Tool: character.create_humanoid",
                    new ToolOperation("character.create_humanoid").setParam("name", projectName).setParam("style", style));
            t2.addDependency("t1");

            TaskNode t3 = new TaskNode("t3", "Binding Skeleton & Skin Weights", "Tool: skeleton.bind",
                    new ToolOperation("skeleton.bind"));
            t3.addDependency("t2");

            TaskNode t4 = new TaskNode("t4", "Configuring IK Limb Controllers", "Tool: rig.create_ik",
                    new ToolOperation("rig.create_ik").setParam("limb", "left_arm"));
            t4.addDependency("t3");

            TaskNode t5 = new TaskNode("t5", "Applying Motion Pose & Animation", "Tool: animation.create_clip",
                    new ToolOperation("animation.create_clip").setParam("clipName", userPrompt.toLowerCase().contains("run") ? "run" : "walk"));
            t5.addDependency("t4");

            TaskNode t6 = new TaskNode("t6", "Validating 3D Mesh & Rig Integrity", "Tool: validation.check_mesh",
                    new ToolOperation("validation.check_mesh"));
            t6.addDependency("t5");

            graph.addTask(t1); graph.addTask(t2); graph.addTask(t3); graph.addTask(t4); graph.addTask(t5); graph.addTask(t6);

        } else if ("ANIMAL".equalsIgnoreCase(category)) {
            // Creature DAG
            TaskNode t1 = new TaskNode("t1", "Understanding Creature Request", "Analyzing creature anatomical structure", null);
            t1.setStatus(TaskNode.Status.COMPLETED);
            if (t0 != null) t1.addDependency("t0");

            TaskNode t2 = new TaskNode("t2", "Generating Creature Mesh & Skeleton", "Tool: character.create_creature",
                    new ToolOperation("character.create_creature").setParam("species", userPrompt.toLowerCase().contains("bird") ? "bird" : "dog").setParam("name", projectName));
            t2.addDependency("t1");

            TaskNode t3 = new TaskNode("t3", "Applying Locomotion Animation", "Tool: animation.create_clip",
                    new ToolOperation("animation.create_clip").setParam("clipName", "walk"));
            t3.addDependency("t2");

            TaskNode t4 = new TaskNode("t4", "Validating Creature Topology", "Tool: validation.check_mesh",
                    new ToolOperation("validation.check_mesh"));
            t4.addDependency("t3");

            graph.addTask(t1); graph.addTask(t2); graph.addTask(t3); graph.addTask(t4);

        } else if ("ARCHITECTURE".equalsIgnoreCase(category)) {
            // House / Villa DAG
            TaskNode t1 = new TaskNode("t1", "Understanding Architectural Request", "Extracting room, pool, and deck specifications", null);
            t1.setStatus(TaskNode.Status.COMPLETED);
            if (t0 != null) t1.addDependency("t0");

            TaskNode t2 = new TaskNode("t2", "Building Villa Structure & Walls", "Tool: geometry.create_procedural",
                    new ToolOperation("geometry.create_procedural").setParam("type", "villa").setParam("name", projectName));
            t2.addDependency("t1");

            TaskNode t3 = new TaskNode("t3", "Constructing Swimming Pool & Deck", "Tool: geometry.create_procedural",
                    new ToolOperation("geometry.create_procedural").setParam("type", "pool").setParam("name", "Pool & Deck"));
            t3.addDependency("t2");

            TaskNode t4 = new TaskNode("t4", "Setting Up Exterior & Interior Lighting", "Tool: scene.add_light",
                    new ToolOperation("scene.add_light").setParam("type", "directional").setParam("intensity", 1.2f));
            t4.addDependency("t3");

            TaskNode t5 = new TaskNode("t5", "Validating Scene Architecture", "Tool: validation.check_mesh",
                    new ToolOperation("validation.check_mesh"));
            t5.addDependency("t4");

            graph.addTask(t1); graph.addTask(t2); graph.addTask(t3); graph.addTask(t4); graph.addTask(t5);

        } else {
            // General Object / Furniture DAG
            TaskNode t1 = new TaskNode("t1", "Analyzing Request", "Extracting 3D geometry requirements", null);
            t1.setStatus(TaskNode.Status.COMPLETED);
            if (t0 != null) t1.addDependency("t0");

            String structType = "sofa";
            if (userPrompt.toLowerCase().contains("table")) structType = "table";
            else if (userPrompt.toLowerCase().contains("tree")) structType = "tree";

            TaskNode t2 = new TaskNode("t2", "Creating 3D Procedural Model", "Tool: geometry.create_procedural",
                    new ToolOperation("geometry.create_procedural").setParam("type", structType).setParam("name", projectName));
            t2.addDependency("t1");

            TaskNode t3 = new TaskNode("t3", "Configuring Material PBR Shading", "Tool: material.set_properties",
                    new ToolOperation("material.set_properties").setParam("colorHex", "#6E3B1F").setParam("metallic", 0.2f).setParam("roughness", 0.4f));
            t3.addDependency("t2");

            TaskNode t4 = new TaskNode("t4", "Validating Mesh & Bounds", "Tool: validation.check_mesh",
                    new ToolOperation("validation.check_mesh"));
            t4.addDependency("t3");

            graph.addTask(t1); graph.addTask(t2); graph.addTask(t3); graph.addTask(t4);
        }

        return plan;
    }

    /**
     * Phase 2 Alignment: Converts structured Gemini AI JSON specifications (AIProductionPlan)
     * into a deterministic local execution plan with mapped task dependencies.
     */
    public ProductionPlan convertStructuredPlanToExecutablePlan(AIProductionRequest request, AIProductionPlan structuredPlan) {
        if (structuredPlan == null || request == null) {
            return createProductionPlan(request != null ? request.getUserPrompt() : "", "Photorealistic", "OpenGL ES / GLTF");
        }

        KnowledgeEntry knowledge = knowledgeManager.retrieveKnowledgeForPrompt(request.getUserPrompt());
        String projectName = extractProjectName(request.getUserPrompt(), structuredPlan.getIntent());
        ProductionPlan plan = new ProductionPlan(projectName, request.getUserPrompt(), structuredPlan.getIntent(), knowledge, request.getReferenceImageUris());
        TaskGraph graph = plan.getTaskGraph();

        List<AIToolCall> toolCalls = structuredPlan.getToolCalls();
        if (toolCalls == null || toolCalls.isEmpty()) {
            return createProductionPlan(request.getUserPrompt(), request.getStyle(), request.getTargetEngine(), request.getReferenceImageUris());
        }

        String previousTaskId = null;
        for (int i = 0; i < toolCalls.size(); i++) {
            AIToolCall call = toolCalls.get(i);
            String taskId = "task_ai_" + (i + 1);
            ToolOperation op = new ToolOperation(call.getToolId());
            if (call.getParameters() != null) {
                for (Map.Entry<String, Object> entry : call.getParameters().entrySet()) {
                    op.setParam(entry.getKey(), entry.getValue());
                }
            }

            TaskNode node = new TaskNode(taskId, call.getDescription() != null ? call.getDescription() : call.getToolId(), "AI Executing: " + call.getToolId(), op);
            
            if (previousTaskId != null) {
                node.addDependency(previousTaskId);
            }
            
            graph.addTask(node);
            previousTaskId = taskId;
        }

        return plan;
    }

    private String extractProjectName(String prompt, String category) {
        if (prompt == null || prompt.trim().isEmpty()) {
            return "3D Project";
        }
        String p = prompt.trim();
        if (p.length() > 28) {
            return p.substring(0, 25) + "...";
        }
        return p;
    }
}