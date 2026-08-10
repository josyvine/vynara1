package com.example.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MainActivity;
import com.example.R;

import java.util.ArrayList;
import java.util.List;

public class AssetsFragment extends Fragment {

    private RecyclerView rvAssets;
    private AssetAdapter adapter;
    private List<AssetItem> allAssets = new ArrayList<>();
    private EditText etSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_assets, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvAssets = view.findViewById(R.id.rv_assets);
        etSearch = view.findViewById(R.id.et_search_assets);

        rvAssets.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new AssetAdapter(asset -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).navigateToStudio();
            }
        });
        rvAssets.setAdapter(adapter);

        loadSampleAssets();

        if (etSearch != null) {
            etSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    filterAssets(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }

        setupCategoryChips(view);
    }

    private void loadSampleAssets() {
        allAssets.clear();
        allAssets.add(new AssetItem("a1", "Modern Villa & Pool", "Architecture", "🏠", "24.5k tris • 4K PBR"));
        allAssets.add(new AssetItem("a2", "Rigged Superhero", "Character", "🦸", "18.2k tris • Rigged + IK"));
        allAssets.add(new AssetItem("a3", "Animated Dog", "Creature", "🐕", "8.4k tris • 3 Anim Clips"));
        allAssets.add(new AssetItem("a4", "Leather Sofa", "Furniture", "🛋️", "3.2k tris • Leather PBR"));
        allAssets.add(new AssetItem("a5", "Tropical Palm Tree", "Vegetation", "🌴", "1.8k tris • Wind Shader"));
        allAssets.add(new AssetItem("a6", "Futuristic Sci-Fi Rover", "Vehicle", "🏎️", "32.1k tris • Emissive Lights"));
        allAssets.add(new AssetItem("a7", "Wooden Dining Table", "Furniture", "🪑", "2.1k tris • Wood Texture"));
        allAssets.add(new AssetItem("a8", "Cyberpunk Katana", "Weapon", "⚔️", "4.5k tris • Metallic Gloss"));

        adapter.setAssets(allAssets);
    }

    private void filterAssets(String query) {
        if (query.isEmpty()) {
            adapter.setAssets(allAssets);
            return;
        }
        List<AssetItem> filtered = new ArrayList<>();
        for (AssetItem item : allAssets) {
            if (item.getName().toLowerCase().contains(query.toLowerCase()) ||
                item.getCategory().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(item);
            }
        }
        adapter.setAssets(filtered);
    }

    private void setupCategoryChips(View root) {
        View chipAll = root.findViewById(R.id.chip_cat_all);
        View chipChar = root.findViewById(R.id.chip_cat_characters);
        View chipObj = root.findViewById(R.id.chip_cat_objects);
        View chipBuild = root.findViewById(R.id.chip_cat_buildings);
        View chipMat = root.findViewById(R.id.chip_cat_materials);

        if (chipAll != null) chipAll.setOnClickListener(v -> adapter.setAssets(allAssets));
        if (chipChar != null) chipChar.setOnClickListener(v -> filterAssets("Character"));
        if (chipObj != null) chipObj.setOnClickListener(v -> filterAssets("Furniture"));
        if (chipBuild != null) chipBuild.setOnClickListener(v -> filterAssets("Architecture"));
        if (chipMat != null) chipMat.setOnClickListener(v -> filterAssets("Vehicle"));
    }
}
