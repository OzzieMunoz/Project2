package com.example.bullshit;

import android.content.Intent;
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
        Button managePlayersButton = findViewById(R.id.managePlayersButton);
        Button monitorLogsButton = findViewById(R.id.monitorLogsButton);
        Button broadcastNotificationsButton = findViewById(R.id.broadcastNotificationsButton);
        Button viewReportsButton = findViewById(R.id.viewReportsButton);

        managePlayersButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminPanelActivity.this, ManagePlayersActivity.class);
            startActivity(intent);
        });

        monitorLogsButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminPanelActivity.this, MonitorLogsActivity.class);
            startActivity(intent);
        });

        broadcastNotificationsButton.setOnClickListener(v ->
                Toast.makeText(this, "Feature coming soon: Broadcast Notifications", Toast.LENGTH_SHORT).show()
        );

        viewReportsButton.setOnClickListener(v ->
                Toast.makeText(this, "Feature coming soon: View Reports", Toast.LENGTH_SHORT).show()
        );
    }
}
