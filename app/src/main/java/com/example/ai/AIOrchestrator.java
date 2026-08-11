package com.example.ai;

import com.example.ai.protocol.AIProductionPlan;
import com.example.ai.protocol.AIProductionRequest;
import com.example.knowledge.KnowledgeEntry;
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
     * CORE UPGRADE: Gemini is now the true production planner.
     * Integrates Knowledge Engine blueprints and strictly enforces JSON schema
     * for scene, objects, materials, animation, tools, and validation rules.
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

        // Inject deep construction knowledge from the Knowledge Engine
        List<KnowledgeEntry> knowledgeEntries = knowledgeManager.retrieveAllKnowledgeForPrompt(request.getUserPrompt());
        StringBuilder contextBuilder = new StringBuilder();
        if (!knowledgeEntries.isEmpty()) {
            contextBuilder.append("KNOWLEDGE ENGINE BLUEPRINTS (Use these structures to plan the generation):\n");
            for (KnowledgeEntry entry : knowledgeEntries) {
                contextBuilder.append("- Domain: ").append(entry.getName()).append("\n");
                contextBuilder.append("  Components required: ").append(entry.getComponents()).append("\n");
                contextBuilder.append("  Capabilities needed: ").append(entry.getRequiredCapabilities()).append("\n");
                contextBuilder.append("  Default materials: ").append(entry.getDefaultMaterials()).append("\n");
            }
        }

        String systemInstruction = "You are Vynara Autonomous 3D AI Artist, acting as the creative director and technical planner. " +
                "Do not use primitive placeholders like cubes for complex objects; utilize the provided Knowledge Engine Blueprints to build procedural assemblies. " +
                "Return a STRICT JSON object representing the production plan.\n" +
                "REQUIRED JSON SCHEMA:\n" +
                "{\n" +
                "  \"intent\": \"string (e.g., CREATE_SCENE, CREATE_CHARACTER)\",\n" +
                "  \"sceneType\": \"string\",\n" +
                "  \"quality\": \"string\",\n" +
                "  \"objects\": [ { \"name\": \"string\", \"components\": [\"string\"], \"dimensions\": {\"width\": 0.0, \"height\": 0.0, \"depth\": 0.0} } ],\n" +
                "  \"materials\": [ { \"name\": \"string\", \"colorHex\": \"#FFFFFF\", \"metallic\": 0.0, \"roughness\": 0.5, \"opacity\": 1.0 } ],\n" +
                "  \"lighting\": \"string\",\n" +
                "  \"camera\": \"string\",\n" +
                "  \"characters\": [ { \"species\": \"string\", \"riggingRequired\": true, \"animationRequired\": true } ],\n" +
                "  \"requiredTools\": [ { \"toolId\": \"string\", \"description\": \"string\", \"parameters\": {} } ],\n" +
                "  \"validationRules\": [ \"string\" ]\n" +
                "}";
        
        String promptWithContext = "USER PROMPT: " + request.getUserPrompt() +
                "\nSTYLE: " + request.getStyle() +
                "\nTARGET ENGINE: " + request.getTargetEngine() +
                "\nATTACHED REFERENCE IMAGES COUNT: " + request.getReferenceImageUris().size() +
                "\n\n" + contextBuilder.toString();

        // Enforce structured JSON API call
        apiClient.generateStructuredJson(apiKeyManager.getApiKey(), apiKeyManager.getSelectedModel(), systemInstruction, promptWithContext, new GeminiApiClient.ApiCallback<String>() {
            @Override
            public void onSuccess(String jsonResult) {
                try {
                    JSONObject root = new JSONObject(jsonResult);
                    AIProductionPlan structuredPlan = AIProductionPlan.fromJson(root);
                    ProductionPlan executablePlan = promptInterpreter.convertStructuredPlanToExecutablePlan(request, structuredPlan);
                    callback.onSuccess(executablePlan);
                } catch (Exception e) {
                    // CRITICAL UPDATE: Propagate the actual parsing anomaly to the callback 
                    // instead of silently falling back to a deterministic house generation.
                    callback.onError("Failed to parse Gemini production plan: " + e.getMessage());
                }
            }

            @Override
            public void onError(String errorMessage) {
                // CRITICAL UPDATE: Propagate the actual connection/network failure to the callback 
                // instead of silently falling back to a deterministic house generation.
                callback.onError("Gemini API connection error: " + errorMessage);
            }
        });
    }

    /**
     * CORE UPGRADE: Processes natural language 3D scene editing.
     * Translates human intent into specific tool parameters applied to existing scene nodes.
     */
    public void processNaturalLanguageStudioEdit(String editPrompt, String activeSceneContextJson, final GeminiApiClient.ApiCallback<String> callback) {
        if (!apiKeyManager.hasApiKey()) {
            callback.onError("Gemini API Key missing. Please set it in Settings.");
            return;
        }

        String sysInst = "You are Vynara Studio Assistant. Interpret direct 3D edit requests on the active scene objects or environment. " +
                "Return a strict JSON response containing the target object ID and the precise transform or material updates required.\n" +
                "JSON FORMAT:\n" +
                "{\n" +
                "  \"targetObjectId\": \"string (match from context)\",\n" +
                "  \"transform\": { \"px\": 0.0, \"py\": 0.0, \"pz\": 0.0, \"rx\": 0.0, \"ry\": 0.0, \"rz\": 0.0, \"sx\": 1.0, \"sy\": 1.0, \"sz\": 1.0 },\n" +
                "  \"material\": { \"colorHex\": \"#FFFFFF\", \"metallic\": 0.0, \"roughness\": 0.5, \"opacity\": 1.0 }\n" +
                "}";
        
        String fullPrompt = "SCENE CONTEXT:\n" + activeSceneContextJson + "\n\nEDIT PROMPT: " + editPrompt;

        apiClient.generateStructuredJson(apiKeyManager.getApiKey(), apiKeyManager.getSelectedModel(), sysInst, fullPrompt, callback);
    }

    /**
     * CORE UPGRADE: AI Correction Loop implementation.
     * Consults Gemini to dynamically determine the exact tool operation needed to fix a validation error.
     */
    public void requestCorrectionPlan(String validationMessage, String validationCategory, String sceneContextJson, final GeminiApiClient.ApiCallback<String> callback) {
        if (!apiKeyManager.hasApiKey()) {
            callback.onError("API key missing. Cannot use AI for corrections.");
            return;
        }

        String sysInst = "You are Vynara AI Corrector. A validation error occurred in the 3D scene during the inspection phase. " +
                "Review the provided Scene Context and the Error Message. Determine the best repair strategy from the registered ToolRegistry.\n" +
                "Return a STRICT JSON object representing the tool operation needed to repair the scene.\n" +
                "JSON FORMAT:\n" +
                "{\n" +
                "  \"toolId\": \"string (e.g., geometry.create_primitive, material.set_properties, skeleton.bind)\",\n" +
                "  \"parameters\": { \"key\": \"value\" }\n" +
                "}";

        String prompt = "ERROR CATEGORY: " + validationCategory + "\n" +
                        "ERROR MESSAGE: " + validationMessage + "\n\n" +
                        "SCENE CONTEXT:\n" + sceneContextJson;

        apiClient.generateStructuredJson(apiKeyManager.getApiKey(), apiKeyManager.getSelectedModel(), sysInst, prompt, callback);
    }

    public GeminiApiClient getApiClient() { return apiClient; }
    public ApiKeyManager getApiKeyManager() { return apiKeyManager; }
    public KnowledgeManager getKnowledgeManager() { return knowledgeManager; }
    public PromptInterpreter getPromptInterpreter() { return promptInterpreter; }
}