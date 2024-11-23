package com.example.bullshit;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AdminPanelActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        // Link buttons with their IDs
        Button viewPlayersButton = findViewById(R.id.viewPlayersButton);
        Button banPlayersButton = findViewById(R.id.banPlayersButton);
        Button monitorLogsButton = findViewById(R.id.monitorLogsButton);
        Button broadcastNotificationsButton = findViewById(R.id.broadcastNotificationsButton);
        Button manageMessagesButton = findViewById(R.id.manageMessagesButton);
        Button viewReportsButton = findViewById(R.id.viewReportsButton);

        // Add click listeners for each button
        viewPlayersButton.setOnClickListener(v ->
                Toast.makeText(this, "Feature coming soon: View Players", Toast.LENGTH_SHORT).show()
        );

        banPlayersButton.setOnClickListener(v ->
                Toast.makeText(this, "Feature coming soon: Ban Players", Toast.LENGTH_SHORT).show()
        );

        monitorLogsButton.setOnClickListener(v ->
                Toast.makeText(this, "Feature coming soon: Monitor Game Logs", Toast.LENGTH_SHORT).show()
        );

        broadcastNotificationsButton.setOnClickListener(v ->
                Toast.makeText(this, "Feature coming soon: Broadcast Notifications", Toast.LENGTH_SHORT).show()
        );

        manageMessagesButton.setOnClickListener(v ->
                Toast.makeText(this, "Feature coming soon: Manage Chat Messages", Toast.LENGTH_SHORT).show()
        );

        viewReportsButton.setOnClickListener(v ->
                Toast.makeText(this, "Feature coming soon: View Reports", Toast.LENGTH_SHORT).show()
        );
    }
}
