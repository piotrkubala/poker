package pl.edu.agh.kis.pz1.util;

import org.testng.annotations.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {
    @Test
    public void shouldCreateDeckObject() {
        // given
        Deck deck = new Deck();
        // when
        //then
        assertNotNull(deck, "Deck object should be created");
        assertEquals(Deck.DECK_SIZE, deck.getDeckSize(), "Deck should contain 52 cards");
    }

    @Test
    public void shouldGetCards() {
        // given
        Deck deck = new Deck();
        // when
        Card[] cards = deck.getCards(5);
        //then
        assertNotNull(cards, "Cards should be returned");
        assertEquals(5, cards.length, "Array should contain 5 cards");
        assertEquals(Deck.DECK_SIZE - 5, deck.getDeckSize(), "Deck should contain 47 cards");
    }

    @Test
    public void shouldGetCardsTwice() {
        // given
        Deck deck = new Deck();
        // when
        Card[] cards1 = deck.getCards(5);
        Card[] cards2 = deck.getCards(15);
        //then
        assertEquals(5, cards1.length, "Array should contain 5 cards");
        assertEquals(15, cards2.length, "Array should contain 15 cards");

        assertEquals(Deck.DECK_SIZE - 5 - 15, deck.getDeckSize(), "Deck should contain 52-5-15=32 cards");
    }

    @Test
    public void shouldGetDifferentCards() {
        // given
        Deck deck = new Deck();
        // when
        Card[] cards1 = deck.getCards(5);
        Card[] cards2 = deck.getCards(15);
        Card[] restOfCards = deck.getCards(deck.getDeckSize());

        for (Card c1 : cards1) {
            for (Card c2 : cards2) {
                assertNotEquals(0, c1.compareTo(c2), "Cards should be different in each array");
            }
        }

        for (Card c1 : cards1) {
            for (Card c2 : restOfCards) {
                assertNotEquals(0, c1.compareTo(c2), "Cards should be different in each array");
            }
        }

        for (Card c1 : cards2) {
            for (Card c2 : restOfCards) {
                assertNotEquals(0, c1.compareTo(c2), "Cards should be different in each array");
            }
        }
        //then
        assertEquals(5, cards1.length, "Array should contain 5 cards");
        assertEquals(15, cards2.length, "Array should contain 15 cards");

        assertEquals(Deck.DECK_SIZE - 52, deck.getDeckSize(), "Deck should be empty");
    }

    @Test
    public void shouldResetDeck() {
        // given
        Deck deck = new Deck();
        // when
        deck.getCards(10);
        deck.resetDeck();
        //then
        assertEquals(Deck.DECK_SIZE, deck.getDeckSize(), "Deck should contain 52 cards");
    }

    @Test
    public void shouldShuffleDeck() {
        // given
        Deck deck = new Deck();
        // when
        deck.shuffle();
        //then
        assertEquals(Deck.DECK_SIZE, deck.getDeckSize(), "Deck should contain 52 cards");
    }
}
