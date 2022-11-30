package pl.edu.agh.kis.pz1.util;

import org.junit.jupiter.api.BeforeEach;
import org.testng.annotations.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Card test class
 * @author Piotr Kubala
 */
public class CardTest {
    /**
     * Test for the construction of Card and the
     */
    @Test
    public void shouldCreateCardObject() {
        // given
        Card card = new Card(Card.Suit.CLUBS, Card.Rank.ACE);
        // when
        //then
        assertNotNull(card, "Card object should be created");
        assertEquals(Card.Suit.CLUBS, card.suit, "Card suit should be CLUBS");
        assertEquals(Card.Rank.ACE, card.rank, "Card rank should be ACE");
    }

    /**
     * Test cards comparison
     */
    @Test
    public void testCompareTo() {
        // given
        Card card1 = new Card(Card.Suit.CLUBS, Card.Rank.ACE);
        Card card2 = new Card(Card.Suit.CLUBS, Card.Rank.ACE);
        Card card3 = new Card(Card.Suit.CLUBS, Card.Rank.KING);
        Card card4 = new Card(Card.Suit.DIAMONDS, Card.Rank.ACE);
        // when
        int result1 = card1.compareTo(card2);
        int result2 = card2.compareTo(card1);
        int result3 = card1.compareTo(card3);
        int result4 = card1.compareTo(card4);
        //then
        assertEquals(0, result1, "Cards should be equal");
        assertEquals(0, result2, "Cards should be equal");
        assertEquals(-1, result3, "Card1 should be less than card3");
        assertEquals(-1, result4, "Card1 should be less than card4");
    }
}
