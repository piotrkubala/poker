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
    public void testHasTwoPair() {
        // given
        Hand handWithout1 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.ACE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.NINE),
                new Card(Card.Suit.SPADES, Card.Rank.QUEEN),
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        Hand handWithout2 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.ACE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.NINE),
                new Card(Card.Suit.SPADES, Card.Rank.QUEEN),
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        Hand handWithout3 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.ACE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.ACE),
                new Card(Card.Suit.SPADES, Card.Rank.QUEEN),
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        Hand handWithout4 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.ACE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.ACE),
                new Card(Card.Suit.SPADES, Card.Rank.ACE),
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        Hand handWithout5 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.DIAMONDS, Card.Rank.JACK),
                new Card(Card.Suit.SPADES, Card.Rank.JACK),
                new Card(Card.Suit.HEARTS, Card.Rank.JACK),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        Hand handWith1 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.DIAMONDS, Card.Rank.JACK),
                new Card(Card.Suit.SPADES, Card.Rank.ACE),
                new Card(Card.Suit.HEARTS, Card.Rank.ACE),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        // when
        boolean hasPair1 = handWithout1.hasTwoPairs();
        boolean hasPair2 = handWithout2.hasTwoPairs();
        boolean hasPair3 = handWithout3.hasTwoPairs();
        boolean hasPair4 = handWithout4.hasTwoPairs();
        boolean hasPair5 = handWithout5.hasTwoPairs();
        boolean hasPair6 = handWith1.hasTwoPairs();

        //then
        assertFalse(hasPair1, "Hand should not have pair");
        assertFalse(hasPair2, "Hand should not have pair");
        assertFalse(hasPair3, "Hand should not have pair");
        assertFalse(hasPair4, "Hand should not have pair");
        assertFalse(hasPair5, "Hand should not have pair");
        assertTrue(hasPair6, "Hand should have pair");
    }

    @Test
    public void testHasThreeOfAKind() {
        // given
        Hand handWithout1 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.ACE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.NINE),
                new Card(Card.Suit.SPADES, Card.Rank.QUEEN),
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        Hand handWithout2 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.ACE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.NINE),
                new Card(Card.Suit.SPADES, Card.Rank.QUEEN),
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        Hand handWithout3 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.ACE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.ACE),
                new Card(Card.Suit.SPADES, Card.Rank.QUEEN),
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        Hand handWithout4 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.DIAMONDS, Card.Rank.JACK),
                new Card(Card.Suit.SPADES, Card.Rank.ACE),
                new Card(Card.Suit.HEARTS, Card.Rank.ACE),
                new Card(Card.Suit.CLUBS, Card.Rank.TWO)
        });
        Hand handWith1 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.ACE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.ACE),
                new Card(Card.Suit.SPADES, Card.Rank.ACE),
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        Hand handWith2 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.DIAMONDS, Card.Rank.JACK),
                new Card(Card.Suit.SPADES, Card.Rank.JACK),
                new Card(Card.Suit.HEARTS, Card.Rank.JACK),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        Hand handWith3 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.TWO),
                new Card(Card.Suit.DIAMONDS, Card.Rank.JACK),
                new Card(Card.Suit.SPADES, Card.Rank.JACK),
                new Card(Card.Suit.HEARTS, Card.Rank.TWO),
                new Card(Card.Suit.CLUBS, Card.Rank.TWO)
        });

        // when
        boolean hasThree1 = handWithout1.hasThreeOfAKind();
        boolean hasThree2 = handWithout2.hasThreeOfAKind();
        boolean hasThree3 = handWithout3.hasThreeOfAKind();
        boolean hasThree4 = handWithout4.hasThreeOfAKind();
        boolean hasThree5 = handWith1.hasThreeOfAKind();
        boolean hasThree6 = handWith2.hasThreeOfAKind();
        boolean hasThree7 = handWith3.hasThreeOfAKind();

        //then
        assertFalse(hasThree1, "Hand should not have three of a kind");
        assertFalse(hasThree2, "Hand should not have three of a kind");
        assertFalse(hasThree3, "Hand should not have three of a kind");
        assertFalse(hasThree4, "Hand should not have three of a kind");
        assertTrue(hasThree5, "Hand should have three of a kind");
        assertTrue(hasThree6, "Hand should have three of a kind");
        assertTrue(hasThree7, "Hand should have three of a kind");
    }

    @Test
    public void testHasStraight() {
        // given
        Hand handWithout1 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.ACE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.NINE),
                new Card(Card.Suit.SPADES, Card.Rank.QUEEN),
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        Hand handWithout2 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.ACE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.NINE),
                new Card(Card.Suit.SPADES, Card.Rank.QUEEN),
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        Hand handWithout3 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.ACE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.ACE),
                new Card(Card.Suit.SPADES, Card.Rank.QUEEN),
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        Hand handWithout4 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.DIAMONDS, Card.Rank.JACK),
                new Card(Card.Suit.SPADES, Card.Rank.ACE),
                new Card(Card.Suit.HEARTS, Card.Rank.ACE),
                new Card(Card.Suit.CLUBS, Card.Rank.TWO)
        });
        Hand handWithout5 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.TWO),
                new Card(Card.Suit.DIAMONDS, Card.Rank.ACE),
                new Card(Card.Suit.SPADES, Card.Rank.KING),
                new Card(Card.Suit.HEARTS, Card.Rank.QUEEN),
                new Card(Card.Suit.CLUBS, Card.Rank.JACK)
        });
        Hand handWith1 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.ACE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.KING),
                new Card(Card.Suit.SPADES, Card.Rank.QUEEN),
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        Hand handWith2 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.ACE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.THREE),
                new Card(Card.Suit.SPADES, Card.Rank.FIVE),
                new Card(Card.Suit.HEARTS, Card.Rank.TWO),
                new Card(Card.Suit.CLUBS, Card.Rank.FOUR)
        });
        Hand handWith3 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.THREE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.FOUR),
                new Card(Card.Suit.SPADES, Card.Rank.SIX),
                new Card(Card.Suit.HEARTS, Card.Rank.FIVE),
                new Card(Card.Suit.CLUBS, Card.Rank.TWO)
        });

        // when
        boolean hasThree1 = handWithout1.hasStraight();
        boolean hasThree2 = handWithout2.hasStraight();
        boolean hasThree3 = handWithout3.hasStraight();
        boolean hasThree4 = handWithout4.hasStraight();
        boolean hasThree5 = handWithout5.hasStraight();
        boolean hasThree6 = handWith1.hasStraight();
        boolean hasThree7 = handWith2.hasStraight();
        boolean hasThree8 = handWith3.hasStraight();

        //then
        assertFalse(hasThree1, "Hand should not have straight");
        assertFalse(hasThree2, "Hand should not have straight");
        assertFalse(hasThree3, "Hand should not have straight");
        assertFalse(hasThree4, "Hand should not have straight");
        assertFalse(hasThree5, "Hand should not have straight");
        assertTrue(hasThree6, "Hand should have straight");
        assertTrue(hasThree7, "Hand should have straight");
        assertTrue(hasThree8, "Hand should have straight");
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
