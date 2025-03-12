package com.example.mid_term_project;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminModifyActivity extends AppCompatActivity implements OnClickListener {
    private EditText titleText;
    private Button updateBtn, deleteBtn;
    private EditText descText;
    private long _id;
    private AdminDBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Modify Item");
        setContentView(R.layout.activity_admin_modify);

        // Initialize DB manager
        dbManager = new AdminDBManager(this);
        dbManager.open();

        // Initialize UI components
        titleText = findViewById(R.id.subject_edittext);
        descText = findViewById(R.id.description_edittext);
        updateBtn = findViewById(R.id.btn_update);
        deleteBtn = findViewById(R.id.btn_delete);

        // Get intent data
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String name = intent.getStringExtra("title");
        String desc = intent.getStringExtra("desc");

        // Validate intent data
        if (id == null || id.isEmpty()) {
            Toast.makeText(this, "Error: No item ID provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Set values
        _id = Long.parseLong(id);
        titleText.setText(name);
        descText.setText(desc);

        // Set click listeners
        updateBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_update) {
            String title = titleText.getText().toString().trim();
            String desc = descText.getText().toString().trim();

            // Validate input
            if (title.isEmpty()) {
                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                dbManager.update(_id, title, desc);
                Toast.makeText(this, "Item updated successfully", Toast.LENGTH_SHORT).show();
                this.returnHome();
            } catch (Exception e) {
                Toast.makeText(this, "Error updating item: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.btn_delete) {
            // Add confirmation dialog
            new AlertDialog.Builder(this)
                    .setTitle("Delete Item")
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        try {
                            dbManager.delete(_id);
                            Toast.makeText(this, "Item deleted successfully",
                                    Toast.LENGTH_SHORT).show();
                            this.returnHome();
                        } catch (Exception e) {
                            Toast.makeText(this, "Error deleting item: " + e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }

    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(),
                Admin.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home_intent);
        finish(); // Proper Activity cleanup
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close database connection
        if (dbManager != null) {
            dbManager.close();
        }
    }
}