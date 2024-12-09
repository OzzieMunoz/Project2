import java.io.*;
import java.net.*;
import java.util.*;

public class GameLogic {
    private static final int PORT = 12345; // Server port
    private static final int MIN_PLAYERS = 3;
    private static final int MAX_PLAYERS = 7;
    private static List<Player> players = new ArrayList<>();
    private static Deck deck = new Deck();
    private static List<Card> pile = new ArrayList<>();
    private static int currentPlayerIndex = 0;

    public static void main(String[] args) {
        System.out.println("Server is running...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (players.size() < MAX_PLAYERS) {
                Socket socket = serverSocket.accept();
                Player player = new Player(socket);
                players.add(player);

                if (players.size() >= MIN_PLAYERS) {
                    System.out.println("Minimum players connected. Game can start.");
                }
            }
            new GameLogic().startGame();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startGame() throws IOException {
        deck.shuffle();
        distributeCards();

        while (true) {
            Player currentPlayer = players.get(currentPlayerIndex);
            currentPlayer.sendMessage("Your turn! Play a card or type 'pass'.");
            String action = currentPlayer.readMessage();

            if (action.equalsIgnoreCase("pass")) {
                nextPlayer();
                continue;
            }

            if (action.startsWith("play:")) {
                String[] parts = action.split(":");
                String cardName = parts[1];
                Card card = currentPlayer.playCard(cardName);

                if (card != null) {
                    pile.add(card);
                    broadcast(currentPlayer.getName() + " played a card.");
                    allowChallenges(currentPlayer);
                } else {
                    currentPlayer.sendMessage("Invalid card. Try again.");
                    continue;
                }
            }

            if (currentPlayer.getHandSize() == 0) {
                broadcast(currentPlayer.getName() + " has won the game!");
                break;
            }

            nextPlayer();
        }
    }

    private void allowChallenges(Player lastPlayer) throws IOException {
        broadcast("Any player can now call 'bluff' within 10 seconds.");

        Timer timer = new Timer();
        boolean[] bluffCalled = {false};
        Player[] challenger = {null};

        for (Player player : players) {
            if (player == lastPlayer) continue;
            new Thread(() -> {
                try {
                    String message = player.readMessage();
                    if (message.equalsIgnoreCase("bluff")) {
                        synchronized (bluffCalled) {
                            if (!bluffCalled[0]) {
                                bluffCalled[0] = true;
                                challenger[0] = player;
                                timer.cancel();
                            }
                        }
                    }
                } catch (IOException ignored) {
                }
            }).start();
        }

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (bluffCalled) {
                    if (!bluffCalled[0]) {
                        try {
                            broadcast("No one called bluff. The game continues.");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            handleBluff(challenger[0], lastPlayer);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, 10000);
    }
}

