package pl.edu.agh.kis.pz1.util;

/**
 * Models card from standard 52-card deck.
 * @author Piotr Kubala
 */
public class Card implements Comparable<Card> {
    /**
     * Models rank of a card.
     */
    public enum Rank {
        ACE(1, "Ace"),
        TWO(2, "Two"),
        THREE(3, "Three"),
        FOUR(4, "Four"),
        FIVE(5, "Five"),
        SIX(6, "Six"),
        SEVEN(7, "Seven"),
        EIGHT(8, "Eight"),
        NINE(9, "Nine"),
        TEN(10, "Ten"),
        JACK(11, "Jack"),
        QUEEN(12, "Queen"),
        KING(13, "King");

        private final int value;
        private final String name;
        Rank(int valueArg, String nameArg) {
            this.value = valueArg;
            this.name = nameArg;
        }

        public int getRank() {
            return this.value;
        }
        public String getName() {
            return this.name;
        }
    }

    /**
     * Models suit of a card.
     */
    public enum Suit {
        HEARTS(3, "Hearts"),
        DIAMONDS(2, "Diamonds"),
        SPADES(1, "Spades"),
        CLUBS(0, "Clubs");

        private final int value;
        private final String name;
        Suit(int valueArg, String nameArg) {
            this.value = valueArg;
            this.name = nameArg;
        }

        public int getSuit() {
            return this.value;
        }

        public String getName() {
            return this.name;
        }
    }

    Suit suit;
    Rank rank;

    /**
     * Creates card with given suit and rank.
     * @param cardSuit suit of the card
     * @param cardRank rank of the card
     */
    public Card(Suit cardSuit, Rank cardRank) {
        this.suit = cardSuit;
        this.rank = cardRank;
    }

    /**
     * Returns string representation of the card.
     * @return string representation of the card
     */
    public String toString() {
        return rank.getName() + " of " + suit.getName();
    }

    /**
     * Compares this card with another card.
     * @param otherPlayer other card
     * @return -1 if this card is smaller, 0 if they are equal, 1 if this card is bigger
     */
    @Override
    public int compareTo(Card otherPlayer) {
        int result = Integer.compare(this.rank.getRank(), otherPlayer.rank.getRank());
        if(result != 0) {
            return result;
        }
        return Integer.compare(this.suit.getSuit(), otherPlayer.suit.getSuit());
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || other.getClass() != Card.class) {
            return false;
        }
        Card otherCard = (Card) other;

        return this.suit == otherCard.suit && this.rank == otherCard.rank;
    }

    @Override
    public int hashCode() {
        return this.suit.getSuit() * 13 + this.rank.getRank();
    }
}
