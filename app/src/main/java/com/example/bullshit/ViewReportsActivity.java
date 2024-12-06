package com.example.bullshit;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

public class ViewReportsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reports);

        TextView reportsTextView = findViewById(R.id.reportsTextView);

        SharedPreferences prefs = getSharedPreferences("Reports", MODE_PRIVATE);
        String reportsJson = prefs.getString("allReports", "[]");

        // Load and display reports
        StringBuilder reportsBuilder = new StringBuilder();
        try {
            JSONArray reportsArray = new JSONArray(reportsJson);
            for (int i = 0; i < reportsArray.length(); i++) {
                reportsBuilder.append(reportsArray.getString(i)).append("\n\n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            reportsBuilder.append("Error loading reports.");
        }

        reportsTextView.setText(reportsBuilder.toString());
    }
}
