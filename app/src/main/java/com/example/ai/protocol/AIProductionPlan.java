package com.example.ai.protocol;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AIProductionPlan {
    private String intent;
    private String style;
    private String quality;
    private final List<String> components = new ArrayList<>();
    private final List<AIToolCall> toolCalls = new ArrayList<>();

    public AIProductionPlan() {
    }

    /**
     * Phase 2 Alignment: Parses Gemini's structured response JSON into 
     * strongly typed intent fields, component lists, and executable tool call DAGs.
     */
    public static AIProductionPlan fromJson(JSONObject json) {
        AIProductionPlan plan = new AIProductionPlan();
        if (json == null) return plan;

        plan.intent = json.optString("intent", "create_scene");
        
        JSONObject scene = json.optJSONObject("scene");
        if (scene != null) {
            plan.style = scene.optString("style", "realistic");
        } else {
            plan.style = json.optString("style", "realistic");
        }
        
        plan.quality = json.optString("quality", "high");

        // Parse structural components list
        JSONArray compArr = json.optJSONArray("components");
        if (compArr != null) {
            for (int i = 0; i < compArr.length(); i++) {
                String comp = compArr.optString(i);
                if (comp != null && !comp.trim().isEmpty()) {
                    plan.components.add(comp);
                }
            }
        }

        // Parse tool calls DAG sequence
        JSONArray toolsArr = json.optJSONArray("toolCalls");
        if (toolsArr != null) {
            for (int i = 0; i < toolsArr.length(); i++) {
                JSONObject toolObj = toolsArr.optJSONObject(i);
                if (toolObj != null) {
                    plan.toolCalls.add(AIToolCall.fromJson(toolObj));
                }
            }
        }

        return plan;
    }

    public String getIntent() { return intent; }
    public String getStyle() { return style; }
    public String getQuality() { return quality; }
    public List<String> getComponents() { return components; }
    public List<AIToolCall> getToolCalls() { return toolCalls; }

    public void setIntent(String intent) { this.intent = intent; }
    public void setStyle(String style) { this.style = style; }
    public void setQuality(String quality) { this.quality = quality; }
}