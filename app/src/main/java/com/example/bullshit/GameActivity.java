package com.example.bullshit;


import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;


public class GameActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);  // Set the correct layout file

        List<Card> userCards = (List<Card>) getIntent().getSerializableExtra("userCards");

        if (userCards != null) {
            displayUserCards(userCards);
        } else {
            Toast.makeText(this, "No cards received", Toast.LENGTH_SHORT).show();
        }
        setUpButtons();
    }

    private void displayUserCards(List<Card> userCards) {
        TextView cardHandTextView = findViewById(R.id.cardHandTextView);
        StringBuilder sb = new StringBuilder("Your cards:\n");
        for (Card card : userCards) {
            sb.append(card.toString()).append("\n");
        }
        cardHandTextView.setText(sb.toString());
    }


    private void setUpButtons() {
        findViewById(R.id.playCardButton).setOnClickListener(v -> {
            Toast.makeText(GameActivity.this, "Playing card...", Toast.LENGTH_SHORT).show();
        });


        findViewById(R.id.callBluffButton).setOnClickListener(v -> {
            Toast.makeText(GameActivity.this, "Calling bluff...", Toast.LENGTH_SHORT).show();
        });
    }
}





