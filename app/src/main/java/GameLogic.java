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
                    System.out.println("Minimum players have connected. Game can start.");
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
                    broadcast(currentPlayer.getName() + " has played a card.");
                    allowChallenges(currentPlayer);
                } else {
                    currentPlayer.sendMessage("Invalid card. please try again.");
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

    private void handleBluff(Player challenger, Player lastPlayer) throws IOException {
        broadcast(challenger.getName() + " called bluff on " + lastPlayer.getName());

        if (lastPlayer.isBluffing()) {
            lastPlayer.addToHand(pile);
            pile.clear();
            broadcast(lastPlayer.getName() + " was bluffing and picked up the pile!");
        } else {
            challenger.addToHand(pile);
            pile.clear();
            broadcast(challenger.getName() + " was wrong and picked up the pile!");
        }
    }

    private void distributeCards() {
        int cardsPerPlayer = deck.size() / players.size();
        for (Player player : players) {
            player.setHand(deck.draw(cardsPerPlayer));
        }
    }

    private void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    private void broadcast(String message) throws IOException {
        for (Player player : players) {
            player.sendMessage(message);
        }
    }
}

class Player {
    private String name;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private List<Card> hand;

    public Player(Socket socket) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.name = in.readLine();
    }

    public String getName() {
        return name;
    }

    public void setHand(List<Card> hand) {
        this.hand = hand;
    }

    public int getHandSize() {
        return hand.size();
    }

    public void addToHand(List<Card> cards) {
        hand.addAll(cards);
    }

    public Card playCard(String cardName) {
        for (Card card : hand) {
            if (card.toString().equalsIgnoreCase(cardName)) {
                hand.remove(card);
                return card;
            }
        }
        return null;
    }

    public boolean isBluffing() {
        return Math.random() > 0.5;
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public String readMessage() throws IOException {
        return in.readLine();
    }
}

class Deck {
    private List<Card> cards = new ArrayList<>();

    public Deck() {
        String[] suits = {"Red", "Blue", "Green", "Yellow"};
        String[] ranks = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"};
        for (String suit : suits) {
            for (String rank : ranks) {
                cards.add(new Card(rank, suit));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public List<Card> draw(int count) {
        List<Card> hand = new ArrayList<>(cards.subList(0, count));
        cards.subList(0, count).clear();
        return hand;
    }

    public int size() {
        return cards.size();
    }
}

class Card {
    private String rank;
    private String suit;

    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}
