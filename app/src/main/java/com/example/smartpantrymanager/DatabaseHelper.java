package com.example.smartpantrymanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 2; // Incremented version to refresh schema

    public static final String TABLE_PANTRY = "pantry";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_UNIT = "unit";
    public static final String COLUMN_EXPIRY = "expiry_date";

    public static final String TABLE_RECIPES = "recipes";
    public static final String COLUMN_RECIPE_ID = "_id";
    public static final String COLUMN_RECIPE_TITLE = "title";
    public static final String COLUMN_RECIPE_INGREDIENTS = "ingredients";
    public static final String COLUMN_RECIPE_STEPS = "steps";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Pantry Table
        String CREATE_PANTRY_TABLE = "CREATE TABLE " + TABLE_PANTRY + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_QUANTITY + " REAL, "
                + COLUMN_UNIT + " TEXT, "
                + COLUMN_EXPIRY + " TEXT)";
        db.execSQL(CREATE_PANTRY_TABLE);

        // Recipes Table
        String CREATE_RECIPES_TABLE = "CREATE TABLE " + TABLE_RECIPES + "("
                + COLUMN_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_RECIPE_TITLE + " TEXT, "
                + COLUMN_RECIPE_INGREDIENTS + " TEXT, "
                + COLUMN_RECIPE_STEPS + " TEXT)";
        db.execSQL(CREATE_RECIPES_TABLE);

        // Seed 15-20 Pre-loaded Recipes
        seedRecipes(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        onCreate(db);
    }

    private void seedRecipes(SQLiteDatabase db) {
        insertRecipe(db, "Pantry Fried Rice", "rice, vegetables, soy sauce, oil", "1. Heat oil in pan.\n2. Add cooked rice and vegetables.\n3. Stir in soy sauce and serve warm.");
        insertRecipe(db, "Simple Omelette", "eggs, milk, salt, pepper, butter", "1. Whisk eggs, milk, salt, and pepper.\n2. Melt butter in a skillet.\n3. Pour mixture, cook until set, and fold.");
        insertRecipe(db, "Pasta Primavera", "pasta, tomatoes, garlic, olive oil", "1. Boil pasta until al dente.\n2. Sauté garlic and tomatoes in olive oil.\n3. Toss pasta into the pan and serve.");
        insertRecipe(db, "Garlic Butter Toast", "bread, garlic, butter, salt", "1. Toast bread slices.\n2. Mix minced garlic with melted butter.\n3. Spread over toast and enjoy.");
        insertRecipe(db, "Tomato Soup", "tomatoes, garlic, onion, olive oil, salt", "1. Sauté onion and garlic in olive oil.\n2. Add chopped tomatoes and simmer.\n3. Blend until smooth and season with salt.");
        insertRecipe(db, "Scrambled Eggs", "eggs, butter, salt, pepper", "1. Crack eggs into a bowl and whisk with salt and pepper.\n2. Melt butter in a pan on medium-low heat.\n3. Stir gently until soft-set.");
        insertRecipe(db, "Classic Tuna Salad", "tuna, mayonnaise, salt, pepper, bread", "1. Drain tuna and mix with mayonnaise, salt, and pepper.\n2. Serve between two slices of bread.");
        insertRecipe(db, "Aglio e Olio Pasta", "pasta, garlic, olive oil, red pepper flakes", "1. Boil pasta.\n2. Sauté garlic and red pepper flakes generously in olive oil.\n3. Toss cooked pasta directly into the oil.");
        insertRecipe(db, "Vegetable Stir Fry", "vegetables, soy sauce, oil, garlic", "1. Chop vegetables and mince garlic.\n2. Heat oil in a wok and stir fry.\n3. Add soy sauce before serving.");
        insertRecipe(db, "Cheesy Bread", "bread, cheese, butter", "1. Butter slices of bread and top with cheese.\n2. Toast in a pan or oven until melted.");
        insertRecipe(db, "Basic Oatmeal", "oats, milk, sugar, cinnamon", "1. Boil milk and stir in oats.\n2. Simmer until thick.\n3. Top with sugar and cinnamon.");
        insertRecipe(db, "Pancakes", "flour, eggs, milk, sugar, butter", "1. Mix flour, sugar, eggs, and milk into a smooth batter.\n2. Cook ladlefuls on a greased skillet until golden.");
        insertRecipe(db, "Rice Pudding", "rice, milk, sugar, cinnamon", "1. Simmer cooked rice in milk and sugar until creamy.\n2. Sprinkle with cinnamon before serving.");
        insertRecipe(db, "Fried Egg Sandwich", "bread, eggs, butter, salt", "1. Fry an egg in butter until cooked to preference.\n2. Place between toasted bread slices with a pinch of salt.");
        insertRecipe(db, "Garlic Rice", "rice, garlic, oil, salt", "1. Sauté minced garlic in oil until golden.\n2. Add cooked rice, season with salt, and toss well.");
    }

    private void insertRecipe(SQLiteDatabase db, String title, String ingredients, String steps) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_RECIPE_TITLE, title);
        values.put(COLUMN_RECIPE_INGREDIENTS, ingredients);
        values.put(COLUMN_RECIPE_STEPS, steps);
        db.insert(TABLE_RECIPES, null, values);
    }

    // CRUD for Pantry
    public boolean addPantryItem(String name, double quantity, String unit, String expiryDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_UNIT, unit);
        values.put(COLUMN_EXPIRY, expiryDate);
        long result = db.insert(TABLE_PANTRY, null, values);
        db.close();
        return result != -1;
    }

    public Cursor getAllPantryItems() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_PANTRY, null, null, null, null, null, COLUMN_NAME + " ASC");
    }

    public boolean updatePantryItem(long id, String name, double quantity, String unit, String expiryDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_QUANTITY, quantity);
        values.put(COLUMN_UNIT, unit);
        values.put(COLUMN_EXPIRY, expiryDate);
        int rowsAffected = db.update(TABLE_PANTRY, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected > 0;
    }

    public void deletePantryItem(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PANTRY, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // READ: Get all seeded recipes
    public Cursor getAllRecipes() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_RECIPES, null, null, null, null, null, null);
    }
}