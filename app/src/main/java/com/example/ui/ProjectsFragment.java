package com.example.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MainActivity;
import com.example.R;

import java.util.ArrayList;
import java.util.List;

public class ProjectsFragment extends Fragment {

    private RecyclerView rvProjects;
    private ProjectAdapter adapter;
    private List<ProjectItem> allProjects = new ArrayList<>();
    private EditText etSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_projects, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvProjects = view.findViewById(R.id.rv_projects);
        etSearch = view.findViewById(R.id.et_search_projects);

        rvProjects.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProjectAdapter(project -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToStudio();
            }
        });
        rvProjects.setAdapter(adapter);

        loadSampleProjects();

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

    private void loadSampleProjects() {
        allProjects.clear();
        allProjects.add(new ProjectItem("p1", "Modern Villa & Pool", "Realistic modern villa with swimming pool, wooden deck, interior lighting and furniture", "COMPLETED", "14 Objects • Updated 2m ago"));
        allProjects.add(new ProjectItem("p2", "Cyberpunk Hero Character", "Stylized superhero with high-tech suit, IK rig and running animations", "COMPLETED", "1 Character • Rigged • Updated 1h ago"));
        allProjects.add(new ProjectItem("p3", "Tropical Island Resort", "Ocean shore with palm trees, wooden huts, beach chairs and realistic water shader", "IN_PROGRESS", "28 Objects • Updated 3h ago"));

        adapter.setProjects(allProjects);
    }

    private void filterProjects(String query) {
        if (query.isEmpty()) {
            adapter.setProjects(allProjects);
            return;
        }
        List<ProjectItem> filtered = new ArrayList<>();
        for (ProjectItem p : allProjects) {
            if (p.getName().toLowerCase().contains(query.toLowerCase()) ||
                p.getPrompt().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(p);
            }
        }
        adapter.setProjects(filtered);
    }
}
