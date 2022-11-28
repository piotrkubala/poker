package pl.edu.agh.kis.pz1.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Deck {
    public static final int DECK_SIZE = 52;
    private ArrayList<Card> cards = new ArrayList<>();

    private Random random = new Random();

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

    public void setRandom(Random random) {
        this.random = random;
    }

    public void shuffle() {
        Collections.shuffle(cards, random);
    }

    public void sort() {
        Collections.sort(cards);
    }

    public Card[] getCards(int count) {
        Card[] result = new Card[count];
        for (int i = 0; i < count; i++) {
            result[i] = cards.get(cards.size() - 1);
            cards.remove(cards.size() - 1);
        }
        return result;
    }
}
