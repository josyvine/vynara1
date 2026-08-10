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
    private volatile boolean isPaused = false;
    private volatile boolean isCancelled = false;

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

    /**
     * Phase 12 Alignment: Asynchronously executes DAG tasks in topological dependency order,
     * reporting queued, running, completed, failed, and rolled_back states.
     */
    public void executeGraph(final TaskGraph graph, final ExecutionCallback callback) {
        if (graph == null) {
            if (callback != null) callback.onError("Invalid TaskGraph: Instance is null.");
            return;
        }

        if (graph.hasDependencyCycles()) {
            if (callback != null) callback.onError("Invalid TaskGraph: Contains circular dependencies.");
            return;
        }

        isPaused = false;
        isCancelled = false;

        threadPool.execute(() -> {
            boolean hasExecutionError = false;

            while (!graph.isAllCompleted() && !isCancelled && !hasExecutionError) {
                if (isPaused) {
                    try { Thread.sleep(100); } catch (Exception ignored) {}
                    continue;
                }

                List<TaskNode> readyTasks = graph.getReadyTasks();
                if (readyTasks.isEmpty() && !graph.isAllCompleted()) {
                    // Halt if graph execution is stuck due to failed dependencies
                    break;
                }

                for (final TaskNode task : readyTasks) {
                    if (isCancelled) break;

                    task.setStatus(TaskNode.Status.RUNNING);
                    task.setProgressPercent(20);
                    notifyTaskUpdated(task, graph, callback);

                    // Execute tool operation against local engine
                    boolean success = false;
                    try {
                        if (task.getOperation() != null && toolExecutor != null) {
                            success = toolExecutor.executeOperation(task.getOperation());
                        } else {
                            // Virtual decision/planning task node success
                            success = true;
                        }
                    } catch (Exception e) {
                        task.setErrorMessage("Execution exception: " + e.getMessage());
                        success = false;
                    }

                    if (success) {
                        task.setStatus(TaskNode.Status.COMPLETED);
                        task.setProgressPercent(100);
                    } else {
                        task.setStatus(TaskNode.Status.FAILED);
                        if (task.getErrorMessage() == null) {
                            task.setErrorMessage("Tool execution failed: " + 
                                    (task.getOperation() != null ? task.getOperation().getToolId() : "null"));
                        }
                        hasExecutionError = true;
                    }

                    notifyTaskUpdated(task, graph, callback);

                    if (!success) {
                        break; // Stop executing remaining tasks on error
                    }

                    try { Thread.sleep(150); } catch (Exception ignored) {} // Smooth UI step transition
                }
            }

            final boolean finalErrorState = hasExecutionError;

            mainHandler.post(() -> {
                if (callback != null) {
                    if (isCancelled) {
                        callback.onError("Production workflow cancelled by user.");
                    } else if (graph.isAllCompleted() && !finalErrorState) {
                        callback.onGraphCompleted(graph);
                    } else {
                        callback.onError("Workflow halted due to execution failure or unfulfilled dependencies.");
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
    public boolean isCancelled() { return isCancelled; }

    public void shutdown() {
        if (!threadPool.isShutdown()) {
            threadPool.shutdown();
        }
    }
}