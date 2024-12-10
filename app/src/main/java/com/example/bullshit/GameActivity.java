package com.example.bullshit;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
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
    private int cardsToPlay;
    private TextView turnTextView;
    private TextView cardHandTextView;
    private List<Card> playedPile;
    private List<CheckBox> cardCheckBoxes;
    private boolean isBotTurn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        turnTextView = findViewById(R.id.turnTextView);
        cardHandTextView = findViewById(R.id.cardHandTextView);
        playedPile = new ArrayList<>();
        cardsToPlay = 4;
        cardCheckBoxes = new ArrayList<>();

        userCards = (List<Card>) getIntent().getSerializableExtra("userCards");
        botCards = (List<Card>) getIntent().getSerializableExtra("botCards");

        if (userCards != null) {
            displayUserCards(userCards);
        } else {
            Toast.makeText(this, "No cards received", Toast.LENGTH_SHORT).show();
        }

        Button playCardButton = findViewById(R.id.playCardButton);
        playCardButton.setOnClickListener(v -> playCards());
    }

    private void playCards() {
        if (!isBotTurn) {
            int userPlayedCardCount = 0;
            for (CheckBox checkBox : cardCheckBoxes) {
                if (checkBox.isChecked()) {
                    userPlayedCardCount++;
                }
            }
            if (userPlayedCardCount < 1) {
                Toast.makeText(this, "Select at least 1 card to play.", Toast.LENGTH_SHORT).show();
            } else if (userPlayedCardCount > 4) {
                Toast.makeText(this, "You can only select up to 4 cards.", Toast.LENGTH_SHORT).show();
            } else {
                List<Card> cardsPlayed = new ArrayList<>();
                for (int i = 0; i < cardCheckBoxes.size(); i++) {
                    CheckBox checkBox = cardCheckBoxes.get(i);
                    if (checkBox.isChecked()) {
                        cardsPlayed.add(userCards.get(i));
                        checkBox.setVisibility(View.GONE);
                    }
                }
                if (!cardsPlayed.isEmpty()) {
                    userCards.removeAll(cardsPlayed);
                    playedPile.addAll(cardsPlayed);
                    displayUserCards(userCards);
                    int correctCardsPlayed = countCorrectCards(cardsPlayed);
                    Toast.makeText(this, "User placed " + correctCardsPlayed + " card(s) of " + cardNumberPlayed + "'s.", Toast.LENGTH_SHORT).show();
                    nextTurn();
                }
            }
        } else {
            botPlayCards();
        }
    }

    private void botPlayCards() {
        if (botCards.size() >= cardsToPlay) {
            List<Card> cardsPlayed = new ArrayList<>();

            for (int i = 0; i < cardsToPlay; i++) {
                cardsPlayed.add(botCards.get(i));
            }
            botCards.removeAll(cardsPlayed);
            playedPile.addAll(cardsPlayed);
            Toast.makeText(this, "Bot played " + cardsPlayed.size() + " " + cardNumberPlayed + "'s", Toast.LENGTH_LONG).show();
            nextTurn();
        } else {
            nextTurn();
        }
    }

    private void nextTurn() {
        turn++;
        cardNumberPlayed++;
        if (cardNumberPlayed > 13) {
            cardNumberPlayed = 1;
        }

        cardsToPlay = cardNumberPlayed;

        if (turn % 2 == 1) {
            isBotTurn = false;
            promptTurn();
        } else {
            isBotTurn = true;
            promptTurn();
            botPlayCards();
        }
    }

    private void promptTurn() {
        if (turn % 2 == 1) {
            turnTextView.setText("User turn: Place " + cardNumberPlayed + "'s");
        } else {
            turnTextView.setText("Bot turn: Place " + cardNumberPlayed + "'s");
        }
    }

    private void displayUserCards(List<Card> userCards) {
        StringBuilder sb = new StringBuilder("Your cards:\n");
        LinearLayout cardContainer = findViewById(R.id.cardContainer);
        cardContainer.removeAllViews();

        cardCheckBoxes.clear();

        for (int i = 0; i < userCards.size(); i++) {
            sb.append(userCards.get(i).toString()).append("\n");

            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(userCards.get(i).toString());
            checkBox.setId(View.generateViewId());
            checkBox.setTag(i);

            cardCheckBoxes.add(checkBox);
            cardContainer.addView(checkBox);
        }

        cardHandTextView.setText(sb.toString());
    }

    private int countCorrectCards(List<Card> cardsPlayed) {
        int correctCount = 0;
        for (Card card : cardsPlayed) {
            if (card.getNumber() == cardNumberPlayed) {
                correctCount++;
            }
        }
        return correctCount;
    }
}
