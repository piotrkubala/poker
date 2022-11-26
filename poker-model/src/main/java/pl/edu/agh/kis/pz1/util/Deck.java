package pl.edu.agh.kis.pz1.util;

import java.util.Vector;
import java.util.Collections;

public class Deck {
    public static final int DECK_SIZE = 52;
    private Vector<Card> cards = new Vector<Card>();

    public Deck() {
        resetDeck();
    }

    public int getDeckSize() {
        return cards.size();
    }

    public void resetDeck() {
        cards.clear();
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public void sort() {
        Collections.sort(cards);
    }

    public Card[] getCards(int count) {
        Card[] result = new Card[count];
        for (int i = 0; i < count; i++) {
            result[i] = cards.lastElement();
            cards.remove(cards.size() - 1);
        }
        return result;
    }
}