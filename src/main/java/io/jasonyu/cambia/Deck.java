package io.jasonyu.cambia;

import io.jasonyu.cambia.error.DeckEmptyError;

import java.util.Arrays;
import java.util.Collections;

public class Deck {
    private String deck;

    public Deck() {
        this.deck = "";
    }

    public Deck(String cards) {
        this.deck = cards;
    }

    public Deck(Card... cards) {
        Collections.shuffle(Arrays.asList(cards));
        // I never actually initialize an array, therefore I am not using one.
        this.deck = "";
        for (Card c : cards) {
            if (c == Card.EMPTY_CARD) continue; // don't add in the empty card to decks
            this.deck += c.toCode();
        }
    }

    /**
     * Remove the top card from the deck and return it.
     * For development purposes, the "top" card is the card at the rightmost end of the deck string.
     * @return {Card} the top card
     */
    public Card pop() {
        if (deck.length() < 1) throw new DeckEmptyError();
        Card c = Card.parse(deck.substring(0, 2));
        deck = deck.replace(c.toCode(), "");
        return c;
    }

    public Card top() {
        if (deck.length() < 1) return Card.EMPTY_CARD;
        return Card.parse(deck.substring(0, 2));
    }

    public int size() {
        return this.deck.length() / 2;
    }

    /**
     * Add a card to the top of the deck (leftmost index in deck string)
     * @param card
     */
    public void push(Card card) {
        this.deck = card.toCode() + this.deck;
    }

    public void push(String card) {
        this.deck = card + this.deck;
    }

    public boolean isEmpty() {
        return this.deck.length() == 0;
    }

    public boolean hasNext() {
        return this.deck.length() > 0;
    }

    public String toString() {
        return this.deck;
    }
}
