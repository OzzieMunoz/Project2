package com.example.bullshit;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CardSelection extends RecyclerView.Adapter<CardSelection.ViewHolder> {
    private final List<Card> cards;
    private final List<Boolean> selections;
    private final List<Boolean> flippedCards;

    public CardSelection(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
        this.selections = new ArrayList<>();
        this.flippedCards = new ArrayList<>();
        for (int i = 0; i < cards.size(); i++) {
            selections.add(false);
            flippedCards.add(true); // Start with cards face up
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_selection_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Card card = cards.get(position);

        // Set card front details
        holder.cardText.setText(String.valueOf(card.getRank()));
        setCardColor(holder.cardFront, card.getSuit());

        // Handle card flip state
        if (flippedCards.get(position)) {
            holder.cardFront.setVisibility(View.VISIBLE);
            holder.cardBack.setVisibility(View.GONE);
        } else {
            holder.cardFront.setVisibility(View.GONE);
            holder.cardBack.setVisibility(View.VISIBLE);
        }

        // Handle selection state
        holder.checkBox.setChecked(selections.get(position));

        holder.itemView.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                selections.set(pos, !selections.get(pos));
                holder.checkBox.setChecked(selections.get(pos));
            }
        });

        // Add option to flip card on long press
        holder.itemView.setOnLongClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                flippedCards.set(pos, !flippedCards.get(pos));
                notifyItemChanged(pos);
            }
            return true;
        });
    }

    private void setCardColor(View cardView, String suit) {
        int color;
        switch (suit) {
            case "RED":
                color = Color.RED;
                break;
            case "YELLOW":
                color = Color.YELLOW;
                break;
            case "GREEN":
                color = Color.GREEN;
                break;
            case "BLUE":
                color = Color.BLUE;
                break;
            default:
                color = Color.BLACK;
        }
        cardView.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public void clearSelections() {
        for (int i = 0; i < selections.size(); i++) {
            selections.set(i, false);
        }
        notifyDataSetChanged();
    }

    public List<Card> getSelectedCards() {
        List<Card> selected = new ArrayList<>();
        for (int i = 0; i < cards.size(); i++) {
            if (selections.get(i)) {
                selected.add(cards.get(i));
            }
        }
        return selected;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView cardText;
        final CheckBox checkBox;
        final View cardFront;
        final ImageView cardBack;

        ViewHolder(View view) {
            super(view);
            cardText = view.findViewById(R.id.cardText);
            checkBox = view.findViewById(R.id.cardCheckBox);
            cardFront = view.findViewById(R.id.cardFront);
            cardBack = view.findViewById(R.id.cardBack);
        }
    }
}