package com.example.bullshit;

import java.util.ArrayList;

public class Deck {

    public Deck() {
        ArrayList<Card> cards = new ArrayList<>();
        String[] colors = {"Red", "Blue", "Green", "Yellow"};
        for (String color : colors) {
            for (int i = 1; i <= 13; i++) {
                cards.add(new Card(i, color));
            }
        }
    }
}
