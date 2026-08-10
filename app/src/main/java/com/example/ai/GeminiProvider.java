package com.example.ai;

import java.util.List;

public class GeminiProvider implements AIProvider {
    private final GeminiApiClient apiClient;

    public GeminiProvider(GeminiApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Override
    public void testConnection(String apiKey, GeminiApiClient.ApiCallback<Boolean> callback) {
        apiClient.testConnection(apiKey, "gemini-2.5-flash", callback);
    }

    @Override
    public void listModels(String apiKey, GeminiApiClient.ApiCallback<List<AIModel>> callback) {
        apiClient.fetchModels(apiKey, callback);
    }

    @Override
    public void generatePlan(String apiKey, String modelId, String userPrompt, String contextJson, GeminiApiClient.ApiCallback<String> callback) {
        String systemInstruction = "You are Vynara Autonomous 3D Artist. Convert natural language user requests into structured 3D execution plans. " +
                "You must strictly return valid JSON specifying intent, required tools, object specifications, skeleton/rigging needs, and task DAG steps. " +
                "Do NOT invent unregistered tools. Use tools: geometry.create_primitive, geometry.create_procedural, material.set_properties, " +
                "character.create_humanoid, character.create_creature, skeleton.bind, rig.create_ik, animation.create_clip, scene.add_light, scene.set_camera, validation.check_mesh.";

        String promptWithContext = "USER REQUEST: " + userPrompt + "\nCONTEXT: " + (contextJson != null ? contextJson : "{}");

        apiClient.generateContent(apiKey, modelId, systemInstruction, promptWithContext, callback);
    }
}
