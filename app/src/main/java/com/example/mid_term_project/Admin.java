package com.example.mid_term_project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Admin extends AppCompatActivity {
    private AdminDBManager dbManager;
    private ListView listView;
    private SimpleCursorAdapter adapter;
    private Cursor cursor;
    private Button addNewButton;
    private Button viewCustomerButton;

    final String[] from = new String[]{AdminDatabaseHelper._ID, AdminDatabaseHelper.SUBJECT, AdminDatabaseHelper.DESC};
    final int[] to = new int[]{R.id.id, R.id.title, R.id.desc};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set activity title
        setTitle("Admin Panel");

        // Initialize database manager
        dbManager = new AdminDBManager(this);
        dbManager.open();

        // Initialize UI components
        listView = findViewById(R.id.list_view);
        addNewButton = findViewById(R.id.add_button);
        viewCustomerButton = findViewById(R.id.view_customer_button);

        // Set empty view for list
        View emptyView = findViewById(R.id.empty);
        if (emptyView != null) {
            listView.setEmptyView(emptyView);
        }

        // Load data
        loadData();

        // Set click listener for add button
        addNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(Admin.this, AdminAddActivity.class);
                startActivity(addIntent);
            }
        });

        // Set click listener for view customer button
        viewCustomerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent customerIntent = new Intent(Admin.this, CustomerBuyPage.class);
                startActivity(customerIntent);
            }
        });

        // Set click listener for list items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {
                TextView idTextView = view.findViewById(R.id.id);
                TextView titleTextView = view.findViewById(R.id.title);
                TextView descTextView = view.findViewById(R.id.desc);

                String id = idTextView.getText().toString();
                String title = titleTextView.getText().toString();
                String desc = descTextView.getText().toString();

                Intent modifyIntent = new Intent(getApplicationContext(), AdminModifyActivity.class);
                modifyIntent.putExtra("title", title);
                modifyIntent.putExtra("desc", desc);
                modifyIntent.putExtra("id", id);
                startActivity(modifyIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data when returning to this activity
        loadData();
    }

    private void loadData() {
        try {
            // Close any existing cursor to prevent memory leaks
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

            cursor = dbManager.fetch();

            // Check if adapter exists
            if (adapter == null) {
                adapter = new SimpleCursorAdapter(
                        this,
                        R.layout.item_view,
                        cursor,
                        from,
                        to,
                        0
                );
                listView.setAdapter(adapter);
            } else {
                // Update with new cursor
                adapter.changeCursor(cursor);
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error loading data: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close cursor and database
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        if (dbManager != null) {
            dbManager.close();
        }
    }
}