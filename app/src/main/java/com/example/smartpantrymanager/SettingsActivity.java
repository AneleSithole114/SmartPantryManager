package com.example.smartpantrymanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchExpiry, switchDarkTheme;
    private Button btnSaveSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchExpiry = findViewById(R.id.switchExpiryAlerts);
        switchDarkTheme = findViewById(R.id.switchDarkTheme);
        btnSaveSettings = findViewById(R.id.btnSaveSettings);

        // Load saved preferences using SharedPreferences
        SharedPreferences prefs = getSharedPreferences("SmartPantryPrefs", MODE_PRIVATE);
        switchExpiry.setChecked(prefs.getBoolean("expiry_alerts", true));
        switchDarkTheme.setChecked(prefs.getBoolean("dark_theme", false));

        btnSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("SmartPantryPrefs", MODE_PRIVATE).edit();
                editor.putBoolean("expiry_alerts", switchExpiry.isChecked());
                editor.putBoolean("dark_theme", switchDarkTheme.isChecked());
                editor.apply();

                Toast.makeText(SettingsActivity.this, "Settings saved successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}