/*
package com.example.bullshit;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameRoomActivity extends AppCompatActivity {
    private TextView statusText;
    private LinearLayout playerHand;
    private TextView pileCount;
    private Button playButton, bluffButton, passButton;
    private GameClient gameClient;
    private boolean isMyTurn = false;
    private List<String> myCards = new ArrayList<>();
    private String playerName;
    private boolean gameStarted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_room);

        // Get player name from intent
        playerName = getIntent().getStringExtra("PLAYER_NAME");
        if (playerName == null) {
            playerName = "Player"; // Default name
        }

        initializeViews();
        setupGameClient();
        setupButtons();
        updateButtonStates(false);
    }

    private void initializeViews() {
        statusText = findViewById(R.id.statusText);
        playerHand = findViewById(R.id.playerHand);
        pileCount = findViewById(R.id.pileCount);
        playButton = findViewById(R.id.playButton);
        bluffButton = findViewById(R.id.bluffButton);
        passButton = findViewById(R.id.passButton);

        statusText.setText("Waiting for players...");
        pileCount.setText("0 cards in pile");
    }

    private void setupGameClient() {
        try {
            gameClient = new GameClient();
            gameClient.setPlayerName(playerName);

            new Thread(() -> {
                try {
                    while (true) {
                        String message = gameClient.readMessage();
                        if (message != null) {
                            handleGameMessage(message);
                        }
                    }
                } catch (IOException e) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Lost connection to server", Toast.LENGTH_LONG).show();
                        finish();
                    });
                }
            }).start();

        } catch (IOException e) {
            Toast.makeText(this, "Could not connect to game server", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void setupButtons() {
        playButton.setOnClickListener(v -> {
            if (isMyTurn) {
                showCardSelectionDialog();
            }
        });

        bluffButton.setOnClickListener(v -> {
            if (!isMyTurn && gameStarted) {
                try {
                    gameClient.sendMessage("BLUFF");
                    bluffButton.setEnabled(false);
                } catch (IOException e) {
                    Toast.makeText(this, "Failed to send bluff", Toast.LENGTH_SHORT).show();
                }
            }
        });

        passButton.setOnClickListener(v -> {
            if (isMyTurn) {
                try {
                    gameClient.sendMessage("PASS");
                    updateTurnStatus(false);
                } catch (IOException e) {
                    Toast.makeText(this, "Failed to pass turn", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void handleGameMessage(String message) {
        runOnUiThread(() -> {
            if (message.startsWith("CARDS:")) {
                String[] cards = message.substring(6).split(",");
                myCards = new ArrayList<>();
                for (String card : cards) {
                    myCards.add(card.trim());
                }
                displayCards();
                gameStarted = true;

            } else if (message.startsWith("TURN:")) {
                String player = message.substring(5);
                updateTurnStatus(player.equals(playerName));

            } else if (message.startsWith("PILE:")) {
                try {
                    int count = Integer.parseInt(message.substring(5));
                    updatePileCount(count);
                } catch (NumberFormatException e) {
                    // Handle invalid number format
                }

            } else if (message.startsWith("PLAYED:")) {
                String player = message.split(":")[1];
                statusText.setText(player + " played cards");
                bluffButton.setEnabled(true);

            } else if (message.startsWith("BLUFF_CALLED:")) {
                String[] parts = message.split(":");
                String caller = parts[1];
                String target = parts[2];
                statusText.setText(caller + " called bluff on " + target);

            } else if (message.startsWith("WINNER:")) {
                String winner = message.substring(7);
                showGameOverDialog(winner);
            }
        });
    }

    private void displayCards() {
        playerHand.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (String cardStr : myCards) {
            View cardView = inflater.inflate(R.layout.card_view, playerHand, false);
            TextView cardText = cardView.findViewById(R.id.cardText);
            cardText.setText(cardStr);
            playerHand.addView(cardView);
        }
    }

    private void updateTurnStatus(boolean isMyTurn) {
        this.isMyTurn = isMyTurn;
        updateButtonStates(isMyTurn);
        statusText.setText(isMyTurn ? "Your Turn" : "Waiting for other players...");
    }

    private void updateButtonStates(boolean enabled) {
        playButton.setEnabled(enabled);
        passButton.setEnabled(enabled);
        bluffButton.setEnabled(!enabled && gameStarted);
    }

    private void updatePileCount(int count) {
        pileCount.setText(count + " cards in pile");
    }

    private void showCardSelectionDialog() {
        if (!isMyTurn) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.card_selection, null);

        RecyclerView recyclerView = dialogView.findViewById(R.id.cardSelectionRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        CardSelection adapter = new CardSelection(myCards);
        recyclerView.setAdapter(adapter);

        AlertDialog dialog = builder.setView(dialogView).create();

        dialogView.findViewById(R.id.cancelButton).setOnClickListener(v -> {
            adapter.clearSelections();
            dialog.dismiss();
        });

        dialogView.findViewById(R.id.playSelectedButton).setOnClickListener(v -> {
            List<String> selectedCards = adapter.getSelectedCardStrings();
            if (!selectedCards.isEmpty()) {
                playSelectedCards(selectedCards);
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Please select at least one card", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void playSelectedCards(List<String> selectedCards) {
        if (gameClient != null) {
            try {
                StringBuilder playMessage = new StringBuilder("PLAY:");
                for (String card : selectedCards) {
                    playMessage.append(card).append(",");
                }
                playMessage.setLength(playMessage.length() - 1);
                gameClient.sendMessage(playMessage.toString());

                myCards.removeAll(selectedCards);
                displayCards();

            } catch (IOException e) {
                Toast.makeText(this, "Failed to play cards", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showGameOverDialog(String winner) {
        new AlertDialog.Builder(this)
                .setTitle("Game Over")
                .setMessage(winner + " has won the game!")
                .setPositiveButton("Play Again", (dialog, which) -> recreate())
                .setNegativeButton("Exit", (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gameClient != null) {
            try {
                gameClient.sendMessage("QUIT");
                gameClient = null;
            } catch (IOException e) {
                // Ignore closing errors
            }
        }
    }
}
 */