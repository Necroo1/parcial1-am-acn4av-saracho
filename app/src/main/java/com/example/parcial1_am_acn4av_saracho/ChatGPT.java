package com.example.parcial1_am_acn4av_saracho;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;

public class ChatGPT {
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private final String apiKey;

    public ChatGPT(String apiKey) {
        this.apiKey = apiKey;
    }

    public void ask(String prompt, Callback callback) {
        OkHttpClient client = new OkHttpClient();

        Request request = null;

        try {
            JSONObject body = new JSONObject();
            body.put("model", "gpt-3.5-turbo");

            JSONArray messages = new JSONArray();
            JSONObject sysMsg = new JSONObject();
            sysMsg.put("role", "system");
            sysMsg.put("content", "Eres un asistente experto en recetas de cocina que responde de manera amable y clara.");
            messages.put(sysMsg);

            JSONObject userMsg = new JSONObject();
            userMsg.put("role", "user");
            userMsg.put("content", prompt);
            messages.put(userMsg);

            body.put("messages", messages);

            RequestBody requestBody = RequestBody.create(
                    body.toString(), MediaType.parse("application/json"));

            request = new Request.Builder()
                    .url(API_URL)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .addHeader("Content-Type", "application/json")
                    .post(requestBody)
                    .build();

        } catch (Exception e) {
            // Si ocurre error en el JSON, invoca al callback de error
            callback.onFailure(null, new IOException("Error al construir JSON: " + e.getMessage()));
            return;
        }

        client.newCall(request).enqueue(callback);
    }
}
