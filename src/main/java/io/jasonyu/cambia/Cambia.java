package io.jasonyu.cambia;

import io.jasonyu.cambia.util.PrettyPrinter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

public class Cambia extends Application {
    public static void main(String[] args) throws InterruptedException, IOException {
        Scanner s = new Scanner(System.in);

        boolean stop = false;

        while (!stop) {
            PrettyPrinter.clearTerminal();
            System.out.println();
            System.out.println("   _____            __  __  ____  _____           \n" +
                    "  / ____|    /\\    |  \\/  ||  _ \\|_   _|    /\\    \n" +
                    " | |        /  \\   | \\  / || |_) | | |     /  \\   \n" +
                    " | |       / /\\ \\  | |\\/| ||  _ <  | |    / /\\ \\  \n" +
                    " | |____  / ____ \\ | |  | || |_) |_| |_  / ____ \\ \n" +
                    "  \\_____|/_/    \\_\\|_|  |_||____/|_____|/_/    \\_\\\n" +
                    "                                                  \n" +
                    "                                                  ");
            System.out.println("\nWelcome to CAMBIA. Available commands:");
            System.out.println("--------------------------------------");
            System.out.println(PrettyPrinter.YELLOW + "instructions" + PrettyPrinter.RESET + " - view a brief description of the game and how it is played");
            System.out.println(PrettyPrinter.GREEN + "play" + PrettyPrinter.RESET + " - gogogo");
            System.out.println(PrettyPrinter.RED + "quit" + PrettyPrinter.RESET + " - graceful exit");

            String cmd = s.nextLine();
            if (cmd.equalsIgnoreCase("instructions")) {
                System.out.println("\nCambia is a game of deception and memory where the objective is to reach the lowest score possible by the end of the game.");
                System.out.println("Both players are dealt four cards face-down in a square-formation. At the start of the game, all players may look at only the two cards closest to them.");
                System.out.println("In this game, the table is visualized with letters representing the positions of each card. For example:");
                System.out.println("A\t\tB\t\tC\nD\t\tE\t\tF");
                System.out.println("To interact with the table, you will be asked to issue text commands. Some commands require a single parameter with the letter card you wish to interact with.");
                System.out.println("\nOn each turn, players may do one of three things: draw a card, burn a card, or call cambia.");
                System.out.println("When a player draws, they must either discard it directly or keep it by swapping it with a card in their hand.");
                System.out.println("At this stage, if a player discards a 7 or an 8, they may look at one of their own cards.");
                System.out.println("In traditional tabletop cambia, if a player discards a jack or a queen, they have the option of blind-swapping any two cards in play. (Swap without looking at what the cards are). However, since less information is available in this game, swapping is left out intentionally.");
                System.out.println("Players will not be able to look at their cards unless a special card played gives them permission to.");
                System.out.println("\nA player may burn a card that is in their hand IF it matches the rank of the card on top of the discard pile. If a player mis-burns, they are penalized with two extra cards.");
                System.out.println("The maximum number of cards a player may hold is 6: if a player is forced to draw more cards as a penalty, those cards are immediately discarded an an extra point is added to the player's total (which is bad).");
                System.out.println("Cards may only be burned at the start of a turn. Once a player draws and plays, their turn ends.");
                System.out.println("Special cards that are discarded in a burn do NOT have their abilities used.");
                System.out.println("\nIf a player does NOT draw a card and instead decides to call 'cambia', the other player gets one more turn and then the player with the lowest point value wins.");
                System.out.println("The player that calls cambia becomes a spectator until the game is over: they cannot touch their cards, nor can they touch any other player's cards");
                System.out.println("Cambia can ONLY BE CALLED AFTER TWO FULL TURNS.");
                System.out.println("\nThe cards 2-10 have points equivalent to their face value (2 is 2, 3 is 3, etc.). Aces are worth 1, jokers are worth 0, acks are worth 11, queens are worth 12, and the black king is 13 points. The RED KING is worth -1 points");

                System.out.println(PrettyPrinter.GREEN + "\n\nTurns" + PrettyPrinter.RESET);
                System.out.println("The starting player is determined randomly. The current player's turn is displayed in block lettering at the top of the screen at every turn.");
                System.out.println("While you make your turn, keep your screen faced away from your opponent so they do not see what card(s) you drew. Once your turn is over, it is safe to give the computer to your opponent.");

                System.out.println(PrettyPrinter.GREEN + "\n\nInformation Available to You" + PrettyPrinter.RESET);
                System.out.println("Since this is a hidden-information game, your goal is to get as much information as possible to determine the optimal point to call cambia.");
                System.out.println("In traditional tabletop cambia, there are more opportunities to gain information in real-time, but since this is a turn based adaptation, you are only able to gain information based on the cards you draw and what is discarded.");
                System.out.println("Burning cards is the best way to reduce your score. If your opponent plays a card you have, you have the option to discard it for free.");

                System.out.println(PrettyPrinter.GREEN + "\n\nAddendum" + PrettyPrinter.RESET);
                System.out.println("In traditional tabletop cambia, players may burn opponents' cards. However, due to the turn-based nature of this text-based adaptation,");
                System.out.println("players in this digital adaptation are not permitted to burn their opponent's cards.");
            } else if (cmd.equalsIgnoreCase("play")) {
                System.out.println(PrettyPrinter.GREEN + "Select game mode:" + PrettyPrinter.RESET);
                System.out.println(PrettyPrinter.YELLOW + "hotseat" + PrettyPrinter.RESET + " - play on the same computer by taking turns on the same seat");
                System.out.println(PrettyPrinter.PURPLE + "online" + PrettyPrinter.RESET + " - play through the internet");
                cmd = s.nextLine();
                if (cmd.equalsIgnoreCase("hotseat")) {
                    Game g = new Game();
                } else if (cmd.equalsIgnoreCase("online")) {
                    launch(args);
                }
            } else if (cmd.equalsIgnoreCase("quit")) {
                stop = true;
                s.close();
            }
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("style.css").toExternalForm());

        stage.setScene(scene);
        stage.show();
        stage.setTitle("Cambia - Login");
    }
}
