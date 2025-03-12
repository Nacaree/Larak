package com.example.mid_term_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetailPage extends AppCompatActivity {
    private Button goBack;
    private Button buyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_page);

        goBack = findViewById(R.id.goBack);
        buyButton = findViewById(R.id.buyButton);

        // Get data from intent
        String text = getIntent().getStringExtra("text");
        int image = getIntent().getIntExtra("image", 0);
        String description = getIntent().getStringExtra("description");

        // Set up "Go Back" button
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailPage.this, CustomerBuyPage.class);
                startActivity(intent);
                finish();
            }
        });

        // Set up "Buy Now" button
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailPage.this, BuyPage.class);
                // Pass the data to BuyPage
                intent.putExtra("itemName", text);
                intent.putExtra("itemImage", image);
                intent.putExtra("itemDescription", description);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get views to display item details
        TextView textView = findViewById(R.id.cityDetailTextView);
        ImageView imageView = findViewById(R.id.cityDetailImageView);
        TextView descriptionTextView = findViewById(R.id.descriptionTextView);

        // Set data to views
        textView.setText(text);
        imageView.setImageResource(image);

        // Set description if the view exists and description is provided
        if (descriptionTextView != null && description != null) {
            descriptionTextView.setText(description);
        }
    }

    @Override
    public void onBackPressed() {
        // Go back to CustomerBuyPage when back button is pressed
        Intent intent = new Intent(DetailPage.this, CustomerBuyPage.class);
        startActivity(intent);
        finish();
    }
}