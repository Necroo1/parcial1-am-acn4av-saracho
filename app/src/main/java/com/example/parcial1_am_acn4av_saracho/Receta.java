package com.example.parcial1_am_acn4av_saracho;

import java.util.List;
import java.util.Map;

public class Receta {
    private String nombre;
    private String pais;
    private String imagen_url;

    public Receta() {} // Firestore lo requiere

    public Receta(String nombre, String pais, String imagen_url) {
        this.nombre = nombre;
        this.pais = pais;
        this.imagen_url = imagen_url;
    }
    public String getNombre() { return nombre; }
    public String getPais() { return pais; }
    public String getImagen_url() { return imagen_url; }
}
