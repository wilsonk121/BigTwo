import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.net.Socket;


/**
 * This class is used for modeling a user interface for the Big Two card game.
 */
public class BigTwoClient {

    private static final char[] SUITS = new char[]{'♦', '♣', '♥', '♠'};

    private final static int MAX_CARD_NUM = 13; // max. no. of cards each player holds
    private BigTwoServer game = null; // a BigTwo object
    private ArrayList<CardGamePlayer> playerList; // the list of players
    private ArrayList<Hand> handsOnTable; // the list of hands played on the
    private Scanner scanner; // the scanner for reading user in put
    private int activePlayer = -1; // the index of the active player
    private boolean[] selected = new boolean[MAX_CARD_NUM]; // selected cards

    private static final String SERVER_ADDRESS = "localhost"; // Change if needed
    private static final int SERVER_PORT = 12345; // Port must match the server's port

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;




    public BigTwoClient() {
        scanner = new Scanner(System.in);

        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT); // Connect to the server
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Connected to the server.");
            System.out.println("♦♣♥♠");
            startInteraction();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startInteraction() {
        String response = null;
        String action = null;
        try {
            while(true){
                response = in.readLine(); // Read server's response
                if(response != null){
                    System.out.println(response);
                    if(response.contains("Enter your action:")){
                        action = scanner.nextLine(); // Send player name to the server
                        out.println(action);
                    }


                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }



    public static void main(String[] args) {
        new BigTwoClient(); // Start the client
    }





}

