package pl.edu.agh.kis.pz1;

import pl.edu.agh.kis.pz1.util.Card;
import pl.edu.agh.kis.pz1.util.Hand;

import java.nio.channels.SelectionKey;
import java.util.HashSet;
import java.util.Set;

public class Player implements Comparable<Player> {
    private SelectionKey key;

    private String name;

    private int money;
    private int bet = 0;

    private boolean folded = false;

    private Hand playerHand;

    private boolean isReady = false;
    private boolean isStartReady = false;

    private boolean isSmallBlind = false;
    private boolean isBigBlind = false;

    private boolean isPlaying = true;

    private boolean wereCardsChanged = false;


    private double handValue = -1;

    private Game game;

    public Player(SelectionKey key_, int money_, Game game_) {
        key = key_;
        money = money_;
        game = game_;
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
}
