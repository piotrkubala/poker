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
        Hand handWithout1 = new Hand(new Card[]{
            new Card(Card.Suit.CLUBS, Card.Rank.ACE),
            new Card(Card.Suit.DIAMONDS, Card.Rank.NINE),
            new Card(Card.Suit.SPADES, Card.Rank.QUEEN),
            new Card(Card.Suit.CLUBS, Card.Rank.JACK),
            new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        Hand handWithout2 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.ACE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.ACE),
                new Card(Card.Suit.SPADES, Card.Rank.ACE),
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        Hand handWithout3 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.DIAMONDS, Card.Rank.JACK),
                new Card(Card.Suit.SPADES, Card.Rank.JACK),
                new Card(Card.Suit.HEARTS, Card.Rank.JACK),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        Hand handWithout4 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.DIAMONDS, Card.Rank.JACK),
                new Card(Card.Suit.SPADES, Card.Rank.ACE),
                new Card(Card.Suit.HEARTS, Card.Rank.ACE),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        Hand handWith1 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.ACE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.ACE),
                new Card(Card.Suit.SPADES, Card.Rank.QUEEN),
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        // when
        boolean hasPair1 = handWithout1.hasPair();
        boolean hasPair2 = handWithout2.hasPair();
        boolean hasPair3 = handWithout3.hasPair();
        boolean hasPair4 = handWithout4.hasPair();
        boolean hasPair5 = handWith1.hasPair();

        //then
        assertFalse(hasPair1, "Hand should not have pair");
        assertFalse(hasPair2, "Hand should not have pair");
        assertFalse(hasPair3, "Hand should not have pair");
        assertFalse(hasPair4, "Hand should not have pair");
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
        Hand handWithout6 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.DIAMONDS, Card.Rank.JACK),
                new Card(Card.Suit.SPADES, Card.Rank.JACK),
                new Card(Card.Suit.HEARTS, Card.Rank.TEN),
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
        boolean hasPair6 = handWithout6.hasTwoPairs();
        boolean hasPair7 = handWith1.hasTwoPairs();

        //then
        assertFalse(hasPair1, "Hand should not have pair");
        assertFalse(hasPair2, "Hand should not have pair");
        assertFalse(hasPair3, "Hand should not have pair");
        assertFalse(hasPair4, "Hand should not have pair");
        assertFalse(hasPair5, "Hand should not have pair");
        assertFalse(hasPair6, "Hand should not have pair");
        assertTrue(hasPair7, "Hand should have pair");
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
        Hand handWithout5 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.DIAMONDS, Card.Rank.JACK),
                new Card(Card.Suit.SPADES, Card.Rank.JACK),
                new Card(Card.Suit.HEARTS, Card.Rank.JACK),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        Hand handWith1 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.ACE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.ACE),
                new Card(Card.Suit.SPADES, Card.Rank.ACE),
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        Hand handWith2 = new Hand(new Card[]{
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
        boolean hasThree5 = handWithout5.hasThreeOfAKind();
        boolean hasThree6 = handWith1.hasThreeOfAKind();
        boolean hasThree7 = handWith2.hasThreeOfAKind();

        //then
        assertFalse(hasThree1, "Hand should not have three of a kind");
        assertFalse(hasThree2, "Hand should not have three of a kind");
        assertFalse(hasThree3, "Hand should not have three of a kind");
        assertFalse(hasThree4, "Hand should not have three of a kind");
        assertFalse(hasThree5, "Hand should not have three of a kind");
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
    public void testHasFlush() {
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
                new Card(Card.Suit.CLUBS, Card.Rank.KING),
                new Card(Card.Suit.CLUBS, Card.Rank.QUEEN),
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        Hand handWith2 = new Hand(new Card[]{
                new Card(Card.Suit.DIAMONDS, Card.Rank.ACE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.THREE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.FIVE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.TWO),
                new Card(Card.Suit.DIAMONDS, Card.Rank.FOUR)
        });
        Hand handWith3 = new Hand(new Card[]{
                new Card(Card.Suit.SPADES, Card.Rank.THREE),
                new Card(Card.Suit.SPADES, Card.Rank.FOUR),
                new Card(Card.Suit.SPADES, Card.Rank.SIX),
                new Card(Card.Suit.SPADES, Card.Rank.FIVE),
                new Card(Card.Suit.SPADES, Card.Rank.TWO)
        });
        Hand handWith4 = new Hand(new Card[]{
                new Card(Card.Suit.SPADES, Card.Rank.THREE),
                new Card(Card.Suit.SPADES, Card.Rank.FOUR),
                new Card(Card.Suit.SPADES, Card.Rank.QUEEN),
                new Card(Card.Suit.SPADES, Card.Rank.FIVE),
                new Card(Card.Suit.SPADES, Card.Rank.TWO)
        });
        Hand handWith5 = new Hand(new Card[]{
                new Card(Card.Suit.SPADES, Card.Rank.THREE),
                new Card(Card.Suit.SPADES, Card.Rank.FOUR),
                new Card(Card.Suit.SPADES, Card.Rank.KING),
                new Card(Card.Suit.SPADES, Card.Rank.FIVE),
                new Card(Card.Suit.SPADES, Card.Rank.TWO)
        });

        // when
        boolean hasThree1 = handWithout1.hasFlush();
        boolean hasThree2 = handWithout2.hasFlush();
        boolean hasThree3 = handWithout3.hasFlush();
        boolean hasThree4 = handWithout4.hasFlush();
        boolean hasThree5 = handWithout5.hasFlush();
        boolean hasThree6 = handWith1.hasFlush();
        boolean hasThree7 = handWith2.hasFlush();
        boolean hasThree8 = handWith3.hasFlush();
        boolean hasThree9 = handWith4.hasFlush();
        boolean hasThree10 = handWith5.hasFlush();

        //then
        assertFalse(hasThree1, "Hand should not have flush");
        assertFalse(hasThree2, "Hand should not have flush");
        assertFalse(hasThree3, "Hand should not have flush");
        assertFalse(hasThree4, "Hand should not have flush");
        assertFalse(hasThree5, "Hand should not have flush");
        assertTrue(hasThree6, "Hand should have flush");
        assertTrue(hasThree7, "Hand should have flush");
        assertTrue(hasThree8, "Hand should have flush");
        assertTrue(hasThree9, "Hand should have flush");
        assertTrue(hasThree10, "Hand should have flush");
    }

    @Test
    public void testHasFullHouse() {
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
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.DIAMONDS, Card.Rank.JACK),
                new Card(Card.Suit.SPADES, Card.Rank.JACK),
                new Card(Card.Suit.HEARTS, Card.Rank.JACK),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        Hand handWith1 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.ACE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.ACE),
                new Card(Card.Suit.SPADES, Card.Rank.ACE),
                new Card(Card.Suit.CLUBS, Card.Rank.SEVEN),
                new Card(Card.Suit.CLUBS, Card.Rank.SEVEN)
        });
        Hand handWith2 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.TWO),
                new Card(Card.Suit.DIAMONDS, Card.Rank.JACK),
                new Card(Card.Suit.SPADES, Card.Rank.JACK),
                new Card(Card.Suit.HEARTS, Card.Rank.TWO),
                new Card(Card.Suit.CLUBS, Card.Rank.TWO)
        });

        // when
        boolean hasThree1 = handWithout1.hasFullHouse();
        boolean hasThree2 = handWithout2.hasFullHouse();
        boolean hasThree3 = handWithout3.hasFullHouse();
        boolean hasThree4 = handWithout4.hasFullHouse();
        boolean hasThree5 = handWithout5.hasFullHouse();
        boolean hasThree6 = handWith1.hasFullHouse();
        boolean hasThree7 = handWith2.hasFullHouse();

        //then
        assertFalse(hasThree1, "Hand should not have full house");
        assertFalse(hasThree2, "Hand should not have full house");
        assertFalse(hasThree3, "Hand should not have full house");
        assertFalse(hasThree4, "Hand should not have full house");
        assertFalse(hasThree5, "Hand should not have full house");
        assertTrue(hasThree6, "Hand should have full house");
        assertTrue(hasThree7, "Hand should have full house");
    }

    @Test
    public void testHasFourOfAKind() {
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
                new Card(Card.Suit.CLUBS, Card.Rank.ACE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.ACE),
                new Card(Card.Suit.SPADES, Card.Rank.ACE),
                new Card(Card.Suit.CLUBS, Card.Rank.SEVEN),
                new Card(Card.Suit.CLUBS, Card.Rank.SEVEN)
        });
        Hand handWithout7 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.TWO),
                new Card(Card.Suit.DIAMONDS, Card.Rank.JACK),
                new Card(Card.Suit.SPADES, Card.Rank.JACK),
                new Card(Card.Suit.HEARTS, Card.Rank.TWO),
                new Card(Card.Suit.CLUBS, Card.Rank.TWO)
        });
        Hand handWith1 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.ACE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.ACE),
                new Card(Card.Suit.SPADES, Card.Rank.ACE),
                new Card(Card.Suit.HEARTS, Card.Rank.ACE),
                new Card(Card.Suit.CLUBS, Card.Rank.SEVEN)
        });
        Hand handWith2 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.THREE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.JACK),
                new Card(Card.Suit.SPADES, Card.Rank.THREE),
                new Card(Card.Suit.HEARTS, Card.Rank.THREE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.THREE)
        });
        Hand handWith3 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.DIAMONDS, Card.Rank.JACK),
                new Card(Card.Suit.SPADES, Card.Rank.JACK),
                new Card(Card.Suit.HEARTS, Card.Rank.JACK),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });

        // when
        boolean hasThree1 = handWithout1.hasFourOfAKind();
        boolean hasThree2 = handWithout2.hasFourOfAKind();
        boolean hasThree3 = handWithout3.hasFourOfAKind();
        boolean hasThree4 = handWithout4.hasFourOfAKind();
        boolean hasThree5 = handWithout5.hasFourOfAKind();
        boolean hasThree6 = handWithout7.hasFourOfAKind();
        boolean hasThree7 = handWith1.hasFourOfAKind();
        boolean hasThree8 = handWith2.hasFourOfAKind();
        boolean hasThree9 = handWith3.hasFourOfAKind();

        //then
        assertFalse(hasThree1, "Hand should not have full house");
        assertFalse(hasThree2, "Hand should not have full house");
        assertFalse(hasThree3, "Hand should not have full house");
        assertFalse(hasThree4, "Hand should not have full house");
        assertFalse(hasThree5, "Hand should not have full house");
        assertFalse(hasThree6, "Hand should not have full house");
        assertTrue(hasThree7, "Hand should have full house");
        assertTrue(hasThree8, "Hand should have full house");
        assertTrue(hasThree9, "Hand should have full house");
    }

    @Test
    public void testHasStraightFlush() {
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
        Hand handWithout6 = new Hand(new Card[]{
                new Card(Card.Suit.SPADES, Card.Rank.THREE),
                new Card(Card.Suit.SPADES, Card.Rank.FOUR),
                new Card(Card.Suit.SPADES, Card.Rank.QUEEN),
                new Card(Card.Suit.SPADES, Card.Rank.FIVE),
                new Card(Card.Suit.SPADES, Card.Rank.TWO)
        });
        Hand handWithout7 = new Hand(new Card[]{
                new Card(Card.Suit.SPADES, Card.Rank.THREE),
                new Card(Card.Suit.SPADES, Card.Rank.FOUR),
                new Card(Card.Suit.SPADES, Card.Rank.KING),
                new Card(Card.Suit.SPADES, Card.Rank.FIVE),
                new Card(Card.Suit.SPADES, Card.Rank.TWO)
        });
        Hand handWith8 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.ACE),
                new Card(Card.Suit.CLUBS, Card.Rank.KING),
                new Card(Card.Suit.DIAMONDS, Card.Rank.QUEEN),
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        Hand handWith1 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.ACE),
                new Card(Card.Suit.CLUBS, Card.Rank.KING),
                new Card(Card.Suit.CLUBS, Card.Rank.QUEEN),
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        Hand handWith2 = new Hand(new Card[]{
                new Card(Card.Suit.DIAMONDS, Card.Rank.ACE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.THREE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.FIVE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.TWO),
                new Card(Card.Suit.DIAMONDS, Card.Rank.FOUR)
        });
        Hand handWith3 = new Hand(new Card[]{
                new Card(Card.Suit.SPADES, Card.Rank.THREE),
                new Card(Card.Suit.SPADES, Card.Rank.FOUR),
                new Card(Card.Suit.SPADES, Card.Rank.SIX),
                new Card(Card.Suit.SPADES, Card.Rank.FIVE),
                new Card(Card.Suit.SPADES, Card.Rank.TWO)
        });

        // when
        boolean hasThree1 = handWithout1.hasStraightFlush();
        boolean hasThree2 = handWithout2.hasStraightFlush();
        boolean hasThree3 = handWithout3.hasStraightFlush();
        boolean hasThree4 = handWithout4.hasStraightFlush();
        boolean hasThree5 = handWithout5.hasStraightFlush();
        boolean hasThree6 = handWithout6.hasStraightFlush();
        boolean hasThree7 = handWithout7.hasStraightFlush();
        boolean hasThree8 = handWith1.hasStraightFlush();
        boolean hasThree9 = handWith2.hasStraightFlush();
        boolean hasThree10 = handWith3.hasStraightFlush();

        //then
        assertFalse(hasThree1, "Hand should not have straight flush");
        assertFalse(hasThree2, "Hand should not have straight flush");
        assertFalse(hasThree3, "Hand should not have straight flush");
        assertFalse(hasThree4, "Hand should not have straight flush");
        assertFalse(hasThree5, "Hand should not have straight flush");
        assertFalse(hasThree6, "Hand should not have straight flush");
        assertFalse(hasThree7, "Hand should not have straight flush");
        assertTrue(hasThree8, "Hand should have straight flush");
        assertTrue(hasThree9, "Hand should have straight flush");
        assertTrue(hasThree10, "Hand should have straight flush");
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

    @Test
    public void testGetHandValue() {
        // given
        Hand royalFlushHand = new Hand(new Card[]{
            new Card(Card.Suit.CLUBS, Card.Rank.ACE),
            new Card(Card.Suit.CLUBS, Card.Rank.KING),
            new Card(Card.Suit.CLUBS, Card.Rank.QUEEN),
            new Card(Card.Suit.CLUBS, Card.Rank.JACK),
            new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        Hand fourOfAKind = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.TWO),
                new Card(Card.Suit.CLUBS, Card.Rank.QUEEN),
                new Card(Card.Suit.DIAMONDS, Card.Rank.QUEEN),
                new Card(Card.Suit.SPADES, Card.Rank.QUEEN),
                new Card(Card.Suit.HEARTS, Card.Rank.QUEEN)
        });
        // when
        double handValue1 = royalFlushHand.getHandValue();
        double handValue2 = fourOfAKind.getHandValue();
        // then
        assertEquals(10.0, handValue1, "Hand should have royal flush value");
        assertEquals(8.8, handValue2, 1.0e-10, "Hand should have four of a kind value");
    }
}
