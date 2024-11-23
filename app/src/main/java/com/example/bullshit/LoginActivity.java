package com.example.bullshit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText usernameInput = findViewById(R.id.usernameInput);
        EditText passwordInput = findViewById(R.id.passwordInput);
        Button loginButton = findViewById(R.id.loginSubmitButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();

                new Thread(() -> {
                    AppDatabase db = MyApplication.getDatabase();
                    User user = db.userDAO().getUserByUsername(username);

                    runOnUiThread(() -> {
                        if (user != null && user.getPassword().equals(password)) {
                            // Successful login
                            Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                            // Redirect to LandingPage
                            Intent intent = new Intent(LoginActivity.this, LandingPage.class);
                            intent.putExtra("username", user.getUsername());
                            intent.putExtra("isAdmin", user.isAdmin());
                            startActivity(intent);
                            finish();
                        } else {
                            // Invalid credentials
                            Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                        }
                    });
                }).start();
            }
        });
    }
}
