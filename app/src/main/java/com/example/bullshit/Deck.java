package com.example.bullshit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck implements Serializable {
    private ArrayList<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        String[] colors = {"Red", "Blue", "Green", "Yellow"};
        for (String color : colors) {
            for (int i = 1; i <= 13; i++) {
                cards.add(new Card(i, color));
            }
        }
        Collections.shuffle(cards);
    }

    public List<List<Card>> dealCards(int playerCount, int cardCount) {
        List<List<Card>> playerCards = new ArrayList<>();
        for (int i = 0; i < playerCount; i++) {
            List<Card> playerHand = new ArrayList<>();
            for (int k = 0; k < cardCount; k++) {
                if (!cards.isEmpty()) {
                    playerHand.add(cards.remove(0));
                }
            }
            playerCards.add(playerHand);
        }
        return playerCards;
    }

    public List<Card> getRemainingCards() {
        return cards;
    }
}
