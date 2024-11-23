package com.example.bullshit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LandingPage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        // Display a welcome message
        TextView welcomeText = findViewById(R.id.welcomeText);
        welcomeText.setText("Welcome to BullShit!");

        // Logout button
        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(LandingPage.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
