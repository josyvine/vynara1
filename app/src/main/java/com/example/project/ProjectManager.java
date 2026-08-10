package com.example.project;

import java.util.ArrayList;
import java.util.List;

public class ProjectManager {
    private final List<Project> projects = new ArrayList<>();

    public ProjectManager() {
        populateDefaultProjects();
    }

    private void populateDefaultProjects() {
        projects.add(new Project("proj_1", "Cyberpunk Hero Outfit", "CHARACTER", "READY", 24800));
        projects.add(new Project("proj_2", "Modern Seaside Villa & Pool", "ARCHITECTURE", "READY", 48200));
        projects.add(new Project("proj_3", "German Shepherd Dog Rig", "CREATURE", "READY", 18400));
        projects.add(new Project("proj_4", "Italian Leather Armchair", "FURNITURE", "READY", 9600));
    }

    public List<Project> getProjects() { return projects; }

    public void addProject(Project p) {
        if (p != null) projects.add(0, p);
    }
}
