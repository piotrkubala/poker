package pl.edu.agh.kis.pz1.util;

public class Card implements Comparable<Card> {
    public enum Rank {
        ACE(1),
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5),
        SIX(6),
        SEVEN(7),
        EIGHT(8),
        NINE(9),
        TEN(10),
        JACK(11),
        QUEEN(12),
        KING(13);

        private int value;
        private Rank(int _value) {
            this.value = _value;
        }

        public int getRank() {
            return this.value;
        }
    }

    public enum Suit {
        HEARTS(3),
        DIAMONDS(2),
        SPADES(1),
        CLUBS(0);

        private int value;
        private Suit(int _value) {
            this.value = _value;
        }

        public int getSuit() {
            return this.value;
        }
    }

    Suit suit;
    Rank rank;

    Card(Suit cardSuit, Rank cardRank) {
        this.suit = cardSuit;
        this.rank = cardRank;
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
