package com.example.parcial1_am_acn4av_saracho;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private TextView selectedCategoryTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    }
}