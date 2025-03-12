package com.example.mid_term_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminAddActivity extends AppCompatActivity implements OnClickListener {
    private Button addBtn;
    private EditText nameEditText;
    private EditText descEditText;
    private AdminDBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Add Item");
        setContentView(R.layout.activity_admin_add);

        // Initialize UI components
        nameEditText = findViewById(R.id.subject_edittext);
        descEditText = findViewById(R.id.description_edittext);
        addBtn = findViewById(R.id.add_record_btn);

        // Initialize DB manager
        dbManager = new AdminDBManager(this);
        dbManager.open();

        // Set click listener
        addBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_record_btn) {
            final String name = nameEditText.getText().toString().trim();
            final String desc = descEditText.getText().toString().trim();

            // Added validation
            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                dbManager.insert(name, desc);
                Intent main = new Intent(AdminAddActivity.this, Admin.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(main);
                finish(); // Proper Activity cleanup
            } catch (Exception e) {
                Toast.makeText(this, "Error adding item: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
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