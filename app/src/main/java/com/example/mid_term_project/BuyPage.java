package com.example.mid_term_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BuyPage extends AppCompatActivity {
    private Button cancelButton;
    private Button confirmButton;
    private TextView itemNameTextView;
    private TextView itemDescriptionTextView;
    private ImageView buyPageImageView;
    private ScrollView invoiceScrollView;
    private TextView invoiceTextView;

    private String itemName;
    private String itemDescription;
    private int itemImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_buy_page);

        // Initialize views
        cancelButton = findViewById(R.id.cancelButton);
        confirmButton = findViewById(R.id.confirmButton);
        itemNameTextView = findViewById(R.id.itemNameTextView);
        itemDescriptionTextView = findViewById(R.id.itemDescriptionTextView);
        buyPageImageView = findViewById(R.id.buyPageImageView);
        invoiceScrollView = findViewById(R.id.invoiceScrollView);
        invoiceTextView = findViewById(R.id.invoiceTextView);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get data from intent
        itemName = getIntent().getStringExtra("itemName");
        itemDescription = getIntent().getStringExtra("itemDescription");
        itemImage = getIntent().getIntExtra("itemImage", 0);

        // Set data to views
        itemNameTextView.setText(itemName);
        itemDescriptionTextView.setText(itemDescription);
        buyPageImageView.setImageResource(itemImage);

        // Set up button click listeners
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Go back to detail page
                finish();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Generate and display invoice
                generateInvoice();

                // Change button text
                confirmButton.setText("Return to Home");
                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Return to main page
                        Intent intent = new Intent(BuyPage.this, CustomerBuyPage.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                });

                // Hide cancel button
                cancelButton.setVisibility(View.GONE);
            }
        });
    }

    private void generateInvoice() {
        // Get current date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDateTime = dateFormat.format(new Date());

        // Generate a unique order ID
        String orderId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // Sample price (you would normally get this from your data)

        // Format the invoice
        StringBuilder invoiceBuilder = new StringBuilder();
        invoiceBuilder.append("INVOICE\n\n");
        invoiceBuilder.append("Order ID: ").append(orderId).append("\n");
        invoiceBuilder.append("Date: ").append(currentDateTime).append("\n\n");
        invoiceBuilder.append("Item: ").append(itemName).append("\n");
        invoiceBuilder.append("Description: ").append(itemDescription).append("\n\n");
        invoiceBuilder.append("Price: 70$\n");
        invoiceBuilder.append("Total: 70$\n");
        invoiceBuilder.append("Thank you for your purchase!");

        // Display the invoice
        invoiceTextView.setText(invoiceBuilder.toString());
        invoiceScrollView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        // Go back to detail page
        finish();
    }
}