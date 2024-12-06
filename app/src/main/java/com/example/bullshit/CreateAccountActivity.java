package com.example.bullshit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        EditText usernameEditText = findViewById(R.id.usernameEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        Button createAccountButton = findViewById(R.id.createAccountSubmitButton);

        createAccountButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(CreateAccountActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            } else {
                new Thread(() -> {
                    AppDatabase db = MyApplication.getDatabase();
                    User existingUser = db.userDAO().getUserByUsername(username);

                    if (existingUser != null) {
                        runOnUiThread(() -> Toast.makeText(CreateAccountActivity.this, "Username already exists", Toast.LENGTH_SHORT).show());
                    } else {
                        db.userDAO().insertUser(new User(username, password, false));

                        runOnUiThread(() -> {
                            Toast.makeText(CreateAccountActivity.this, "Account created successfully!", Toast.LENGTH_SHORT).show();

                            // Redirect to Login Page
                            Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        });
                    }
                }).start();
            }
        });
    }
}
