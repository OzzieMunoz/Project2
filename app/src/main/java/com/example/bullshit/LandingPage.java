package com.example.bullshit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LandingPage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        TextView welcomeText = findViewById(R.id.welcomeText);
        Button logoutButton = findViewById(R.id.logoutButton);
        Button adminButton = findViewById(R.id.adminButton);
        Button newsPageButton = findViewById(R.id.newsPageButton);
        Button reportIssuesButton = findViewById(R.id.reportIssuesButton);

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

        // News Page button action
        newsPageButton.setOnClickListener(v -> {
            Intent newsIntent = new Intent(LandingPage.this, NewsPageActivity.class);
            startActivity(newsIntent);
        });

        // Report Issues button action
        reportIssuesButton.setOnClickListener(v -> {
            Intent reportIntent = new Intent(LandingPage.this, ReportPageActivity.class);
            startActivity(reportIntent);
        });
    }
}

