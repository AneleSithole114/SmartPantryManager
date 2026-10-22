# SmartPantryManager
# Smart Pantry Manager

## Description
Smart Pantry Manager is an Android application written in Java designed to help users reduce food waste. It tracks home pantry inventory (ingredients, quantities, units, and expiry dates) and utilizes a strict-matching algorithm to suggest recipes that can be cooked using *only* the ingredients currently available at home.

## Database Choice & Justification
- **Choice:** SQLite (via `SQLiteOpenHelper`)
- **Justification:** SQLite is implemented locally on-device, providing fast, reliable, and lightweight relational data storage without requiring an external server or internet connection. It natively supports full CRUD operations and seamlessly handles pre-seeded database assets like our built-in collection of 15+ recipes.

## Key Features
- **Pantry CRUD:** Add, view, update, and delete pantry inventory items.
- **Strict-Matching Recipe Engine:** Automatically filters pre-seeded recipes to display only those where every required ingredient is present in the pantry.
- **Recipe Details:** Tap any recipe to view its full ingredient list and step-by-step preparation instructions.
- **Settings & Preferences:** Toggle expiry-soon alerts and theme settings backed by `SharedPreferences`.

## Setup and Run Instructions
1. Clone or download this repository.
2. Open **Android Studio**.
3. Select **Open an Existing Project** and navigate to the cloned project folder.
4. Allow Gradle to sync dependencies.
5. Run the application on an Android Emulator or a physical Android device (API 24 or higher recommended).
