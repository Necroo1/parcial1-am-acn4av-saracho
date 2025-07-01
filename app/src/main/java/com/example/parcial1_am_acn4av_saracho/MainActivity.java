package com.example.parcial1_am_acn4av_saracho;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView selectedCategoryTitle;
    private LinearLayout addButtonContainer;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addButtonContainer = findViewById(R.id.addButtonContainer);
        selectedCategoryTitle = findViewById(R.id.selected_category_title);
        Button fruitsButton = findViewById(R.id.category_fruits_button);
        Button vegetablesButton = findViewById(R.id.category_vegetables_button);
        Button whiteMeatsButton = findViewById(R.id.category_white_meats_button);
        Button dairyButton = findViewById(R.id.category_dairy_button);
        Button redMeatsButton = findViewById(R.id.category_red_meats_button);
        fruitsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCategoryTitle.setText(R.string.category_fruits);
            }
        });

        vegetablesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCategoryTitle.setText(R.string.category_vegetables);
            }
        });

        whiteMeatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCategoryTitle.setText(R.string.category_white_meats);
            }
        });

        dairyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCategoryTitle.setText(R.string.category_dairy);
            }
        });

        redMeatsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedCategoryTitle.setText(R.string.category_red_meats);
            }
        });
//BOTON +
        Button addButton = new Button(this);
        addButton.setText("+");
        addButton.setTextSize(24);
        addButton.setTextColor(getResources().getColor(android.R.color.white));
        addButton.setGravity(Gravity.CENTER);
        GradientDrawable roundedBackground = new GradientDrawable();
        roundedBackground.setShape(GradientDrawable.RECTANGLE);
        roundedBackground.setColor(Color.BLACK);
        roundedBackground.setCornerRadius(400f);
        addButton.setBackground(roundedBackground);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        addButtonContainer.addView(addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí iría la lógica para abrir la pantalla de carga de ingredientes
                Log.d("MainActivity", "Botón '+' clickeado");
            }
        });
    }
}