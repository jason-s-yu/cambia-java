package io.jasonyu.cambia;

public class Player {
    private Hand hand;
    public final String name;

    private int penalty;

    public Player() {
        this.name = "";
        this.hand = new Hand();
    }

    public Player(String name) {
        this.name = name;
        this.hand = new Hand();
    }

    public Hand getHand() {
        return this.hand;
    }

    public boolean giveCard(Card card) {
        int pointsAdded = hand.give(card);
        this.penalty += pointsAdded;
        return pointsAdded == 0;
    }

    public Card burn(String pos) {
        Card discarded = hand.discard(pos);
        return discarded;
    }

    public Card replace(Card newCard, String pos) {
        return hand.replace(newCard, pos);
    }

    public int getScore() {
        return penalty + hand.getTotalPoints();
    }
}
