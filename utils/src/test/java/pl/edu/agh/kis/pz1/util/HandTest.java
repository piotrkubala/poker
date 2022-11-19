package pl.edu.agh.kis.pz1.util;

import org.junit.jupiter.api.BeforeEach;
import org.testng.annotations.Test;

import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

public class HandTest {
    private Hand handRoyalFlush;

    @BeforeEach
    public void setUp() {
        handRoyalFlush = new Hand(new Card[]{
            new Card(Card.Suit.CLUBS, Card.Rank.ACE),
            new Card(Card.Suit.CLUBS, Card.Rank.KING),
            new Card(Card.Suit.CLUBS, Card.Rank.QUEEN),
            new Card(Card.Suit.CLUBS, Card.Rank.JACK),
            new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
    }

    @Test
    public void testHand() {
        // given
        Deck deck = new Deck();
        Hand hand = new Hand(deck.getCards(5));
        // when
        //then
        assertNotNull(hand, "Hand object should be created");
    }

    @Test
    public void testGetCards() {
        // given
        Hand hand = new Hand(new Card[]{
            new Card(Card.Suit.CLUBS, Card.Rank.ACE),
            new Card(Card.Suit.CLUBS, Card.Rank.KING),
            new Card(Card.Suit.CLUBS, Card.Rank.QUEEN),
            new Card(Card.Suit.CLUBS, Card.Rank.JACK),
            new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        // when
        Card[] cards = hand.getCards();
        //then
        assertNotNull(cards, "Cards should be returned");
        assertEquals(5, cards.length, "Array should contain 5 cards");
        assertEquals(Card.Suit.CLUBS, cards[0].suit, "First card should be CLUBS");
        assertEquals(Card.Rank.ACE, cards[0].rank, "First card should be ACE");
    }

    @Test
    public void testHasPair() {
        // given
        Hand handWithout = new Hand(new Card[]{
            new Card(Card.Suit.CLUBS, Card.Rank.ACE),
            new Card(Card.Suit.DIAMONDS, Card.Rank.NINE),
            new Card(Card.Suit.SPADES, Card.Rank.QUEEN),
            new Card(Card.Suit.CLUBS, Card.Rank.JACK),
            new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        Hand handWith1 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.ACE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.ACE),
                new Card(Card.Suit.SPADES, Card.Rank.QUEEN),
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        Hand handWith2 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.ACE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.ACE),
                new Card(Card.Suit.SPADES, Card.Rank.ACE),
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        Hand handWith3 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.DIAMONDS, Card.Rank.JACK),
                new Card(Card.Suit.SPADES, Card.Rank.JACK),
                new Card(Card.Suit.HEARTS, Card.Rank.JACK),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        Hand handWith4 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.DIAMONDS, Card.Rank.JACK),
                new Card(Card.Suit.SPADES, Card.Rank.ACE),
                new Card(Card.Suit.HEARTS, Card.Rank.ACE),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        // when
        boolean hasPair1 = handWithout.hasPair();
        boolean hasPair2 = handWith1.hasPair();
        boolean hasPair3 = handWith2.hasPair();
        boolean hasPair4 = handWith3.hasPair();
        boolean hasPair5 = handWith4.hasPair();

        //then
        assertFalse(hasPair1, "Hand should not have pair");
        assertTrue(hasPair2, "Hand should have pair");
        assertTrue(hasPair3, "Hand should have pair");
        assertTrue(hasPair4, "Hand should have pair");
        assertTrue(hasPair5, "Hand should have pair");
    }



    @Test
    public void testHasRoyalFlush() {
        // given
        Hand hand = new Hand(new Card[]{
            new Card(Card.Suit.CLUBS, Card.Rank.ACE),
            new Card(Card.Suit.CLUBS, Card.Rank.KING),
            new Card(Card.Suit.CLUBS, Card.Rank.QUEEN),
            new Card(Card.Suit.CLUBS, Card.Rank.JACK),
            new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        // when
        boolean hasRoyalFlush = hand.hasRoyalFlush();
        // then
        assertTrue(hasRoyalFlush, "Hand should have royal flush");
    }
}
