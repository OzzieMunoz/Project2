package com.example.bullshit;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class NewsPageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_page);

        TextView newsTextView = findViewById(R.id.newsTextView);

        // Sample notifications
        ArrayList<String> notifications = new ArrayList<>();
        notifications.add("Small Update / Patch Notes");
        notifications.add("Update 1.0.1");

        // Display the notifications
        StringBuilder newsBuilder = new StringBuilder();
        for (String notification : notifications) {
            newsBuilder.append(notification).append("\n\n");
        }

        newsTextView.setText(newsBuilder.toString());
    }
}
