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
        /**
         * Ace.
         */
        ACE(1, "Ace"),
        /**
         * Two.
         */
        TWO(2, "Two"),
        /**
         * Three.
         */
        THREE(3, "Three"),
        /**
         * Four.
         */
        FOUR(4, "Four"),
        /**
         * Five.
         */
        FIVE(5, "Five"),
        /**
         * Six.
         */
        SIX(6, "Six"),
        /**
         * Seven.
         */
        SEVEN(7, "Seven"),
        /**
         * Eight.
         */
        EIGHT(8, "Eight"),
        /**
         * Nine.
         */
        NINE(9, "Nine"),
        /**
         * Ten.
         */
        TEN(10, "Ten"),
        /**
         * Jack.
         */
        JACK(11, "Jack"),
        /**
         * Queen.
         */
        QUEEN(12, "Queen"),
        /**
         * King.
         */
        KING(13, "King");

        private final int value;
        private final String name;

        /**
         * Creates a new rank with the given value and name.
         * @param valueArg value of the rank
         * @param nameArg name of the rank
         */
        Rank(int valueArg, String nameArg) {
            this.value = valueArg;
            this.name = nameArg;
        }

        /**
         * Returns the value of the rank.
         * @return value of the rank
         */
        public int getRank() {
            return this.value;
        }

        /**
         * Returns the name of the rank.
         * @return name of the rank
         */
        public String getName() {
            return this.name;
        }
    }

    /**
     * Models suit of a card.
     */
    public enum Suit {
        /**
         * Clubs.
         */
        HEARTS(3, "Hearts"),
        /**
         * Diamonds.
         */
        DIAMONDS(2, "Diamonds"),
        /**
         * Hearts.
         */
        SPADES(1, "Spades"),
        /**
         * Spades.
         */
        CLUBS(0, "Clubs");

        private final int value;
        private final String name;

        /**
         * Creates a new suit.
         * @param valueArg value of the suit
         * @param nameArg name of the suit
         */
        Suit(int valueArg, String nameArg) {
            this.value = valueArg;
            this.name = nameArg;
        }

        /**
         * Returns value of the suit.
         * @return value of the suit
         */
        public int getSuit() {
            return this.value;
        }

        /**
         * Returns name of the suit.
         * @return name of the suit
         */
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

    /**
     * Checks if this card is equal to another card.
     * @param other other card
     * @return true if cards are equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other == null || other.getClass() != Card.class) {
            return false;
        }
        Card otherCard = (Card) other;

        return this.suit == otherCard.suit && this.rank == otherCard.rank;
    }

    /**
     * Returns hash code of this card.
     * @return hash code of this card
     */
    @Override
    public int hashCode() {
        return this.suit.getSuit() * 13 + this.rank.getRank();
    }
}
