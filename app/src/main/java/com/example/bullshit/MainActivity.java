package com.example.bullshit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the Login button
        findViewById(R.id.loginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to LoginActivity
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // Set up the Create Account button
        findViewById(R.id.createAccountButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Placeholder for future account creation functionality
            }
        });
    }
}
