package com.example.bullshit;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ManagePlayersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_players);

        RecyclerView playersRecyclerView = findViewById(R.id.playersRecyclerView);
        playersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        new Thread(() -> {
            AppDatabase db = MyApplication.getDatabase();

            // Log all users in the database
            List<User> allUsers = db.userDAO().getAllUsers();
            for (User user : allUsers) {
                System.out.println("User: " + user.getUsername() + ", isAdmin: " + user.isAdmin());
            }

            List<User> players = db.userDAO().getNonAdminUsers();
            System.out.println("Number of players fetched: " + players.size());

            runOnUiThread(() -> {
                PlayersAdapter adapter = new PlayersAdapter(players);
                playersRecyclerView.setAdapter(adapter);
            });
        }).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPlayers();
    }

    private void loadPlayers() {
        new Thread(() -> {
            AppDatabase db = MyApplication.getDatabase();
            List<User> players = db.userDAO().getNonAdminUsers();
            runOnUiThread(() -> {
                RecyclerView playersRecyclerView = findViewById(R.id.playersRecyclerView);
                PlayersAdapter adapter = new PlayersAdapter(players);
                playersRecyclerView.setAdapter(adapter);
            });
        }).start();
    }
}