package com.example.smartpantrymanager;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecipeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecipeAdapter recipeAdapter;
    private TextView textNoRecipes;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewRecipes);
        textNoRecipes = findViewById(R.id.textNoRecipes);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadStrictlyMatchedRecipes();
    }

    private void loadStrictlyMatchedRecipes() {
        // 1. Gather all user pantry ingredient names into a Set (normalized to lowercase)
        Set<String> pantryIngredients = new HashSet<>();
        Cursor pantryCursor = dbHelper.getAllPantryItems();
        if (pantryCursor != null) {
            int nameIndex = pantryCursor.getColumnIndex(DatabaseHelper.COLUMN_NAME);
            while (pantryCursor.moveToNext()) {
                if (nameIndex != -1) {
                    String name = pantryCursor.getString(nameIndex).trim().toLowerCase();
                    pantryIngredients.add(name);
                }
            }
            pantryCursor.close();
        }

        // 2. Fetch all seeded recipes from database
        List<Recipe> matchedRecipes = new ArrayList<>();
        Cursor recipeCursor = dbHelper.getAllRecipes();
        if (recipeCursor != null) {
            int titleIdx = recipeCursor.getColumnIndex(DatabaseHelper.COLUMN_RECIPE_TITLE);
            int ingIdx = recipeCursor.getColumnIndex(DatabaseHelper.COLUMN_RECIPE_INGREDIENTS);
            int stepIdx = recipeCursor.getColumnIndex(DatabaseHelper.COLUMN_RECIPE_STEPS);

            while (recipeCursor.moveToNext()) {
                String title = titleIdx != -1 ? recipeCursor.getString(titleIdx) : "";
                String ingredientsStr = ingIdx != -1 ? recipeCursor.getString(ingIdx) : "";
                String steps = stepIdx != -1 ? recipeCursor.getString(stepIdx) : "";

                // 3. Strict-Matching Core Logic Algorithm
                boolean hasAllIngredients = true;
                String[] requiredItems = ingredientsStr.split(",");

                for (String item : requiredItems) {
                    String trimmedItem = item.trim().toLowerCase();
                    if (!trimmedItem.isEmpty()) {
                        // Check if pantry contains this required ingredient (robust substring/exact check)
                        boolean found = false;
                        for (String pantryItem : pantryIngredients) {
                            if (pantryItem.contains(trimmedItem) || trimmedItem.contains(pantryItem)) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            hasAllIngredients = false;
                            break; // Fails strict matching if even one ingredient is missing
                        }
                    }
                }

                // If user has EVERY single required ingredient, add to suggestions list
                if (hasAllIngredients && !requiredItems[0].isEmpty()) {
                    matchedRecipes.add(new Recipe(title, "Ingredients: " + ingredientsStr, steps));
                }
            }
            recipeCursor.close();
        }

        // 4. Handle empty state feedback if zero recipes match
        if (matchedRecipes.isEmpty()) {
            textNoRecipes.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            textNoRecipes.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        // 5. Populate RecyclerView Adapter
        recipeAdapter = new RecipeAdapter(this, matchedRecipes);
        recyclerView.setAdapter(recipeAdapter);
    }
}
