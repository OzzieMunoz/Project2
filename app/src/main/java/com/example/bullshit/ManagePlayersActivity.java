package com.example.bullshit;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ManagePlayersActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_players);

        // TODO: Implement functionality to fetch and display players
        Toast.makeText(this, "Manage Players Screen", Toast.LENGTH_SHORT).show();
    }
}
