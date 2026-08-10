package com.example.tasks;

import com.example.tools.ToolOperation;

import java.util.ArrayList;
import java.util.List;

public class TaskNode {
    public enum Status { QUEUED, WAITING, RUNNING, COMPLETED, FAILED, RETRYING, SKIPPED }

    private String id;
    private String title;
    private String description;
    private Status status;
    private int progressPercent;
    private ToolOperation operation;
    private List<String> dependencyTaskIds = new ArrayList<>();
    private String errorMessage;

    public TaskNode(String id, String title, String description, ToolOperation operation) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.operation = operation;
        this.status = Status.QUEUED;
        this.progressPercent = 0;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Status getStatus() { return status; }
    public int getProgressPercent() { return progressPercent; }
    public ToolOperation getOperation() { return operation; }
    public List<String> getDependencyTaskIds() { return dependencyTaskIds; }
    public String getErrorMessage() { return errorMessage; }

    public void setStatus(Status status) { this.status = status; }
    public void setProgressPercent(int progress) { this.progressPercent = progress; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public TaskNode addDependency(String taskId) {
        dependencyTaskIds.add(taskId);
        return this;
    }
}
