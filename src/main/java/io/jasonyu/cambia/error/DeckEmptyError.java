package io.jasonyu.cambia.error;

public class DeckEmptyError extends Error {
    public DeckEmptyError() {
        super("Deck is empty");
    }
}
