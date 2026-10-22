package com.example.smartpantrymanager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddPantryActivity extends AppCompatActivity {

    private EditText editName, editQuantity, editUnit, editExpiry;
    private Button btnSave;
    private DatabaseHelper dbHelper;

    private boolean isEditMode = false;
    private long itemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pantry);

        dbHelper = new DatabaseHelper(this);

        editName = findViewById(R.id.editItemName);
        editQuantity = findViewById(R.id.editItemQuantity);
        editUnit = findViewById(R.id.editItemUnit);
        editExpiry = findViewById(R.id.editItemExpiry);
        btnSave = findViewById(R.id.btnSaveItem);

        // Check if data was passed for editing
        if (getIntent().hasExtra("ID")) {
            isEditMode = true;
            itemId = getIntent().getLongExtra("ID", -1);
            editName.setText(getIntent().getStringExtra("NAME"));
            editQuantity.setText(String.valueOf(getIntent().getDoubleExtra("QTY", 0)));
            editUnit.setText(getIntent().getStringExtra("UNIT"));
            editExpiry.setText(getIntent().getStringExtra("EXPIRY"));
            btnSave.setText("Update Pantry Item");
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString().trim();
                String qtyStr = editQuantity.getText().toString().trim();
                String unit = editUnit.getText().toString().trim();
                String expiry = editExpiry.getText().toString().trim();

                // Input validation
                if (name.isEmpty() || qtyStr.isEmpty() || unit.isEmpty() || expiry.isEmpty()) {
                    Toast.makeText(AddPantryActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                double quantity = Double.parseDouble(qtyStr);

                if (isEditMode) {
                    boolean success = dbHelper.updatePantryItem(itemId, name, quantity, unit, expiry);
                    if (success) {
                        Toast.makeText(AddPantryActivity.this, "Item updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddPantryActivity.this, "Error updating item", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    boolean success = dbHelper.addPantryItem(name, quantity, unit, expiry);
                    if (success) {
                        Toast.makeText(AddPantryActivity.this, "Item added to pantry", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddPantryActivity.this, "Error saving item", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}