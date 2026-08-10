package com.example.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MainActivity;
import com.example.R;
import com.example.tasks.TaskGraph;
import com.example.tasks.TaskNode;

import java.util.ArrayList;
import java.util.List;

public class ProductionFragment extends Fragment {

    private static final String ARG_PROMPT = "arg_prompt";

    private TextView tvProjectTitle;
    private TextView tvStatus;
    private TextView tvTaskCounter;
    private TextView tvProgressPercent;
    private ProgressBar progressBar;
    private RecyclerView rvTasks;
    private TaskNodeAdapter adapter;

    private String prompt = "3D Asset Creation";
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean isPaused = false;
    private int currentStep = 0;
    private List<TaskNode> taskNodes = new ArrayList<>();

    public static ProductionFragment newInstance(String prompt) {
        ProductionFragment fragment = new ProductionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PROMPT, prompt);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            prompt = getArguments().getString(ARG_PROMPT, prompt);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_production, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvProjectTitle = view.findViewById(R.id.tv_project_title);
        tvStatus = view.findViewById(R.id.tv_current_task_status);
        tvTaskCounter = view.findViewById(R.id.tv_task_counter);
        tvProgressPercent = view.findViewById(R.id.tv_progress_percent);
        progressBar = view.findViewById(R.id.progress_production);
        rvTasks = view.findViewById(R.id.rv_tasks);

        tvProjectTitle.setText("Creating: " + prompt);

        rvTasks.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TaskNodeAdapter();
        rvTasks.setAdapter(adapter);

        initTasks();

        Button btnPause = view.findViewById(R.id.btn_pause_production);
        if (btnPause != null) {
            btnPause.setOnClickListener(v -> {
                isPaused = !isPaused;
                btnPause.setText(isPaused ? "Resume" : "Pause");
                Toast.makeText(getContext(), isPaused ? "Pipeline Paused" : "Pipeline Resumed", Toast.LENGTH_SHORT).show();
            });
        }

        Button btnCancel = view.findViewById(R.id.btn_cancel_production);
        if (btnCancel != null) {
            btnCancel.setOnClickListener(v -> {
                handler.removeCallbacksAndMessages(null);
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).navigateToCreate();
                }
            });
        }

        Button btnViewStudio = view.findViewById(R.id.btn_open_in_studio);
        if (btnViewStudio != null) {
            btnViewStudio.setOnClickListener(v -> {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).navigateToStudio();
                }
            });
        }

        startSimulation();
    }

    private void initTasks() {
        taskNodes.clear();
        taskNodes.add(new TaskNode("t1", "1. Knowledge Base Query & Intent Analysis", "AI analyzing request: " + prompt, null));
        taskNodes.add(new TaskNode("t2", "2. Procedural Geometry Blueprint", "Generating vertices, indices, and primitive meshes", null));
        taskNodes.add(new TaskNode("t3", "3. PBR Material & Texture Synthesis", "Synthesizing Albedo, Roughness, Metallic & Normal maps", null));
        taskNodes.add(new TaskNode("t4", "4. Skeleton & Rigging Construction", "Building bone hierarchy and joints for rigging", null));
        taskNodes.add(new TaskNode("t5", "5. Inverse Kinematics (IK) & Animation", "Binding skin weights and creating keyframe clips", null));
        taskNodes.add(new TaskNode("t6", "6. Scene Composition & Lighting", "Placing lights, environment maps, and target camera", null));
        taskNodes.add(new TaskNode("t7", "7. Real-Time Scene Mesh Validation", "Validating geometry normals, texture bounds & shader compatibility", null));
        taskNodes.add(new TaskNode("t8", "8. Final GLTF Scene Assembly", "Exporting optimized 3D scene graph", null));

        adapter.setTasks(taskNodes);
    }

    private void startSimulation() {
        currentStep = 0;
        runNextTaskStep();
    }

    private void runNextTaskStep() {
        if (isPaused) {
            handler.postDelayed(this::runNextTaskStep, 1000);
            return;
        }

        if (currentStep < taskNodes.size()) {
            TaskNode node = taskNodes.get(currentStep);
            node.setStatus(TaskNode.Status.RUNNING);
            adapter.notifyItemChanged(currentStep);

            tvStatus.setText("AI Status: " + node.getTitle());
            int percent = (int) (((currentStep + 1) / (float) taskNodes.size()) * 100);
            progressBar.setProgress(percent);
            tvProgressPercent.setText(percent + "%");
            tvTaskCounter.setText("Tasks: " + (currentStep + 1) + " / " + taskNodes.size());

            handler.postDelayed(() -> {
                node.setStatus(TaskNode.Status.COMPLETED);
                adapter.notifyItemChanged(currentStep);
                currentStep++;
                if (currentStep < taskNodes.size()) {
                    runNextTaskStep();
                } else {
                    tvStatus.setText("AI Status: All Tasks Completed Successfully! ✦");
                    Toast.makeText(getContext(), "3D Generation Complete!", Toast.LENGTH_LONG).show();
                }
            }, 1200);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
    }
}
