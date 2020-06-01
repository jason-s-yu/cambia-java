package io.jasonyu.cambia;

import io.jasonyu.cambia.network.Session;
import io.jasonyu.cambia.util.PrettyPrinter;

import java.io.Console;
import java.io.IOException;
import java.net.ProtocolException;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class Game {
    /**
     * The game history is a string that contains all actions as 3 letter "action groups".
     * The first character represents the action (d for draw, b for burn, c for cambia, p for play/discard)
     */
    private String history;

    /**
     * The dealer deck contains the draw pile
     */
    private Deck dealer;

    /**
     * The discard deck is the discard pile
     */
    private Deck discard;

    // player hands
    private Player p1;
    private Player p2;

    /**
     * turn state - true means p1 has the floor; false means p2 has the floor
     */
    private boolean p1Turn;

    /**
     * game state - stores the player # who called cambia. (player 1 = 1, player 2 = 2, not called yet = 0)
     */
    private int cambia;

    private int totalTurns;

    public Game() throws InterruptedException {
        this.totalTurns = 0;
        this.history = "";
        this.cambia = 0;

        this.discard = new Deck();
        this.dealer = new Deck(Card.values());

        this.p1Turn = ThreadLocalRandom.current().nextInt(1, 3) == 1;

        this.p1 = new Player();
        this.p2 = new Player();

        this.dealAll();

        this.viewStartingCards();
        this.run();
    }

    /**
     * Allow players to view their cards. Only executed once at the start of the game.
     */
    private void viewStartingCards() throws InterruptedException {
        PrettyPrinter.clearTerminal();
        Scanner in = new Scanner(System.in);
        for (int i = 1; i < 3; i++) {
            Player p;
            if (i == 1) {
                p = this.p1;
            } else p = this.p2;
            System.out.println("Viewing player " + i + "'s cards. Press ENTER to reveal...");
            in.nextLine();

            System.out.println("Card D: " + p.getHand().view("d").pretty() + "\t\t\tCard E: " + p.getHand().view("e").pretty() + "\n");

            System.out.print("Press ENTER when done...");
            in.nextLine();

            PrettyPrinter.clearTerminal();
        }
    }

    /**
     * Run the game process after both players have viewed their two starting cards.
     */
    public void run() throws InterruptedException {
        String player1Greeting = "  _____   _                             __ \n" +
                " |  __ \\ | |                           /_ |\n" +
                " | |__) || |  __ _  _   _   ___  _ __   | |\n" +
                " |  ___/ | | / _` || | | | / _ \\| '__|  | |\n" +
                " | |     | || (_| || |_| ||  __/| |     | |\n" +
                " |_|     |_| \\__,_| \\__, | \\___||_|     |_|\n" +
                "                     __/ |                 \n" +
                "                    |___/                  ";
        String player2Greeting = "  _____   _                             ___  \n" +
                " |  __ \\ | |                           |__ \\ \n" +
                " | |__) || |  __ _  _   _   ___  _ __     ) |\n" +
                " |  ___/ | | / _` || | | | / _ \\| '__|   / / \n" +
                " | |     | || (_| || |_| ||  __/| |     / /_ \n" +
                " |_|     |_| \\__,_| \\__, | \\___||_|    |____|\n" +
                "                     __/ |                   \n" +
                "                    |___/                    ";
        Scanner in = new Scanner(System.in);

        boolean misplay = false;

        while (cambia < 2) {
            // see if cambia has been called already
            if (cambia == 1 && !misplay) {
                cambia ++;
                // increment so the game continues for the last time, then conclude the game.
            } else if (misplay) misplay = false;
            int playerIndex = p1Turn ? 1 : 2;
            if (p1Turn) {
                System.out.println(player1Greeting);
            } else System.out.println(player2Greeting);
            System.out.println("\n");

            System.out.println("Opponent's cards:");
            if (playerIndex == 1) {
                // show player 2's cards north of current player's cards. Print inversely because from current player's perspective they are inverted
                System.out.println((p2.getHand().d == Card.EMPTY_CARD ? " " : "D") + "\t\t" + (p2.getHand().e == Card.EMPTY_CARD ? " " : "E") + "\t\t" + (p2.getHand().f == Card.EMPTY_CARD ? " " : "F"));
                System.out.println((p2.getHand().a == Card.EMPTY_CARD ? " " : "A") + "\t\t" + (p2.getHand().b == Card.EMPTY_CARD ? " " : "B") + "\t\t" + (p2.getHand().c == Card.EMPTY_CARD ? " " : "C"));
            } else if (playerIndex == 2) {
                System.out.println((p1.getHand().d == Card.EMPTY_CARD ? " " : "D") + "\t\t" + (p1.getHand().e == Card.EMPTY_CARD ? " " : "E") + "\t\t" + (p1.getHand().f == Card.EMPTY_CARD ? " " : "F"));
                System.out.println((p1.getHand().a == Card.EMPTY_CARD ? " " : "A") + "\t\t" + (p1.getHand().b == Card.EMPTY_CARD ? " " : "B") + "\t\t" + (p1.getHand().c == Card.EMPTY_CARD ? " " : "C"));
            }

            System.out.println("\nYour Cards:");
            if (playerIndex == 2) {
                System.out.println((p2.getHand().a == Card.EMPTY_CARD ? " " : "A") + "\t\t" + (p2.getHand().b == Card.EMPTY_CARD ? " " : "B") + "\t\t" + (p2.getHand().c == Card.EMPTY_CARD ? " " : "C"));
                System.out.println((p2.getHand().d == Card.EMPTY_CARD ? " " : "D") + "\t\t" + (p2.getHand().e == Card.EMPTY_CARD ? " " : "E") + "\t\t" + (p2.getHand().f == Card.EMPTY_CARD ? " " : "F"));
            } else if (playerIndex == 1) {
                System.out.println((p1.getHand().a == Card.EMPTY_CARD ? " " : "A") + "\t\t" + (p1.getHand().b == Card.EMPTY_CARD ? " " : "B") + "\t\t" + (p1.getHand().c == Card.EMPTY_CARD ? " " : "C"));
                System.out.println((p1.getHand().d == Card.EMPTY_CARD ? " " : "D") + "\t\t" + (p1.getHand().e == Card.EMPTY_CARD ? " " : "E") + "\t\t" + (p1.getHand().f == Card.EMPTY_CARD ? " " : "F"));
            }

            Card discardTop = discard.top();
            System.out.println("\n" + PrettyPrinter.GREEN + "Last played card: " + (discardTop == Card.EMPTY_CARD ? "none" : discardTop.pretty()) + PrettyPrinter.RESET);

            System.out.println("\nMoves available: draw" + (this.discard.hasNext() ? ", burn <position>" : "") + ((this.totalTurns > 2 && cambia < 2) ? ", cambia" : ""));

            String cmd = in.nextLine();
            if (cmd.equalsIgnoreCase("draw")) {
                Card drawn = this.dealer.pop();
                if (drawn.rank == 0) {
                    // joker
                    System.out.println("You drew a " + (drawn.red ? "RED" : "BLACK") + " JOKER");
                } else {
                    System.out.println("You drew a " + (drawn.red ? "RED" : "BLACK") + " " + drawn.rankString.toUpperCase() + " of " + drawn.suitString + " (" + drawn.toString() + ")");
                }

                boolean validCmd = false;
                boolean didReplacement = false;

                while (!validCmd && !didReplacement) {
                    System.out.println("\nMoves available: discard, replace <position>");
                    cmd = in.nextLine();

                    if (cmd.toLowerCase().startsWith("discard")) {
                        this.discard.push(drawn);
                        System.out.println("You discarded a " + drawn.pretty());

                        if (drawn == Card.C7 || drawn == Card.D7 || drawn == Card.H7 || drawn == Card.S7 || drawn == Card.C8 || drawn == Card.D8 || drawn == Card.H8 || drawn == Card.S8) {
                            boolean validLookCard = false;
                            while (!validLookCard) {
                                System.out.println("Type the index of the card in your hand you wish to view.");
                                String pos = in.nextLine().toLowerCase();

                                if (!"abcdef".contains(pos)) {
                                    System.out.println("Invalid position. Try again.");
                                    continue;
                                }
                                validLookCard = this.handleViewOwn(pos);
                            }

                            validCmd = true;
                            didReplacement = true;
                            continue;
                        }

                        validCmd = true;
                        didReplacement = true;
                        Thread.sleep(1200);
                        continue;
                    }

                    String arg;
                    try {
                        arg = cmd.split(" ")[1];
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("You must specify a card.");
                        continue;
                    }

                    if (cmd.toLowerCase().startsWith("replace") && "abcdef".contains(arg.toLowerCase())) {
                        handleReplace(drawn, arg);
                    } else {
                        System.out.println("You must specify a valid card in your hand to replace.");
                        continue;
                    }
                    validCmd = true;
                }
            } else if (cmd.toLowerCase().startsWith("burn") && this.discard.hasNext()) {
                boolean validCmd = false;

                while (!validCmd) {
                    String arg;
                    try {
                        arg = cmd.split(" ")[1];
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("You must specify a card in your hand to burn. You are now in burn-mode and must burn a valid card.");
                        cmd = in.nextLine();
                        continue;
                    }
                    if (!this.handleBurn(arg)) {
                        System.out.println("You must select a valid card to burn. You cannot burn an empty card. You are now in burn-mode and must burn a valid card.");
                        cmd = in.nextLine();
                        continue;
                    }
                    validCmd = true;
                }
            } else if (cmd.equalsIgnoreCase("cambia") && this.totalTurns > 2 && cambia < 2) {
                cambia ++;
                this.totalTurns ++;
                this.endTurn();
                PrettyPrinter.clearTerminal();
                continue;
            } else {
                misplay = true;
                PrettyPrinter.clearTerminal();
                continue;
            }

            this.totalTurns ++;
            this.endTurn();
            PrettyPrinter.clearTerminal();
        }

        int p1Score = this.p1.getScore();
        int p2Score = this.p2.getScore();
        if (p1Score < p2Score) {
            System.out.println("  _____   _                             __  __          __ _____  _   _   _____ \n" +
                    " |  __ \\ | |                           /_ | \\ \\        / /|_   _|| \\ | | / ____|\n" +
                    " | |__) || |  __ _  _   _   ___  _ __   | |  \\ \\  /\\  / /   | |  |  \\| || (___  \n" +
                    " |  ___/ | | / _` || | | | / _ \\| '__|  | |   \\ \\/  \\/ /    | |  | . ` | \\___ \\ \n" +
                    " | |     | || (_| || |_| ||  __/| |     | |    \\  /\\  /    _| |_ | |\\  | ____) |\n" +
                    " |_|     |_| \\__,_| \\__, | \\___||_|     |_|     \\/  \\/    |_____||_| \\_||_____/ \n" +
                    "                     __/ |                                                      \n" +
                    "                    |___/                                                       ");
        } else if (p1Score > p2Score) {
            System.out.println("  _____   _                             ___   __          __ _____  _   _   _____ \n" +
                    " |  __ \\ | |                           |__ \\  \\ \\        / /|_   _|| \\ | | / ____|\n" +
                    " | |__) || |  __ _  _   _   ___  _ __     ) |  \\ \\  /\\  / /   | |  |  \\| || (___  \n" +
                    " |  ___/ | | / _` || | | | / _ \\| '__|   / /    \\ \\/  \\/ /    | |  | . ` | \\___ \\ \n" +
                    " | |     | || (_| || |_| ||  __/| |     / /_     \\  /\\  /    _| |_ | |\\  | ____) |\n" +
                    " |_|     |_| \\__,_| \\__, | \\___||_|    |____|     \\/  \\/    |_____||_| \\_||_____/ \n" +
                    "                     __/ |                                                        \n" +
                    "                    |___/                                                         ");
        } else {
            System.out.println("  _____   _____        __          __\n" +
                    " |  __ \\ |  __ \\     /\\\\ \\        / /\n" +
                    " | |  | || |__) |   /  \\\\ \\  /\\  / / \n" +
                    " | |  | ||  _  /   / /\\ \\\\ \\/  \\/ /  \n" +
                    " | |__| || | \\ \\  / ____ \\\\  /\\  /   \n" +
                    " |_____/ |_|  \\_\\/_/    \\_\\\\/  \\/    \n" +
                    "                                     \n" +
                    "                                     ");
        }

        System.out.println("\n\nFinal scores:");
        System.out.println("P1: " + p1Score);
        System.out.println("P2: " + p2Score);

        System.out.println("\n\nPress ENTER to continue...");
        in.nextLine();
    }

    public boolean handleReplace(Card newCard, String pos) throws InterruptedException {
        Scanner in = new Scanner(System.in);
        if (p1Turn) {
            Card out = this.p1.replace(newCard, pos);
            if (out == Card.EMPTY_CARD) {
                System.out.println("You must replace a card in your hand. You cannot just keep it.");
                return false;
            }
            this.discard.push(out);
            System.out.println("You discarded a " + out.pretty());

            if (out == Card.C7 || out == Card.D7 || out == Card.H7 || out == Card.S7 || out == Card.C8 || out == Card.D8 || out == Card.H8 || out == Card.S8) {
                boolean validLookCard = false;
                while (!validLookCard) {
                    System.out.println("Type the index of the card in your hand you wish to view.");
                    String viewPos = in.nextLine().toLowerCase();

                    if (!"abcdef".contains(viewPos)) {
                        System.out.println("Invalid position. Try again.");
                        continue;
                    }
                    validLookCard = this.handleViewOwn(viewPos);
                }
            } else Thread.sleep(1200);
        } else {
            Card out = this.p2.replace(newCard, pos);
            if (out == Card.EMPTY_CARD) {
                System.out.println("You must replace a card in your hand. You cannot just keep it.");
                return false;
            }
            this.discard.push(out);
            System.out.println("You discarded a " + out.pretty());

            if (out == Card.C7 || out == Card.D7 || out == Card.H7 || out == Card.S7 || out == Card.C8 || out == Card.D8 || out == Card.H8 || out == Card.S8) {
                boolean validLookCard = false;
                while (!validLookCard) {
                    System.out.println("Type the index of the card in your hand you wish to view.");
                    String viewPos = in.nextLine().toLowerCase();

                    if (!"abcdef".contains(viewPos)) {
                        System.out.println("Invalid position. Try again.");
                        continue;
                    }
                    validLookCard = this.handleViewOwn(viewPos);
                }
            } else Thread.sleep(1200);
        }
        return true;
    }

    /**
     *
     * @param pos
     * @return true if the burn was valid (i.e. card pos exists) and false if the card referenced does not exist
     */
    private boolean handleBurn(String pos) throws InterruptedException {
        if (p1Turn) {
            Card discarded = p1.burn(pos);
            if (discarded == Card.EMPTY_CARD) return false;
            if (discarded.rankCode != discard.top().rankCode) {
                p1.giveCard(dealer.pop());
                p1.getHand();
                p1.giveCard(dealer.pop());
                System.out.println("You mis-burned your " + discarded.pretty() + ". Two cards have been added to your hand as penalty.");
            } else {
                System.out.println("You successfully burned a " + discarded.pretty());
            }
            this.discard.push(discarded);
            Thread.sleep(1500);
            return true;
        } else {
            Card discarded = p2.burn(pos);
            if (discarded == Card.EMPTY_CARD) return false;
            if (discarded.rankCode != discard.top().rankCode) {
                p2.giveCard(dealer.pop());
                p2.giveCard(dealer.pop());
                System.out.println("You mis-burned your " + discarded.pretty() + ". Two cards have been added to your hand as penalty.");
            } else {
                System.out.println("You successfully burned a " + discarded.pretty());
            }
            this.discard.push(discarded);
            Thread.sleep(1500);
            return true;
        }
    }

    public boolean handleViewOwn(String pos) {
        Scanner in = new Scanner(System.in);

        if (p1Turn) {
            Card viewed = p1.getHand().view(pos);
            if (viewed == Card.EMPTY_CARD) {
                System.out.println(PrettyPrinter.RED + "There is no card in pos " + pos.toUpperCase() + PrettyPrinter.RESET);
                return false;
            }
            System.out.println("The card in position " + pos.toUpperCase() + " is " + viewed.pretty());
            System.out.println("Press ENTER to continue...");
            in.nextLine();
        } else {
            Card viewed = p2.getHand().view(pos);
            if (viewed == Card.EMPTY_CARD) {
                System.out.println(PrettyPrinter.RED + "There is no card in pos " + pos.toUpperCase() + PrettyPrinter.RESET);
                return false;
            }
            System.out.println("The card in position " + pos.toUpperCase() + " is " + viewed.pretty());
            System.out.println("Press ENTER to continue...");
            in.nextLine();
        }
        return true;
    }


    public void getHelp() {
        System.out.println("7 or 8: view one of your own cards");
        System.out.println("9 or 10: view one of your opponent's cards");
    }

    public String getHistory() {
        return history;
    }

    /**
     * Deal four cards to all players (for game start)
     */
    private void dealAll() {
        for (int i = 0; i < 4; i++) {
            p1.giveCard(dealer.pop());
            p2.giveCard(dealer.pop());
        }
    }

    /**
     * End the current player's turn.
     */
    public void endTurn() {
        this.p1Turn = !this.p1Turn;
    }

    public Player getP1() {
        return p1;
    }

    public Player getP2() {
        return p2;
    }

    public Deck getDealer() {
        return dealer;
    }

    public Deck getDiscard() {
        return discard;
    }
}
