package com.example.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MainActivity;
import com.example.R;
import com.example.project.Project;
import com.example.project.ProjectManager;
import com.example.runtime.ProjectRuntime;

import java.util.ArrayList;
import java.util.List;

public class ProjectsFragment extends Fragment {

    private RecyclerView rvProjects;
    private ProjectAdapter adapter;
    private final List<ProjectItem> displayedProjects = new ArrayList<>();
    private final List<Project> realProjects = new ArrayList<>();
    private EditText etSearch;
    private ProjectRuntime runtime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_projects, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Phase 1 Alignment: Fetch shared ProjectRuntime instance
        if (getActivity() instanceof MainActivity) {
            runtime = ((MainActivity) getActivity()).getProjectRuntime();
        } else {
            runtime = ProjectRuntime.getInstance(requireContext());
        }

        rvProjects = view.findViewById(R.id.rv_projects);
        etSearch = view.findViewById(R.id.et_search_projects);

        rvProjects.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProjectAdapter(projectItem -> {
            if (projectItem == null) return;

            // Phase 14 Alignment: Load selected project file from disk into active runtime state
            boolean loadSuccess = runtime.loadProjectState(projectItem.getId());
            if (loadSuccess) {
                Toast.makeText(getContext(), "Loaded Project: " + projectItem.getName(), Toast.LENGTH_SHORT).show();
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).navigateToStudio();
                }
            } else {
                Toast.makeText(getContext(), "Failed to load project file", Toast.LENGTH_SHORT).show();
            }
        });
        rvProjects.setAdapter(adapter);

        loadStoredProjects();

        if (etSearch != null) {
            etSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    filterProjects(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }

        Button btnNewProject = view.findViewById(R.id.btn_new_project);
        if (btnNewProject != null) {
            btnNewProject.setOnClickListener(v -> {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).navigateToCreate();
                }
            });
        }

        Button btnFirstProject = view.findViewById(R.id.btn_create_first_project);
        if (btnFirstProject != null) {
            btnFirstProject.setOnClickListener(v -> {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).navigateToCreate();
                }
            });
        }
    }

    /**
     * Phase 14 Alignment: Reads active project data models from disk-persistence
     * storage via ProjectManager instead of loading static mock defaults.
     */
    private void loadStoredProjects() {
        displayedProjects.clear();
        realProjects.clear();

        ProjectManager projMgr = runtime.getProjectManager();
        List<Project> storedList = projMgr.getProjects();

        if (storedList != null && !storedList.isEmpty()) {
            realProjects.addAll(storedList);
            for (Project p : storedList) {
                String subInfo = p.getFormattedDate() + " • " + p.getPolyCount() + " tris";
                ProjectItem item = new ProjectItem(p.getId(), p.getTitle(), p.getUserPrompt(), p.getStatus(), subInfo);
                displayedProjects.add(item);
            }
            view.findViewById(R.id.layout_empty_projects_state).setVisibility(View.GONE);
            rvProjects.setVisibility(View.VISIBLE);
        } else {
            // Display empty layout if zero projects are registered
            view.findViewById(R.id.layout_empty_projects_state).setVisibility(View.VISIBLE);
            rvProjects.setVisibility(View.GONE);
        }

        adapter.setProjects(displayedProjects);
    }

    private void filterProjects(String query) {
        if (query == null || query.isEmpty()) {
            adapter.setProjects(displayedProjects);
            return;
        }

        List<ProjectItem> filtered = new ArrayList<>();
        String q = query.toLowerCase().trim();
        for (ProjectItem item : displayedProjects) {
            if ((item.getName() != null && item.getName().toLowerCase().contains(q)) ||
                (item.getPrompt() != null && item.getPrompt().toLowerCase().contains(q))) {
                filtered.add(item);
            }
        }
        adapter.setProjects(filtered);
    }
}