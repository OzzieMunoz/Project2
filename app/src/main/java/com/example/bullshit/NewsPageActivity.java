package com.example.bullshit;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

public class NewsPageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_page);

        TextView newsTextView = findViewById(R.id.newsTextView);

        SharedPreferences prefs = getSharedPreferences("Announcements", MODE_PRIVATE);
        String announcementsJson = prefs.getString("allAnnouncements", "[]");

        // Display announcements
        StringBuilder newsBuilder = new StringBuilder();
        try {
            JSONArray announcementsArray = new JSONArray(announcementsJson);
            for (int i = 0; i < announcementsArray.length(); i++) {
                newsBuilder.append(announcementsArray.getString(i)).append("\n\n");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            newsBuilder.append("Error loading announcements!");
        }

        newsTextView.setText(newsBuilder.toString());
    }
}
