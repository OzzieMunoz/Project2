package com.example.bullshit;


import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class GameActivity extends AppCompatActivity {
    private List<Card> userCards;
    private List<Card> botCards;
    private int turn = 1;
    private int cardNumberPlayed = 1;
    private TextView turnTextView;
    private TextView playedCardsTextView;
    private List<Card> playedPile;
    private int cardsToPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);  // Set the correct layout file

        turnTextView = findViewById(R.id.turnTextView);
        playedPile = new ArrayList<>();
        cardsToPlay = 1;
        userCards = (List<Card>) getIntent().getSerializableExtra("userCards");
        botCards = (List<Card>) getIntent().getSerializableExtra("botCards");

        if (userCards != null) {
            displayUserCards(userCards);
        } else {
            Toast.makeText(this, "No cards received", Toast.LENGTH_SHORT).show();
        }
    }

    private void playCards() {

    }

    private void displayUserCards(List<Card> userCards) {
        TextView cardHandTextView = findViewById(R.id.cardHandTextView);
        StringBuilder sb = new StringBuilder("Your cards:\n");
        for (Card card : userCards) {
            sb.append(card.toString()).append("\n");
        }
        cardHandTextView.setText(sb.toString());
    }

    private void promptTurn() {
        if (turn % 2 == 1) {
            turnTextView.setText("User turn: Place " + (cardNumberPlayed + 1) / 2 + "'s") ;
        } else {
            turnTextView.setText("Bot turn: Place " + (cardNumberPlayed / 2) + "'s");
        }
    }

    private void nextTurn() {
        turn++;
        if (turn % 2 == 1) {
            cardNumberPlayed++;
        }
        promptTurn();
    }
}





