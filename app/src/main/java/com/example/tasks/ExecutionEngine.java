package com.example.tasks;

import android.os.Handler;
import android.os.Looper;

import com.example.tools.ToolExecutor;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutionEngine {
    private final ToolExecutor toolExecutor;
    private final ExecutorService threadPool;
    private final Handler mainHandler;
    private boolean isPaused = false;
    private boolean isCancelled = false;

    public interface ExecutionCallback {
        void onTaskUpdated(TaskNode node, TaskGraph graph);
        void onGraphCompleted(TaskGraph graph);
        void onError(String errorMessage);
    }

    public ExecutionEngine(ToolExecutor toolExecutor) {
        this.toolExecutor = toolExecutor;
        this.threadPool = Executors.newSingleThreadExecutor();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public void executeGraph(final TaskGraph graph, final ExecutionCallback callback) {
        if (graph == null || graph.hasDependencyCycles()) {
            if (callback != null) callback.onError("Invalid TaskGraph: Contains circular dependencies.");
            return;
        }

        isPaused = false;
        isCancelled = false;

        threadPool.execute(() -> {
            while (!graph.isAllCompleted() && !isCancelled) {
                if (isPaused) {
                    try { Thread.sleep(200); } catch (Exception ignored) {}
                    continue;
                }

                List<TaskNode> readyTasks = graph.getReadyTasks();
                if (readyTasks.isEmpty() && !graph.isAllCompleted()) {
                    // Check if stuck
                    break;
                }

                for (final TaskNode task : readyTasks) {
                    if (isCancelled) break;

                    task.setStatus(TaskNode.Status.RUNNING);
                    notifyTaskUpdated(task, graph, callback);

                    // Execute tool operation
                    boolean success = toolExecutor.executeOperation(task.getOperation());

                    if (success) {
                        task.setStatus(TaskNode.Status.COMPLETED);
                        task.setProgressPercent(100);
                    } else {
                        task.setStatus(TaskNode.Status.FAILED);
                        task.setErrorMessage("Tool execution failed: " + (task.getOperation() != null ? task.getOperation().getToolId() : "null"));
                    }

                    notifyTaskUpdated(task, graph, callback);

                    try { Thread.sleep(300); } catch (Exception ignored) {} // Smooth UI step transition
                }
            }

            mainHandler.post(() -> {
                if (callback != null) {
                    if (isCancelled) {
                        callback.onError("Production workflow cancelled by user.");
                    } else if (graph.isAllCompleted()) {
                        callback.onGraphCompleted(graph);
                    } else {
                        callback.onError("Workflow halted before all tasks completed.");
                    }
                }
            });
        });
    }

    private void notifyTaskUpdated(final TaskNode task, final TaskGraph graph, final ExecutionCallback callback) {
        mainHandler.post(() -> {
            if (callback != null) callback.onTaskUpdated(task, graph);
        });
    }

    public void pause() { isPaused = true; }
    public void resume() { isPaused = false; }
    public void cancel() { isCancelled = true; }
    public boolean isPaused() { return isPaused; }
}
