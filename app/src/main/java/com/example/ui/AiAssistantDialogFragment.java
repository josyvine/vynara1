package com.example.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.R;

public class AiAssistantDialogFragment extends DialogFragment {

    private EditText etPrompt;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_ai_assistant, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etPrompt = view.findViewById(R.id.et_studio_ai_prompt);

        Button btnCancel = view.findViewById(R.id.btn_dialog_cancel);
        if (btnCancel != null) {
            btnCancel.setOnClickListener(v -> dismiss());
        }

        Button btnApply = view.findViewById(R.id.btn_dialog_apply);
        if (btnApply != null) {
            btnApply.setOnClickListener(v -> {
                String input = etPrompt.getText().toString().trim();
                if (input.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter an edit prompt", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getContext(), "Executing AI edit: " + input, Toast.LENGTH_LONG).show();
                dismiss();
            });
        }
    }
}
