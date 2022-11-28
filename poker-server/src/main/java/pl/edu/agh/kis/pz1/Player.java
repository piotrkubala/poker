package pl.edu.agh.kis.pz1;

import pl.edu.agh.kis.pz1.util.Hand;

import java.nio.channels.SelectionKey;

public class Player implements Comparable<Player> {
    private final SelectionKey key;

    private String name;

    private int money;
    private int bet = 0;

    private Hand playerHand;

    private boolean isReady = false;
    private boolean isStartReady = false;

    private boolean isSmallBlind = false;
    private boolean isBigBlind = false;

    private boolean isPlaying = true;

    private boolean wereCardsChanged = false;

    private boolean isWinner = false;

    private double handValue = -1;

    private boolean nextRoundReady = false;

    private final Game game;

    public Player(SelectionKey key_, int money_, Game game_) {
        key = key_;
        money = money_;
        game = game_;
    }

    public void resetPlayerForNextRound() {
        bet = 0;
        isReady = false;
        isStartReady = false;
        isSmallBlind = false;
        isBigBlind = false;
        isPlaying = true;
        wereCardsChanged = false;
        isWinner = false;
        handValue = -1;
        nextRoundReady = false;
    }

    public boolean getNextRoundReady() {
        return nextRoundReady;
    }

    public void setNextRoundReady(boolean nextRoundReady_) {
        if (nextRoundReady == nextRoundReady_) {
            return;
        }

        nextRoundReady = nextRoundReady_;

        if (nextRoundReady) {
            game.increasePlayersWhoAreReadyForNextRoundCount();
        } else {
            game.decreasePlayersWhoAreReadyForNextRoundCount();
        }
    }

    public SelectionKey getKey() {
        return key;
    }

    private void setReady() {
        game.nextPlayerIsReady(this);
        isReady = true;
    }

    public void setName(String name_) {
        name = name_;

        setReady();
    }

    public String getUsername() {
        return name;
    }

    public Hand getPlayerHand() {
        return playerHand;
    }

    public int getMoney() {
        return money;
    }

    public int getBet() {
        return bet;
    }

    public int setStartReady(boolean isReady_) {
        if (isReady_ == isStartReady) {
            return game.getReadyPlayersForStart();
        }

        isStartReady = isReady_;

        if (isStartReady) {
            game.increaseReadyPlayersForStart();
        } else {
            game.decreaseReadyPlayersForStart();
        }

        return game.getReadyPlayersForStart();
    }

    public void giveHand(Hand hand) {
        playerHand = hand;
    }

    public void setSmallBlind(boolean isSmallBlind_) {
        isSmallBlind = isSmallBlind_;
    }

    public void setBigBlind(boolean isBigBlind_) {
        isBigBlind = isBigBlind_;
    }

    public boolean isSmallBlind() {
        return isSmallBlind;
    }

    public boolean isBigBlind() {
        return isBigBlind;
    }

    public void raiseBet(int amount) {
        money -= amount - bet;

        game.addMoneyToPool(amount - bet);
        game.setCurrentBet(amount);

        bet = amount;
    }

    public void setPlaying(boolean isPlaying_) {
        if (isPlaying_ == isPlaying) {
            return;
        }

        isPlaying = isPlaying_;

        if (isPlaying) {
            game.increaseStillPlayingPlayers();
        } else {
            game.decreaseStillPlayingPlayers();
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setCardsChanged(boolean wereCardsChanged_) {
        if (wereCardsChanged_ == wereCardsChanged) {
            return;
        }

        wereCardsChanged = wereCardsChanged_;

        if (wereCardsChanged) {
            game.increasePlayersWhoChangedCards();
        } else {
            game.decreasePlayersWhoChangedCards();
        }
    }

    public boolean wereCardsChanged() {
        return wereCardsChanged;
    }

    // call before comparing hands
    public void calculateHandValue() {
        handValue = playerHand.getHandValue();
    }

    public double getHandValue() {
        return handValue;
    }

    // call after calculating hand values of hands
    @Override
    public int compareTo(Player player) {
        if (handValue > player.handValue) {
            return 1;
        } else if (handValue < player.handValue) {
            return -1;
        }
        return 0;
    }

    public void setWinner(boolean isWinner_) {
        isWinner = isWinner_;
    }

    public boolean isWinner() {
        return isWinner;
    }

    public void setBet(int amount) {
        bet = amount;
    }

    public void addMoney(int amount) {
        money += amount;
    }
}
