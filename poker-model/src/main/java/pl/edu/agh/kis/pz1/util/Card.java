package pl.edu.agh.kis.pz1.util;

public class Card implements Comparable<Card> {
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

    Card(Suit cardSuit, Rank cardRank) {
        this.suit = cardSuit;
        this.rank = cardRank;
    }

    public String toString() {
        return rank.getName() + " of " + suit.getName();
    }

    @Override
    public int compareTo(Card otherPlayer) {
        int result = Integer.compare(this.rank.getRank(), otherPlayer.rank.getRank());
        if(result != 0) {
            return result;
        }
        return Integer.compare(this.suit.getSuit(), otherPlayer.suit.getSuit());
    }
}
