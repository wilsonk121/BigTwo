import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;


public class ClientHandler extends Thread {

    private final static int MAX_CARD_NUM = 13; // max. no. of cards each player holds
    private boolean[] selected = new boolean[MAX_CARD_NUM]; // selected cards


    private BigTwoServer server = null; // a BigTwo object
    private ArrayList<Hand> handsOnTable; // the list of hands played on the
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    private int activePlayer = -1; // the index of the active player
    private ArrayList<CardGamePlayer> playerList; // the list of players
    private CardGamePlayer player;



    public ClientHandler(Socket socket, BigTwoServer server, CardGamePlayer player) {
        this.socket = socket; // Store the Client instance
        this.server = server; // Store the Server instance
        this.player = player;
        handsOnTable = server.getHandsOnTable();
        playerList = server.getPlayerList();

    }

    public void repaint() {

        try {
            // Set up input and output streams if not already done
            if (in == null) {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // client reply
            }
            if (out == null) {
                out = new PrintWriter(socket.getOutputStream(), true); // response to client
            }

            out.println("----------------------------------------------------");
            out.println("<Your Card is>");
            out.println(player.getCardsInHand());

            out.println("<Table>");
            Hand lastHandOnTable = (handsOnTable.isEmpty()) ? null : handsOnTable.get(handsOnTable.size() - 1);
            if (lastHandOnTable != null) {
                out.print("    <" + lastHandOnTable.getPlayer().getName() + "> {" + lastHandOnTable.getType() + "} ");
                lastHandOnTable.print(true, false);
                out.println(lastHandOnTable);
                lastHandOnTable.print(true, false);
            } else {
                out.println("[Empty]");
            }

        } catch (IOException e) {
            e.printStackTrace(); // Log the error
        }

    }

    public int[] getPlayerAction(int size) {

        String Action = null;
        try {
            // Set up input and output streams if not already done
            if (in == null) {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream())); // client reply
            }
            if (out == null) {
                out = new PrintWriter(socket.getOutputStream(), true); // response to client
            }

            // Loop until a valid player name is received
            while (Action == null || Action.trim().isEmpty()) {
                out.println("Enter your action:");
                Action = in.readLine(); // Read the name from the client

                // Validate player name
                if (Action == null || Action.trim().isEmpty()) {
                    out.println("Invalid name. Please enter a valid action");
                }
            }

        } catch (IOException e) {
            e.printStackTrace(); // Log the error
        }


        int[] cardIdx=StringToIntList(Action, size);
        resetSelected();

        return cardIdx; // Return the valid player name


    }

    public void message(String msg) {

        try {
            // Set up input and output streams if not already done
            if (out == null) {
                out = new PrintWriter(socket.getOutputStream(), true);
            }

            out.println(msg);

        } catch (IOException e) {
            e.printStackTrace(); // Log the error
        }

    }

    private int[] StringToIntList(String input, int size) {

        StringTokenizer st = new StringTokenizer(input);
        while (st.hasMoreTokens()) {
            try {
                int idx = Integer.parseInt(st.nextToken());
                if (idx >= 0 && idx < MAX_CARD_NUM && idx < size) {
                    selected[idx] = true;
                }
            } catch (Exception e) {
                // e.printStackTrace();
            }
        }

        int[] cardIdx = null;
        int count = 0;
        for (int j = 0; j < selected.length; j++) {
            if (selected[j]) {
                count++;
            }
        }

        if (count != 0) {
            cardIdx = new int[count];
            count = 0;
            for (int j = 0; j < selected.length; j++) {
                if (selected[j]) {
                    cardIdx[count] = j;
                    count++;
                }
            }
        }
        return cardIdx;
    }

    private void resetSelected() {
        for (int j = 0; j < selected.length; j++) {
            selected[j] = false;
        }
    }



}