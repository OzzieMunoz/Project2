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

    public List<List<Card>> dealCards() {
        List<List<Card>> playerCards = new ArrayList<>();
        int half = cards.size()/2;
        List<Card> userHand = cards.subList(0, half);
        List<Card> botHand = cards.subList(half, cards.size());
        playerCards.add(userHand);
        playerCards.add(botHand);
        return playerCards;
    }
}
