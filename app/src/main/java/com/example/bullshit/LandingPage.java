package com.example.bullshit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.List;

public class LandingPage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        TextView welcomeText = findViewById(R.id.welcomeText);
        Button logoutButton = findViewById(R.id.logoutButton);
        Button adminButton = findViewById(R.id.adminButton);
        Button startGameButton = findViewById(R.id.startGameButton);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        boolean isAdmin = intent.getBooleanExtra("isAdmin", false);

        // Display welcome message with the username
        welcomeText.setText("Welcome, " + username + "!");

        // Show/hide the admin button based on the isAdmin flag
        if (isAdmin) {
            adminButton.setVisibility(View.VISIBLE);
        } else {
            adminButton.setVisibility(View.GONE);
        }

        // Set logout button functionality
        logoutButton.setOnClickListener(v -> {
            Intent logoutIntent = new Intent(LandingPage.this, MainActivity.class);
            startActivity(logoutIntent);
            finish();
        });

        // Admin button action
        adminButton.setOnClickListener(v -> {
            Intent adminIntent = new Intent(LandingPage.this, AdminPanelActivity.class);
            startActivity(adminIntent);
        });

        startGameButton.setOnClickListener(v -> {
            Toast.makeText(LandingPage.this, "Starting game...", Toast.LENGTH_SHORT).show();

            Deck deck = new Deck();
            List<List<Card>> playerCards = deck.dealCards();
            List<Card> userCards = playerCards.get(0);
            List<Card> botCards = playerCards.get(1);
            if (userCards != null && botCards != null) {
                Intent gameIntent = new Intent(LandingPage.this, GameActivity.class);
                gameIntent.putExtra("userCards", (Serializable) userCards);
                gameIntent.putExtra("botCards", (Serializable) botCards);
                startActivity(gameIntent);
            } else {
                Toast.makeText(LandingPage.this, "Error: No cards dealt", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

