package com.example.mid_term_project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText etLoginUsername, etLoginPassword;
    private Button btnLogin;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLoginUsername = findViewById(R.id.etLoginUsername);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        db = new DatabaseHelper(this);

        btnLogin.setOnClickListener(v -> {
            String username = etLoginUsername.getText().toString();
            String password = etLoginPassword.getText().toString();

            if (username.equals("admin") && password.equals("admin")) {
                // Open Admin page
                Toast.makeText(this, "Admin Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, Admin.class));
                finish();
            } else if (db.checkUser(username, password)) {
                // Open Customer page
                SharedPreferences sp = getSharedPreferences("UserSession", MODE_PRIVATE);
                sp.edit().putBoolean("isLoggedIn", true).apply();

                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, CustomerBuyPage.class));
                finish();
            } else {
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        });
;
    }
}
