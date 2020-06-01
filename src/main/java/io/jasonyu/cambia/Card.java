package io.jasonyu.cambia;

import io.jasonyu.cambia.util.PrettyPrinter;

public enum Card {
    /**
     * REASONS FOR USING UNICODE SUIT EMOJIS INSTEAD OF A CHARACTER
     */
    BLACK_JOKER(0, false),
    RED_JOKER(0, true),
    H1('♥', 1),
    H2('♥', 2),
    H3('♥', 3),
    H4('♥', 4),
    H5('♥', 5),
    H6('♥', 6),
    H7('♥', 7),
    H8('♥', 8),
    H9('♥', 9),
    H10('♥', 10),
    H11('♥', 11),
    H12('♥', 12),
    H13('♥', 13),
    D1('♦', 1),
    D2('♦', 2),
    D3('♦', 3),
    D4('♦', 4),
    D5('♦', 5),
    D6('♦', 6),
    D7('♦', 7),
    D8('♦', 8),
    D9('♦', 9),
    D10('♦', 10),
    D11('♦', 11),
    D12('♦', 12),
    D13('♦', 13),
    C1('♣', 1),
    C2('♣', 2),
    C3('♣', 3),
    C4('♣', 4),
    C5('♣', 5),
    C6('♣', 6),
    C7('♣', 7),
    C8('♣', 8),
    C9('♣', 9),
    C10('♣', 10),
    C11('♣', 11),
    C12('♣', 12),
    C13('♣', 13),
    S1('♠', 1),
    S2('♠', 2),
    S3('♠', 3),
    S4('♠', 4),
    S5('♠', 5),
    S6('♠', 6),
    S7('♠', 7),
    S8('♠', 8),
    S9('♠', 9),
    S10('♠', 10),
    S11('♠', 11),
    S12('♠', 12),
    S13('♠', 13),
    EMPTY_CARD(' ', -1);

    final char suit;
    final String suitString;
    final int rank;
    final char rankCode;
    final String rankString;
    final int value;
    final boolean red;

    /**
     *
     * @param suit: the card's suit (h - hearts, d - diamonds, c - clubs, s - spades)
     * @param rank: the card's rank (0 - joker, 1 - ace, 2 - 2, ..., 10 - 10, 11 - J, 12 - Q, 13 K)
     */
    Card(char suit, int rank) {
        this.suit = suit;
        this.rank = rank;
        if (suit == '♥' || suit == '♦') {
            this.red = true;
        } else this.red = false;

        // check red king
        if (red && rank == 13) value = -1;
        else if (suit == ' ') value = 0;
        else value = rank;

        if (rank < 10) this.rankCode = Character.forDigit(rank, 10);
        else if (rank == 10) this.rankCode = 't';
        else if (rank == 11) this.rankCode = 'j';
        else if (rank == 12) this.rankCode = 'q';
        else if (rank == 13) this.rankCode = 'k';
        else if (rank == -1) this.rankCode = 'n';
        else throw new IllegalArgumentException("Rank code invalid");

        this.suitString = Card.suitToText(suit);
        this.rankString = Card.rankCodeToText(this.rankCode);
    }

    /**
     * Constructor for joker (no suit).
     * @param rank
     */
    Card(int rank, boolean red) {
        if (rank != 0) throw new Error("Rank-only constructor only valid for joker");
        this.rank = rank;
        this.value = 0;
        this.suit = 'x';
        this.red = red;
        this.rankCode = '0';

        this.suitString = "";
        this.rankString = Card.rankCodeToText(this.rankCode);
    }

    public String toString() {
        if (rank == 0) return "" + rank + (red ? "r" : "b");
        return "" + rank + (red ? PrettyPrinter.RED : "") + suit + PrettyPrinter.RESET;
    }

    public String pretty() {
        if (rank == 0) return (red ? PrettyPrinter.RED + "red" : "black") + " JOKER" + PrettyPrinter.RESET;
        return "" + rankString.toUpperCase() + (red ? PrettyPrinter.RED : "") + " " + suit + PrettyPrinter.RESET;
    }

    public String toCode() {
        char rankCode;
        switch (this.rank) {
            case 10:
                rankCode = 't';
                break;
            case 11:
                rankCode = 'j';
                break;
            case 12:
                rankCode = 'q';
                break;
            case 13:
                rankCode = 'k';
                break;
            default:
                rankCode = Character.forDigit(rank, 10);
        }
        return "" + rankCode + suit;
    }

    public static String suitToText(char suit) {
        switch (suit) {
            case '♥':
                return "hearts";
            case '♦':
                return "diamonds";
            case '♣':
                return "clubs";
            case '♠':
                return "spades";
            default:
                return "";
        }
    }

    public static String rankCodeToText(char rankCode) {
        switch (rankCode) {
            case '0':
                return "joker";
            case '1':
                return "ace";
            case 't':
                return "10";
            case 'j':
                return "jack";
            case 'q':
                return "queen";
            case 'k':
                return "king";
            default:
                return "" + rankCode;
        }
    }

    public static Card parse(String value) {
        if (value.startsWith("0")) {
            if (value.charAt(1) == 'r') return RED_JOKER;
            else return BLACK_JOKER;
        }
        for (Card c : values()) {
            int suit = value.charAt(value.length() - 1);
            try {
                int rank = Integer.parseInt(value.substring(0, value.length() - 1));
                if (c.suit == suit && c.rank == rank) return c;
            } catch (NumberFormatException e) {
                // input string is in code format, parse appropriately as such
                char rankCode = value.charAt(value.length() - 2);
                if (c.suit == suit && c.rankCode == rankCode) return c;
            }
        }
        throw new IllegalArgumentException("Cannot parse string " + value + " as card.");
    }
}
