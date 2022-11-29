package pl.edu.agh.kis.pz1.util;

import java.util.Arrays;

/**
 * Class representing a hand of cards.
 * @author Piotr Kubala
 */
public class Hand implements Comparable<Hand> {
    /**
     * Number of cards in the hand, should be 5.
     */
    public static final int HAND_SIZE = 5;

    /**
     * Number of different ranks, should be 13.
     */
    public static final int RANKS_NUMBER = 13;

    /**
     * Array of cards in the hand.
     */
    private Card[] cards;

    /**
     * Counter of cards of each rank.
     */
    private int[] rankCounts = new int[RANKS_NUMBER + 1];

    /**
     * Counter of cards of each suit.
     */
    private int[] suitCounts = new int[Card.Suit.values().length];

    /**
     * updates rankCounts and suitCounts arrays
     */
    private void updateRanks() {
        Arrays.fill(rankCounts, 0);
        Arrays.fill(suitCounts, 0);
        for (Card card : cards) {
            suitCounts[card.suit.getSuit()]++;
        }
        for (int i = 0; i < HAND_SIZE; i++) {
            rankCounts[cards[i].rank.getRank()]++;
        }
    }

    /**
     * Sorts the hand and updates rankCounts and suitCounts arrays.
     */
    private void sortAndUpdateRanks() {
        Arrays.sort(cards);
        updateRanks();
    }

    /**
     * Creates a new hand of cards.
     * @param cards cards in the hand
     */
    public Hand(Card[] cards) throws IllegalArgumentException {
        setCards(cards);
    }

    /**
     * Returns cards in the hand.
     * @return cards in the hand
     */
    public Card[] getCards() {
        return cards;
    }

    /**
     * Sets cards in the hand.
     * @param cards cards in the hand
     */
    public void setCards(Card[] cards) throws IllegalArgumentException {
        if (cards.length != HAND_SIZE) {
            throw new IllegalArgumentException("Hand should contain " + HAND_SIZE + " cards");
        }

        this.cards = cards;

        sortAndUpdateRanks();
    }

    /**
     * changes card at given index
     * @param index index of card to change
     * @param card new card
     * @throws IllegalArgumentException
     */
    public void changeCard(int index, Card card) throws IllegalArgumentException {
        if (index < 0 || index >= HAND_SIZE) {
            throw new IllegalArgumentException("Index should be in range [0, " + HAND_SIZE + ")");
        }

        cards[index] = card;

        sortAndUpdateRanks();
    }

    /**
     * returns string representation of hand
     * @return string representation of hand
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < HAND_SIZE; i++) {
            sb.append("(" + i + ") ");
            sb.append(cards[i].toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * checks if all cards in hand are of the same colour
     * @return true if all cards in hand are of the same colour
     */
    boolean allTheSameColour() {
        for (int i = 0; i < suitCounts.length; i++) {
            if (suitCounts[i] == HAND_SIZE) {
                return true;
            }
        }
        return false;
    }

    /**
     * checks if there is royal flush in hand
     * @return true if there is royal flush in hand
     */
    public boolean hasRoyalFlush() {
        return hasStraightFlush() && cards[0].rank == Card.Rank.ACE && cards[HAND_SIZE - 1].rank == Card.Rank.KING;
    }

    /**
     * checks if there is straight flush in hand
     * @return true if there is straight flush in hand
     */
    public boolean hasStraightFlush() {
        return hasStraight() && allTheSameColour();
    }

    /**
     * checks if there is four of a kind in hand
     * @return true if there is four of a kind in hand
     */
    public boolean hasFourOfAKind() {
        for (int i = 0; i < rankCounts.length; i++) {
            if (rankCounts[i] == 4) {
                return true;
            }
        }
        return false;
    }

    /**
     * checks if there is full house in hand
     * @return true if there is full house in hand
     */
    public boolean hasFullHouse() {
        return hasThreeOfAKind() && hasPair();
    }

    /**
     * checks if there is flush in hand
     * @return true if there is flush in hand
     */
    public boolean hasFlush() {
        return allTheSameColour();
    }

    /**
     * checks if there is straight in hand
     * @return true if there is straight in hand
     */
    public boolean hasStraight() {
        for (int i = 0; i < HAND_SIZE - 1; i++) {
            if (cards[i].rank.getRank() + 1 != cards[i + 1].rank.getRank()) {
                if (cards[0].rank != Card.Rank.ACE || cards[HAND_SIZE - 1].rank != Card.Rank.KING) {
                    return false;
                }
                for (int j = 1; j < HAND_SIZE - 1; j++) {
                    if (cards[j].rank.getRank() + 1 != cards[j + 1].rank.getRank()) {
                        return false;
                    }
                }
                return true;
            }
        }
        return true;
    }

    /**
     * checks if there is three of a kind in hand
     * @return true if there is exactly three of the kind in the hand
     */
    public boolean hasThreeOfAKind() {
        for (int i = 0; i < rankCounts.length; i++) {
            if (rankCounts[i] == 3) {
                return true;
            }
        }
        return false;
    }

    /**
     * checks if there is two pairs in hand
     * @return true if there is exactly one two different pairs in the hand
     */
    public boolean hasTwoPairs() {
        int pairs = 0;
        for (int i = 0; i < rankCounts.length; i++) {
            if (rankCounts[i] == 2) {
                pairs++;
            }

            if (pairs == 2) {
                return true;
            }
        }
        return false;
    }

    /**
     * checks if there is pair in hand
     * @return true if there is exactly one pair in the hand
     */
    public boolean hasPair() {
        int pairs = 0;
        for (int i = 0; i < rankCounts.length; i++) {
            if (rankCounts[i] == 2) {
                pairs++;
            }
        }
        return pairs == 1;
    }

    /**
     * calculates hand value assuming there is straight flush in hand
     * @return hand value
     */
    double calculateValueForStraightFlush() {
        return 9.0 + cards[HAND_SIZE - 1].rank.getRank() / 15.0;
    }

    /**
     * calculates hand value assuming there is four of a kind in hand
     * @return hand value
     */
    double calculateValueForFourOfAKind() {
        double sum = 0.0;
        for (int i = rankCounts.length - 1; i >= 0; i--) {
            if (rankCounts[i] == 4) {
                sum += (i == 1 ? 14 : i);
            }
            else if (rankCounts[i] == 1) {
                sum += (i == 1 ? 14 : i) / 15.0;
            }
        }

        return 8.0 + sum / 15.0;
    }

    /**
     * calculates hand value assuming there is full house in hand
     * @return hand value
     */
    double calculateValueForFullHouse() {
        double sum = 0.0;
        for (int i = 0; i < rankCounts.length; i++) {
            if (rankCounts[i] == 3) {
                sum += (i == 1 ? 14.0 : i);
            }
            else if (rankCounts[i] == 2) {
                sum += (i == 1 ? 14.0 : i) / 15.0;
            }
        }

        return 7.0 + sum / 15.0;
    }

    /**
     * calculates hand value assuming there is flush in hand
     * @return hand value
     */
    double calculateValueForFlush() {
        double sum = 0.0;
        double multiplier = 1.0;
        if (rankCounts[1] != 0) {
            sum += 14.0;
            multiplier /= 15.0;
        }
        for (int i = HAND_SIZE - 1; i >= 0; i--) {
            if (cards[i].rank.getRank() == 1) {
                break;
            }
            sum += cards[i].rank.getRank() * multiplier;
            multiplier /= 15.0;
        }

        return 6.0 + sum / 15.0;
    }

    /**
     * calculates hand value assuming there is straight in hand
     * @return hand value
     */
    double calculateValueForStraight() {
        if (cards[0].rank == Card.Rank.ACE && cards[HAND_SIZE - 1].rank == Card.Rank.KING) {
            return 5.0 + 14.0;
        }
        return 5.0 + cards[HAND_SIZE - 1].rank.getRank() / 15.0;
    }

    /**
     * calculates hand value assuming there is three of a kind in hand
     * @return hand value
     */
    double calculateValueForThreeOfAKind() {
        double sum = 0.0;
        double multiplier = 1.0 / 15.0;
        if (rankCounts[1] == 1) {
            sum += 14.0 * multiplier;
            multiplier /= 15.0;
        }
        for (int i = rankCounts.length - 1; i >= 1; i--) {
            if (rankCounts[i] == 3) {
                sum += (i == 1 ? 14.0 : i);
            }
            else if (rankCounts[i] == 1 && i != 1) {
                sum += (i == 1 ? 14.0 : i) * multiplier;
                multiplier /= 15.0;
            }
        }

        return 4.0 + sum / 15.0;
    }

    /**
     * calculates hand value assuming there is two pairs in hand
     * @return hand value
     */
    double calculateValueForTwoPairs() {
        double sum = 0.0;
        double multiplier = 1.0;

        if (rankCounts[1] == 2) {
            sum += 14.0 * multiplier;
            multiplier /= 15.0;
        }

        for (int i = RANKS_NUMBER; i >= 0; i--) {
            if (rankCounts[i] == 2 && i != 1) {
                sum += i * multiplier;
                multiplier /= 15.0;
            }
            else if (rankCounts[i] == 1) {
                sum += (i == 1 ? 14.0 : i) / 225.0;
            }
        }

        return 3.0 + sum / 15.0;
    }

    /**
     * calculates hand value assuming there is pair in hand
     * @return hand value
     */
    double calculateValueForPair() {
        double sum = 0.0;
        double multiplier = 1.0;

        for (int i = RANKS_NUMBER; i >= 0; i--) {
            if (rankCounts[i] == 2) {
                sum += (i == 1 ? 14.0 : i) * multiplier;
                multiplier /= 15.0;
                break;
            }
        }

        if (rankCounts[1] == 1) {
            sum += 14.0 * multiplier;
            multiplier /= 15.0;
        }

        for (int i = RANKS_NUMBER; i >= 2; i--) {
            if (rankCounts[i] == 1) {
                sum += i * multiplier;
                multiplier /= 15.0;
            }
        }

        return 2.0 + sum / 15.0;
    }

    /**
     * calculates hand value assuming there is no special combination in hand
     * @return hand value
     */
    double calculateValueForHighCard() {
        double sum = 0.0;
        double multiplier = 1.0;

        for (int i = 0; i < HAND_SIZE && cards[i].rank == Card.Rank.ACE; i++) {
            sum += 14.0 * multiplier;
            multiplier /= 15.0;
        }

        for (int i = HAND_SIZE - 1; i >= 0 && cards[i].rank != Card.Rank.ACE; i--) {
            sum += cards[i].rank.getRank() * multiplier;
            multiplier /= 15.0;
        }

        return 1.0 + sum / 15.0;
    }

    /**
     * calculates hand value
     * @return hand value
     */
    public double getHandValue() {
        if (hasRoyalFlush()) {
            return 10.0;
        }

        if (hasStraightFlush()) {
            return calculateValueForStraightFlush();
        }

        if (hasFourOfAKind()) {
            return calculateValueForFourOfAKind();
        }

        if (hasFullHouse()) {
            return calculateValueForFullHouse();
        }

        if (hasFlush()) {
            return calculateValueForFlush();
        }

        if (hasStraight()) {
            return calculateValueForStraight();
        }

        if (hasThreeOfAKind()) {
            return calculateValueForThreeOfAKind();
        }

        if (hasTwoPairs()) {
            return calculateValueForTwoPairs();
        }

        if (hasPair()) {
            return calculateValueForPair();
        }

        return calculateValueForHighCard();
    }

    /**
     * compares two hands
     * @param otherHand hand to compare with
     * @return 1 if this hand is better, -1 if other hand is better, 0 if hands are equal
     */
    @Override
    public int compareTo(Hand otherHand) {
        return Double.compare(getHandValue(), otherHand.getHandValue());
    }

    /**
     * checks if this hand is equal in value to other hand
     * @param other hand to compare with
     * @return true if hands are equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other == null || other.getClass() != Hand.class) {
            return false;
        }
        Hand otherHand = (Hand) other;

        double val1 = getHandValue();
        double val2 = otherHand.getHandValue();

        return Math.abs(val1 - val2) < 1.0e-12;
    }

    /**
     * return hashCode of hand
     * @return hashCode of hand
     */
    @Override
    public int hashCode() {
        return (int) (getHandValue() * 1.0e7);
    }
}

