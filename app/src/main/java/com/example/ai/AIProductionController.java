package com.example.ai;

import android.content.Context;

import com.example.character.CharacterManager;
import com.example.engine.ThreeDEngine;
import com.example.knowledge.KnowledgeManager;
import com.example.tasks.ExecutionEngine;
import com.example.tasks.ProductionPlan;
import com.example.tasks.TaskGraph;
import com.example.tasks.TaskNode;
import com.example.tools.ToolExecutor;
import com.example.tools.ToolRegistry;
import com.example.validation.ValidationManager;

public class AIProductionController {
    private final ApiKeyManager apiKeyManager;
    private final GeminiApiClient apiClient;
    private final GeminiProvider geminiProvider;
    private final KnowledgeManager knowledgeManager;
    private final ToolRegistry toolRegistry;
    private final ThreeDEngine threeDEngine;
    private final CharacterManager characterManager;
    private final ValidationManager validationManager;
    private final ToolExecutor toolExecutor;
    private final ExecutionEngine executionEngine;
    private final AIOrchestrator orchestrator;

    private ProductionPlan currentPlan;

    public AIProductionController(Context context) {
        this.apiKeyManager = new ApiKeyManager(context);
        this.apiClient = new GeminiApiClient();
        this.geminiProvider = new GeminiProvider(apiClient);
        this.knowledgeManager = new KnowledgeManager();
        this.toolRegistry = new ToolRegistry();
        this.threeDEngine = new ThreeDEngine();
        this.characterManager = new CharacterManager(threeDEngine);
        this.validationManager = new ValidationManager();
        this.toolExecutor = new ToolExecutor(threeDEngine, characterManager, validationManager);
        this.executionEngine = new ExecutionEngine(toolExecutor);
        this.orchestrator = new AIOrchestrator(apiClient, apiKeyManager, knowledgeManager);
    }

    public ProductionPlan generatePlan(String userPrompt, String style, String engine) {
        currentPlan = orchestrator.planProduction(userPrompt, style, engine);
        return currentPlan;
    }

    public void executeCurrentPlan(ExecutionEngine.ExecutionCallback callback) {
        if (currentPlan != null && currentPlan.getTaskGraph() != null) {
            executionEngine.executeGraph(currentPlan.getTaskGraph(), callback);
        } else {
            if (callback != null) callback.onError("No active production plan to execute.");
        }
    }

    public ApiKeyManager getApiKeyManager() { return apiKeyManager; }
    public GeminiApiClient getApiClient() { return apiClient; }
    public GeminiProvider getGeminiProvider() { return geminiProvider; }
    public KnowledgeManager getKnowledgeManager() { return knowledgeManager; }
    public ToolRegistry getToolRegistry() { return toolRegistry; }
    public ThreeDEngine getThreeDEngine() { return threeDEngine; }
    public CharacterManager getCharacterManager() { return characterManager; }
    public ValidationManager getValidationManager() { return validationManager; }
    public ExecutionEngine getExecutionEngine() { return executionEngine; }
    public AIOrchestrator getOrchestrator() { return orchestrator; }
    public ProductionPlan getCurrentPlan() { return currentPlan; }
}
