package com.example.parcial1_am_acn4av_saracho;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.List;

public class RecetasActivity extends AppCompatActivity {

    private RecyclerView recyclerRecetas;
    private RecetaAdapter adapter;
    private List<Receta> listaRecetas = new ArrayList<>();
    private List<Receta> listaFiltrada = new ArrayList<>();

    private EditText etBuscar;
    private Button btnArgentina, btnMexico, btnPeru, btnEspana, btnItalia;
    private String filtroPais = "";
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recetas_image);

        recyclerRecetas = findViewById(R.id.recyclerRecetas);
        etBuscar = findViewById(R.id.etBuscar);
        btnArgentina = findViewById(R.id.btnArgentina);
        btnMexico = findViewById(R.id.btnMexico);
        btnPeru = findViewById(R.id.btnPeru);
        btnEspana = findViewById(R.id.btnEspana);
        btnItalia = findViewById(R.id.btnItalia);

        recyclerRecetas.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecetaAdapter(this, listaFiltrada, receta -> {
            // TODO: Abrir detalle (Receta Only)
        });
        recyclerRecetas.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        // Filtro por país
        btnArgentina.setOnClickListener(v -> filtrarPorPais("Argentina"));
        btnMexico.setOnClickListener(v -> filtrarPorPais("México"));
        btnPeru.setOnClickListener(v -> filtrarPorPais("Perú"));
        btnEspana.setOnClickListener(v -> filtrarPorPais("España"));
        btnItalia.setOnClickListener(v -> filtrarPorPais("Italia"));

        // Filtro por búsqueda
        etBuscar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) { filtrar(); }
            @Override public void afterTextChanged(Editable s) {}
        });

        // --- BARRA DE NAVEGACIÓN INFERIOR ---
        ImageButton btnIngredientes = findViewById(R.id.ingredientes);
        ImageButton btnRecetas = findViewById(R.id.recetas);

        // Estamos en recetas: deshabilitar el botón de Recetas
        btnRecetas.setEnabled(false);

        btnIngredientes.setOnClickListener(v -> {
            Intent intent = new Intent(RecetasActivity.this, MainActivity.class); // MainActivity = Ingredientes
            startActivity(intent);
            finish();
        });

        // Cargar todas las recetas al inicio
        cargarRecetas();
    }

    private void cargarRecetas() {
        db.collection("recetas").get()
                .addOnSuccessListener(querySnapshot -> {
                    listaRecetas.clear();
                    for (QueryDocumentSnapshot doc : querySnapshot) {
                        Receta receta = doc.toObject(Receta.class);
                        listaRecetas.add(receta);
                    }
                    filtrar();
                });
    }

    private void filtrarPorPais(String pais) {
        filtroPais = pais;
        filtrar();
    }

    private void filtrar() {
        String busqueda = etBuscar.getText().toString().toLowerCase();
        listaFiltrada.clear();
        for (Receta receta : listaRecetas) {
            boolean coincidePais = filtroPais.isEmpty() || receta.getPais().equalsIgnoreCase(filtroPais);
            boolean coincideTexto = receta.getNombre().toLowerCase().contains(busqueda);
            if (coincidePais && coincideTexto) {
                listaFiltrada.add(receta);
            }
        }
        adapter.updateList(new ArrayList<>(listaFiltrada));
    }
}
