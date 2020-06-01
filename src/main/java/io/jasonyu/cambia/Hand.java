package io.jasonyu.cambia;

public class Hand extends Deck {
    Card a = Card.EMPTY_CARD, b = Card.EMPTY_CARD, c = Card.EMPTY_CARD,
         d = Card.EMPTY_CARD, e = Card.EMPTY_CARD, f = Card.EMPTY_CARD;

    public Hand() {
        super();
    }

    public Hand(String cards) {
        super(cards);
    }

    public int getTotalPoints() {
        return a.value + b.value + c.value + d.value + e.value + f.value;
    }

    public Card view(String pos) {
        if (pos.equalsIgnoreCase("a")) {
            return a;
        } else if (pos.equalsIgnoreCase("b")) {
            return b;
        } else if (pos.equalsIgnoreCase("c")) {
            return c;
        } else if (pos.equalsIgnoreCase("d")) {
            return d;
        } else if (pos.equalsIgnoreCase("e")) {
            return e;
        } else if (pos.equalsIgnoreCase("f")) {
            return f;
        }
        return Card.EMPTY_CARD;
    }

    public Card replace(Card newCard, String pos) {
        if (pos.equalsIgnoreCase("a")) {
            Card old = this.a;
            if (old == Card.EMPTY_CARD) return old;
            this.a = newCard;
            return old;
        } else if (pos.equalsIgnoreCase("b")) {
            Card old = this.b;
            if (old == Card.EMPTY_CARD) return old;
            this.b = newCard;
            return old;
        } else if (pos.equalsIgnoreCase( "c")) {
            Card old = this.c;
            if (old == Card.EMPTY_CARD) return old;
            this.c = newCard;
            return old;
        } else if (pos.equalsIgnoreCase("d")) {
            Card old = this.d;
            if (old == Card.EMPTY_CARD) return old;
            this.d = newCard;
            return old;
        } else if (pos.equalsIgnoreCase("e")) {
            Card old = this.e;
            if (old == Card.EMPTY_CARD) return old;
            this.e = newCard;
            return old;
        } else if (pos.equalsIgnoreCase("f")) {
            Card old = this.f;
            if (old == Card.EMPTY_CARD) return old;
            this.f = newCard;
            return old;
        }
        return Card.EMPTY_CARD;
    }

    public String getEmptyCardSlots() {
        String res = "";
        if (a == Card.EMPTY_CARD) res += "a";
        if (b == Card.EMPTY_CARD) res += "b";
        if (c == Card.EMPTY_CARD) res += "c";
        if (d == Card.EMPTY_CARD) res += "d";
        if (e == Card.EMPTY_CARD) res += "e";
        if (f == Card.EMPTY_CARD) res += "f";
        return res;
    }

    public Card discard(String pos) {
        if (this.getEmptyCardSlots().contains(pos.toLowerCase())) return Card.EMPTY_CARD;
        if (pos.equalsIgnoreCase("a")) {
            Card card = this.a;
            this.a = Card.EMPTY_CARD;
            return card;
        }
        else if (pos.equalsIgnoreCase("b")) {
            Card card = this.b;
            this.b = Card.EMPTY_CARD;
            return card;
        }
        else if (pos.equalsIgnoreCase("c")) {
            Card card = this.c;
            this.c = Card.EMPTY_CARD;
            return card;
        }
        else if (pos.equalsIgnoreCase("d")) {
            Card card = this.d;
            this.d = Card.EMPTY_CARD;
            return card;
        }
        else if (pos.equalsIgnoreCase("e")) {
            Card card = this.e;
            this.e = Card.EMPTY_CARD;
            return card;
        }
        else if (pos.equalsIgnoreCase("f")) {
            Card card = this.f;
            this.f = Card.EMPTY_CARD;
            return card;
        }
        return Card.EMPTY_CARD;
    }

    public Card getCardAt(String pos) {
        if (pos.equalsIgnoreCase("a")) return this.a;
        if (pos.equalsIgnoreCase("b")) return this.b;
        if (pos.equalsIgnoreCase("c")) return this.c;
        if (pos.equalsIgnoreCase("d")) return this.d;
        if (pos.equalsIgnoreCase("e")) return this.e;
        if (pos.equalsIgnoreCase("f")) return this.f;
        return null;
    }

    /**
     * Give card to the hand. If full, returns extra points to be added to player total.
     * If return value is 1, the card should be thrown in the discard pile without using its ability.
     * Cards added to a, b, then d, e, then c and f in order
     */
    public int give(Card card) {
        if (a == Card.EMPTY_CARD) this.a = card;
        else if (b == Card.EMPTY_CARD) this.b = card;
        else if (d == Card.EMPTY_CARD) this.d = card;
        else if (e == Card.EMPTY_CARD) this.e = card;
        else if (c == Card.EMPTY_CARD) this.c = card;
        else if (f == Card.EMPTY_CARD) this.f = card;
        else return 1;
        return 0;
    }

    @Override
    public String toString() {
        return "" + this.a + "" + this.b + "" + this.c + "" + this.d + "" + this.e + "" + this.f;
    }
}
