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

public class CreateAnnouncementActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_announcement);

        EditText announcementEditText = findViewById(R.id.announcementEditText);
        Button postAnnouncementButton = findViewById(R.id.postAnnouncementButton);

        postAnnouncementButton.setOnClickListener(v -> {
            String announcement = announcementEditText.getText().toString();

            if (!announcement.isEmpty()) {
                // Get the current date
                String date = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(new Date());
                String announcementWithDate = date + " - " + announcement;

                SharedPreferences prefs = getSharedPreferences("Announcements", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                String announcementsJson = prefs.getString("allAnnouncements", "[]");
                try {
                    JSONArray announcementsArray = new JSONArray(announcementsJson);
                    announcementsArray.put(announcementWithDate);

                    editor.putString("allAnnouncements", announcementsArray.toString());
                    editor.apply();

                    Toast.makeText(this, "Announcement posted!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(CreateAnnouncementActivity.this, AdminPanelActivity.class);
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error saving announcement!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please write an announcement before posting.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
