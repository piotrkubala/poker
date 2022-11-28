package pl.edu.agh.kis.pz1.util;

import org.junit.jupiter.api.BeforeEach;
import org.testng.annotations.Test;

import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

public class HandTest {
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
    public void testChangeCard() {
        // given
        Hand hand = new Hand(new Card[]{
            new Card(Card.Suit.CLUBS, Card.Rank.ACE),
            new Card(Card.Suit.HEARTS, Card.Rank.KING),
            new Card(Card.Suit.SPADES, Card.Rank.QUEEN),
            new Card(Card.Suit.DIAMONDS, Card.Rank.JACK),
            new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        Card[] expected = new Card[]{
                new Card(Card.Suit.DIAMONDS, Card.Rank.TWO),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN),
                new Card(Card.Suit.DIAMONDS, Card.Rank.JACK),
                new Card(Card.Suit.SPADES, Card.Rank.QUEEN),
                new Card(Card.Suit.HEARTS, Card.Rank.KING)
        };

        // when
        hand.changeCard(0, new Card(Card.Suit.DIAMONDS, Card.Rank.TWO));

        // then
        assertEquals(0, expected[0].compareTo(hand.getCards()[0]), "Cards should be changed");
    }

    @Test
    public void testToString() {
        // given
        Hand hand = new Hand(new Card[]{
            new Card(Card.Suit.CLUBS, Card.Rank.ACE),
            new Card(Card.Suit.HEARTS, Card.Rank.KING),
            new Card(Card.Suit.SPADES, Card.Rank.QUEEN),
            new Card(Card.Suit.DIAMONDS, Card.Rank.JACK),
            new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        String expected = "(0) Ace of Clubs\n(1) Ten of Clubs\n(2) Jack of Diamonds\n(3) Queen of Spades\n(4) King of Hearts\n";
        // when
        String result = hand.toString();
        // then
        assertEquals(expected, result, "String should be returned");
    }

    @Test
    public void testAllTheSameColour() {
        // given
        Hand hand = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.ACE),
                new Card(Card.Suit.CLUBS, Card.Rank.KING),
                new Card(Card.Suit.CLUBS, Card.Rank.QUEEN),
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });

        // when
        boolean result = hand.allTheSameColour();

        // then
        assertTrue(result, "All cards should be the same colour");
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
        Hand straightFlushHand = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.NINE),
                new Card(Card.Suit.CLUBS, Card.Rank.KING),
                new Card(Card.Suit.CLUBS, Card.Rank.QUEEN),
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        Hand fourOfAKindHand = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.TWO),
                new Card(Card.Suit.CLUBS, Card.Rank.QUEEN),
                new Card(Card.Suit.DIAMONDS, Card.Rank.QUEEN),
                new Card(Card.Suit.SPADES, Card.Rank.QUEEN),
                new Card(Card.Suit.HEARTS, Card.Rank.QUEEN)
        });
        Hand fullHouseHand = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.TWO),
                new Card(Card.Suit.CLUBS, Card.Rank.QUEEN),
                new Card(Card.Suit.DIAMONDS, Card.Rank.QUEEN),
                new Card(Card.Suit.SPADES, Card.Rank.QUEEN),
                new Card(Card.Suit.HEARTS, Card.Rank.TWO)
        });
        Hand flushHand = new Hand(new Card[]{
                new Card(Card.Suit.DIAMONDS, Card.Rank.EIGHT),
                new Card(Card.Suit.DIAMONDS, Card.Rank.KING),
                new Card(Card.Suit.DIAMONDS, Card.Rank.QUEEN),
                new Card(Card.Suit.DIAMONDS, Card.Rank.JACK),
                new Card(Card.Suit.DIAMONDS, Card.Rank.TEN)
        });
        Hand straightHand = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.NINE),
                new Card(Card.Suit.CLUBS, Card.Rank.KING),
                new Card(Card.Suit.DIAMONDS, Card.Rank.QUEEN),
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        Hand threeOfAKindHand = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.TWO),
                new Card(Card.Suit.CLUBS, Card.Rank.QUEEN),
                new Card(Card.Suit.DIAMONDS, Card.Rank.QUEEN),
                new Card(Card.Suit.SPADES, Card.Rank.QUEEN),
                new Card(Card.Suit.HEARTS, Card.Rank.SIX)
        });
        Hand twoPairsHand1 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.FOUR),
                new Card(Card.Suit.DIAMONDS, Card.Rank.FOUR),
                new Card(Card.Suit.DIAMONDS, Card.Rank.QUEEN),
                new Card(Card.Suit.SPADES, Card.Rank.ACE),
                new Card(Card.Suit.HEARTS, Card.Rank.ACE)
        });
        Hand twoPairsHand2 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.FOUR),
                new Card(Card.Suit.DIAMONDS, Card.Rank.FOUR),
                new Card(Card.Suit.DIAMONDS, Card.Rank.QUEEN),
                new Card(Card.Suit.SPADES, Card.Rank.QUEEN),
                new Card(Card.Suit.HEARTS, Card.Rank.ACE)
        });
        Hand pairHand1 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.FOUR),
                new Card(Card.Suit.DIAMONDS, Card.Rank.FOUR),
                new Card(Card.Suit.DIAMONDS, Card.Rank.KING),
                new Card(Card.Suit.SPADES, Card.Rank.QUEEN),
                new Card(Card.Suit.HEARTS, Card.Rank.ACE)
        });
        Hand pairHand2 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.FIVE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.FOUR),
                new Card(Card.Suit.DIAMONDS, Card.Rank.QUEEN),
                new Card(Card.Suit.SPADES, Card.Rank.ACE),
                new Card(Card.Suit.HEARTS, Card.Rank.ACE)
        });
        Hand highCardHand1 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.FIVE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.FOUR),
                new Card(Card.Suit.DIAMONDS, Card.Rank.QUEEN),
                new Card(Card.Suit.SPADES, Card.Rank.ACE),
                new Card(Card.Suit.HEARTS, Card.Rank.TWO)
        });
        Hand highCardHand2 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.FIVE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.FOUR),
                new Card(Card.Suit.DIAMONDS, Card.Rank.QUEEN),
                new Card(Card.Suit.SPADES, Card.Rank.JACK),
                new Card(Card.Suit.HEARTS, Card.Rank.TWO)
        });

        // when
        double handValue1 = royalFlushHand.getHandValue();
        double handValue2 = straightFlushHand.getHandValue();
        double handValue3 = fourOfAKindHand.getHandValue();
        double handValue4 = fullHouseHand.getHandValue();
        double handValue5 = flushHand.getHandValue();
        double handValue6 = straightHand.getHandValue();
        double handValue7 = threeOfAKindHand.getHandValue();
        double handValue8 = twoPairsHand1.getHandValue();
        double handValue9 = twoPairsHand2.getHandValue();
        double handValue10 = pairHand1.getHandValue();
        double handValue11 = pairHand2.getHandValue();
        double handValue12 = highCardHand1.getHandValue();
        double handValue13 = highCardHand2.getHandValue();

        // then
        assertEquals(10.0, handValue1, 1.0e-10, "Hand should have royal flush value");
        assertEquals(9.86666666666666, handValue2, 1.0e-10, "Hand should have four of a straight flush value");
        assertEquals(8.80888888888888, handValue3, 1.0e-10, "Hand should have four of a kind value");
        assertEquals(7.80888888888888, handValue4, 1.0e-10, "Hand should have full house value");
        assertEquals(6.92346732510, handValue5, 1.0e-10, "Hand should have flush value");
        assertEquals(5.86666666666666, handValue6, 1.0e-10, "Hand should have straight value");
        assertEquals(4.82725925925, handValue7, 1.0e-10, "Hand should have three of a kind value");
        assertEquals(3.95466666666666, handValue8, 1.0e-10, "Hand should have two pairs value");
        assertEquals(3.82192592592, handValue9, 1.0e-10, "Hand should have two pairs value");
        assertEquals(2.33297777777777, handValue10, 1.0e-10, "Hand should have pair value");
        assertEquals(2.98822716049, handValue11, 1.0e-10, "Hand should have pair value");
        assertEquals(1.98822979423, handValue12, 1.0e-10, "Hand should have high card value");
        assertEquals(1.85045201646, handValue13, 1.0e-10, "Hand should have high card value");
    }

    @Test
    void testCompareTo() {
        // given
        Hand hand1 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.FIVE),
                new Card(Card.Suit.SPADES, Card.Rank.SIX),
                new Card(Card.Suit.HEARTS, Card.Rank.KING),
                new Card(Card.Suit.SPADES, Card.Rank.QUEEN),
                new Card(Card.Suit.HEARTS, Card.Rank.ACE)
        });
        Hand hand2 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.NINE),
                new Card(Card.Suit.CLUBS, Card.Rank.KING),
                new Card(Card.Suit.CLUBS, Card.Rank.QUEEN),
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });
        Hand hand3 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.TWO),
                new Card(Card.Suit.CLUBS, Card.Rank.QUEEN),
                new Card(Card.Suit.DIAMONDS, Card.Rank.QUEEN),
                new Card(Card.Suit.SPADES, Card.Rank.QUEEN),
                new Card(Card.Suit.HEARTS, Card.Rank.QUEEN)
        });
        Hand hand4 = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.TWO),
                new Card(Card.Suit.CLUBS, Card.Rank.ACE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.THREE),
                new Card(Card.Suit.SPADES, Card.Rank.SEVEN),
                new Card(Card.Suit.HEARTS, Card.Rank.ACE)
        });

        // when
        boolean hand1IsBetterThanHand2 = hand1.compareTo(hand2) > 0;
        boolean hand2IsBetterThanHand1 = hand2.compareTo(hand1) > 0;
        boolean hand1IsBetterThanHand3 = hand1.compareTo(hand3) > 0;
        boolean hand3IsBetterThanHand1 = hand3.compareTo(hand1) > 0;
        boolean hand1IsBetterThanHand4 = hand1.compareTo(hand4) > 0;
        boolean hand4IsBetterThanHand1 = hand4.compareTo(hand1) > 0;
        boolean hand2IsEqualHand2 = hand2.compareTo(hand2) == 0;

        // then
        assertFalse(hand1IsBetterThanHand2, "Hand 1 should not be better than hand 2");
        assertTrue(hand2IsBetterThanHand1, "Hand 2 should be better than hand 1");
        assertFalse(hand1IsBetterThanHand3, "Hand 1 should not be better than hand 3");
        assertTrue(hand3IsBetterThanHand1, "Hand 3 should be better than hand 1");
        assertFalse(hand1IsBetterThanHand4, "Hand 1 should not be better than hand 4");
        assertTrue(hand4IsBetterThanHand1, "Hand 4 should be better than hand 1");
        assertTrue(hand2IsEqualHand2, "Hand 2 should be equal to hand 2");
    }

    @Test
    void testCalculateValueForStraightFlush() {
        // given
        Hand hand = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.NINE),
                new Card(Card.Suit.CLUBS, Card.Rank.KING),
                new Card(Card.Suit.CLUBS, Card.Rank.QUEEN),
                new Card(Card.Suit.CLUBS, Card.Rank.JACK),
                new Card(Card.Suit.CLUBS, Card.Rank.TEN)
        });

        // when
        double handValue = hand.calculateValueForStraightFlush();

        // then
        assertEquals(9.86666666666666, handValue, 1.0e-10, "Hand should have straight flush value");
    }

    @Test
    void testCalculateValueForFourOfAKind() {
        // given
        Hand hand = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.TWO),
                new Card(Card.Suit.CLUBS, Card.Rank.QUEEN),
                new Card(Card.Suit.DIAMONDS, Card.Rank.QUEEN),
                new Card(Card.Suit.SPADES, Card.Rank.QUEEN),
                new Card(Card.Suit.HEARTS, Card.Rank.QUEEN)
        });

        // when
        double handValue = hand.calculateValueForFourOfAKind();

        // then
        assertEquals(8.80888888888888, handValue, 1.0e-10, "Hand should have four of a kind value");
    }

    @Test
    void testCalculateValueForFullHouse() {
        // given
        Hand hand = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.THREE),
                new Card(Card.Suit.CLUBS, Card.Rank.ACE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.THREE),
                new Card(Card.Suit.SPADES, Card.Rank.THREE),
                new Card(Card.Suit.HEARTS, Card.Rank.ACE)
        });

        // when
        double handValue = hand.calculateValueForFullHouse();

        // then
        assertEquals(7.2622222222222222, handValue, 1.0e-10, "Hand should have full house value");
    }

    @Test
    void testCalculateValueForFlush() {
        // given
        Hand hand = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.TWO),
                new Card(Card.Suit.CLUBS, Card.Rank.ACE),
                new Card(Card.Suit.CLUBS, Card.Rank.THREE),
                new Card(Card.Suit.CLUBS, Card.Rank.SEVEN),
                new Card(Card.Suit.CLUBS, Card.Rank.QUEEN)
        });

        // when
        double handValue = hand.calculateValueForFlush();

        // then
        assertEquals(6.98880263374, handValue, 1.0e-10, "Hand should have flush value");
    }

    @Test
    void testCalculateValueForStraight() {
        // given
        Hand hand = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.TWO),
                new Card(Card.Suit.CLUBS, Card.Rank.ACE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.THREE),
                new Card(Card.Suit.SPADES, Card.Rank.FOUR),
                new Card(Card.Suit.HEARTS, Card.Rank.FIVE)
        });

        // when
        double handValue = hand.calculateValueForStraight();

        // then
        assertEquals(5.333333333333333, handValue, 1.0e-10, "Hand should have straight value");
    }

    @Test
    void testCalculateValueForThreeOfAKind() {
        // given
        Hand hand = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.TWO),
                new Card(Card.Suit.CLUBS, Card.Rank.ACE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.THREE),
                new Card(Card.Suit.SPADES, Card.Rank.THREE),
                new Card(Card.Suit.HEARTS, Card.Rank.THREE)
        });

        // when
        double handValue = hand.calculateValueForThreeOfAKind();

        // then
        assertEquals(4.26281481481, handValue, 1.0e-10, "Hand should have three of a kind value");
    }

    @Test
    void testCalculateValueForTwoPairs() {
        // given
        Hand hand = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.TWO),
                new Card(Card.Suit.CLUBS, Card.Rank.ACE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.THREE),
                new Card(Card.Suit.SPADES, Card.Rank.THREE),
                new Card(Card.Suit.HEARTS, Card.Rank.ACE)
        });

        // when
        double handValue = hand.calculateValueForTwoPairs();

        // then
        assertEquals(3.9472592592592592, handValue, 1.0e-10, "Hand should have two pairs value");
    }

    @Test
    void testCalculateValueForPair() {
        // given
        Hand hand = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.TWO),
                new Card(Card.Suit.CLUBS, Card.Rank.ACE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.THREE),
                new Card(Card.Suit.SPADES, Card.Rank.FOUR),
                new Card(Card.Suit.HEARTS, Card.Rank.ACE)
        });

        // when
        double handValue = hand.calculateValueForPair();

        // then
        assertEquals(2.9520395061728397, handValue, 1.0e-10, "Hand should have pair value");
    }

    @Test
    void testCalculateValueForHighCard() {
        // given
        Hand hand = new Hand(new Card[]{
                new Card(Card.Suit.CLUBS, Card.Rank.TWO),
                new Card(Card.Suit.CLUBS, Card.Rank.ACE),
                new Card(Card.Suit.DIAMONDS, Card.Rank.SEVEN),
                new Card(Card.Suit.SPADES, Card.Rank.FOUR),
                new Card(Card.Suit.HEARTS, Card.Rank.FIVE)
        });

        // when
        double handValue = hand.calculateValueForHighCard();

        // then
        assertEquals(1.966007572016461, handValue, 1.0e-10, "Hand should have high card value");
    }
}
