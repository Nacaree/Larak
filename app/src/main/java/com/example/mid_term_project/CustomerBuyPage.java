package com.example.mid_term_project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CustomerBuyPage extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private List<ItemModel> itemList;
    private AdminDBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_buy_page);

        // Set activity title
        setTitle("Available Items");

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        // Initialize database manager
        dbManager = new AdminDBManager(this);
        dbManager.open();

        // Load items from database
        loadItemsFromDatabase();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadItemsFromDatabase() {
        itemList = new ArrayList<>();

        try {
            Cursor cursor = dbManager.fetch();

            if (cursor != null && cursor.getCount() > 0) {
                do {
                    // Get subject and description from cursor
                    int idIndex = cursor.getColumnIndex(AdminDatabaseHelper._ID);
                    int subjectIndex = cursor.getColumnIndex(AdminDatabaseHelper.SUBJECT);
                    int descIndex = cursor.getColumnIndex(AdminDatabaseHelper.DESC);

                    if (idIndex >= 0 && subjectIndex >= 0 && descIndex >= 0) {
                        long id = cursor.getLong(idIndex);
                        String subject = cursor.getString(subjectIndex);
                        String desc = cursor.getString(descIndex);

                        // Use placeholder image for now
                        // In production, you would store image references in the database
                        itemList.add(new ItemModel(subject, R.drawable.racket, desc));
                    }
                } while (cursor.moveToNext());
            } else {
                // If no items in database, show a message
                Toast.makeText(this, "No items available", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error loading items: " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }

        // Set adapter with the items from database
        adapter = new CustomAdapter(this, itemList);
        recyclerView.setAdapter(adapter);

        // Set click listener to handle item clicks
        adapter.setOnItemClickListener(new CustomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ItemModel item) {
                Intent intent = new Intent(CustomerBuyPage.this, DetailPage.class);
                intent.putExtra("text", item.getName());
                intent.putExtra("image", item.getImage());
                intent.putExtra("description", item.getDescription());
                startActivity(intent);
            }
        });
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