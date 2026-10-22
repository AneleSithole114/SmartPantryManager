package com.example.smartpantrymanager;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        TextView textTitle = findViewById(R.id.textDetailTitle);
        TextView textIngredients = findViewById(R.id.textDetailIngredients);
        TextView textSteps = findViewById(R.id.textDetailSteps);

        // Get recipe details passed via Intent
        String title = getIntent().getStringExtra("TITLE");
        String ingredients = getIntent().getStringExtra("INGREDIENTS");
        String steps = getIntent().getStringExtra("STEPS");

        textTitle.setText(title);
        textIngredients.setText(ingredients);
        textSteps.setText(steps != null ? steps : "1. Prepare all required ingredients.\n2. Cook according to standard instructions.\n3. Serve warm and enjoy!");
    }
}