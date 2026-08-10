package com.example.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TaskGraph {
    private final Map<String, TaskNode> nodes = new HashMap<>();

    public void addTask(TaskNode node) {
        if (node != null) {
            nodes.put(node.getId(), node);
        }
    }

    public TaskNode getNode(String id) {
        return nodes.get(id);
    }

    public List<TaskNode> getAllNodes() {
        return new ArrayList<>(nodes.values());
    }

    public boolean hasDependencyCycles() {
        Set<String> visited = new HashSet<>();
        Set<String> recStack = new HashSet<>();

        for (String id : nodes.keySet()) {
            if (isCyclicUtil(id, visited, recStack)) {
                return true;
            }
        }
        return false;
    }

    private boolean isCyclicUtil(String id, Set<String> visited, Set<String> recStack) {
        if (recStack.contains(id)) return true;
        if (visited.contains(id)) return false;

        visited.add(id);
        recStack.add(id);

        TaskNode node = nodes.get(id);
        if (node != null) {
            for (String depId : node.getDependencyTaskIds()) {
                if (isCyclicUtil(depId, visited, recStack)) {
                    return true;
                }
            }
        }

        recStack.remove(id);
        return false;
    }

    public List<TaskNode> getReadyTasks() {
        List<TaskNode> ready = new ArrayList<>();
        for (TaskNode node : nodes.values()) {
            if (node.getStatus() == TaskNode.Status.QUEUED || node.getStatus() == TaskNode.Status.WAITING) {
                boolean allDepsCompleted = true;
                for (String depId : node.getDependencyTaskIds()) {
                    TaskNode dep = nodes.get(depId);
                    if (dep == null || dep.getStatus() != TaskNode.Status.COMPLETED) {
                        allDepsCompleted = false;
                        break;
                    }
                }
                if (allDepsCompleted) {
                    ready.add(node);
                }
            }
        }
        return ready;
    }

    public boolean isAllCompleted() {
        for (TaskNode node : nodes.values()) {
            if (node.getStatus() != TaskNode.Status.COMPLETED && node.getStatus() != TaskNode.Status.SKIPPED) {
                return false;
            }
        }
        return true;
    }

    public int getCompletedCount() {
        int count = 0;
        for (TaskNode node : nodes.values()) {
            if (node.getStatus() == TaskNode.Status.COMPLETED) {
                count++;
            }
        }
        return count;
    }

    public int getTotalCount() {
        return nodes.size();
    }
}
