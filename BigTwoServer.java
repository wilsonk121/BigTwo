import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BigTwoServer implements CardGame {
    // private member variables
    private static final int PORT = 12345; // Port for the server
    private int numOfPlayers;
    private int currentPlayerIdx;
    private Deck deck;
    private ArrayList<CardGamePlayer> playerList;
    private ArrayList<Hand> handsOnTable;
    private BigTwoClient ui;//no need this variable

    private List<ClientHandler> clientHandlerArrayList;

    // Constructors
    public BigTwoServer() {
        numOfPlayers = 4;
        deck = new BigTwoDeck();
        playerList = new ArrayList<CardGamePlayer>();
        for (int i = 0; i < numOfPlayers; i++) {
            playerList.add(new CardGamePlayer());
        }
        handsOnTable = new ArrayList<Hand>();
        clientHandlerArrayList = new ArrayList<ClientHandler>();

    }

    // public methods
    public int getNumOfPlayers() // done
    {
        return numOfPlayers;
    }

    public Deck getDeck() // done
    {
        return deck;
    }

    public ArrayList<CardGamePlayer> getPlayerList() // done
    {
        return playerList;
    }

    public ArrayList<Hand> getHandsOnTable() // done
    {
        return handsOnTable;
    }

    public int getCurrentPlayerIdx() // done
    {
        return currentPlayerIdx;
    }

    public void start(Deck deck) {
        // remove card from player
        for (CardGamePlayer p : playerList) {
            p.removeAllCards();
        }



        // distribute the shuffled deck to the players
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 13; j++) {
                playerList.get(i).addCard(deck.getCard(i * 13 + j));
            }
            playerList.get(i).sortCardsInHand();
        }

        // identify the player with the three of diamond
        CardGamePlayer starter = new CardGamePlayer();
        for (CardGamePlayer p : playerList) {
            if (p.getCardsInHand().contains(new BigTwoCard(0, 2))) {
                starter = p;
            }
        }
        // set both the currentPlayerIdx of the BigTwo object and the activePlayer of
        // the BigTwoUI object to the index of the player who holds the Three of
        // Diamonds
        currentPlayerIdx = playerList.indexOf(starter);


        // call the repaint() method of the BigTwoUI object to show the cards on the
        // table
        repaint();
        // call the promptActivePlayer() method of the BigTwoUI object to prompt user to
        // select cards and make his/her move

        promptActivePlayer(currentPlayerIdx, playerList.get(currentPlayerIdx).getCardsInHand().size());

    }

    public void makeMove(int playerIdx, int[] cardIdx) {
        checkMove(playerIdx, cardIdx);
    }

    public void checkMove(int playerIdx, int[] cardIdx) {
        // create a CardList from the cardIdx
        Hand playerHand;
        if (cardIdx == null) {
            // The player did not enter any card, the player choose to pass
            playerHand = new Pass(playerList.get(playerIdx), new CardList());
        } else {
            // Form a hand with the player choosen hand
            CardList playerAttempt = new CardList();
            for (int c : cardIdx) {
                playerAttempt.addCard(playerList.get(playerIdx).getCardsInHand().getCard(c));
            }
            playerHand = composeHand(playerList.get(playerIdx), playerAttempt);
        }

        // flag is a control variable to check if the hand inputted by the player is a
        // valid move
        boolean flag = true;

        if (playerHand == null) {
            // No valid hand can be composed from the player input, obvisouly an invalid
            // move

            checkMoveMessage("Not a Legal Move!!! (Not a valid hand)");
            flag = false;
        } else {

            if (handsOnTable.isEmpty()) {
                // This player is the first to make a move
                // Make a hand that must contains the three of Diamond
                if (playerHand.getType() == "Pass") {

                    checkMoveMessage("Not a Legal Move!!! (First Player Cannot Pass)");
                    flag = false;
                } else if (!playerHand.contains(new BigTwoCard(0, 2))) {

                    checkMoveMessage("Not a Legal Move!!! (Move By First Player Must Contain Three of Diamond)");
                    flag = false;
                }
            } else {
                // if player is NOT the first to make a move
                if (handsOnTable.get(handsOnTable.size() - 1).getPlayer() == playerList.get(currentPlayerIdx)) {
                    // This player is 'biggest', he can make any hand he want
                    flag = true;
                } else if (playerHand.size() != handsOnTable.get(handsOnTable.size() - 1).size()
                        && playerHand.getType() != "Pass") {

                    checkMoveMessage("Not a Legal Move!!! (The hand made must be the same number of card as the hand on table)");
                    flag = false;
                } else {
                    if (!playerHand.beats(handsOnTable.get(handsOnTable.size() - 1))) {

                        checkMoveMessage("Not a Legal Move!!! (Does not beat the hand in table)");
                        flag = false;
                    }
                }
                // check if the player who passed is the last player to make a valid hand
                if (playerHand.getType() == "Pass"
                        && handsOnTable.get(handsOnTable.size() - 1).getPlayer() == playerList.get(currentPlayerIdx)) {

                    checkMoveMessage("Not a Legal Move!!! (This player passed two times)");
                    flag = false;
                }
            }
        }

        if (flag == true) {
            // the player move is valid
            //Print the player's move to the screen
            if (playerHand.getType() != "Pass") {
                //System.out.print("{" + playerHand.getType() + "} ");
                //playerHand.print(true, false);
            } else {
                //System.out.println("{" + playerHand.getType() + "} ");
            }

            //Add the player's move to HandsOnTable if it is not pass
            if (playerHand.getType() != "Pass") {
                handsOnTable.add(playerHand);
                playerList.get(currentPlayerIdx).removeCards(playerHand);
            }
        } else {
            // promot the player to enter again
            promptActivePlayer(currentPlayerIdx, playerList.get(currentPlayerIdx).getCardsInHand().size());
        }
    }

    public boolean endOfGame() {
        for (CardGamePlayer p : playerList) {
            if (p.getCardsInHand().size() == 0)
                return true;
        }
        return false;
    }

    public void endOfGameDisplay() {
        System.out.println("");
        // print the cardlist of each player displaying all their cards
        for (int i = 0; i < playerList.size(); i++) {
            CardGamePlayer player = playerList.get(i);
            String name = player.getName();
            System.out.println("<" + name + ">");
            System.out.print("    ");
            player.getCardsInHand().print(true, true);
        }
        // print the table
        System.out.println("<Table>");
        Hand lastHandOnTable = (handsOnTable.isEmpty()) ? null : handsOnTable.get(handsOnTable.size() - 1);
        if (lastHandOnTable != null) {
            System.out
                    .print("    <" + lastHandOnTable.getPlayer().getName() + "> {" + lastHandOnTable.getType() + "} ");
            lastHandOnTable.print(true, false);
        } else {
            System.out.println("  [Empty]");
        }

        // print number of cards left by each player
        System.out.println("");
        System.out.println("Game Ends");
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).getNumOfCards() == 0)
                System.out.printf("Player %s wins the game.", i).println();
            else
                System.out.printf("Player %s has %s cards in hand.", i, playerList.get(i).getNumOfCards()).println();

        }
    }



    public void promptActivePlayer(int playerIdx, int size){
        System.out.println(playerIdx + "'s turn: ");
        int[] cardIdx = clientHandlerArrayList.get(playerIdx).getPlayerAction(size);
        this.makeMove(currentPlayerIdx, cardIdx);


    }
    public void repaint(){
        for (ClientHandler item:clientHandlerArrayList){
            item.repaint();

        }

    }

    public void checkMoveMessage(String msg){
        clientHandlerArrayList.get(currentPlayerIdx).message(msg);

    }

    public static Hand composeHand(CardGamePlayer player, CardList cards) {

        Single trySingle = new Single(player, cards);
        Pair tryPair = new Pair(player, cards);
        Triple tryTriple = new Triple(player, cards);
        Straight tryStraight = new Straight(player, cards);
        Flush tryFlush = new Flush(player, cards);
        FullHouse tryFullHouse = new FullHouse(player, cards);
        Quad tryQuad = new Quad(player, cards);
        StraightFlush tryStraightFlush = new StraightFlush(player, cards);
        Pass tryPass = new Pass(player, cards);

        if (tryPass.isValid())
            return tryPass;
        if (tryStraightFlush.isValid())
            return tryStraightFlush;
        if (tryQuad.isValid())
            return tryQuad;
        if (tryFullHouse.isValid())
            return tryFullHouse;
        if (tryFlush.isValid())
            return tryFlush;
        if (tryStraight.isValid())
            return tryStraight;
        if (tryTriple.isValid())
            return tryTriple;
        if (tryPair.isValid())
            return tryPair;
        if (trySingle.isValid())
            return trySingle;

        return null;
    }
    public static void main(String[] args) {

        BigTwoServer currentGame = new BigTwoServer();
        BigTwoDeck currentDeck = new BigTwoDeck();
        currentDeck.initailize();
        currentDeck.shuffle();
        //wait till 4 player
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running and waiting for clients...");
            int numOfPlayer = 0;

            while (true) {
                Socket clientSocket = serverSocket.accept(); // Accept a client connection
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Handle the new client connection in a separate thread
                ClientHandler clientHandler = new ClientHandler(clientSocket, currentGame, currentGame.playerList.get(numOfPlayer));
                currentGame.clientHandlerArrayList.add(clientHandler);
                numOfPlayer++;

                if (numOfPlayer == 4) { // Start the game when 3 players are connected
                    break;
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }



        currentGame.start(currentDeck);

        // send everyone their card
        while (!currentGame.endOfGame()) {
            currentGame.currentPlayerIdx = (currentGame.currentPlayerIdx + 1) % 4;


            System.out.println("");
            currentGame.repaint();
            currentGame.promptActivePlayer(currentGame.currentPlayerIdx, currentGame.playerList.get(currentGame.currentPlayerIdx).getCardsInHand().size());

        }
        currentGame.endOfGameDisplay();

    }
}
