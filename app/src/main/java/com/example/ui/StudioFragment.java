package com.example.ui;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.R;
import com.example.engine.StudioGLRenderer;
import com.example.engine.ThreeDEngine;

public class StudioFragment extends Fragment {

    private GLSurfaceView glSurfaceView;
    private StudioGLRenderer renderer;
    private ThreeDEngine engine;
    private TextView tvStats;
    private TextView tvSelectedInfo;
    private TextView tvAnimTime;
    private SeekBar seekbarTimeline;
    private ImageButton btnAnimPlay;
    private boolean isPlaying = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_studio, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        glSurfaceView = view.findViewById(R.id.gl_surface_view);
        tvStats = view.findViewById(R.id.tv_studio_poly_stats);
        tvSelectedInfo = view.findViewById(R.id.tv_selected_object_info);
        tvAnimTime = view.findViewById(R.id.tv_anim_time);
        seekbarTimeline = view.findViewById(R.id.seekbar_timeline);
        btnAnimPlay = view.findViewById(R.id.btn_anim_play);

        // Setup OpenGL ES 2.0
        glSurfaceView.setEGLContextClientVersion(2);
        engine = new ThreeDEngine();
        renderer = new StudioGLRenderer(engine.getSceneManager(), engine.getCameraManager(), engine.getLightManager());
        glSurfaceView.setRenderer(renderer);
        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        // Header buttons
        View btnUndo = view.findViewById(R.id.btn_undo);
        if (btnUndo != null) {
            btnUndo.setOnClickListener(v -> Toast.makeText(getContext(), "Undo action", Toast.LENGTH_SHORT).show());
        }
        View btnRedo = view.findViewById(R.id.btn_redo);
        if (btnRedo != null) {
            btnRedo.setOnClickListener(v -> Toast.makeText(getContext(), "Redo action", Toast.LENGTH_SHORT).show());
        }
        View btnExport = view.findViewById(R.id.btn_export_gltf);
        if (btnExport != null) {
            btnExport.setOnClickListener(v -> Toast.makeText(getContext(), "3D Scene exported as .gltf / .glb", Toast.LENGTH_LONG).show());
        }

        // Tool transform buttons
        View btnSelect = view.findViewById(R.id.btn_tool_select);
        if (btnSelect != null) {
            btnSelect.setOnClickListener(v -> {
                tvSelectedInfo.setText("Selected: Modern Villa House (Main Mesh)");
                Toast.makeText(getContext(), "Select Tool Active", Toast.LENGTH_SHORT).show();
            });
        }
        View btnMove = view.findViewById(R.id.btn_tool_move);
        if (btnMove != null) {
            btnMove.setOnClickListener(v -> Toast.makeText(getContext(), "Translate Tool Active (XYZ Axis)", Toast.LENGTH_SHORT).show());
        }
        View btnRotate = view.findViewById(R.id.btn_tool_rotate);
        if (btnRotate != null) {
            btnRotate.setOnClickListener(v -> Toast.makeText(getContext(), "Rotate Gizmo Active", Toast.LENGTH_SHORT).show());
        }
        View btnHierarchy = view.findViewById(R.id.btn_tool_hierarchy);
        if (btnHierarchy != null) {
            btnHierarchy.setOnClickListener(v -> Toast.makeText(getContext(), "Scene Graph: 12 Nodes, 3 Lights, 1 Camera", Toast.LENGTH_SHORT).show());
        }

        // Timeline controls
        if (btnAnimPlay != null) {
            btnAnimPlay.setOnClickListener(v -> {
                isPlaying = !isPlaying;
                btnAnimPlay.setImageResource(isPlaying ? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play);
            });
        }

        if (seekbarTimeline != null) {
            seekbarTimeline.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    float seconds = (progress / 100.0f) * 5.0f;
                    tvAnimTime.setText(String.format("%.1fs / 5.0s", seconds));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });
        }

        // AI Assistant Button
        View btnAi = view.findViewById(R.id.btn_ai_studio_assistant);
        if (btnAi != null) {
            btnAi.setOnClickListener(v -> {
                AiAssistantDialogFragment dialog = new AiAssistantDialogFragment();
                dialog.show(getChildFragmentManager(), "AiAssistantDialog");
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (glSurfaceView != null) glSurfaceView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (glSurfaceView != null) glSurfaceView.onPause();
    }
}
