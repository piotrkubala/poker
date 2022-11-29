package pl.edu.agh.kis.pz1.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Class representing a deck of cards.
 * @author Piotr Kubala
 */
public class Deck {
    public static final int DECK_SIZE = 52;
    private ArrayList<Card> cards = new ArrayList<>();

    private Random random = new Random();

    /**
     * Creates a new deck of cards.
     */
    public Deck() {
        resetDeck();
    }

    /**
     * Resets the deck to the initial state.
     * @return this deck
     */
    public int getDeckSize() {
        return cards.size();
    }

    /**
     * Resets the deck to the initial state.
     * @return this deck
     */
    public void resetDeck() {
        cards.clear();
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
    }

    /**
     * Set custom random number generator, used mostly for testing purposes
     * @param random random number generator
     */
    public void setRandom(Random random) {
        this.random = random;
    }

    /**
     * Shuffles the deck.
     */
    public void shuffle() {
        Collections.shuffle(cards, random);
    }

    /**
     * Sorts the deck.
     */
    public void sort() {
        Collections.sort(cards);
    }

    /**
     * Draws a card from the deck.
     * @param count number of cards to draw
     * @return drawn cards
     */
    public Card[] getCards(int count) {
        Card[] result = new Card[count];
        for (int i = 0; i < count; i++) {
            result[i] = cards.get(cards.size() - 1);
            cards.remove(cards.size() - 1);
        }
        return result;
    }
}
