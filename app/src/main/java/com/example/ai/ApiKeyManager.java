package com.example.ai;

import android.content.Context;
import android.content.SharedPreferences;

public class ApiKeyManager {
    private static final String PREF_NAME = "vynara_secure_prefs";
    private static final String KEY_GEMINI_API_KEY = "gemini_api_key";
    private static final String KEY_SELECTED_MODEL = "selected_gemini_model";

    private final SharedPreferences prefs;

    public ApiKeyManager(Context context) {
        this.prefs = context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveApiKey(String apiKey) {
        if (apiKey != null) {
            prefs.edit().putString(KEY_GEMINI_API_KEY, apiKey.trim()).apply();
        }
    }

    public String getApiKey() {
        return prefs.getString(KEY_GEMINI_API_KEY, "");
    }

    public boolean hasApiKey() {
        String key = getApiKey();
        return key != null && !key.trim().isEmpty();
    }

    public String getMaskedApiKey() {
        String key = getApiKey();
        if (key == null || key.length() < 8) {
            return "••••••••••••••••";
        }
        return key.substring(0, 4) + "••••••••••••" + key.substring(key.length() - 3);
    }

    public void saveSelectedModel(String modelId) {
        prefs.edit().putString(KEY_SELECTED_MODEL, modelId).apply();
    }

    public String getSelectedModel() {
        return prefs.getString(KEY_SELECTED_MODEL, "gemini-2.5-flash");
    }

    public void clearApiKey() {
        prefs.edit().remove(KEY_GEMINI_API_KEY).apply();
    }
}
