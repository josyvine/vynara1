package com.example;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.runtime.ProjectRuntime;
import com.example.ui.AssetsFragment;
import com.example.ui.CreateFragment;
import com.example.ui.LandingFragment;
import com.example.ui.ProductionFragment;
import com.example.ui.ProjectsFragment;
import com.example.ui.SettingsFragment;
import com.example.ui.StudioFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private ProjectRuntime projectRuntime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Phase 1 Alignment: Initialize unified shared 3D project runtime instance
        projectRuntime = ProjectRuntime.getInstance(getApplicationContext());

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (savedInstanceState == null) {
            loadFragment(new LandingFragment());
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_create) {
                loadFragment(new CreateFragment());
                return true;
            } else if (id == R.id.nav_projects) {
                loadFragment(new ProjectsFragment());
                return true;
            } else if (id == R.id.nav_assets) {
                loadFragment(new AssetsFragment());
                return true;
            } else if (id == R.id.nav_studio) {
                loadFragment(new StudioFragment());
                return true;
            } else if (id == R.id.nav_settings) {
                loadFragment(new SettingsFragment());
                return true;
            }
            return false;
        });
    }

    public void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    public void navigateToCreate() {
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_create);
        }
    }

    public void navigateToStudio() {
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_studio);
        }
    }

    public void startProduction(String prompt) {
        startProduction(prompt, "Photorealistic", "OpenGL ES / GLTF", new ArrayList<>());
    }

    public void startProduction(String prompt, String style, String targetEngine, List<String> referenceImageUris) {
        loadFragment(ProductionFragment.newInstance(prompt, style, targetEngine, referenceImageUris));
    }

    public ProjectRuntime getProjectRuntime() {
        if (projectRuntime == null) {
            projectRuntime = ProjectRuntime.getInstance(getApplicationContext());
        }
        return projectRuntime;
    }
}