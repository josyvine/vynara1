package com.example.ai;

import com.example.knowledge.KnowledgeManager;
import com.example.tasks.ProductionPlan;

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

    public void processNaturalLanguageStudioEdit(String editPrompt, String activeSceneContextJson, final GeminiApiClient.ApiCallback<String> callback) {
        if (!apiKeyManager.hasApiKey()) {
            callback.onError("Gemini API Key missing. Please set it in Settings.");
            return;
        }

        String sysInst = "You are Vynara Studio Assistant. Interpret direct 3D edit requests on active scene objects or environment. Respond with a concise action statement and JSON parameters for 3D engine transform, material, lighting, or animation updates.";
        String fullPrompt = "SCENE CONTEXT: " + activeSceneContextJson + "\nEDIT PROMPT: " + editPrompt;

        apiClient.generateContent(apiKeyManager.getApiKey(), apiKeyManager.getSelectedModel(), sysInst, fullPrompt, callback);
    }
}