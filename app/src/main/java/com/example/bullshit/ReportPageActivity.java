package com.example.bullshit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReportPageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_page);

        EditText reportEditText = findViewById(R.id.reportEditText);
        Button submitReportButton = findViewById(R.id.submitReportButton);

        submitReportButton.setOnClickListener(v -> {
            String report = reportEditText.getText().toString();

            if (!report.isEmpty()) {
                String currentDate = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());
                String reportWithDate = currentDate + " - " + report;

                SharedPreferences prefs = getSharedPreferences("Reports", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                String reportsJson = prefs.getString("allReports", "[]");
                try {
                    JSONArray reportsArray = new JSONArray(reportsJson);
                    reportsArray.put(reportWithDate);

                    // Save updated reports
                    editor.putString("allReports", reportsArray.toString());
                    editor.apply();

                    Toast.makeText(this, "Report submitted!", Toast.LENGTH_SHORT).show();
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error submitting report!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please write something to report.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
