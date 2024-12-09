package com.example.bullshit;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MonitorLogsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_logs);

        TextView logsTextView = findViewById(R.id.logsTextView);
        logsTextView.setText("Game Logs will appear here.");
    }
}
