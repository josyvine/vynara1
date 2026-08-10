package com.example.ai;

import com.example.ai.protocol.AIProductionPlan;
import com.example.ai.protocol.AIProductionRequest;
import com.example.knowledge.KnowledgeManager;
import com.example.tasks.ProductionPlan;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AIOrchestrator {
    private final GeminiApiClient apiClient;
    private final ApiKeyManager apiKeyManager;
    private final KnowledgeManager knowledgeManager;
    private final PromptInterpreter promptInterpreter;

    public AIOrchestrator(GeminiApiClient apiClient, ApiKeyManager apiKeyManager, KnowledgeManager knowledgeManager) {
        this.apiClient = apiClient;
        this.apiKeyManager = apiKeyManager;
        this.knowledgeManager = knowledgeManager;
        this.promptInterpreter = new PromptInterpreter(knowledgeManager);
    }

    public ProductionPlan planProduction(String userPrompt, String style, String targetEngine) {
        return planProduction(userPrompt, style, targetEngine, new ArrayList<>());
    }

    public ProductionPlan planProduction(String userPrompt, String style, String targetEngine, List<String> referenceImageUris) {
        return promptInterpreter.createProductionPlan(userPrompt, style, targetEngine, referenceImageUris);
    }

    /**
     * Phase 2 Alignment: Structured Gemini AI Planning Protocol.
     * Queries Gemini API asynchronously for a structured 3D production specification JSON,
     * parsing it into an executable local ProductionPlan and TaskGraph.
     */
    public void planProductionWithGemini(final AIProductionRequest request, final GeminiApiClient.ApiCallback<ProductionPlan> callback) {
        if (request == null || callback == null) return;

        if (!apiKeyManager.hasApiKey()) {
            // Offline / missing API key fallback: Execute local prompt interpreter plan
            ProductionPlan localPlan = promptInterpreter.createProductionPlan(
                    request.getUserPrompt(), request.getStyle(), request.getTargetEngine(), request.getReferenceImageUris());
            callback.onSuccess(localPlan);
            return;
        }

        String systemInstruction = "You are Vynara Autonomous 3D AI Artist. Interpret the user request and return strict structured JSON specifying 3D scene parameters, procedural components, PBR materials, character anatomical specs, lighting, camera, and task execution tools.";
        
        String promptWithContext = "USER PROMPT: " + request.getUserPrompt() +
                "\nSTYLE: " + request.getStyle() +
                "\nTARGET ENGINE: " + request.getTargetEngine() +
                "\nATTACHED REFERENCE IMAGES COUNT: " + request.getReferenceImageUris().size();

        apiClient.generateContent(apiKeyManager.getApiKey(), apiKeyManager.getSelectedModel(), systemInstruction, promptWithContext, new GeminiApiClient.ApiCallback<String>() {
            @Override
            public void onSuccess(String jsonResult) {
                try {
                    AIProductionPlan structuredPlan = AIProductionPlan.fromJson(new JSONObject(jsonResult));
                    ProductionPlan executablePlan = promptInterpreter.convertStructuredPlanToExecutablePlan(request, structuredPlan);
                    callback.onSuccess(executablePlan);
                } catch (Exception e) {
                    // Fallback to local deterministic interpreter if AI JSON parsing encounters anomalies
                    ProductionPlan fallbackPlan = promptInterpreter.createProductionPlan(
                            request.getUserPrompt(), request.getStyle(), request.getTargetEngine(), request.getReferenceImageUris());
                    callback.onSuccess(fallbackPlan);
                }
            }

            @Override
            public void onError(String errorMessage) {
                // Network or API error fallback: Seamlessly switch to offline local interpreter
                ProductionPlan fallbackPlan = promptInterpreter.createProductionPlan(
                        request.getUserPrompt(), request.getStyle(), request.getTargetEngine(), request.getReferenceImageUris());
                callback.onSuccess(fallbackPlan);
            }
        });
    }

    public void processNaturalLanguageStudioEdit(String editPrompt, String activeSceneContextJson, final GeminiApiClient.ApiCallback<String> callback) {
        if (!apiKeyManager.hasApiKey()) {
            callback.onError("Gemini API Key missing. Please set it in Settings.");
            return;
        }

        String sysInst = "You are Vynara Studio Assistant. Interpret direct 3D edit requests on active scene objects or environment. Respond with a concise action statement and JSON parameters for 3D engine transform, material, lighting, or animation updates.";
        String fullPrompt = "SCENE CONTEXT: " + activeSceneContextJson + "\nEDIT PROMPT: " + editPrompt;

        apiClient.generateContent(apiKeyManager.getApiKey(), apiKeyManager.getSelectedModel(), sysInst, fullPrompt, callback);
    }

    public GeminiApiClient getApiClient() { return apiClient; }
    public ApiKeyManager getApiKeyManager() { return apiKeyManager; }
    public KnowledgeManager getKnowledgeManager() { return knowledgeManager; }
    public PromptInterpreter getPromptInterpreter() { return promptInterpreter; }
}