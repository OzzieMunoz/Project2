package com.example.bullshit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PlayersAdapter extends RecyclerView.Adapter<PlayersAdapter.PlayerViewHolder> {

    private final List<User> players;

    public PlayersAdapter(List<User> players) {
        this.players = players;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.player_item, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        User player = players.get(position);

        holder.usernameTextView.setText(player.getUsername());

        holder.banButton.setText(player.isBanned() ? "Unban" : "Ban");

        holder.banButton.setOnClickListener(v -> {
            boolean isCurrentlyBanned = player.isBanned();
            player.setBanned(!isCurrentlyBanned);

            new Thread(() -> {
                AppDatabase db = MyApplication.getDatabase();
                db.userDAO().updateUser(player);
            }).start();

            holder.banButton.setText(player.isBanned() ? "Unban" : "Ban");

            Toast.makeText(holder.itemView.getContext(),
                    (player.isBanned() ? "Banned " : "Unbanned ") + player.getUsername(),
                    Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    static class PlayerViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        Button banButton;

        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.usernameTextView);
            banButton = itemView.findViewById(R.id.banButton);
        }
    }
}
