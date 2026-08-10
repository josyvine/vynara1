package com.example.tasks;

import com.example.knowledge.KnowledgeEntry;

import java.util.ArrayList;
import java.util.List;

public class ProductionPlan {
    private String projectName;
    private String userPrompt;
    private String intent; // CREATE_3D_ASSET, CREATE_CHARACTER, CREATE_CREATURE, CREATE_ARCHITECTURE, MODIFY_SCENE
    private KnowledgeEntry knowledgeReference;
    private TaskGraph taskGraph;
    private List<String> referenceImageUris;

    public ProductionPlan(String projectName, String userPrompt, String intent, KnowledgeEntry knowledgeReference) {
        this.projectName = projectName;
        this.userPrompt = userPrompt;
        this.intent = intent;
        this.knowledgeReference = knowledgeReference;
        this.taskGraph = new TaskGraph();
        this.referenceImageUris = new ArrayList<>();
    }

    public ProductionPlan(String projectName, String userPrompt, String intent, KnowledgeEntry knowledgeReference, List<String> referenceImageUris) {
        this(projectName, userPrompt, intent, knowledgeReference);
        if (referenceImageUris != null) {
            this.referenceImageUris.addAll(referenceImageUris);
        }
    }

    public String getProjectName() { return projectName; }
    public String getUserPrompt() { return userPrompt; }
    public String getIntent() { return intent; }
    public KnowledgeEntry getKnowledgeReference() { return knowledgeReference; }
    public TaskGraph getTaskGraph() { return taskGraph; }

    public List<String> getReferenceImageUris() { return referenceImageUris; }

    public void setReferenceImageUris(List<String> referenceImageUris) {
        this.referenceImageUris = referenceImageUris != null ? referenceImageUris : new ArrayList<>();
    }

    public void addReferenceImageUri(String uriStr) {
        if (uriStr != null && !uriStr.trim().isEmpty()) {
            this.referenceImageUris.add(uriStr);
        }
    }

    public boolean hasReferenceImages() {
        return referenceImageUris != null && !referenceImageUris.isEmpty();
    }
}