package com.example.ai;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GeminiApiClient {
    private static final String BASE_URL = "https://generativelanguage.googleapis.com/v1beta/";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient client;
    private final Handler mainHandler;

    public interface ApiCallback<T> {
        void onSuccess(T result);
        void onError(String errorMessage);
    }

    public GeminiApiClient() {
        this.client = new OkHttpClient.Builder().build();
        this.mainHandler = new Handler(Looper.getMainLooper());
    }

    public void fetchModels(String apiKey, final ApiCallback<List<AIModel>> callback) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            callback.onError("Gemini API key is required.");
            return;
        }

        String url = BASE_URL + "models?key=" + apiKey.trim();
        Request request = new Request.Builder().url(url).get().build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mainHandler.post(() -> callback.onError("Network error: " + e.getLocalizedMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    final String err = "HTTP " + response.code() + " from Gemini API";
                    mainHandler.post(() -> callback.onError(err));
                    return;
                }

                try {
                    String bodyStr = response.body().string();
                    JSONObject json = new JSONObject(bodyStr);
                    JSONArray modelsArray = json.optJSONArray("models");
                    final List<AIModel> modelList = new ArrayList<>();

                    if (modelsArray != null) {
                        for (int i = 0; i < modelsArray.length(); i++) {
                            JSONObject m = modelsArray.getJSONObject(i);
                            String rawName = m.optString("name", "");
                            String cleanName = rawName.startsWith("models/") ? rawName.substring(7) : rawName;
                            String displayName = m.optString("displayName", cleanName);
                            String description = m.optString("description", "");

                            if (!cleanName.isEmpty()) {
                                modelList.add(new AIModel(cleanName, displayName, description, true));
                            }
                        }
                    }

                    if (modelList.isEmpty()) {
                        modelList.add(new AIModel("gemini-3.5-flash", "gemini-3.5-flash", "Default fast model", true));
                        modelList.add(new AIModel("gemini-3.1-pro-preview", "gemini-3.1-pro-preview", "Advanced reasoning model", true));
                        modelList.add(new AIModel("gemini-3.1-flash-lite-preview", "gemini-3.1-flash-lite-preview", "Lite speed model", true));
                        modelList.add(new AIModel("gemini-2.5-flash", "gemini-2.5-flash", "Multimodal fast model", true));
                        modelList.add(new AIModel("gemini-2.5-pro", "gemini-2.5-pro", "Pro reasoning model", true));
                        modelList.add(new AIModel("gemini-2.5-flash-image", "gemini-2.5-flash-image", "Image generation model", true));
                        modelList.add(new AIModel("gemini-3.1-flash-image-preview", "gemini-3.1-flash-image-preview", "High-quality image model", true));
                        modelList.add(new AIModel("gemini-2.5-flash-preview-tts", "gemini-2.5-flash-preview-tts", "Text-to-speech model", true));
                        modelList.add(new AIModel("veo-3.1-fast-generate-preview", "veo-3.1-fast-generate-preview", "Video generation model", true));
                    }

                    mainHandler.post(() -> callback.onSuccess(modelList));
                } catch (Exception e) {
                    mainHandler.post(() -> callback.onError("Failed to parse models response: " + e.getMessage()));
                }
            }
        });
    }

    public void testConnection(String apiKey, String modelId, final ApiCallback<Boolean> callback) {
        generateContent(apiKey, modelId, "You are a 3D creation assistant.", "Ping test. Respond with OK.", new ApiCallback<String>() {
            @Override
            public void onSuccess(String result) {
                callback.onSuccess(true);
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }

    public void generateContent(String apiKey, String modelId, String systemInstruction, String userPrompt, final ApiCallback<String> callback) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            callback.onError("Gemini API key is missing. Please configure it in Settings.");
            return;
        }

        String targetModel = (modelId != null && !modelId.trim().isEmpty()) ? modelId.trim() : "gemini-2.5-flash";
        String url = BASE_URL + "models/" + targetModel + ":generateContent?key=" + apiKey.trim();

        try {
            JSONObject root = new JSONObject();

            if (systemInstruction != null && !systemInstruction.trim().isEmpty()) {
                JSONObject systemInstObj = new JSONObject();
                JSONArray sysParts = new JSONArray();
                JSONObject sysPartObj = new JSONObject();
                sysPartObj.put("text", systemInstruction);
                sysParts.put(sysPartObj);
                systemInstObj.put("parts", sysParts);
                root.put("systemInstruction", systemInstObj);
            }

            JSONArray contents = new JSONArray();
            JSONObject userMsg = new JSONObject();
            userMsg.put("role", "user");
            JSONArray parts = new JSONArray();
            JSONObject partText = new JSONObject();
            partText.put("text", userPrompt);
            parts.put(partText);
            userMsg.put("parts", parts);
            contents.put(userMsg);
            root.put("contents", contents);

            RequestBody body = RequestBody.create(root.toString(), JSON);
            Request request = new Request.Builder().url(url).post(body).build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {
                    mainHandler.post(() -> callback.onError("Network error: " + e.getLocalizedMessage()));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        final String err = "HTTP Error " + response.code() + ": " + response.message();
                        mainHandler.post(() -> callback.onError(err));
                        return;
                    }

                    try {
                        String responseStr = response.body().string();
                        JSONObject json = new JSONObject(responseStr);
                        JSONArray candidates = json.optJSONArray("candidates");

                        if (candidates != null && candidates.length() > 0) {
                            JSONObject firstCand = candidates.getJSONObject(0);
                            JSONObject content = firstCand.optJSONObject("content");

                            if (content != null) {
                                JSONArray resParts = content.optJSONArray("parts");

                                if (resParts != null && resParts.length() > 0) {
                                    final String textResult = resParts.getJSONObject(0).optString("text", "");
                                    mainHandler.post(() -> callback.onSuccess(textResult));
                                    return;
                                }
                            }
                        }

                        mainHandler.post(() -> callback.onError("No text returned in Gemini response."));
                    } catch (Exception e) {
                        mainHandler.post(() -> callback.onError("Error parsing Gemini response: " + e.getMessage()));
                    }
                }
            });

        } catch (Exception e) {
            callback.onError("Error constructing Gemini request: " + e.getMessage());
        }
    }
}
