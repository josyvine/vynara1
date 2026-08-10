package com.example.project;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Project {
    private String id;
    private String title;
    private String type; // CHARACTER, CREATURE, ARCHITECTURE, SCENE, FURNITURE
    private String status; // READY, IN_PROGRESS, DRAFT
    private int polyCount;
    private long lastModifiedMs;

    public Project(String id, String title, String type, String status, int polyCount) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.status = status;
        this.polyCount = polyCount;
        this.lastModifiedMs = System.currentTimeMillis();
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getType() { return type; }
    public String getStatus() { return status; }
    public int getPolyCount() { return polyCount; }

    public String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy • HH:mm", Locale.getDefault());
        return sdf.format(new Date(lastModifiedMs));
    }
}
