package com.example.parcial1_am_acn4av_saracho;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.IOException;
import java.util.ArrayList;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.json.JSONObject;

public class RecetinActivity extends AppCompatActivity {

    private RecyclerView rvChat;
    private EditText etMensaje;
    private ImageButton btnEnviar;
    private ChatAdapter chatAdapter;
    private ArrayList<ChatMessage> chatList;
    private ChatGPT chatGPT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recetin);

        rvChat = findViewById(R.id.rvChat);
        etMensaje = findViewById(R.id.etMensaje);
        btnEnviar = findViewById(R.id.btnEnviar);

        chatList = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatList);
        rvChat.setAdapter(chatAdapter);
        rvChat.setLayoutManager(new LinearLayoutManager(this));

        // Mensaje predeterminado
        chatAdapter.addMessage(new ChatMessage("¿Qué te gustaría cocinar hoy? Contame qué ingredientes tenés.", false));

        chatGPT = new ChatGPT("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");

        btnEnviar.setOnClickListener(v -> enviarMensaje());

        etMensaje.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                enviarMensaje();
                return true;
            }
            return false;
        });
        ImageButton btnIngredientes = findViewById(R.id.ingredientes);
        ImageButton btnRecetas = findViewById(R.id.recetin);
        ImageButton btnRecetin2 = findViewById(R.id.recetin2);

        btnRecetin2.setEnabled(false);
        btnIngredientes.setOnClickListener(v -> {
            Intent intent = new Intent(RecetinActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        btnRecetas.setOnClickListener(v -> {
            Intent intent = new Intent(RecetinActivity.this, RecetasActivity.class);
            startActivity(intent);
            finish();
        });


    }

    private void enviarMensaje() {
        String texto = etMensaje.getText().toString().trim();
        if (texto.isEmpty()) return;

        // Agrega el mensaje del usuario al chat
        chatAdapter.addMessage(new ChatMessage(texto, true));
        rvChat.scrollToPosition(chatAdapter.getItemCount() - 1);
        etMensaje.setText("");

        chatGPT.ask(texto, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    chatAdapter.addMessage(new ChatMessage("Error de red: " + e.getMessage(), false));
                    rvChat.scrollToPosition(chatAdapter.getItemCount() - 1);
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String respuesta = "";
                try {
                    String json = response.body().string();
                    // LOG para debug
                    android.util.Log.d("c", json);

                    if (!response.isSuccessful()) {
                        respuesta = "Error: " + response.code() + " - " + json;
                    } else {
                        JSONObject obj = new JSONObject(json);
                        respuesta = obj.getJSONArray("choices")
                                .getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content")
                                .trim();
                    }
                } catch (Exception e) {
                    respuesta = "Error procesando respuesta: " + e.getMessage();
                }
                final String respuestaFinal = respuesta;
                runOnUiThread(() -> {
                    chatAdapter.addMessage(new ChatMessage(respuestaFinal, false));
                    rvChat.scrollToPosition(chatAdapter.getItemCount() - 1);
                });
            }
        });
    }
}