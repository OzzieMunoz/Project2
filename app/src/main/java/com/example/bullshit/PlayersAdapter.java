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

        // Log binding
        System.out.println("Binding player: " + player.getUsername());

        holder.usernameTextView.setText(player.getUsername());

        holder.banButton.setOnClickListener(v -> {
            Toast.makeText(holder.itemView.getContext(),
                    "Banned " + player.getUsername(),
                    Toast.LENGTH_SHORT).show();

            // Remove the player from the list
            players.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, players.size());
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
