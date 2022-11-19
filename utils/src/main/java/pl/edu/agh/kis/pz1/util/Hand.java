package pl.edu.agh.kis.pz1.util;

import java.util.Arrays;

public class Hand implements Comparable<Hand> {
    public static final int HAND_SIZE = 5;
    public static final int RANKS_NUMBER = 13;
    private Card[] cards;

    private int[] rankCounts = new int[RANKS_NUMBER + 1];
    private int[] suitCounts = new int[Card.Suit.values().length];

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

    private void sortAndUpdateRanks() {
        Arrays.sort(cards);
        updateRanks();
    }

    public Hand(Card[] cards) throws IllegalArgumentException {
        setCards(cards);
    }

    public Card[] getCards() {
        return cards;
    }

    public void setCards(Card[] cards) throws IllegalArgumentException {
        if (cards.length != HAND_SIZE) {
            throw new IllegalArgumentException("Hand should contain " + HAND_SIZE + " cards");
        }

        this.cards = cards;

        sortAndUpdateRanks();
    }

    private boolean allTheSameColour() {
        for (int i = 0; i < suitCounts.length; i++) {
            if (suitCounts[i] == HAND_SIZE) {
                return true;
            }
        }
        return false;
    }

    public boolean hasRoyalFlush() {
        return hasStraightFlush() && cards[0].rank == Card.Rank.ACE && cards[HAND_SIZE - 1].rank == Card.Rank.KING;
    }

    public boolean hasStraightFlush() {
        return hasStraight() && allTheSameColour();
    }

    public boolean hasFourOfAKind() {
        for (int i = 0; i < rankCounts.length; i++) {
            if (rankCounts[i] == 4) {
                return true;
            }
        }
        return false;
    }

    public boolean hasFullHouse() {
        return hasThreeOfAKind() && hasPair();
    }

    public boolean hasFlush() {
        return allTheSameColour();
    }

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

    public double getHandValue() {
        if (hasRoyalFlush()) {
            return 10.0;
        }

        if (hasStraightFlush()) {
            return 9.0 + cards[HAND_SIZE - 1].rank.getRank() / 15.0;
        }

        if (hasFourOfAKind()) {
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

        if (hasFullHouse()) {
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

        if (hasFlush()) {
            double sum = 0.0, multiplier = 1.0;
            if (cards[0].rank == Card.Rank.ACE) {
                sum += 14.0;
                multiplier /= 15.0;
            }
            for (int i = HAND_SIZE - 1; i >= 0; i--) {
                sum += cards[i].rank.getRank() * multiplier;
                multiplier /= 15.0;
            }

            return 6.0 + sum / 15.0;
        }

        if (hasStraight()) {
            if (cards[0].rank == Card.Rank.ACE && cards[HAND_SIZE - 1].rank == Card.Rank.KING) {
                return 5.0 + 14.0;
            }
            return 5.0 + cards[HAND_SIZE - 1].rank.getRank() / 15.0;
        }

        if (hasThreeOfAKind()) {
            double sum = 0.0, multiplier = 1.0 / 15.0;
            if (rankCounts[1] == 1) {
                sum += 14.0 * multiplier;
                multiplier /= 15.0;
            }
            for (int i = rankCounts.length - 1; i >= 1; i--) {
                if (rankCounts[i] == 3) {
                    sum += (i == 1 ? 14.0 : i);
                }
                else if (rankCounts[i] == 1 && i != 0) {
                    sum += (i == 1 ? 14.0 : i) * multiplier;
                    multiplier /= 15.0;
                }
            }

            return 4.0 + sum / 15.0;
        }

        if (hasTwoPairs()) {
            double sum = 0.0, multiplier = 1.0;

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

        if (hasPair()) {
            double sum = 0.0, multiplier = 1.0;

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

        {
            double sum = 0.0, multiplier = 1.0;

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
    }

    @Override
    public int compareTo(Hand otherHand) {
        return Double.compare(getHandValue(), otherHand.getHandValue());
    }
}

