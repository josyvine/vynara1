package com.example.knowledge;

import java.util.List;

public class KnowledgeManager {
    private final ConceptGraph conceptGraph;

    public KnowledgeManager() {
        this.conceptGraph = new ConceptGraph();
    }

    public KnowledgeEntry retrieveKnowledgeForPrompt(String userPrompt) {
        if (userPrompt == null || userPrompt.trim().isEmpty()) {
            return conceptGraph.getConcept("house");
        }
        String p = userPrompt.toLowerCase();
        if (p.contains("human") || p.contains("man") || p.contains("woman") || p.contains("character") || p.contains("superhero") || p.contains("person")) {
            return conceptGraph.getConcept("humanoid");
        } else if (p.contains("dog") || p.contains("cat") || p.contains("animal") || p.contains("wolf") || p.contains("quadruped")) {
            return conceptGraph.getConcept("dog");
        } else if (p.contains("bird") || p.contains("eagle") || p.contains("fly") || p.contains("dragon") || p.contains("wing")) {
            return conceptGraph.getConcept("bird");
        } else if (p.contains("house") || p.contains("villa") || p.contains("building") || p.contains("pool") || p.contains("architecture") || p.contains("room") || p.contains("village")) {
            return conceptGraph.getConcept("house");
        } else if (p.contains("sofa") || p.contains("couch")) {
            return conceptGraph.getConcept("sofa");
        } else if (p.contains("table") || p.contains("desk")) {
            return conceptGraph.getConcept("table");
        } else if (p.contains("tree") || p.contains("plant") || p.contains("forest")) {
            return conceptGraph.getConcept("tree");
        }
        return conceptGraph.getConcept(p);
    }

    public ConceptGraph getConceptGraph() {
        return conceptGraph;
    }
}
