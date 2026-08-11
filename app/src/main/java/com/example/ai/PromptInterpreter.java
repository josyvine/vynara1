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

    /**
     * Dynamic offline fallback generator.
     * Uses KnowledgeManager multi-concept extraction to generate tasks for ALL detected entities
     * in the prompt (e.g., house + pool + sofa + tree) instead of relying on hardcoded single-category branching.
     */
    public ProductionPlan createProductionPlan(String userPrompt, String style, String targetEngine, List<String> referenceImageUris) {
        List<KnowledgeEntry> matchedKnowledge = knowledgeManager.retrieveAllKnowledgeForPrompt(userPrompt);
        KnowledgeEntry primaryKnowledge = matchedKnowledge.get(0);

        String projectName = extractProjectName(userPrompt, primaryKnowledge.getCategory());
        ProductionPlan plan = new ProductionPlan(projectName, userPrompt, primaryKnowledge.getCategory(), primaryKnowledge, referenceImageUris);
        TaskGraph graph = plan.getTaskGraph();

        int taskCounter = 1;
        String previousTaskId = null;

        // Step 0: Reference Image Analysis Task if images are attached
        if (plan.hasReferenceImages()) {
            String t0Id = "task_" + taskCounter++;
            TaskNode t0 = new TaskNode(t0Id, "Processing Reference Images", "Analyzing " + plan.getReferenceImageUris().size() + " visual reference image(s)", null);
            t0.setStatus(TaskNode.Status.COMPLETED);
            graph.addTask(t0);
            previousTaskId = t0Id;
        }

        // Generate procedural creation steps for EVERY concept detected in the prompt
        for (KnowledgeEntry entry : matchedKnowledge) {
            String cat = entry.getCategory();
            String conceptId = entry.getId();

            if ("CHARACTER".equalsIgnoreCase(cat)) {
                String tMeshId = "task_" + taskCounter++;
                TaskNode tMesh = new TaskNode(tMeshId, "Generating " + entry.getName() + " Mesh", "Tool: character.create_humanoid",
                        new ToolOperation("character.create_humanoid").setParam("name", entry.getName()).setParam("style", style).setParam("height", 1.8f));
                if (previousTaskId != null) tMesh.addDependency(previousTaskId);
                graph.addTask(tMesh);

                String tBindId = "task_" + taskCounter++;
                TaskNode tBind = new TaskNode(tBindId, "Binding Skeleton & Skin Weights", "Tool: skeleton.bind",
                        new ToolOperation("skeleton.bind"));
                tBind.addDependency(tMeshId);
                graph.addTask(tBind);

                String tRigId = "task_" + taskCounter++;
                TaskNode tRig = new TaskNode(tRigId, "Configuring IK Limb Controllers", "Tool: rig.create_ik",
                        new ToolOperation("rig.create_ik").setParam("limb", "left_arm"));
                tRig.addDependency(tBindId);
                graph.addTask(tRig);

                String tAnimId = "task_" + taskCounter++;
                String clipName = userPrompt.toLowerCase().contains("run") ? "run" : (userPrompt.toLowerCase().contains("jump") ? "jump" : "walk");
                TaskNode tAnim = new TaskNode(tAnimId, "Applying Animation Clip (" + clipName + ")", "Tool: animation.create_clip",
                        new ToolOperation("animation.create_clip").setParam("clipName", clipName));
                tAnim.addDependency(tRigId);
                graph.addTask(tAnim);

                previousTaskId = tAnimId;

            } else if ("ANIMAL".equalsIgnoreCase(cat)) {
                String species = conceptId.contains("bird") ? "bird" : "dog";
                String tCreatureId = "task_" + taskCounter++;
                TaskNode tCreature = new TaskNode(tCreatureId, "Generating " + entry.getName() + " Anatomy", "Tool: character.create_creature",
                        new ToolOperation("character.create_creature").setParam("species", species).setParam("name", entry.getName()));
                if (previousTaskId != null) tCreature.addDependency(previousTaskId);
                graph.addTask(tCreature);

                String tAnimId = "task_" + taskCounter++;
                TaskNode tAnim = new TaskNode(tAnimId, "Applying Locomotion Animation", "Tool: animation.create_clip",
                        new ToolOperation("animation.create_clip").setParam("clipName", "walk"));
                tAnim.addDependency(tCreatureId);
                graph.addTask(tAnim);

                previousTaskId = tAnimId;

            } else {
                // Procedural Architecture, Furniture, Environment, or Vehicle
                String tStructId = "task_" + taskCounter++;
                TaskNode tStruct = new TaskNode(tStructId, "Building " + entry.getName(), "Tool: geometry.create_procedural",
                        new ToolOperation("geometry.create_procedural").setParam("type", conceptId).setParam("name", entry.getName()));
                if (previousTaskId != null) tStruct.addDependency(previousTaskId);
                graph.addTask(tStruct);

                previousTaskId = tStructId;
            }
        }

        // Add Lighting Setup
        String tLightId = "task_" + taskCounter++;
        TaskNode tLight = new TaskNode(tLightId, "Configuring Scene Lighting", "Tool: scene.add_light",
                new ToolOperation("scene.add_light").setParam("type", "directional").setParam("intensity", 1.2f).setParam("colorHex", "#FFF4E0"));
        if (previousTaskId != null) tLight.addDependency(previousTaskId);
        graph.addTask(tLight);

        // Add Validation Check Step (Triggers AI Correction System if errors are found)
        String tValidId = "task_" + taskCounter;
        TaskNode tValid = new TaskNode(tValidId, "Inspecting Mesh & Scene Integrity", "Tool: validation.check_mesh",
                new ToolOperation("validation.check_mesh"));
        tValid.addDependency(tLightId);
        graph.addTask(tValid);

        return plan;
    }

    /**
     * Converts Gemini's structured JSON output into an executable TaskGraph.
     * Executes real tools and dependencies defined dynamically by AI.
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

            String desc = call.getDescription() != null && !call.getDescription().isEmpty()
                    ? call.getDescription()
                    : "Executing tool: " + call.getToolId();

            TaskNode node = new TaskNode(taskId, call.getToolId(), desc, op);
            
            // Connect sequential dependencies unless specific DAG rules apply
            if (previousTaskId != null) {
                node.addDependency(previousTaskId);
            }
            
            graph.addTask(node);
            previousTaskId = taskId;
        }

        // Guarantee a final validation step for the AI Correction Loop
        String finalValidationId = "task_ai_validation";
        TaskNode validationNode = new TaskNode(finalValidationId, "validation.check_mesh",
                "Inspecting Generated Scene Integrity", new ToolOperation("validation.check_mesh"));
        if (previousTaskId != null) {
            validationNode.addDependency(previousTaskId);
        }
        graph.addTask(validationNode);

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