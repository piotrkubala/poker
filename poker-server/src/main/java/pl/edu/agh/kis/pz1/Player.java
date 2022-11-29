package pl.edu.agh.kis.pz1;

import pl.edu.agh.kis.pz1.util.Hand;

import java.nio.channels.SelectionKey;

/**
 * Model of a player in the poker game on the server side.
 * @author Piotr Kubala
 */
public class Player implements Comparable<Player> {
    final SelectionKey key;

    String name;

    int money;
    int bet = 0;

    Hand playerHand;

    boolean isStartReady = false;

    boolean isSmallBlind = false;
    boolean isBigBlind = false;

    boolean isPlaying = true;

    boolean wereCardsChanged = false;

    boolean isWinner = false;

    double handValue = -1;

    boolean nextRoundReady = false;

    final Game game;

    boolean testMode = false;

    /**
     * Creates a new player with the given selecetion key, name and money.
     * @param keyArg selection key of the player socket
     * @param moneyArg money of the player
     * @param gameArg game the player is playing in
     */
    public Player(SelectionKey keyArg, int moneyArg, Game gameArg) {
        key = keyArg;
        money = moneyArg;
        game = gameArg;
    }

    /**
     * resets player for the next round
     */
    public void resetPlayerForNextRound() {
        bet = 0;
        isStartReady = false;
        isSmallBlind = false;
        isBigBlind = false;
        isPlaying = true;
        wereCardsChanged = false;
        isWinner = false;
        handValue = -1;
        nextRoundReady = false;
    }

    /**
     * get player's nextRoundReady
     * @return player's nextRoundReady
     */
    public boolean getNextRoundReady() {
        return nextRoundReady;
    }

    /**
     * set player's nextRoundReady
     * @param nextRoundReadyArg player's nextRoundReady
     */
    public void setNextRoundReady(boolean nextRoundReadyArg) {
        if (nextRoundReady == nextRoundReadyArg) {
            return;
        }

        nextRoundReady = nextRoundReadyArg;

        if (testMode) {
            return;
        }

        if (nextRoundReady) {
            game.increasePlayersWhoAreReadyForNextRoundCount();
        } else {
            game.decreasePlayersWhoAreReadyForNextRoundCount();
        }
    }

    /**
     * get player's selection key
     * @return player's selection key
     */
    public SelectionKey getKey() {
        return key;
    }

    /**
     * set player's ready for start
     */
    private void setReady() {
        if (testMode){
            return;
        }
        game.nextPlayerIsReady(this);
    }

    /**
     * set player's name
     * @param nameArg player's name
     */
    public void setName(String nameArg) {
        name = nameArg;

        setReady();
    }

    /**
     * get player's name
     * @return player's name
     */
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

    /**
     * set that player is ready for start
     * @param isReadyArg is player ready for start
     * @return number of players who are ready for start
     */
    public int setStartReady(boolean isReadyArg) {
        if (isReadyArg == isStartReady) {
            return game.getReadyPlayersForStart();
        }

        isStartReady = isReadyArg;

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

    public void setSmallBlind(boolean isSmallBlindArg) {
        isSmallBlind = isSmallBlindArg;
    }

    public void setBigBlind(boolean isBigBlindArg) {
        isBigBlind = isBigBlindArg;
    }

    public boolean isSmallBlind() {
        return isSmallBlind;
    }

    public boolean isBigBlind() {
        return isBigBlind;
    }

    /**
     * raise player's bet to the given value
     * @param amount amount to raise
     */
    public void raiseBet(int amount) {
        money -= amount - bet;

        game.addMoneyToPool(amount - bet);
        game.setCurrentBet(amount);

        bet = amount;
    }

    public void setPlaying(boolean isPlayingArg) {
        if (isPlayingArg == isPlaying) {
            return;
        }

        isPlaying = isPlayingArg;

        if (isPlaying) {
            game.increaseStillPlayingPlayers();
        } else {
            game.decreaseStillPlayingPlayers();
        }
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setCardsChanged(boolean wereCardsChangedArg) {
        if (wereCardsChangedArg == wereCardsChanged) {
            return;
        }

        wereCardsChanged = wereCardsChangedArg;

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

    @Override
    public boolean equals(Object other) {
        if (other == null || other.getClass() != Player.class) {
            return false;
        }
        Player otherPlayer = (Player) other;

        return key.equals(otherPlayer.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    public void setWinner(boolean isWinnerArg) {
        isWinner = isWinnerArg;
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
